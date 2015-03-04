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

import edu.pitt.dbmi.ccd.algorithm.tetrad.data.DataSetFactory;
import edu.pitt.dbmi.ccd.algorithm.tetrad.data.DataSetIO;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * Mar 4, 2015 9:45:48 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class SimulateDataApp {

    private static final String USAGE = "java -cp ccd-algorithm.jar "
            + "edu.pitt.dbmi.ccd.algorithm.tetrad.SimulateDataApp "
            + "--case <int> --var <int> --edge <int> --out <file>";

    private static final String CASE_FLAG = "--case";

    private static final String VAR_FLAG = "--var";

    private static final String EDGE_FLAG = "--edge";

    private static final String OUT_FLAG = "--out";

    private static final int NUM_REQ_ARGS = 8;

    private static int numOfCases;

    private static int numOfVariables;

    private static int numOfEdges;

    private static Path fileOut;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args == null || args.length != NUM_REQ_ARGS) {
            System.err.println(USAGE);
            System.exit(-127);
        }

        try {
            for (int i = 0; i < args.length; i++) {
                String flag = args[i];
                switch (flag) {
                    case CASE_FLAG:
                        numOfCases = Integer.parseInt(args[++i]);
                        break;
                    case VAR_FLAG:
                        numOfVariables = Integer.parseInt(args[++i]);
                        break;
                    case EDGE_FLAG:
                        numOfEdges = Integer.parseInt(args[++i]);
                        break;
                    case OUT_FLAG:
                        fileOut = Paths.get(args[++i]);
                        break;
                    default:
                        throw new IllegalArgumentException(String.format("Unknown flag: %s.\n", flag));
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace(System.err);
            System.exit(-128);
        }

        try {
            DataSetIO.write(DataSetFactory.buildSemSimulateDataAcyclic(numOfVariables, (double) numOfEdges, numOfCases),
                    '\t', fileOut.toFile());
        } catch (IOException exception) {
            exception.printStackTrace(System.err);
        }
    }

}
