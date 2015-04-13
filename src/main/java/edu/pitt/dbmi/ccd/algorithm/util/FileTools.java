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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

/**
 *
 * Mar 4, 2015 2:40:36 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class FileTools {

    private static final byte NEW_LINE = '\n';

    private static final byte TAB = '\t';

    private static final byte SPACE = ' ';

    public static int countColumn(Path file, byte delimiter) throws IOException {
        int count = 0;

        try (FileChannel fc = new RandomAccessFile(file.toFile(), "r").getChannel()) {
            MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            while (buffer.hasRemaining()) {
                byte currentChar = buffer.get();
                if (currentChar == delimiter || currentChar == NEW_LINE) {
                    count++;
                    if (currentChar == NEW_LINE) {
                        break;
                    }
                }
            }
        }

        return count;
    }

    public static int countLine(Path file) throws IOException {
        int count = 0;

        try (FileChannel fc = new RandomAccessFile(file.toFile(), "r").getChannel()) {
            MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            byte prevChar = -1;
            while (buffer.hasRemaining()) {
                byte currentChar = buffer.get();
                if (currentChar == NEW_LINE && prevChar != NEW_LINE) {
                    count++;
                }
                prevChar = currentChar;
            }
        }

        return count;
    }

}
