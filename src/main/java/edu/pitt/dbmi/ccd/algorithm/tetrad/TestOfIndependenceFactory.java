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
import edu.cmu.tetrad.search.IndependenceTest;
import edu.pitt.dbmi.ccd.algorithm.data.Parameters;
import edu.pitt.dbmi.ccd.algorithm.data.TestOfIndependence;
import edu.pitt.dbmi.ccd.algorithm.tetrad.util.TetradIndependenceTestFactory;

/**
 *
 * Feb 17, 2015 10:48:24 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class TestOfIndependenceFactory {

    private TestOfIndependenceFactory() {
    }

    public static TestOfIndependence<IndependenceTest> buildTetradIndTest(final Class tetradIndTest, final DataSet data, final Parameters parameters) {
        return new TestOfIndependence<IndependenceTest>() {
            private final IndependenceTest test = TetradIndependenceTestFactory.buildIndependenceTest(tetradIndTest, data, parameters);

            @Override
            public IndependenceTest getIndependenceTest() {
                return test;
            }
        };
    }

}
