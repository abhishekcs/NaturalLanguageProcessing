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



		