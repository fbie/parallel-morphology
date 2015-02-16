#!/usr/bin/env python

import argparse
import os
import sys
import re

from collections import defaultdict

import matplotlib.pyplot as plt
import numpy as np

RANGE = range(0, 18, 2)

class Style:

    def __init__(self):
        self.colors  = ["b", "g", "r", "y", "m"]
        self.markers = ["D", "^", "x", "s", "o"]
        self.lines   = ["-", "--"]

        self.ic = 0
        self.im = 0
        self.il = 0

    def line(self):
        return self.colors[self.ic] + self.lines[self.il]

    def marker(self):
        return self.colors[self.ic] + self.markers[self.im]

    def full(self):
        return self.colors[self.ic] + self.markers[self.im] + self.lines[self.il]

    def next(self):
        style = self.full()
        # Proceed to the next style
        self.ic = (self.ic + 1) % len(self.colors)

        # Switch to different line style
        if self.ic == 0:
            self.il = (self.il + 1) % len(self.lines)

        # Shift the order if all line styles used
        if self.il == 0:
            self.im = (self.im + 1) % len(self.markers)

        self.im = (self.im + 1) % len(self.markers)
        return style

class Entry:
    def __init__(self, cols, line):
        entries = line.split(",")
        self.function = entries[0]

        self.data = defaultdict(lambda: 0)
        for c, e in zip(cols[1:], entries[1:]):
            self.data[c] = float(e)

def flatten(entries):
    f = defaultdict(lambda: [])
    for e in entries:
        if not "sequential" in e.function:
            f[e.function].append(e)
    return f

def baseline(data, entries):
    vals = [e.data[data] for e in entries if "sequential" in e.function]
    return sum(vals) / len(vals)

def plot(entries, data, bl, legend, degree=0, max_y=0):
    style = Style()

    plt.ylabel(data)
    flat = flatten(entries)
    for key, val in sorted(flat.items()):
        x = [v.data["threads"] for v in val]
        y = [v.data[data] for v in val]
        if degree > 0:
            coefficients = np.polyfit(x, y, degree)
            poly = np.poly1d(coefficients)
            ys = poly(x)
            plt.plot(x,
                     y,
                     style.marker())
            plt.plot(x,
                     ys,
                     style.line())
            plt.plot([], [], style.next(), label=key)
        else:
            plt.plot(x,
                     y,
                     style.next(),
                     label=key)

    # Draw timing baseline (i.e. sequential results)
    if bl:
        b = baseline(data, entries)
        plt.axhline(b, 0, 17, ls=":", color="black", label="Sequential baseline")

    # Draw common stuff
    plt.xlabel("Number of threads")

    if legend:
        plt.legend(loc="best")

    plt.xticks([x for x in RANGE])
    plt.xlim((0, 17))
    if max_y > 0:
        plt.ylim((0, max_y))

if __name__ == "__main__":

    parser = argparse.ArgumentParser(description="Plots data from concurrent algorithm experiments.")
    parser.add_argument("file",
                        help=".csv file containing performance data for concurrent algorithms")
    parser.add_argument("-i", "--include",
                        default=None,
                        help="part of algorithm names to include in plot")
    parser.add_argument("-r", "--remove",
                        default=None,
                        help="part of algorithm names remove from plot")
    parser.add_argument("-s", "--save",
                        default=None,
                        help="store plot under given path as a .png file and exit")
    parser.add_argument("-t", "--tex",
                        action="store_true",
                        help="use latex for rendering text")
    parser.add_argument("-b", "--baseline",
                        action="store_true",
                        help="draw baseline")
    parser.add_argument("-g", "--legend",
                        action="store_true",
                        help="draw legend")
    parser.add_argument("-p", "--poly",
                        default=0,
                        help="approximate data with a polynome of given degree")
    parser.add_argument("-y", "--maxy",
                        default=0,
                        help="set max value on the y-axis")

    group = parser.add_mutually_exclusive_group()
    group.add_argument("-d", "--data",
                       help="decide what data to plot")
    group.add_argument("-l", "--list",
                       action="store_true",
                       help="print a list of possible values to plot for a given file")

    args = parser.parse_args()

    # Function to assert line validity
    removeexp = re.compile(r"%s" %args.remove)
    includeexp = re.compile(r"%s" %args.include)
    valid = lambda l : not l.startswith("#") and\
        ("sequential" in l or\
             (not args.include or includeexp.search(l) is not None) and\
             not (args.remove and removeexp.search(l) is not None))

    # The column to plot
    column = 0
    entries = []
    cols = []

    # Load data from file
    with open(args.file) as f:
        for line in f.readlines():

            # Find which column we want to plot
            if line.startswith("#"):
                cols = [l.strip() for l in line.split(",")]
                if args.list:
                    print "Available data columns:"
                    for c in cols[2:]:
                        print "  %s" %c
                    exit()

                # Handle unspecified column name
                if not args.data:
                    print "Please specify a column name using --data."
                    exit(1)
                if not args.data in cols:
                    print "\"%s\" is not a valid column name. Use --list to list available columns." %args.data
                    exit(1)

            # Read in data
            if valid(line):
                #print "parsing \"%s\"" %line
                entries.append(Entry(cols, line))

    # Use TeX if required
    if args.tex:
        plt.rc("text", usetex=True)
        plt.rc("font", family="serif")

    # Plot loaded data
    plot(entries, args.data, args.baseline, args.legend, int(args.poly), int(float(args.maxy)))

    # Get lambda value and image name
    if "area-open" in args.file:
        lam = args.file.split("-")[-2]
#       img = args.file.split("-")[-3]
        plt.title("$\lambda=%d$" %int(lam))

    if args.save:
        appendix = "-".join([x.replace(" ", "_") for x in ["d%s" %args.data, "i%s" %args.include, "r%s" %args.remove] if not "None" in x])
        filename = args.file.replace(".", "_").replace("_csv", "-%s.pdf" %appendix)
        plt.savefig(os.path.join(args.save, os.path.basename(filename)))
    else:
        plt.show()
