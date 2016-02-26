package nlp.lm;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BackwardBigramModel {
	// This is an instance of the BigramModel class which will be fed sentences
	// with words in reverse order to implement the Backward Model.
	BigramModel backwardModel;

	public BackwardBigramModel() {
		backwardModel = new BigramModel();
	    }

	    /** Train the model on a List of sentences represented as
	     *  Lists of String tokens */
	    public void train (List<List<String>> sentences) {
		reverseSentences(sentences);
		backwardModel.train(sentences);

		// Ensure that the input is not modified.
		reverseSentences(sentences);
	    }
	    
	    // Helper function to reverse every sentence in the list of sentences
	    private void reverseSentences(List<List<String>> sentences){
	    	for (List<String> sentence : sentences) {
			    Collections.reverse(sentence);
			}
	    }
	    
	    /** Computes both perplexity and word perplexity for corpus */
	    public void test (List<List<String>> sentences) {
	    // long startTime = System.currentTimeMillis();
	    reverseSentences(sentences);
	    // long endTime = System.currentTimeMillis();
	    // System.out.println("First Reverse :" + String.valueOf(endTime - startTime) + "\n");
		backwardModel.test(sentences);
		backwardModel.test2(sentences);
		// startTime = System.currentTimeMillis();
		// System.out.println("Processing :" + String.valueOf(startTime - endTime) + "\n");
		reverseSentences(sentences);
		// endTime = System.currentTimeMillis();
		// System.out.println("Second Reverse :" + String.valueOf(endTime - startTime) + "\n");
	    }
	    
	    // Helper function to reverse a double array in the range 0.....(range-1)
	    private void reverseArray(double[] d, int range){
	    	int n = range;
	    	for(int i = 0; i < (n/2); i++){
	    		double temp = d[i];
	    		d[i] = d[n - 1 -i];
	    		d[n - 1 - i] = temp;
	    	}
	    }
	    // Computes log probabilities for each token in the sentence (from sentence start to sentence end)
	    public double[] sentenceTokenProbs (List<String> sentence){
	    	Collections.reverse(sentence);
	    	// This contains probabilities from sentence end to sentence start
	    	double[] probs = backwardModel.sentenceTokenProbs(sentence);

	    	// Here we reverse the order to make it from sentence start to sentece end
	    	reverseArray(probs, sentence.size());
	    	return probs;	    	
	    }
	    
	public static void main(String[] args) {
		// All but last arg is a file/directory of LDC tagged input data
		File[] files = new File[args.length - 1];
		for (int i = 0; i < files.length; i++) 
		    files[i] = new File(args[i]);
		// Last arg is the TestFrac
		double testFraction = Double.valueOf(args[args.length -1]);
		// Get list of sentences from the LDC POS tagged input files
		List<List<String>> sentences = 	POSTaggedFile.convertToTokenLists(files);
		int numSentences = sentences.size();
		// Compute number of test sentences based on TestFrac
		int numTest = (int)Math.round(numSentences * testFraction);
		// Take test sentences from end of data
		List<List<String>> testSentences = sentences.subList(numSentences - numTest, numSentences);
		// Take training sentences from start of data
		List<List<String>> trainSentences = sentences.subList(0, numSentences - numTest);
		System.out.println("# Train Sentences = " + trainSentences.size() + 
				   " (# words = " + BigramModel.wordCount(trainSentences) + 
				   ") \n# Test Sentences = " + testSentences.size() +
				   " (# words = " + BigramModel.wordCount(testSentences) + ")");
		// Create a bigram model and train it.
		BackwardBigramModel model = new BackwardBigramModel();
		System.out.println("Training...");
		model.train(trainSentences);
		// Test on training data
		model.test(trainSentences);
		System.out.println("Testing...");
		// Test on test data
		model.test(testSentences);

	}

}
