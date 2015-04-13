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

import edu.cmu.tetrad.search.IndTestChiSquare;
import edu.cmu.tetrad.search.IndTestFisherZ;
import edu.cmu.tetrad.search.PcStable;
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
 * Feb 17, 2015 9:21:53 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class PcStableApp {

    private static final String USAGE = "java -cp ccd-algorithm.jar "
            + "edu.pitt.dbmi.ccd.algorithm.tetrad.PcStableApp "
            + "--data <file> "
            + "--out <dir> "
            + "[--alpha <double>] "
            + "[--depth <int>] "
            + "[--continuous]"
            + "[--verbose]";

    private static final String DATA_FLAG = "--data";
    private static final String ALPHA_FLAG = "--alpha";
    private static final String DEPTH_FLAG = "--depth";
    private static final String CONTINUOUS_FLAG = "--continuous";
    private static final String VERBOSE_FLAG = "--verbose";
    private static final String OUT_FLAG = "--out";

    private static Path dataFile;
    private static Path dirOut;
    private static Double alpha;
    private static Integer depth;
    private static Boolean verbose;
    private static boolean continuous;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.err.println(USAGE);
            System.exit(-127);
        }

        alpha = 0.0001;
        depth = 3;
        continuous = false;
        verbose = Boolean.FALSE;
        try {
            for (int i = 0; i < args.length; i++) {
                String flag = args[i];
                switch (flag) {
                    case DATA_FLAG:
                        dataFile = InputArgs.getPathFile(args[++i]);
                        break;
                    case ALPHA_FLAG:
                        alpha = new Double(args[++i]);
                        break;
                    case DEPTH_FLAG:
                        depth = new Integer(args[++i]);
                        break;
                    case CONTINUOUS_FLAG:
                        continuous = true;
                        break;
                    case VERBOSE_FLAG:
                        verbose = Boolean.TRUE;
                        break;
                    case OUT_FLAG:
                        dirOut = Paths.get(args[++i]);
                        break;
                    default:
                        throw new Exception(String.format("Unknown flag: %s.\n", flag));
                }
            }
            if (dataFile == null) {
                throw new IllegalArgumentException(String.format("Switch %s is required.", DATA_FLAG));
            }
            if (dirOut == null) {
                throw new IllegalArgumentException(String.format("Switch %s is required.", OUT_FLAG));
            }
        } catch (Exception exception) {
            exception.printStackTrace(System.err);
            System.exit(-127);
        }

        // create output file
        String fileName = String.format("pc-stable_%falpha_%ddepth_%d.txt",
                alpha, depth, System.currentTimeMillis());
        Path fileOut = Paths.get(dirOut.toString(), fileName);
        try {
            if (!Files.exists(dirOut)) {
                Files.createDirectory(dirOut);
            }
        } catch (IOException exception) {
            exception.printStackTrace(System.err);
            System.exit(-128);
        }

        try (PrintStream stream = new PrintStream(new BufferedOutputStream(Files.newOutputStream(fileOut, StandardOpenOption.CREATE)))) {
            // print out the parameters
            printOutParameters(stream);

            // read in the dataset
            TetradDataSet dataset = new TetradDataSet();
            dataset.readDataFile(dataFile, '\t', continuous);

            // build the parameters
            Parameters params = ParameterFactory.buildPcStableParameters(alpha, depth, verbose);

            // run algorithm
            Algorithm algorithm = new TetradAlgorithm();
            algorithm.setExecutionOutput(stream);
            if (continuous) {
                algorithm.run(PcStable.class, IndTestFisherZ.class, dataset, params);
            } else {
                algorithm.run(PcStable.class, IndTestChiSquare.class, dataset, params);
            }
            GraphIO.write(algorithm.getGraph(), false, stream);
        } catch (Exception exception) {
            exception.printStackTrace(System.err);
        }
    }

    private static void printOutParameters(PrintStream stream) {
        stream.println("Graph Parameters:");
        stream.println(String.format("depth = %d", depth));
        stream.println(String.format("alpha = %f", alpha));
        stream.println();
    }

}
