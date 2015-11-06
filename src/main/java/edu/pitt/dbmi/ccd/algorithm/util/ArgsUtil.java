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
package edu.pitt.dbmi.ccd.algorithm.util;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility class to help parse inputs from command line.
 *
 * Feb 26, 2015 3:21:51 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class ArgsUtil {

    private ArgsUtil() {
    }

    public static List<Path> getFiles(String... files) throws FileNotFoundException {
        List<Path> fileList = new LinkedList<>();

        for (String file : files) {
            fileList.add(getPathFile(file));
        }

        return fileList;
    }

    public static double getDoubleValue(String[] args, int index, String flag) {
        return Double.parseDouble(getParam(args, index, flag));
    }

    public static int getIntValue(String[] args, int index, String flag, int minimum) {
        int value = getIntValue(args, index, flag);
        if (value < minimum) {
            throw new IllegalArgumentException(
                    String.format("Value for switch %s must be a minimum of %d.", flag, minimum));
        }

        return value;
    }

    public static int getIntValue(String[] args, int index, String flag) {
        return Integer.parseInt(getParam(args, index, flag));
    }

    public static String getParam(String[] args, int index, String flag) {
        if (index >= args.length || args[index] == null || args[index].length() == 0) {
            throw new IllegalArgumentException(
                    String.format("A value is required for switch %s.", flag));
        }
        return args[index];
    }

    public static char getCharacter(String character) {
        if (character.length() == 1) {
            return character.charAt(0);
        } else {
            throw new IllegalArgumentException(String.format("'%s' must be a single character.", character));
        }
    }

    public static Path getPathDir(String dir, boolean required) throws FileNotFoundException {
        Path path = Paths.get(dir);

        if (Files.exists(path)) {
            if (!Files.isDirectory(path)) {
                throw new FileNotFoundException(String.format("'%s' is not a directory.\n", dir));
            }
        } else {
            if (required) {
                throw new FileNotFoundException(String.format("Directory '%s' does not exist.\n", dir));
            }
        }

        return path;
    }

    public static Path getPathFile(String file) throws FileNotFoundException {
        Path path = Paths.get(file);

        if (Files.exists(path)) {
            if (!Files.isRegularFile(path)) {
                throw new FileNotFoundException(String.format("'%s' is not a file.\n", file));
            }
        } else {
            throw new FileNotFoundException(String.format("File '%s' does not exist.\n", file));
        }

        return path;
    }

}
