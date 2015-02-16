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
package edu.pitt.dbmi.ccd.algorithm.tetrad.algo;

import edu.pitt.dbmi.ccd.algorithm.data.Parameters;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Feb 16, 2015 2:07:26 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public abstract class TetradParameters implements Parameters {

    protected final Map<String, Object> params;

    public TetradParameters() {
        this.params = new HashMap<>();
    }

    @Override
    public Object getParameter(String name) {
        return params.get(name);
    }

}
