package edu.pitt.dbmi.ccd.algorithm.data;

/**
 *
 * Feb 16, 2015 9:22:23 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public interface Dataset<T> {

    public T getDataSet();

    public boolean isContinuous();

}
