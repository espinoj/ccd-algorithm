package edu.pitt.dbmi.ccd.algorithm.tetrad.data;

import edu.cmu.tetrad.data.DataSet;
import java.io.File;
import java.io.IOException;

/**
 *
 * Feb 16, 2015 9:23:38 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class TetradDataSet implements edu.pitt.dbmi.ccd.algorithm.data.Dataset<DataSet> {

    private DataSet dataSet;

    public TetradDataSet() {
    }

    public void readDataFile(File data, char delimiter) throws IOException {
        if (data == null) {
            throw new IllegalArgumentException("Dataset file is required.");
        }
        dataSet = DataSetIO.read(delimiter, data);
//        dataSet = DataSetIO.read(delimiter, data, true);
    }

    @Override
    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

}
