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

import edu.pitt.dbmi.ccd.algorithm.data.Parameters;
import edu.pitt.dbmi.ccd.algorithm.tetrad.algo.param.GesParams;
import edu.pitt.dbmi.ccd.algorithm.tetrad.algo.param.PcStableParams;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Feb 16, 2015 9:52:23 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class ParameterFactory {

    private ParameterFactory() {
    }

    /**
     * Wraps Map into Parameters.
     *
     * @param parameters Map object
     * @return Parameters object
     */
    public static final Parameters wrapParameters(final Map<String, Object> parameters) {
        if (parameters == null) {
            return new Parameters() {
                Map<String, Object> params = new HashMap<>();

                @Override
                public Object getParameter(String name) {
                    return parameters.get(name);
                }
            };
        } else {
            return new Parameters() {
                Map<String, Object> params = parameters;

                @Override
                public Object getParameter(String name) {
                    return parameters.get(name);
                }
            };
        }

    }

    public static Parameters buildPcStableParameters(Double alpha, Integer depth, Boolean verbose) {
        Map<String, Object> params = new HashMap<>();
        params.put(PcStableParams.ALPHA, alpha);
        params.put(PcStableParams.DEPTH, depth);
        params.put(PcStableParams.VERBOSE, verbose);

        return wrapParameters(params);
    }

    public static Parameters buildGesParameters(Double penaltyDiscount, Boolean faithful, Boolean verbose) {
        final Map<String, Object> params = new HashMap<>();
        params.put(GesParams.FAITHFUL, faithful);
        params.put(GesParams.PENALTY_DISCOUNT, penaltyDiscount);
        params.put(GesParams.VERBOSE, verbose);

        return wrapParameters(params);
    }

}
