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
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * Feb 17, 2015 9:21:53 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class PcStableApp {

    private static final String USAGE = "java -cp ccd-algorithm-<version>.jar "
            + "edu.pitt.dbmi.ccd.algorithm.tetrad.PcStableApp --data <file> "
            + "--alpha <double> --depth <int>";

    private static final String DATA_FLAG = "--data";
    private static final String ALPHA_FLAG = "--alpha";
    private static final String DEPTH_FLAG = "--depth";
    private static final String OUT_FLAG = "--out";

    private static final int NUM_REG_ARGS = 8;

    private static File dataFile;
    private static File dirOut;
    private static Double alpha;
    private static Integer depth;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args == null || args.length != NUM_REG_ARGS) {
            System.err.println(USAGE);
            System.exit(-127);
        }

        try {
            for (int i = 0; i < args.length; i++) {
                String flag = args[i];
                switch (flag) {
                    case DATA_FLAG:
                        dataFile = getFile(args[++i]);
                        break;
                    case ALPHA_FLAG:
                        alpha = new Double(args[++i]);
                        break;
                    case DEPTH_FLAG:
                        depth = new Integer(args[++i]);
                        break;
                    case OUT_FLAG:
                        dirOut = getDir(args[++i]);
                        break;
                    default:
                        throw new Exception(String.format("Unknown flag: %s.\n", flag));
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace(System.err);
            System.exit(-128);
        }

        try {
            TetradDataSet dataset = new TetradDataSet();
            dataset.readDataFile(dataFile, ' ');

            Parameters p = ParameterFactory.buildPcStableParameters(alpha, depth, true);

            Algorithm algorithm = new TetradAlgorithm();
            if (dataset.getDataSet().isContinuous()) {
                algorithm.run(PcStable.class, IndTestFisherZ.class, dataset, p);
            } else {
                algorithm.run(PcStable.class, IndTestChiSquare.class, dataset, p);
            }
            GraphIO.write(algorithm.getGraph(), false, new File(dirOut, "pc_stable_grph.txt"));
        } catch (Exception exception) {
            exception.printStackTrace(System.err);
        }
    }

    public static File getFile(String fileName) throws FileNotFoundException {
        Path path = Paths.get(fileName);
        if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                throw new FileNotFoundException(String.format("'%s' is not a file.\n", fileName));
            }
            return path.toFile();
        } else {
            throw new FileNotFoundException(String.format("Unable to find file: %s.\n", fileName));
        }
    }

    public static File getDir(String fileName) throws FileNotFoundException {
        Path path = Paths.get(fileName);
        if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            if (!Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                throw new FileNotFoundException(String.format("'%s' is not a directory.\n", fileName));
            }
            return path.toFile();
        } else {
            throw new FileNotFoundException(String.format("Unable to find directory: %s.\n", fileName));
        }
    }

}
