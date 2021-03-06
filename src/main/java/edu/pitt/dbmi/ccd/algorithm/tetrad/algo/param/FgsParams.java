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
package edu.pitt.dbmi.ccd.algorithm.tetrad.algo.param;

/**
 *
 * Feb 26, 2015 4:29:57 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public interface FgsParams extends TetradParams {

    /**
     * True if we assume the graph is faithful (exclude zero correlation edges).
     */
    public static String FAITHFUL = "faithful";

    /**
     * The BIC penalty is multiplied by this (for continuous variables).
     */
    public static String PENALTY_DISCOUNT = "penaltyDiscount";

    public static String DEPTH = "depth";

}
