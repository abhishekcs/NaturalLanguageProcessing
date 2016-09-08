# NaturalLanguageProcessing
This folder contains the assignments for the course CS388 - Natural Language Processing being taught in Spring 2016 (https://www.cs.utexas.edu/~mooney/cs388/)

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


## Sequence Labelling
```
mallet_codebase
	-- TokenAccuracyEvaluator.java
		-- evaluateInstanceList has been modified to keep a set of the tokens encountered during training and hence calculate the percentage of out of vocabulary tokens during testing.
converter_codebase
	-- PreprocessData
		- The main function in this class takes 4 arguments

		Inpath: The absolute path of input
		Outpath: The output path where the files in Mallet format are written to
		Data Set Type: 'atis' or 'wsj'. If 'atis' then we assume that the input is just a single file specified by inpath. If 'wsj' , then we assume that inpath is a directory with the same structure as that of the wsj dataset.
		Process Type: This specifies how we want to process the data.
			NON_ORTHOGRAPHIC: No orthographic features to generate
			ALL: Generate all orthographic features
			PREFIX: Generate only prefix and hyphen orthographic features
			SUFFIX: Generate only suffix orthographic features


Scripts
	Bash Scripts
		crf_atis.sh: Takes 3 arguments: the path of input data (in Mallet compatible form), output path (where to write output and error files), number of iterations

		hmm_atis.sh: Takes as input number of iterations
		hmm_wsj.sh : Takes 4 arguments: path of training data, path of test data, where to write err, where to write out

	Condor Scripts
		crf_wsj.config: Condor script for running crf on wsj data
		crf_wsj_iterations: Condor script for running crf on wsj data with different number of iterations


mallet_pos
	preprocessed_pos: pos files with no orthographic features
	preprocessed_ortho_pos: pos files with all orthographic features
	preprocessed_ortho_pref_pos: pos files with only prefix features (including hyphen)
	preprocessed_ortho_suff_pos: pos files with only suffix features



		
```

## Statistical Parsing
```
LexicalizedParserCommandLineInterface.java
		-- This is the JAVA file for Lexicalized Parser Command Line Interface
			-- This is modelled on elements from ParserDemo.java and main function of LexicalizedParser.java
		-- Usage Instructions
			-- Compile
				-- Example Usage
					javac -cp <CLASSPATH> LexicalizedParserCommandLineInterface.java
			-- Execute
				-- java -cp <CLASSPATH> LexicalizedParserCommandLineInterface <SEED_PATH> <NO_SEED_SENTENCES> <TARGET_TRAIN_PATH> <NO_OF_TARGET_TRAIN_SENTENCES> <TARGET_TEST_PATH> <NO_OF_TARGET_TEST_SENTENCES>
				-- Number of sentences can be -1 -- take all sentences, 0 -- take no sentence, Greater than 0 -- take specified number of sentences

				-- Example usage
					java -cp "./stanford-parser.jar:./slf4j-api.jar:." LexicalizedParserCommandLineInterface ../wsj_train/02/ 500 ../brown_train/ 100 ../brown_test/ 50
```
