package nlp.lm;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public class BidirectionalBigramModel {
	private BigramModel forwardModel;
	private BackwardBigramModel backwardModel;
	// Weight for forward Model
	private double forwardWeight = 0.5;
	// Weight for backward model
	private double backwardWeight = 0.5;
	
	public BidirectionalBigramModel(){
		forwardModel = new BigramModel();
		backwardModel = new BackwardBigramModel();
	}
	
	public void train (List<List<String>> sentences) {	
		forwardModel.train(sentences);
		backwardModel.train(sentences);
	}
	    
	    
	    /** Computes word perplexity for corpus */
    public void test (List<List<String>> sentences) {
		double totalLogProb = 0;
		double totalNumTokens = 0;
		for (List<String> sentence : sentences) {
		    totalNumTokens += sentence.size();
		    double sentenceLogProb = sentenceLogProb(sentence);
		    totalLogProb += sentenceLogProb;
		}
		double perplexity = Math.exp(-totalLogProb / totalNumTokens);
		System.out.println("Word Perplexity = " + perplexity );
    }
    
    // Compute sentence log probability of the whole sentence
    // by adding log probabilities of individual tokens.
    // Log probability of individual token is the weighted average
    // of the predictions of the forward and the backward model
    private double sentenceLogProb (List<String> sentence){
    	double[] probsForward = forwardModel.sentenceTokenProbs(sentence);
    	double[] probsBackward = backwardModel.sentenceTokenProbs(sentence);
   
    	int sz = sentence.size();
    	double sentenceLogProb = 0;
    	for(int i = 0; i < sz; i++){
    		sentenceLogProb += Math.log(interpolateForwardBackward(probsForward[i], probsBackward[i]));
    	}
    	return sentenceLogProb;
    }

    // Interpolate forward and backward model predictions
    private double interpolateForwardBackward(double forwardProb, double backwardProb){
    	return forwardWeight*forwardProb + backwardWeight*backwardProb;
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
		BidirectionalBigramModel model = new BidirectionalBigramModel();
		System.out.println("Training...");
		model.train(trainSentences);
		// Test on training data using test and test2
		model.test(trainSentences);
		System.out.println("Testing...");
		// Test on test data using test and test2
		model.test(testSentences);

	}
}
    
