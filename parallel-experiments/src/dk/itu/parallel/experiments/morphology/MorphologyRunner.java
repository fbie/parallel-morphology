package dk.itu.parallel.experiments.morphology;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import dk.itu.parallel.experiments.Experiment;
import dk.itu.parallel.experiments.Runner;
import dk.itu.parallel.morphology.filter.BlockGrayLevel;
import dk.itu.parallel.morphology.filter.HashedLines;
import dk.itu.parallel.morphology.filter.QueuePixels;
import dk.itu.parallel.morphology.filter.Sequential;
import dk.itu.parallel.morphology.filter.Split;
import dk.itu.parallel.morphology.filter.SplitCounting;
import dk.itu.parallel.morphology.filter.QueueLines;
import dk.itu.parallel.morphology.unionfind.Optimistic;
import dk.itu.parallel.morphology.unionfind.SequentialUnionFind;
import dk.itu.parallel.morphology.unionfind.Transactional;
import dk.itu.parallel.morphology.util.Line;
import dk.itu.parallel.morphology.util.Utils;
import dk.itu.parallel.morphology.util.image.Image;
import dk.itu.parallel.morphology.util.image.ImageUtils;
import dk.itu.parallel.morphology.util.queues.QueuesBuilder;
import dk.itu.parallel.morphology.util.sort.LinearPixelSortBuilder;

public class MorphologyRunner extends Runner {

	Image img;
	String imgPath;
	int lambda;

	@Override
	protected void initDefault() {
		super.initDefault();
		img = null;
		imgPath = "";
		lambda = 500;
	}

	public MorphologyRunner(final String[] args) {
		super(args);
	}

	@Override
	public ArrayList<Experiment> setupExperiments() {
		final ArrayList<Experiment> experiments = new ArrayList<Experiment>();

		for (int t = 1; t <= threads; ++t) {

			// Baseline
			experiments.add(new MorphologyExperiment(new Sequential(new SequentialUnionFind(), t), img, lambda));

			// Wait-free STM tests
			experiments.add(new MorphologyExperiment(new BlockGrayLevel(new Transactional(), t, LinearPixelSortBuilder.bucket()), img, lambda));
			experiments.add(new MorphologyExperiment(new BlockGrayLevel(new Transactional(), t, LinearPixelSortBuilder.counting()), img, lambda));

//			experiments.add(new MorphologyExperiment(new QueuePixels(new Transactional(), t, QueuesBuilder.<Long>msq()), img, lambda));
//			experiments.add(new MorphologyExperiment(new QueuePixels(new Transactional(), t, QueuesBuilder.<Long>array()), img, lambda));
			System.err.println("Warning: Not testing stm-pixel-queues!");

			experiments.add(new MorphologyExperiment(new Split(new Transactional(), t), img, lambda));
			experiments.add(new MorphologyExperiment(new SplitCounting(new Transactional(), t), img, lambda));

			experiments.add(new MorphologyExperiment(new QueueLines(new Transactional(), t, QueuesBuilder.<Line>msq()), img, lambda));
			experiments.add(new MorphologyExperiment(new QueueLines(new Transactional(), t, QueuesBuilder.<Line>array()), img, lambda));

			experiments.add(new MorphologyExperiment(new HashedLines(new Transactional(), t), img, lambda));
			
			// Optimistic tests
			experiments.add(new MorphologyExperiment(new BlockGrayLevel(new Optimistic(), t, LinearPixelSortBuilder.bucket()), img, lambda));
			experiments.add(new MorphologyExperiment(new BlockGrayLevel(new Optimistic(), t, LinearPixelSortBuilder.counting()), img, lambda));

//			experiments.add(new MorphologyExperiment(new QueuePixels(new Optimistic(), t, QueuesBuilder.<Long>msq()), img, lambda));
//			experiments.add(new MorphologyExperiment(new QueuePixels(new Optimistic(), t, QueuesBuilder.<Long>array()), img, lambda));
			System.err.println("Warning: Not testing opt-pixel-queues!");

			experiments.add(new MorphologyExperiment(new Split(new Optimistic(), t), img, lambda));
			experiments.add(new MorphologyExperiment(new SplitCounting(new Optimistic(), t), img, lambda));

			experiments.add(new MorphologyExperiment(new QueueLines(new Optimistic(), t, QueuesBuilder.<Line>msq()), img, lambda));
			experiments.add(new MorphologyExperiment(new QueueLines(new Optimistic(), t, QueuesBuilder.<Line>array()), img, lambda));
			
			experiments.add(new MorphologyExperiment(new HashedLines(new Optimistic(), t), img, lambda));
		}
		return experiments;
	}

	@Override
	public void parseArgs(final String[] args) {
		initDefault();

		switch (args.length) {
		case 4:
			runs = Integer.parseInt(args[3]);
		case 3:
			threads = Integer.parseInt(args[2]);
		case 2:
			lambda = Integer.parseInt(args[1]);
		case 1:
			// If the argument is -h or --help, fall through to
			// default case to print help.
			if (!(args[0].equals("-h") || args[0].equals("--help"))) {
				imgPath = args[0];
				break;
			}
		default:
			printHelp();
			System.exit(0);
		}

		// Load image now.
		try {
			img = ImageUtils.toImage(Utils.toGrayScale(ImageIO.read(new File(imgPath))));
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	@Override
	public void printHelp() {
		System.err.println("Usage: java dk.itu.parallel.experiments.MorphologyRunner img [lambda*] [nThreads*] [nRuns*]");
		System.err.println(" img		Path to image.");
		System.err.println(" lambda		Minimum area for connected component filtering. Default is 500.");
		System.err.println(" nThreads	Upper limit of threads to use for tests. Default is 4.");
		System.err.println(" nRuns		Number of runs to average timing over. Default is 3.");
	}

	/**
	 * @param args See printHelp()
	 */
	public static void main(String[] args) {
		final MorphologyRunner r = new MorphologyRunner(args);
		r.run();
	}

}
