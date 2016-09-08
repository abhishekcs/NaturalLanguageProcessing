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