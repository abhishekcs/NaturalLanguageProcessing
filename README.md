# NaturalLanguageProcessing
This folder contains the assignments for the course CS388 - Natural Language Processing being taught in Spring 2016

## Bidirectional Language Models
2 new files have been added to the ./nlp/lm directory.
```
1. BackwarwdBigramModel.java
2. BidirectionalBigramModel.java
```
Just as the names indicate, the 2 files implement the backward bigram model
and the bidrectional model respectively making use of the original 
bigram model class.



How to Run the Programs
```
1. From the top level directiory, run 
		javac nlp/lm/*.java

2. To run the program, run the following command template from the top level directory

		java FILENAME DATAPATH FRACTION
	For example,
	java nlp.lm.BidirectionalBigramModel ./pos/wsj/ 0.1
```
