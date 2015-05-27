/*
 * Copyright (C) 2015 University of Pittsburgh.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package edu.pitt.dbmi.ccd.algorithm.tetrad;

import edu.cmu.tetrad.search.GesGes;
import edu.pitt.dbmi.ccd.algorithm.Algorithm;
import edu.pitt.dbmi.ccd.algorithm.data.Parameters;
import edu.pitt.dbmi.ccd.algorithm.tetrad.algo.TetradAlgorithm;
import edu.pitt.dbmi.ccd.algorithm.tetrad.data.TetradDataSet;
import edu.pitt.dbmi.ccd.algorithm.tetrad.graph.GraphIO;
import edu.pitt.dbmi.ccd.algorithm.util.InputArgs;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 *
 * Feb 26, 2015 1:59:33 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class GesApp {

	private static final String USAGE = "java -jar fastges-cli.jar " + "--data <file> " + "--out <dir> "
			+ "[--penalty-discount <double>] " + "[--exclude-zero-corr-edge] " + "[--continuous] " + "[--verbose] "
			+ "[--base-filename] " + "[--graphml] " + "[--delimiter <char>]";

	// only switches that are boolean are called flags
	private static final String DATA_PARAM = "--data";
	private static final String PENALTY_DISCOUNT_PARAM = "--penalty-discount";
	private static final String EXCLUDE_ZERO_CORR_EDGE_FLAG = "--exclude-zero-corr-edge";
	private static final String CONTINUOUS_FLAG = "--continuous";
	private static final String VERBOSE_FLAG = "--verbose";
	private static final String OUT_PARAM = "--out";
	private static final String BASE_FILE_NAME_PARAM = "--base-filename";
	private static final String GRAPHML_FLAG = "--graphml";
    private static final String DELIMITER_PARAM = "--delimiter";

	private static Path dataFile;
	private static Path dirOut;
	private static Double penaltyDiscount;
	private static Boolean excludeZeroCorrelationEdges;
	private static Boolean verbose;
	private static Boolean continuous;
	private static String baseFileName;
	private static Boolean isOutputGraphml;
    private static char delimiter;

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {

		if (args == null || args.length == 0) {
			System.err.println(USAGE);
			System.exit(-127);
		}

		// defaults
		penaltyDiscount = 2.0;
		excludeZeroCorrelationEdges = Boolean.FALSE;
		continuous = false;
		verbose = Boolean.FALSE;
		baseFileName = null;
		isOutputGraphml = Boolean.FALSE;
        delimiter = '\t';

		try {
			for (int i = 0; i < args.length; i++) {
				String flag = args[i];
				switch (flag) {
				case DATA_PARAM:
					dataFile = InputArgs.getPathFile(args[++i]);
					break;
				case PENALTY_DISCOUNT_PARAM:
					penaltyDiscount = new Double(args[++i]);
					break;
				case BASE_FILE_NAME_PARAM:
					baseFileName = args[++i];
					break;
				case EXCLUDE_ZERO_CORR_EDGE_FLAG:
					excludeZeroCorrelationEdges = true;
					break;
				case CONTINUOUS_FLAG:
					continuous = true;
					break;
				case VERBOSE_FLAG:
					verbose = true;
					break;
				case OUT_PARAM:
					dirOut = Paths.get(args[++i]);
					break;
				case GRAPHML_FLAG:
					isOutputGraphml = true;
					break;
                case DELIMITER_PARAM:
                    delimiter = args[++i].charAt(0);
                    break;
				default:
					throw new Exception(String.format("Unknown flag: %s.\n", flag));
				}
			}
			if (dataFile == null) {
				throw new IllegalArgumentException(String.format("Switch %s is required.", DATA_PARAM));
			}
			if (dirOut == null) {
				throw new IllegalArgumentException(String.format("Switch %s is required.", OUT_PARAM));
			}
		} catch (Exception exception) {
			exception.printStackTrace(System.err);
			System.exit(-127);
		}

		// create output file
		if (baseFileName == null) {
			if (excludeZeroCorrelationEdges) {
				baseFileName = String.format("ges_%1.2fpd_excld_%d", penaltyDiscount, System.currentTimeMillis());
			} else {
				baseFileName = String.format("ges_%1.2fpd_%d", penaltyDiscount, System.currentTimeMillis());
			}
		}

		// setup output file paths
		Path txtFileOut = Paths.get(dirOut.toString(), baseFileName + ".txt");
		Path graphmlFileOut = Paths.get(dirOut.toString(), baseFileName + ".graphml");

		// create output directory if it doesn't exist
		try {
			if (!Files.exists(dirOut)) {
				Files.createDirectory(dirOut);
			}
		} catch (IOException exception) {
			exception.printStackTrace(System.err);
			System.exit(-128);
		}

		// run ges and output
		try {
			PrintStream txtOutStream = new PrintStream(new BufferedOutputStream(Files.newOutputStream(txtFileOut,
					StandardOpenOption.CREATE)));
			printOutParameters(txtOutStream);
			txtOutStream.flush();

			// read in the dataset
			TetradDataSet dataset = new TetradDataSet();
			dataset.readDataFile(dataFile, delimiter, continuous);

			// build the parameters
			Parameters params = ParameterFactory.buildGesParameters(penaltyDiscount, excludeZeroCorrelationEdges,
					verbose);

			// run GES
			Algorithm algorithm = new TetradAlgorithm();
			algorithm.setExecutionOutput(txtOutStream);
			algorithm.run(GesGes.class, null, dataset, params);
			txtOutStream.flush();

			// output the graph to file
			GraphIO.write(algorithm.getGraph(), GraphIO.GraphOutputType.TETRAD, txtOutStream, baseFileName);
			txtOutStream.flush();

			// optionally output graphml file
			if (isOutputGraphml) {
				PrintStream graphmlOutStream = new PrintStream(new BufferedOutputStream(Files.newOutputStream(
						graphmlFileOut, StandardOpenOption.CREATE)));
				GraphIO.write(algorithm.getGraph(), GraphIO.GraphOutputType.GRAPHML, graphmlOutStream, baseFileName);
				graphmlOutStream.flush();
			}

		} catch (Exception exception) {
			exception.printStackTrace(System.err);
		}
	}

	private static void printOutParameters(PrintStream stream) {
		stream.println("Graph Parameters:");
		stream.println(String.format("penalty discount = %f", penaltyDiscount));
		stream.println(String.format("exclude zero correlation edges = %s", excludeZeroCorrelationEdges));
		stream.println();
	}

}
