##CCD Algorithm: Fast GES

CCD Algorithm is a Java application that provides an API interface to run a collection of algorithms that are found in Tetrad and others.  This particular release is focused on FastGES, the latest version of GES that can handle large dataset and run efficiently.


## How can I use it?

#### System Requirement
<a href="http://www.oracle.com/technetwork/java/javase/downloads/index.html">Java 8 SDK</a> and above to compile and run.
<a href="https://maven.apache.org/download.cgi">Apache Maven 3.x</a> to compile.

#### Required Dependencies
* colt-1.2.0.jar
* commons-collections-3.1.jar
* commons-math3-3.3.jar
* jama-1.0.2.jar
* mtj-0.9.14.jar
* xom-1.1.jar
* lib-tetrad-0.3.jar

All the dependecies above are public except for lib-tetrad-0.3.jar.  Download the zip file <a href="https://github.com/bd2kccd/lib-tetrad/archive/v0.3.zip">lib-tetrad-0.3.zip</a> and do a maven install.  After lib-tetrad-0.3 is installed, do a maven package.  It should produce fastges-cli.jar.
#### Run as an Application

##### Usage
Show the usage and the list of options
><pre>java -jar fastges-cli.jar</pre>

##### Run Fast GES
><pre>java -jar fastges-cli.jar --data test_data.txt</pre>
