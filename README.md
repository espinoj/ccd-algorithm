## What is CCD Algorithm

CCD Algorithm is a Java application that provides an CLI and API for algorithms produced by the Center for Causal Discovery.  The application currently includes the algorithm(s):  FGS (Fast Greedy Search)


## How can I use it?

#### Run as an Application

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

##### Create Simulated Dataset
```java
// create dataset with 20 variables, 100 cases, and 1 edge per node
java -cp lib/ccd-algorithm-0.4.3.jar edu.pitt.dbmi.ccd.algorithm.tetrad.SimulateDataApp --var 20 --case 100 --edge 1 --out output/
```

##### Run FGS
```java
java -cp lib/ccd-algorithm-0.4.3.jar edu.pitt.dbmi.ccd.algorithm.tetrad.FgsApp --data data.txt --delimiter $'\t' --penalty-discount 4.0 --depth 3 --verbose --out output/
```

#### Use as an API

##### Input
```java
boolean continuous = true;
File dataFile = new File("data.txt");

// read in tab-delimited dataset
TetradDataSet dataset = new TetradDataSet();
dataset.readDataFile(dataFile, '\t', continuous);
```

##### Run FGS
```java
Double penaltyDiscount = 4.0;
Integer depth = 3;
Boolean faithful = Boolean.TRUE;
Boolean verbose = Boolean.TRUE;

// create parameters for FGS
Parameters params = ParameterFactory.buildFgsParameters(penaltyDiscount, depth, faithful, verbose);

// run the FGS algorithm
Algorithm algorithm = new TetradAlgorithm();
algorithm.setExecutionOutput(System.out);  // write verbose messages to standard out
algorithm.run(FastGes.class, null, dataset, params);

```

##### Output Graph
```java
Path outputFile = Paths.get("fgs_graph.txt");

// write out graph
Graph graph = algorithm.getGraph();
GraphIO.write(graph, GraphIO.GraphOutputType.TETRAD, outputFile);
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
