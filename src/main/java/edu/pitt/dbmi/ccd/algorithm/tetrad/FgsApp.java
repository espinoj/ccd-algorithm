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

import edu.cmu.tetrad.search.FastGes;
import edu.pitt.dbmi.ccd.algorithm.Algorithm;
import edu.pitt.dbmi.ccd.algorithm.data.Parameters;
import edu.pitt.dbmi.ccd.algorithm.tetrad.algo.TetradAlgorithm;
import edu.pitt.dbmi.ccd.algorithm.tetrad.data.TetradDataSet;
import edu.pitt.dbmi.ccd.algorithm.tetrad.graph.GraphIO;
import edu.pitt.dbmi.ccd.algorithm.util.ArgsUtil;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Fast Greedy Search (FGS) application.
 * <p/>
 * Nov 10, 2015 2:43:43 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class FgsApp {

    private static final String USAGE =
            "    ccd-algorithm is a Java application that provides an CLI and API for algorithms produced by the Center for Causal Discovery\n" +
                    "    Copyright (C) 2015 University of Pittsburgh and Carnegie Mellon University\n" +
                    "\n" +
                    "    This program is free software; you can redistribute it and/or modify\n" +
                    "    it under the terms of the GNU General Public License as published by\n" +
                    "    the Free Software Foundation; either version 2 of the License, or\n" +
                    "    (at your option) any later version.\n" +
                    "\n" +
                    "    This program is distributed in the hope that it will be useful,\n" +
                    "    but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
                    "    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
                    "    GNU General Public License for more details.\n" +
                    "\n" +
                    "    You should have received a copy of the GNU General Public License along\n" +
                    "    with this program; if not, write to the Free Software Foundation, Inc.,\n" +
                    "    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.\n\n" +

                    "Usage: java -cp ccd-algorithm.jar "
                    + "edu.pitt.dbmi.ccd.algorithm.tetrad.FgsApp "
                    + "--data <file> "
                    + "[--out <dir>] "
                    + "[--delimiter <char>] "
                    + "[--penalty-discount <double>] "
                    + "[--depth <int>] "
                    + "[--verbose] "
                    + "[--graphml] "
                    + "[--out-filename <string>]";

    private static final String DATA_PARAM = "--data";

    private static final String OUT_PARAM = "--out";

    private static final String DELIM_PARAM = "--delimiter";

    private static final String PENALTY_DISCOUNT_PARAM = "--penalty-discount";

    private static final String DEPTH_PARAM = "--depth";

    private static final String VERBOSE_FLAG = "--verbose";

    private static final String OUT_FILENAME_PARAM = "--out-filename";

    private static final String GRAPHML_FLAG = "--graphml";

    private static final String HELP_INFO = "================================================================================\n"
            + String.format("%-18s\t%s\n", DATA_PARAM, "The input data file.")
            + String.format("%-18s\t%s\n", OUT_PARAM, "Directory where results will be written to.  Current working directory is the default.")
            + String.format("%-18s\t%s\n", DELIM_PARAM, "A single character used to separate data in a line.  A tab character is the default.")
            + String.format("%-18s\t%s\n", PENALTY_DISCOUNT_PARAM, "Penalty discount.  The default value is 4.0.")
            + String.format("%-18s\t%s\n", DEPTH_PARAM, "The search depth.  The default value is 3, minimum value is -1.")
            + String.format("%-18s\t%s\n", VERBOSE_FLAG, "Output additional information from the algorithm.  No additional information by default.")
            + String.format("%-18s\t%s\n", GRAPHML_FLAG, "Output graphml formatted file.")
            + String.format("%-18s\t%s\n", OUT_FILENAME_PARAM, "The base name of the output files.  The algorithm's name with an integer timestamp is the default.");
    private static Path dataFile;

    private static Path dirOut;

    private static char delimiter;

    private static Double penaltyDiscount;

    private static Integer depth;

    private static Boolean verbose;

    private static String baseOutputFileName;

    private static Boolean isOutputGraphml;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.err.println(USAGE);
            System.err.println(HELP_INFO);
            System.exit(-127);
        }

        dataFile = null;
        dirOut = Paths.get(".");
        delimiter = '\t';
        penaltyDiscount = 4.0;
        depth = 3;
        verbose = Boolean.FALSE;
        baseOutputFileName = null;
        isOutputGraphml = Boolean.FALSE;
        try {
            for (int i = 0; i < args.length; i++) {
                String flag = args[i];
                switch (flag) {
                    case DATA_PARAM:
                        dataFile = ArgsUtil.getPathFile(ArgsUtil.getParam(args, ++i, flag));
                        break;
                    case OUT_PARAM:
                        dirOut = ArgsUtil.getPathDir(ArgsUtil.getParam(args, ++i, flag), false);
                        break;
                    case DELIM_PARAM:
                        delimiter = ArgsUtil.getCharacter(ArgsUtil.getParam(args, ++i, flag));
                        break;
                    case PENALTY_DISCOUNT_PARAM:
                        penaltyDiscount = new Double(ArgsUtil.getParam(args, ++i, flag));
                        break;
                    case DEPTH_PARAM:
                        depth = ArgsUtil.getIntValue(args, ++i, flag, -1);
                        break;
                    case VERBOSE_FLAG:
                        verbose = Boolean.TRUE;
                        break;
                    case OUT_FILENAME_PARAM:
                        baseOutputFileName = ArgsUtil.getParam(args, ++i, flag);
                        break;
                    case GRAPHML_FLAG:
                        isOutputGraphml = true;
                        break;
                    default:
                        throw new Exception(String.format("Unknown switch: %s.\n", flag));
                }
            }
            if (dataFile == null) {
                throw new IllegalArgumentException(String.format("Switch %s is required.", DATA_PARAM));
            }
        } catch (Exception exception) {
            exception.printStackTrace(System.err);
            System.exit(-127);
        }

        try {
            if (Files.notExists(dirOut)) {
                Files.createDirectory(dirOut);
            }
        } catch (IOException exception) {
            exception.printStackTrace(System.err);
            System.exit(-128);
        }

        // create output file
        if (baseOutputFileName == null) {
            baseOutputFileName = String.format("fgs_%d", System.currentTimeMillis());
        }
        String outputFileName = baseOutputFileName + ".txt";

        Path outputFile = Paths.get(dirOut.toString(), outputFileName);
        try (PrintStream stream = new PrintStream(new BufferedOutputStream(Files.newOutputStream(outputFile, StandardOpenOption.CREATE)))) {
            printOutParameters(stream);
            stream.flush();

            // read in the dataset
            TetradDataSet dataset = new TetradDataSet();
            dataset.readDataFile(dataFile, delimiter, true);

            // build the parameters
            Parameters params = ParameterFactory.buildFgsParameters(penaltyDiscount, depth, true, verbose);

            Algorithm algorithm = new TetradAlgorithm();
            algorithm.setExecutionOutput(stream);
            algorithm.run(FastGes.class, null, dataset, params);
            stream.flush();

            GraphIO.write(algorithm.getGraph(), GraphIO.GraphOutputType.TETRAD, stream, outputFileName);
            stream.flush();

            // optionally output graphml file
            if (isOutputGraphml) {
                Path graphOutputFile = Paths.get(dirOut.toString(), baseOutputFileName + ".graphml");
                PrintStream graphmlOutStream = new PrintStream(new BufferedOutputStream(Files.newOutputStream(
                        graphOutputFile, StandardOpenOption.CREATE)));
                GraphIO.write(algorithm.getGraph(), GraphIO.GraphOutputType.GRAPHML, graphmlOutStream, baseOutputFileName);
                graphmlOutStream.flush();
            }
        } catch (Exception exception) {
            exception.printStackTrace(System.err);
            System.exit(-1);
        }
    }

    private static void printOutParameters(PrintStream stream) {
        stream.println("Datasets:");
        stream.println(dataFile.getFileName().toString());
        stream.println();

        stream.println("Graph Parameters:");
        stream.println(String.format("penalty discount = %f", penaltyDiscount));
        stream.println(String.format("depth = %s", depth));
        stream.println();
    }

}
