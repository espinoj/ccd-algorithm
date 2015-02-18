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
package edu.pitt.dbmi.ccd.algorithm.tetrad.util;

import edu.cmu.tetrad.data.CovarianceMatrix2;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.search.IndTestChiSquare;
import edu.cmu.tetrad.search.IndTestFisherZ;
import edu.cmu.tetrad.search.IndependenceTest;
import edu.pitt.dbmi.ccd.algorithm.data.Parameters;
import edu.pitt.dbmi.ccd.algorithm.tetrad.algo.param.IndTestParams;

/**
 *
 * Feb 16, 2015 2:54:41 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class TetradIndependenceTestFactory {

    private TetradIndependenceTestFactory() {
    }

    public static IndependenceTest buildIndependenceTest(Class independenceTest, DataSet data, Parameters parameters) {
        IndependenceTest test = null;

        Double d = (Double) parameters.getParameter(IndTestParams.ALPHA);
        double alpha = (d == null) ? 0.0001 : d;
        if (IndTestFisherZ.class == independenceTest) {
            test = new IndTestFisherZ(new CovarianceMatrix2(data), alpha);
        } else if (IndTestChiSquare.class == independenceTest) {
            test = new IndTestChiSquare(data, alpha);
        }

        return test;
    }

}
