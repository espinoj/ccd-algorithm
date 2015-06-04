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

    private static final String USAGE = "java -cp ccd-algorithm.jar "
            + "edu.pitt.dbmi.ccd.algorithm.tetrad.GesApp "
            + "--data <file> "
            + "--out <dir> "
            + "[--delimiter <char>] "
            + "[--penalty-discount <double>] "
            + "[--depth <int>] "
            + "[--verbose] "
            + "[--out-filename <string>]";

    private static final String DATA_PARAM = "--data";

    private static final String OUT_PARAM = "--out";

    private static final String DELIM_PARAM = "--delimiter";

    private static final String PENALTY_DISCOUNT_PARAM = "--penalty-discount";

    private static final String DEPTH_PARAM = "--depth";

    private static final String VERBOSE_FLAG = "--verbose";

    private static final String OUT_FILENAME_PARAM = "--out-filename";

    private static Path dataFile;

    private static Path dirOut;

    private static char delimiter;

    private static Double penaltyDiscount;

    private static Integer depth;

    private static Boolean verbose;

    private static String outFilename;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.err.println(USAGE);
            System.exit(-127);
        }

        dataFile = null;
        dirOut = null;
        delimiter = '\t';
        penaltyDiscount = 2.0;
        depth = 3;
        verbose = Boolean.FALSE;
        outFilename = null;
        try {
            for (int i = 0; i < args.length; i++) {
                String flag = args[i];
                switch (flag) {
                    case DATA_PARAM:
                        dataFile = ArgsUtil.getPathFile(ArgsUtil.getParam(args, ++i, flag));
                        break;
                    case OUT_PARAM:
                        dirOut = Paths.get(ArgsUtil.getParam(args, ++i, flag));
                        break;
                    case DELIM_PARAM:
                        delimiter = ArgsUtil.getCharacter(ArgsUtil.getParam(args, ++i, flag));
                        break;
                    case PENALTY_DISCOUNT_PARAM:
                        penaltyDiscount = new Double(ArgsUtil.getParam(args, ++i, flag));
                        break;
                    case DEPTH_PARAM:
                        depth = new Integer(ArgsUtil.getParam(args, ++i, flag));
                        break;
                    case VERBOSE_FLAG:
                        verbose = Boolean.TRUE;
                        break;
                    case OUT_FILENAME_PARAM:
                        outFilename = ArgsUtil.getParam(args, ++i, flag);
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
        if (outFilename == null) {
            outFilename = String.format("ges_%d.txt", System.currentTimeMillis());
        }

        Path fileOut = Paths.get(dirOut.toString(), outFilename);
        try (PrintStream stream = new PrintStream(new BufferedOutputStream(Files.newOutputStream(fileOut, StandardOpenOption.CREATE)))) {
            printOutParameters(stream);
            stream.flush();

            // read in the dataset
            TetradDataSet dataset = new TetradDataSet();
            dataset.readDataFile(dataFile, delimiter, true);

            // build the parameters
            Parameters params = ParameterFactory.buildGesParameters(penaltyDiscount, depth, true, verbose);

            Algorithm algorithm = new TetradAlgorithm();
            algorithm.setExecutionOutput(stream);
            algorithm.run(FastGes.class, null, dataset, params);
            stream.flush();

            GraphIO.write(algorithm.getGraph(), false, stream);
            stream.flush();
        } catch (Exception exception) {
            exception.printStackTrace(System.err);
        }
    }

    private static void printOutParameters(PrintStream stream) {
        stream.println("Graph Parameters:");
        stream.println(String.format("penalty discount = %f", penaltyDiscount));
        stream.println(String.format("depth = %s", depth));
        stream.println();
    }

}
