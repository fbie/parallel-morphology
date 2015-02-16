BUILD=./build
PLOTS=./plots

PLOTTER=../scripts/plot-csv.py
FILES=../test

OPTS_SAVE=--save $(PLOTS)
OPTS_SAVE_RAW=--save $(PLOTS)/raw
OPTS_MS=--data ms
OPTS_RETRIES=--data retries
OPTS_CACHE_MISSES=--data cache-misses
OPTS_CACHE_PREFETCH=--data L1-icache-prefetches
OPTS_INSTRUCTIONS=--data instructions
OPTS_GC=--data gc

UNIONFIND=union-find-14-04-08-14-23-26-fbie@dolab-

SYNTH1=area-open-14-05-14-18-25-15-fbie@mtlab-synth001_large.jpg-3000-10.csv
SYNTH2=area-open-14-05-14-18-25-15-fbie@mtlab-synth002_large.jpg-3000-10.csv
SYNTH3A=area-open-14-05-14-13-01-07-fbie@mtlab-synth003_large.jpg-3000-10.csv
SYNTH3B=area-open-14-05-15-12-14-54-fbie@dolab-synth003_large.jpg-6000-10.csv
SYNTH3C=area-open-14-05-16-12-28-50-fbie@dolab-synth003_large.jpg-9000-10.csv
SYNTH4=area-open-14-05-13-17-33-25-fbie@dolab-synth004_large.jpg-3000-10.csv
SYNTH5=area-open-14-05-14-18-23-10-fbie@dolab-synth005_large.jpg-3000-10.csv

PIXEL=area-open-14-04-23-09-11-21-fbie@dolab-synth004.jpg-1500-10.csv

NAT1=area-open-14-05-02-11-45-18-fbie@dolab-natural001_large.jpg-1500-10.csv
NAT2=area-open-14-04-23-09-11-21-fbie@dolab-natural002.jpg-1500-10.csv
NAT3=area-open-14-04-23-09-11-21-fbie@dolab-natural003.jpg-1500-10.csv

FILENAME=main
TEX=pdflatex

all: plots text

text:
	mkdir -p $(BUILD)
	$(TEX) -output-directory=$(BUILD) -draftmode $(FILENAME).tex
	bibtex $(BUILD)/$(FILENAME).aux
	$(TEX) -output-directory=$(BUILD) -draftmode $(FILENAME).tex
	$(TEX) -output-directory=$(BUILD) --output-format=PDF $(FILENAME).tex

plots:
	mkdir -p $(PLOTS)

# 	union-find benchmarks
	$(PLOTTER) $(FILES)/$(UNIONFIND)n500000_p0.000001.gr-10.csv $(OPTS_MS) $(OPTS_SAVE) -g
	$(PLOTTER) $(FILES)/$(UNIONFIND)n500000_p0.000005.gr-10.csv $(OPTS_MS) $(OPTS_SAVE)
	$(PLOTTER) $(FILES)/$(UNIONFIND)n1000000_p0.000005.gr-10.csv $(OPTS_MS) $(OPTS_SAVE)
	$(PLOTTER) $(FILES)/$(UNIONFIND)n1000000_p0.00001.gr-10.csv $(OPTS_MS) $(OPTS_SAVE)
#	union-find retries
	$(PLOTTER) $(FILES)/$(UNIONFIND)n500000_p0.000001.gr-10.csv $(OPTS_RETRIES) $(OPTS_SAVE) -y 70 -g
	$(PLOTTER) $(FILES)/$(UNIONFIND)n500000_p0.000005.gr-10.csv $(OPTS_RETRIES) $(OPTS_SAVE) -y 70
#	union-find cache-misses
	$(PLOTTER) $(FILES)/$(UNIONFIND)n500000_p0.000001.gr-10.csv $(OPTS_CACHE_MISSES) $(OPTS_SAVE) -y 2500000
	$(PLOTTER) $(FILES)/$(UNIONFIND)n500000_p0.000005.gr-10.csv $(OPTS_CACHE_MISSES) $(OPTS_SAVE) -y 2500000
# 	union-find instructions
	$(PLOTTER) $(FILES)/$(UNIONFIND)n500000_p0.000001.gr-10.csv $(OPTS_INSTRUCTIONS) $(OPTS_SAVE) -y 1e9
	$(PLOTTER) $(FILES)/$(UNIONFIND)n500000_p0.000005.gr-10.csv $(OPTS_INSTRUCTIONS) $(OPTS_SAVE) -y 1e10

#	area opening
#	hypothesis 1
	$(PLOTTER) $(FILES)/$(SYNTH1) $(OPTS_MS) $(OPTS_SAVE) -i opt -r "array|bucket" -y 14000 -b -g
	$(PLOTTER) $(FILES)/$(SYNTH1) $(OPTS_MS) $(OPTS_SAVE) -i stm -r "array|bucket" -y 14000 -b -g
	$(PLOTTER) $(FILES)/$(SYNTH2) $(OPTS_MS) $(OPTS_SAVE) -i opt -r "array|bucket" -y 14000 -b
	$(PLOTTER) $(FILES)/$(SYNTH2) $(OPTS_MS) $(OPTS_SAVE) -i stm -r "array|bucket" -y 14000 -b
	$(PLOTTER) $(FILES)/$(SYNTH1) $(OPTS_RETRIES) $(OPTS_SAVE) -i opt -r "array|bucket" -g
	$(PLOTTER) $(FILES)/$(SYNTH2) $(OPTS_CACHE_MISSES) $(OPTS_SAVE) -i opt -r "array|bucket"
#	hypothesis 2
	$(PLOTTER) $(FILES)/$(SYNTH3A) $(OPTS_MS) $(OPTS_SAVE) -i opt -r "array|bucket" -y 6000 -b
	$(PLOTTER) $(FILES)/$(SYNTH3A) $(OPTS_MS) $(OPTS_SAVE) -i stm -r "array|bucket" -y 6000 -b
	$(PLOTTER) $(FILES)/$(SYNTH4) $(OPTS_MS) $(OPTS_SAVE) -i opt -r "array|bucket" -y 6000 -b -g
	$(PLOTTER) $(FILES)/$(SYNTH4) $(OPTS_MS) $(OPTS_SAVE) -i stm -r "array|bucket" -y 6000 -b -g
#	hypothesis 3
	$(PLOTTER) $(FILES)/$(SYNTH5) $(OPTS_MS) $(OPTS_SAVE) -i opt -r "array|bucket" -y 14000 -b
	$(PLOTTER) $(FILES)/$(SYNTH5) $(OPTS_MS) $(OPTS_SAVE) -i stm -r "array|bucket" -y 14000 -b
	$(PLOTTER) $(FILES)/$(SYNTH5) $(OPTS_RETRIES) $(OPTS_SAVE) -i opt -r "array|bucket" -g
	$(PLOTTER) $(FILES)/$(SYNTH5) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE) -i opt -r "array|bucket"
#	pixels
	$(PLOTTER) $(FILES)/$(PIXEL) $(OPTS_MS) $(OPTS_SAVE) -i pixel -g -b
	$(PLOTTER) $(FILES)/$(PIXEL) $(OPTS_CACHE_MISSES) $(OPTS_SAVE) -i pixel
	$(PLOTTER) $(FILES)/$(PIXEL) $(OPTS_RETRIES) $(OPTS_SAVE) -i pixel
	$(PLOTTER) $(FILES)/$(PIXEL) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE) -i pixel
#	natural
	$(PLOTTER) $(FILES)/$(NAT1) $(OPTS_MS) $(OPTS_SAVE) -i opt -r "array|bucket" -b -g
	$(PLOTTER) $(FILES)/$(NAT2) $(OPTS_MS) $(OPTS_SAVE) -i opt -r "array|bucket|pixel" -y 3000 -b
	$(PLOTTER) $(FILES)/$(NAT3) $(OPTS_MS) $(OPTS_SAVE) -i opt -r "array|bucket|pixel" -y 3000 -b
#	scaling
	$(PLOTTER) $(FILES)/$(SYNTH3B) $(OPTS_MS) $(OPTS_SAVE) -i opt -r "array|bucket" -y 5000 -b -g
	$(PLOTTER) $(FILES)/$(SYNTH3C) $(OPTS_MS) $(OPTS_SAVE) -i opt -r "array|bucket" -y 5000 -b

plots-raw:
	mkdir -p $(PLOTS)/raw

	$(PLOTTER) $(FILES)/$(SYNTH1) $(OPTS_MS) $(OPTS_SAVE_RAW) -i opt -g -b
	$(PLOTTER) $(FILES)/$(SYNTH1) $(OPTS_MS) $(OPTS_SAVE_RAW) -i stm -g -b
	$(PLOTTER) $(FILES)/$(SYNTH1) $(OPTS_RETRIES) $(OPTS_SAVE_RAW) -i opt
	$(PLOTTER) $(FILES)/$(SYNTH1) $(OPTS_RETRIES) $(OPTS_SAVE_RAW) -i stm
	$(PLOTTER) $(FILES)/$(SYNTH1) $(OPTS_CACHE_MISSES) $(OPTS_SAVE_RAW) -i opt -g
	$(PLOTTER) $(FILES)/$(SYNTH1) $(OPTS_CACHE_MISSES) $(OPTS_SAVE_RAW) -i stm -g
	$(PLOTTER) $(FILES)/$(SYNTH1) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE_RAW) -i opt
	$(PLOTTER) $(FILES)/$(SYNTH1) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE_RAW) -i stm

	$(PLOTTER) $(FILES)/$(SYNTH2) $(OPTS_MS) $(OPTS_SAVE_RAW) -i opt -b
	$(PLOTTER) $(FILES)/$(SYNTH2) $(OPTS_MS) $(OPTS_SAVE_RAW) -i stm -b
	$(PLOTTER) $(FILES)/$(SYNTH2) $(OPTS_RETRIES) $(OPTS_SAVE_RAW) -i opt -g
	$(PLOTTER) $(FILES)/$(SYNTH2) $(OPTS_RETRIES) $(OPTS_SAVE_RAW) -i stm -g
	$(PLOTTER) $(FILES)/$(SYNTH2) $(OPTS_CACHE_MISSES) $(OPTS_SAVE_RAW) -i opt
	$(PLOTTER) $(FILES)/$(SYNTH2) $(OPTS_CACHE_MISSES) $(OPTS_SAVE_RAW) -i stm
	$(PLOTTER) $(FILES)/$(SYNTH2) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE_RAW) -i opt
	$(PLOTTER) $(FILES)/$(SYNTH2) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE_RAW) -i stm

	$(PLOTTER) $(FILES)/$(SYNTH3A) $(OPTS_MS) $(OPTS_SAVE_RAW) -i opt -g -b
	$(PLOTTER) $(FILES)/$(SYNTH3A) $(OPTS_MS) $(OPTS_SAVE_RAW) -i stm -g -b
	$(PLOTTER) $(FILES)/$(SYNTH3A) $(OPTS_RETRIES) $(OPTS_SAVE_RAW) -i opt
	$(PLOTTER) $(FILES)/$(SYNTH3A) $(OPTS_RETRIES) $(OPTS_SAVE_RAW) -i stm
	$(PLOTTER) $(FILES)/$(SYNTH3A) $(OPTS_CACHE_MISSES) $(OPTS_SAVE_RAW) -i opt
	$(PLOTTER) $(FILES)/$(SYNTH3A) $(OPTS_CACHE_MISSES) $(OPTS_SAVE_RAW) -i stm
	$(PLOTTER) $(FILES)/$(SYNTH3A) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE_RAW) -i opt -g
	$(PLOTTER) $(FILES)/$(SYNTH3A) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE_RAW) -i stm -g

	$(PLOTTER) $(FILES)/$(SYNTH4) $(OPTS_MS) $(OPTS_SAVE_RAW) -i opt -b
	$(PLOTTER) $(FILES)/$(SYNTH4) $(OPTS_MS) $(OPTS_SAVE_RAW) -i stm -b
	$(PLOTTER) $(FILES)/$(SYNTH4) $(OPTS_RETRIES) $(OPTS_SAVE_RAW) -i opt
	$(PLOTTER) $(FILES)/$(SYNTH4) $(OPTS_RETRIES) $(OPTS_SAVE_RAW) -i stm
	$(PLOTTER) $(FILES)/$(SYNTH4) $(OPTS_CACHE_MISSES) $(OPTS_SAVE_RAW) -i opt -g
	$(PLOTTER) $(FILES)/$(SYNTH4) $(OPTS_CACHE_MISSES) $(OPTS_SAVE_RAW) -i stm -g
	$(PLOTTER) $(FILES)/$(SYNTH4) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE_RAW) -i opt
	$(PLOTTER) $(FILES)/$(SYNTH4) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE_RAW) -i stm

	$(PLOTTER) $(FILES)/$(SYNTH5) $(OPTS_MS) $(OPTS_SAVE_RAW) -i opt -b
	$(PLOTTER) $(FILES)/$(SYNTH5) $(OPTS_MS) $(OPTS_SAVE_RAW) -i stm -b
	$(PLOTTER) $(FILES)/$(SYNTH5) $(OPTS_RETRIES) $(OPTS_SAVE_RAW) -i opt -g
	$(PLOTTER) $(FILES)/$(SYNTH5) $(OPTS_RETRIES) $(OPTS_SAVE_RAW) -i stm -g
	$(PLOTTER) $(FILES)/$(SYNTH5) $(OPTS_CACHE_MISSES) $(OPTS_SAVE_RAW) -i opt
	$(PLOTTER) $(FILES)/$(SYNTH5) $(OPTS_CACHE_MISSES) $(OPTS_SAVE_RAW) -i stm
	$(PLOTTER) $(FILES)/$(SYNTH5) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE_RAW) -i opt
	$(PLOTTER) $(FILES)/$(SYNTH5) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE_RAW) -i stm

	$(PLOTTER) $(FILES)/$(NAT1) $(OPTS_MS) $(OPTS_SAVE_RAW) -i opt -b -g
	$(PLOTTER) $(FILES)/$(NAT1) $(OPTS_MS) $(OPTS_SAVE_RAW) -i stm -b -g
	$(PLOTTER) $(FILES)/$(NAT1) $(OPTS_RETRIES) $(OPTS_SAVE_RAW) -i opt
	$(PLOTTER) $(FILES)/$(NAT1) $(OPTS_RETRIES) $(OPTS_SAVE_RAW) -i stm
	$(PLOTTER) $(FILES)/$(NAT1) $(OPTS_CACHE_MISSES) $(OPTS_SAVE_RAW) -i opt
	$(PLOTTER) $(FILES)/$(NAT1) $(OPTS_CACHE_MISSES) $(OPTS_SAVE_RAW) -i stm
	$(PLOTTER) $(FILES)/$(NAT1) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE_RAW) -i opt -g
	$(PLOTTER) $(FILES)/$(NAT1) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE_RAW) -i stm -g

	$(PLOTTER) $(FILES)/$(NAT2) $(OPTS_MS) $(OPTS_SAVE_RAW) -i opt -r "pixel" -b
	$(PLOTTER) $(FILES)/$(NAT2) $(OPTS_MS) $(OPTS_SAVE_RAW) -i stm -r "pixel" -b
	$(PLOTTER) $(FILES)/$(NAT2) $(OPTS_RETRIES) $(OPTS_SAVE_RAW) -i opt -r "pixel"
	$(PLOTTER) $(FILES)/$(NAT2) $(OPTS_RETRIES) $(OPTS_SAVE_RAW) -i stm -r "pixel"
	$(PLOTTER) $(FILES)/$(NAT2) $(OPTS_CACHE_MISSES) $(OPTS_SAVE_RAW) -i opt -r "pixel" -g
	$(PLOTTER) $(FILES)/$(NAT2) $(OPTS_CACHE_MISSES) $(OPTS_SAVE_RAW) -i stm -r "pixel" -g
	$(PLOTTER) $(FILES)/$(NAT2) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE_RAW) -i opt -r "pixel"
	$(PLOTTER) $(FILES)/$(NAT2) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE_RAW) -i stm -r "pixel"

	$(PLOTTER) $(FILES)/$(NAT3) $(OPTS_MS) $(OPTS_SAVE_RAW) -i opt -r "pixel" -b
	$(PLOTTER) $(FILES)/$(NAT3) $(OPTS_MS) $(OPTS_SAVE_RAW) -i stm -r "pixel" -b
	$(PLOTTER) $(FILES)/$(NAT3) $(OPTS_RETRIES) $(OPTS_SAVE_RAW) -i opt -r "pixel" -g
	$(PLOTTER) $(FILES)/$(NAT3) $(OPTS_RETRIES) $(OPTS_SAVE_RAW) -i stm -r "pixel" -g
	$(PLOTTER) $(FILES)/$(NAT3) $(OPTS_CACHE_MISSES) $(OPTS_SAVE_RAW) -i opt -r "pixel"
	$(PLOTTER) $(FILES)/$(NAT3) $(OPTS_CACHE_MISSES) $(OPTS_SAVE_RAW) -i stm -r "pixel"
	$(PLOTTER) $(FILES)/$(NAT3) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE_RAW) -i opt -r "pixel"
	$(PLOTTER) $(FILES)/$(NAT3) $(OPTS_INSTRUCTIONS) $(OPTS_SAVE_RAW) -i stm -r "pixel"

clean: clean-text clean-plots

clean-text:
	rm -rf $(BUILD)

clean-plots:
	rm -rf $(PLOTS)

replot: clean-plots plots

remake: replot text
