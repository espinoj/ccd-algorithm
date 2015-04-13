package edu.pitt.dbmi.ccd.algorithm.tetrad.data;

import edu.cmu.tetrad.data.DataSet;
import edu.pitt.dbmi.ccd.algorithm.data.Dataset;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * Feb 16, 2015 9:23:38 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class TetradDataSet implements Dataset<DataSet> {

    private DataSet dataSet;

    private boolean continuous;

    public TetradDataSet() {
    }

    public void readDataFile(Path data, char delimiter, boolean continuous) throws IOException {
        if (data == null) {
            throw new IllegalArgumentException("Dataset file is required.");
        }
        this.dataSet = DataSetIO.read(data, continuous, delimiter);
        this.continuous = continuous;
    }

    @Override
    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public boolean isContinuous() {
        return continuous;
    }

    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }

}
