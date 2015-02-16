package edu.pitt.dbmi.ccd.algorithm.tetrad.algo;

import edu.cmu.tetrad.data.CovarianceMatrix2;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.search.GesGes;
import edu.cmu.tetrad.search.IndTestFisherZ;
import edu.cmu.tetrad.search.PcStable;
import edu.pitt.dbmi.ccd.algorithm.Algorithm;
import edu.pitt.dbmi.ccd.algorithm.data.Dataset;
import edu.pitt.dbmi.ccd.algorithm.data.Parameters;
import edu.pitt.dbmi.ccd.algorithm.tetrad.data.TetradDataSet;

/**
 *
 * Feb 16, 2015 9:19:37 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class TetradAlgorithm implements Algorithm {

    @Override
    public void run(Class algorithm, Class testOfIndependence, Dataset dataset, Parameters parameters) {
        if (algorithm == null) {
            throw new IllegalArgumentException("Algorithm class is required.");
        }
        if (dataset == null || !(dataset instanceof TetradDataSet)) {
            throw new IllegalArgumentException("TetradDataSet is required.");
        }

        DataSet dataSet = (DataSet) dataset.getDataSet();
        if (algorithm == PcStable.class) {
            if (testOfIndependence == null) {
                throw new IllegalArgumentException("Test of independence is required.");
            }

            if (IndTestFisherZ.class == testOfIndependence) {
                double alpha = 0.001;
                IndTestFisherZ test = new IndTestFisherZ(new CovarianceMatrix2(dataSet), alpha);

                PcStable pcStable = new PcStable(test);
                pcStable.setVerbose(true);
            }

        } else if (algorithm == GesGes.class) {

        } else {
            throw new IllegalArgumentException(String.format("Unknow algorithm class %s.", algorithm.getName()));
        }
    }

}
