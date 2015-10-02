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

import edu.cmu.tetrad.search.FastImages2;
import edu.pitt.dbmi.ccd.algorithm.Algorithm;
import edu.pitt.dbmi.ccd.algorithm.data.Dataset;
import edu.pitt.dbmi.ccd.algorithm.data.Parameters;
import edu.pitt.dbmi.ccd.algorithm.tetrad.algo.TetradAlgorithm;
import edu.pitt.dbmi.ccd.algorithm.tetrad.data.TetradDataSet;
import edu.pitt.dbmi.ccd.algorithm.tetrad.graph.GraphIO;
import edu.pitt.dbmi.ccd.algorithm.util.ArgsUtil;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * Sep 29, 2015 1:43:53 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class FastImagesApp {

    private static final String USAGE = "Usage: java -cp ccd-algorithm.jar "
            + "edu.pitt.dbmi.ccd.algorithm.tetrad.FastImagesApp "
            + "--data-dir"
            + "[--prefix]"
            + "[--out <dir>] "
            + "[--delimiter <char>] "
            + "[--penalty-discount <double>] "
            + "[--depth <int>] "
            + "[--verbose] "
            + "[--graphml] "
            + "[--out-filename <string>]";

    private static final String DATA_DIR_PARAM = "--data-dir";

    private static final String PREFIX_PARAM = "--prefix";

    private static final String OUT_PARAM = "--out";

    private static final String DELIM_PARAM = "--delimiter";

    private static final String PENALTY_DISCOUNT_PARAM = "--penalty-discount";

    private static final String DEPTH_PARAM = "--depth";

    private static final String VERBOSE_FLAG = "--verbose";

    private static final String OUT_FILENAME_PARAM = "--out-filename";

    private static final String GRAPHML_FLAG = "--graphml";

    private static final String HELP_INFO = "================================================================================\n"
            + String.format("%-18s\t%s\n", DATA_DIR_PARAM, "Directory containing the data.")
            + String.format("%-18s\t%s\n", PREFIX_PARAM, "The prefix of the image files.  The subfix should be the file number.  For an example: data.txt.1,data.txt.2,data.txt.3; the prefix is data.txt")
            + String.format("%-18s\t%s\n", OUT_PARAM, "Directory where results will be written to.  Current working directory is the default.")
            + String.format("%-18s\t%s\n", DELIM_PARAM, "A single character used to separate data in a line.  A tab character is the default.")
            + String.format("%-18s\t%s\n", PENALTY_DISCOUNT_PARAM, "Penality discount.  The default value is 2.0.")
            + String.format("%-18s\t%s\n", DEPTH_PARAM, "The search depth.  The default value is -1, minimum value is -1.")
            + String.format("%-18s\t%s\n", VERBOSE_FLAG, "Output additional information from the algorithm.  No additional information by default.")
            + String.format("%-18s\t%s\n", GRAPHML_FLAG, "Output graphml formatted file.")
            + String.format("%-18s\t%s\n", OUT_FILENAME_PARAM, "The base name of the output files.  The algorithm's name with an integer timestamp is the default.");

    private static Path dataDir;

    private static String prefix;

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

        dataDir = null;
        prefix = null;
        dirOut = Paths.get(".");
        delimiter = '\t';
        penaltyDiscount = 2.0;
        depth = -1;
        verbose = Boolean.FALSE;
        baseOutputFileName = null;
        isOutputGraphml = Boolean.FALSE;
        try {
            for (int i = 0; i < args.length; i++) {
                String flag = args[i];
                switch (flag) {
                    case DATA_DIR_PARAM:
                        dataDir = ArgsUtil.getPathDir(ArgsUtil.getParam(args, ++i, flag), true);
                        break;
                    case PREFIX_PARAM:
                        prefix = ArgsUtil.getParam(args, ++i, flag);
                        break;
                    case OUT_PARAM:
                        dirOut = ArgsUtil.getPathDir(ArgsUtil.getParam(args, ++i, flag), false);
                        break;
                    case DELIM_PARAM:
                        delimiter = ArgsUtil.getCharacter(ArgsUtil.getParam(args, ++i, flag));
                        break;
                    case PENALTY_DISCOUNT_PARAM:
                        penaltyDiscount = ArgsUtil.getDoubleValue(args, ++i, flag);
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
            if (dataDir == null) {
                throw new IllegalArgumentException(String.format("Switch %s is required.", DATA_DIR_PARAM));
            }
        } catch (Exception exception) {
            exception.printStackTrace(System.err);
            System.exit(-127);
        }

        List<Path> files = new LinkedList<>();
        try {
            if (Files.notExists(dirOut)) {
                Files.createDirectory(dirOut);
            }

            files.addAll(getFiles(dataDir, prefix));
        } catch (IOException exception) {
            exception.printStackTrace(System.err);
            System.exit(-128);
        }

        System.out.println(files);

        // create output file
        if (baseOutputFileName == null) {
            baseOutputFileName = String.format("fast_imaGES_%d", System.currentTimeMillis());
        }
        String outputFileName = baseOutputFileName + ".txt";

        Path outputFile = Paths.get(dirOut.toString(), outputFileName);
        try (PrintStream stream = new PrintStream(new BufferedOutputStream(Files.newOutputStream(outputFile, StandardOpenOption.CREATE)))) {
            printOutParameters(stream);
            stream.flush();

            // read in the datasets
            List<Dataset> datasets = new LinkedList<>();
            for (Path dataFile : files) {
                TetradDataSet dataset = new TetradDataSet();
                dataset.readDataFile(dataFile, delimiter, true);
                datasets.add(dataset);
            }

            // build the parameters
            Parameters params = ParameterFactory.buildGesParameters(penaltyDiscount, depth, true, verbose);

            Algorithm algorithm = new TetradAlgorithm();
            algorithm.setExecutionOutput(stream);
            algorithm.run(FastImages2.class, null, datasets, params);
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
        stream.println("Graph Parameters:");
        stream.println(String.format("penalty discount = %f", penaltyDiscount));
        stream.println(String.format("depth = %s", depth));
        stream.println();
    }

    private static List<Path> getFiles(Path dir, String prefix) throws IOException {
        List<Path> files = new LinkedList<>();

        if (prefix == null) {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (attrs.isRegularFile()) {
                        files.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (attrs.isRegularFile()) {
                        String fileName = file.getFileName().toString();
                        if (fileName.startsWith(prefix)) {
                            files.add(file);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }

        return files;
    }

}
