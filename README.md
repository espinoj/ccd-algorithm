## What is CCD Algorithm

CCD Algorithm is a Java application that provides an API interface to run a collection of algorithms that are found in Tetrad and others. 


## How can I use it?

#### Required Dependencies
* colt-1.2.0.jar
* commons-collections-3.1.jar
* commons-math3-3.3.jar
* jama-1.0.2.jar
* lib-tetrad-0.3-SNAPSHOT.jar
* mtj-0.9.14.jar
* xom-1.1.jar

Put all the dependency jars along with ccd-algorithm-1.0.jar in a lib folder

#### Run as an Application

##### Create Simulated Dataset
```java
// create dataset with 20 variables, 100 cases, and 1 edge per node
java -cp lib/ccd-algorithm.jar edu.pitt.dbmi.ccd.algorithm.tetrad.SimulateDataApp --var 20 --case 100 --edge 1 --out output/
```

##### Run PC-Stable
```java
java -cp lib/ccd-algorithm.jar edu.pitt.dbmi.ccd.algorithm.tetrad.PcStableApp --data data.txt --continuous --alpha 0.0001 --depth 3 --verbose --out output/
```

##### Run GES
```java
java -cp lib/ccd-algorithm.jar edu.pitt.dbmi.ccd.algorithm.tetrad.GesApp --data data.csv --delim $',' --penalty-discount 2.0 --depth 3 --verbose --out output/
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

##### Run PC-Stable
```java
double alpha = 0.0001;
int depth = 3;
boolean verbose = true;

// create parameters for PC-Stable
Parameters params = ParameterFactory.buildPcStableParameters(alpha, depth, verbose);

// run the PC-Stable algorithm
Algorithm algorithm = new TetradAlgorithm();
algorithm.setExecutionOutput(System.out);  // print verbose messages to standard out
if (dataset.isContinuous()) {
    algorithm.run(PcStable.class, IndTestFisherZ.class, dataset, params);
} else {
    algorithm.run(PcStable.class, IndTestChiSquare.class, dataset, params);
}
```

##### Run GES
```java
Double penaltyDiscount = 2.0;
Integer depth = 3;
Boolean faithful = Boolean.TRUE;
Boolean verbose = Boolean.TRUE;

// create parameters for GES
Parameters params = ParameterFactory.buildGesParameters(penaltyDiscount, depth, faithful, verbose);

// run the GES algorithm
Algorithm algorithm = new TetradAlgorithm();
algorithm.setExecutionOutput(System.out);  // write verbose messages to standard out
algorithm.run(FastGes.class, null, dataset, params);

```

##### Output Graph
```java
Path outputFile = Paths.get("ges_graph.txt");
OutputStream out = Files.newOutputStream(outputFile, StandardOpenOption.CREATE);
PrintStream outputWriter = new PrintStream(new BufferedOutputStream(out));
boolean writeAsXml = false;

// write out graph
Graph graph = algorithm.getGraph();
GraphIO.write(graph, writeAsXml, outputWriter);
```
