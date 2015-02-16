package edu.pitt.dbmi.ccd.algorithm.data;

/**
 *
 * Feb 16, 2015 9:45:21 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public interface Parameters {

    public Object getParameter(String name);

    public void addParameter(String name, Object value);

}
