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

import edu.cmu.tetrad.search.GesGes3;
import edu.pitt.dbmi.ccd.algorithm.Algorithm;
import edu.pitt.dbmi.ccd.algorithm.data.Parameters;
import edu.pitt.dbmi.ccd.algorithm.tetrad.algo.TetradAlgorithm;
import edu.pitt.dbmi.ccd.algorithm.tetrad.data.TetradDataSet;
import edu.pitt.dbmi.ccd.algorithm.tetrad.graph.GraphIO;
import edu.pitt.dbmi.ccd.algorithm.util.InputArgs;
import java.io.File;
import java.nio.file.Path;

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
            + "--delim <char> "
            + "--penalty-discount <double> "
            + "--pattern-store <int> "
            + "[--verbose] "
            + "[--faithful]";

    private static final String DATA_FLAG = "--data";
    private static final String OUT_FLAG = "--out";
    private static final String DELIM_FLAG = "--delim";
    private static final String VERBOSE_FLAG = "--verbose";
    private static final String FAITHFUL_FLAG = "--faithful";
    private static final String PENALTY_DISCOUNT_FLAG = "--penalty-discount";
    private static final String PATTERN_STORE_FLAG = "--pattern-store";

    private static final int NUM_REG_ARGS = 6;

    private static Path dataFile;
    private static Path dirOut;
    private static char delim;
    private static Boolean verbose;
    private static Boolean faithful;
    private static Double penaltyDiscount;
    private static Integer numPatternsToStore;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args == null || args.length < NUM_REG_ARGS) {
            System.err.println(USAGE);
            System.exit(-127);
        }

        dataFile = null;
        dirOut = null;
        delim = (char) -1;
        verbose = Boolean.FALSE;
        faithful = Boolean.FALSE;
        penaltyDiscount = 2.0;
        numPatternsToStore = 0;
        try {
            for (int i = 0; i < args.length; i++) {
                String flag = args[i];
                switch (flag) {
                    case DATA_FLAG:
                        dataFile = InputArgs.getPathFile(args[++i]);
                        break;
                    case OUT_FLAG:
                        dirOut = InputArgs.getPathDir(args[++i]);
                        break;
                    case DELIM_FLAG:
                        delim = InputArgs.getCharacter(args[++i]);
                        break;
                    case VERBOSE_FLAG:
                        verbose = Boolean.TRUE;
                        break;
                    case FAITHFUL_FLAG:
                        faithful = Boolean.TRUE;
                        break;
                    case PENALTY_DISCOUNT_FLAG:
                        penaltyDiscount = Double.parseDouble(args[++i]);
                        break;
                    case PATTERN_STORE_FLAG:
                        numPatternsToStore = Integer.parseInt(args[++i]);
                        break;
                    default:
                        throw new IllegalArgumentException(String.format("Unknown flag: %s.\n", flag));
                }
            }
            if (dataFile == null) {
                throw new IllegalArgumentException(String.format("Switch %s is required.", DATA_FLAG));
            }
            if (dirOut == null) {
                throw new IllegalArgumentException(String.format("Switch %s is required.", OUT_FLAG));
            }
            if (delim == ((char) -1)) {
                throw new IllegalArgumentException(String.format("Switch %s is required.", DELIM_FLAG));
            }
        } catch (Exception exception) {
            exception.printStackTrace(System.err);
            System.exit(-128);
        }

        try {
            TetradDataSet dataset = new TetradDataSet();
            dataset.readDataFile(dataFile.toFile(), delim);

            Parameters p = ParameterFactory.buildGesParameters(penaltyDiscount, numPatternsToStore, faithful, verbose);
            Algorithm algorithm = new TetradAlgorithm();
            algorithm.run(GesGes3.class, null, dataset, p);
            GraphIO.write(algorithm.getGraph(), false, new File(dirOut.toFile(), GesGes3.class.getName() + "_grph.txt"));
        } catch (Exception exception) {
            exception.printStackTrace(System.err);
        }
    }

}
