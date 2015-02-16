package edu.pitt.dbmi.ccd.algorithm.tetrad.data;

import edu.cmu.tetrad.data.DataSet;

/**
 *
 * Feb 16, 2015 9:23:38 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class TetradDataSet implements edu.pitt.dbmi.ccd.algorithm.data.Dataset<DataSet> {

    private final DataSet dataSet;

    public TetradDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public DataSet getDataSet() {
        return dataSet;
    }

}
