package edu.pitt.dbmi.ccd.algorithm;

import edu.pitt.dbmi.ccd.algorithm.data.Dataset;
import edu.pitt.dbmi.ccd.algorithm.data.Parameters;

/**
 *
 * Feb 16, 2015 9:19:04 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public interface Algorithm {

    public void run(Class algorithm, Class testOfIndependence, Dataset dataset, Parameters parameters) throws AlgorithmException;

}
