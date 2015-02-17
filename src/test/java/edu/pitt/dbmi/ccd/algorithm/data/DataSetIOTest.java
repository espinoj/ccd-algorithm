package edu.pitt.dbmi.ccd.algorithm.data;

import edu.cmu.tetrad.data.DataSet;
import edu.pitt.dbmi.ccd.algorithm.tetrad.data.DataSetFactory;
import edu.pitt.dbmi.ccd.algorithm.tetrad.data.DataSetIO;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * Feb 16, 2015 8:18:50 AM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class DataSetIOTest {

    public DataSetIOTest() {
    }

    /**
     * Test of write method, of class DataSetIO.
     */
    @Test
    @Ignore
    public void testWrite() throws Exception {
        System.out.println("write");
        char delimiter = '\t';
        Path path = Paths.get("test/data/data.txt");
        int numofVars = 100;
        double edgesPerNode = 1.0;
        int numOfCases = 50;
        DataSet dataSet = DataSetFactory.buildSemSimulateDataAcyclic(numofVars, edgesPerNode, numOfCases);
        DataSetIO.write(dataSet, delimiter, path.toFile());
    }

    /**
     * Test of read method, of class DataSetIO.
     */
    @Test
    @Ignore
    public void testRead() throws Exception {
        System.out.println("read");
        char delimiter = ' ';
        File file = null;
        DataSet expResult = null;
        DataSet result = DataSetIO.read(delimiter, file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
