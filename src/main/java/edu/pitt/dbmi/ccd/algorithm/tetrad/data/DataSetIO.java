package edu.pitt.dbmi.ccd.algorithm.tetrad.data;

import edu.cmu.tetrad.data.DataReader;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.data.DataWriter;
import edu.cmu.tetrad.data.DelimiterType;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class that handles reading and writing dataset.
 *
 * Feb 13, 2015 1:52:04 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
public class DataSetIO {

    public static final char TAB_DELIMITER = '\t';
    public static final char SPACE_DELIMITER = ' ';
    public static final char COMMA_DELIMITER = ',';
    public static final char COLON_DELIMITER = ':';

    public static void write(DataSet dataSet, char delimiter, File file) throws IOException {
        Path path = file.toPath();
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            DataWriter.writeRectangularData(dataSet, writer, delimiter);
        }
    }

    public static DataSet read(char delimiter, File file) throws IOException {
        DataReader dataReader = new DataReader();
        switch (delimiter) {
            case TAB_DELIMITER:
                dataReader.setDelimiter(DelimiterType.TAB);
                break;
            case COLON_DELIMITER:
                dataReader.setDelimiter(DelimiterType.COLON);
                break;
            case COMMA_DELIMITER:
                dataReader.setDelimiter(DelimiterType.COMMA);
                break;
            default:
                dataReader.setDelimiter(DelimiterType.WHITESPACE);
                break;
        }
        return dataReader.parseTabular(file);
    }

}
