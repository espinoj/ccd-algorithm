## What is CCD Algorithm

CCD Algorithm is a Java application that provides an API interface to run a collection of algorithms that are found in Tetrad and others. 


## How can I use it?

#### Required Dependencies
* colt-1.2.0.jar
* commons-collections-3.1.jar
* commons-math3-3.3.jar
* jama-1.0.2.jar
* lib-tetrad-5.1.0-11-SNAPSHOT.jar
* mtj-0.9.14.jar
* xom-1.1.jar

Put all the dependency jars along with ccd-algorithm-1.0-SNAPSHOT.jar in a lib folder

#### Run as an Application
java -cp lib/ccd-algorithm-1.0-SNAPSHOT.jar edu.pitt.dbmi.ccd.algorithm.tetrad.PcStableApp --data data.txt --alpha 0.0001 --depth 3 --out output/

#### Use as an API

##### Input
```java
File dataFile = new File("data.txt");

// read in tab-delimited dataset
TetradDataSet dataset = new TetradDataSet();
dataset.readDataFile(dataFile, '\t');
```

##### PC-Stable
```java
double alpha = 0.0001;
int depth = 3;
boolean verbose = true;

// create parameters for PC-Stable
Parameters params = ParameterFactory.buildPcStableParameters(alpha, depth, verbose);

// run the PC-Stable algorithm
Algorithm algorithm = new TetradAlgorithm();
if (dataset.getDataSet().isContinuous()) {
    algorithm.run(PcStable.class, IndTestFisherZ.class, dataset, params);
} else {
    algorithm.run(PcStable.class, IndTestChiSquare.class, dataset, params);
}
```

##### GES
```java
double penaltyDiscount = 2.0;
int numPatternsToStore = 0;
boolean faithful = true;
boolean verbose = true;

// create parameters for GES
Parameters params = ParameterFactory.buildGesParameters(penaltyDiscount, numPatternsToStore, faithful, verbose);

// run the GES algorithm
Algorithm algorithm = new TetradAlgorithm();
algorithm.run(GesGes.class, null, dataset, params);
```

##### Output
```java
File outputFile = new File("graph_out.txt");
boolean writeAsXml = false;

// write out graph
Graph graph = algorithm.getGraph();
GraphIO.write(graph, writeAsXml, outputFile);
```
