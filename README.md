## What is CCD Algorithm

CCD Algorithm is a Java application that provides a command-line interface (CLI) and application programming interface (API) for causal discovery algorithms produced by the Center for Causal Discovery.  The application currently includes the algorithm(s):  
* FGS (Fast Greedy Search) - this is an optimization of the Greedy Equivalence Search algorithm	(GES,	Meek	1995;	Chickering	2003).  The optimizations are described in http://arxiv.org/ftp/arxiv/papers/1507/1507.07749.pdf

Causal discovery algorithms are a class of search algorithms that explore a space of graphical causal models, i.e., graphical models where directed edges imply causation, for a model (or models) that are a good fit for a dataset.  We suggest that newcomers to the field review Causation, Prediction and Search by Spirtes, Glymour and Scheines for a primer on the subject.

Causal discovery algorithms allow a user to uncover the causal relationships between variables in a dataset.  These discovered causal relationships may be used further--understanding the underlying the processes of a system (e.g., the metabolic pathways of an organism), hypothesis generation (e.g., variables that best explain an outcome), guide experimentation (e.g., what gene knockout experiments should be performed) or prediction (e.g. parameterization of the causal graph using data and then using it as a classifier).


## How can I use it?

### Run as an Application

#### Create Simulated Dataset
```java
// create dataset with 20 variables, 100 cases, and 1 edge per node
java -cp ccd-algorithm-0.4.3.jar edu.pitt.dbmi.ccd.algorithm.tetrad.SimulateDataApp --var 20 --case 100 --edge 1 --out output/
```
The program will simulate at dataset (100 cases) derived from a graph of 20 nodes with an average of 1 edge per node to a directory called output.  The name of the file has the following format 
```
sim_data_<# of variables>vars_<# of cases>cases_<system timestamp>.txt"
```

#### Run FGS
```java
java -cp ccd-algorithm-0.4.3.jar edu.pitt.dbmi.ccd.algorithm.tetrad.FgsApp --data <file to analyze e.g., Retention.txt or simulated data from above> --delimiter $'\t' --penalty-discount 4.0 --depth 3 --verbose --out output/
```

The program will output the results of the FGS search procedure as a text file (in this example to the directory 'output').   The beginning of the file contains the algorithm parameters used in the search.  

In FGS, "Elapsed getEffectEdges = XXms" refers to the amount of time it took to evaluate all pairs of variables for correlation.  The file then details each step taken in the greedy search procedure i.e., insertion or deletion of edges based on a scoring function (i.e., BIC score difference for each chosen search operation).

The end of the file contains the causal graph from the search procedure.  Here is a key to the edge types
```
A---B There is causal relationship between variable A and B but we cannot determine the direction of the relationship
A-->B There is a causal relationship from variable A to B
```

### Run an example output using known data
Download the this file, [Retention.txt](http://www.ccd.pitt.edu/wp-content/uploads/files/Retention.txt), which is a dataset containing information on college graduation and used in the publication "What Do College Ranking Data Tell Us About Student Retention?" by Drudzel and Glymour, 1994.


Run FGS
```java
java -cp ccd-algorithm-0.4.3.jar edu.pitt.dbmi.ccd.algorithm.tetrad.FgsApp --data Retention.txt --delimiter $'\t' --penalty-discount 4.0 --depth 3 --verbose --out output/
```

Inspect the output which should show a graph with the following edges. 
```
Graph Edges: 
1. fac_salary --- spending_per_stdt
2. spending_per_stdt --> rjct_rate
3. spending_per_stdt --- stdt_tchr_ratio
4. stdt_accept_rate --- fac_salary
5. stdt_clss_stndng --> rjct_rate
6. tst_scores --- fac_salary
7. tst_scores --- grad_rate
8. tst_scores --- spending_per_stdt
9. tst_scores --- stdt_clss_stndng
```



#### Use as an API

```java
import edu.cmu.tetrad.search.FastGes;
import edu.pitt.dbmi.ccd.algorithm.data.Parameters;
import edu.pitt.dbmi.ccd.algorithm.tetrad.ParameterFactory;
import edu.pitt.dbmi.ccd.algorithm.tetrad.algo.TetradAlgorithm;
import edu.pitt.dbmi.ccd.algorithm.tetrad.data.TetradDataSet;

import java.io.File;

public class UseAsApi {

    public static void main(String[] args) throws Exception {

        boolean continuous = true;
        File dataFile = new File("data.txt");

        // read in a tab-delimited dataset
        TetradDataSet dataset = new TetradDataSet();
        dataset.readDataFile(dataFile, '\t', continuous);

        // declare parameters
        Double penaltyDiscount = 4.0;
        Integer depth = 3;
        Boolean faithful = Boolean.TRUE;
        Boolean verbose = Boolean.TRUE;

        // create parameters object for FGS
        Parameters params = ParameterFactory.buildFgsParameters(penaltyDiscount, depth, faithful, verbose);

        // run the FGS algorithm
        Algorithm algorithm = new TetradAlgorithm();
        algorithm.setExecutionOutput(System.out);  // write verbose messages to standard out
        algorithm.run(FastGes.class, null, dataset, params);

    }
}
```

#### Command line interface usage
```
Usage: java -cp ccd-algorithm.jar edu.pitt.dbmi.ccd.algorithm.tetrad.FgsApp --data <file> [--out <dir>] [--delimiter <char>] [--penalty-discount <double>] [--depth <int>] [--verbose] [--graphml] [--out-filename <string>]
================================================================================
--data            	The input data file.
--out             	Directory where results will be written to.  Current working directory is the default.
--delimiter       	A single character used to separate data in a line.  A tab character is the default.
--penalty-discount	Penality discount.  The default value is 4.0.
--depth           	The search depth.  The default value is 3, minimum value is -1.
--verbose         	Output additional information from the algorithm.  No additional information by default.
--graphml         	Output graphml formatted file.
--out-filename    	The base name of the output files.  The algorithm's name with an integer timestamp is the default.
```

## Dependencies
(not necessary if built with the -Denv=cli option which creates an uber jar)
* colt-1.2.0.jar
* commons-collections-3.1.jar
* commons-math3-3.3.jar
* jama-1.0.2.jar
* lib-tetrad-0.4.1.jar
* mtj-0.9.14.jar
* pal-1.5.1.jar
* xom-1.1.jar
