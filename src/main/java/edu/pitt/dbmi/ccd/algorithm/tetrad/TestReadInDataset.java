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

import edu.cmu.tetrad.data.DataSet;
import edu.pitt.dbmi.ccd.algorithm.tetrad.data.DataSetIO;
import java.io.IOException;
import java.nio.file.Paths;

/**
 *
 * Mar 4, 2015 11:16:37 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class TestReadInDataset {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            DataSet dataSet = DataSetIO.read('\t', Paths.get(args[0]).toFile());
//            DataSet dataSet = DataSetIO.read('\t', Paths.get(args[0]).toFile(), true);
        } catch (IOException exception) {
            exception.printStackTrace(System.err);
        }
    }

}
