## What is CCD Algorithm

CCD Algorithm is a Java application that provides an API interface to run a collection of algorithms that are found in Tetrad and others. 


## How can I use it?

#### Required Dependencies
* colt-1.2.0.jar
* commons-collections-3.1.jar
* commons-math3-3.3.jar
* jama-1.0.2.jar
* lib-tetrad-0.4.1.jar
* mtj-0.9.14.jar
* pal-1.5.1.jar
* xom-1.1.jar

Put all the dependency jars along with ccd-algorithm-0.4.3.jar in a lib folder

#### Run as an Application

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
