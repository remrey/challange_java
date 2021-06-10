#!/bin/bash
rm -rf results
javac -classpath gson-2.8.6.jar:. *.java
java -cp gson-2.8.6.jar:. JSONreader > results
