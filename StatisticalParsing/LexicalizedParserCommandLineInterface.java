import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.EvaluateTreebank;
import edu.stanford.nlp.parser.lexparser.Options;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.common.ArgUtils;
import edu.stanford.nlp.parser.common.ParserGrammar;
import edu.stanford.nlp.trees.MemoryTreebank;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.Treebank;
import edu.stanford.nlp.util.Pair;
import edu.stanford.nlp.util.Timing;

public class LexicalizedParserCommandLineInterface {

	private static MemoryTreebank makeTreebank(String treebankPath, Options op, FileFilter filt) {
	    System.err.println("Training a parser from treebank dir: " + treebankPath);
	    MemoryTreebank trainTreebank = new MemoryTreebank("utf-8");
	    System.err.print("Reading trees...");
	    if (filt == null) {
	      trainTreebank.loadPath(treebankPath);
	    } else {
	      trainTreebank.loadPath(treebankPath, filt);
	    }

	    Timing.tick("done [read " + trainTreebank.size() + " trees].");
	    return trainTreebank;
	}
	
	public static MemoryTreebank fetchSentences( MemoryTreebank tb, int numSentences){
		if(numSentences == -1){
			return tb;
		}
		else{
			MemoryTreebank ret = new MemoryTreebank("utf-8");
			int i = 0;
			for(Tree t: tb){
				if(i >= numSentences){
					break;
				}
				else{
					ret.add(t);
					i += 1;
				}
			}
			return ret;		
		}
	}
  
	public static void main(String[] args) {
	    String seedTreebankPath = null;
	    String selfTreebankPath = null;
	    String testTreebankPath = null;

	    MemoryTreebank trainBank = null;
	    MemoryTreebank selfTreeBank = null;
	    MemoryTreebank testTreeBank = null;
	    
	    int numSeedSentences = -1, numSelfTrainSentences = -1, numTestSentences = -1;

	    LexicalizedParser lp = null;
	    	    
	    Options op = new Options();
	    op.doPCFG = true;
	    op.doDep = false;
	    op.setOptions("-goodPCFG", "-evals", "tsv");

        seedTreebankPath = args[0];
        trainBank = makeTreebank(seedTreebankPath, op, null);
		numSeedSentences = Integer.valueOf(args[1]);

        selfTreebankPath = args[2];
        selfTreeBank = makeTreebank(selfTreebankPath, op, null);
		numSelfTrainSentences = Integer.valueOf(args[3]);

        testTreebankPath = args[4];
        testTreeBank = makeTreebank(testTreebankPath, op, null);
        numTestSentences = Integer.valueOf(args[5]);
		
		System.out.println(seedTreebankPath + selfTreebankPath + testTreebankPath + String.valueOf(numSeedSentences)  + String.valueOf(numSelfTrainSentences) + String.valueOf(numTestSentences));
			
		trainBank = fetchSentences(trainBank, numSeedSentences);
		selfTreeBank = fetchSentences(selfTreeBank, numSelfTrainSentences);
		testTreeBank = fetchSentences(testTreeBank, numTestSentences);

		System.out.println( String.valueOf(trainBank.size()) + " " + String.valueOf(selfTreeBank.size()) + " " + String.valueOf(testTreeBank.size()) + " ");
		
		// // Initial Training
        lp = LexicalizedParser.trainFromTreebank(trainBank,op);
        
        // Test Initially without Self 
        EvaluateTreebank evaluator = new EvaluateTreebank(lp);
        evaluator.testOnTreebank(testTreeBank);
        
		// Predict Labels on Self Training Set
		Function<List<? extends HasWord>, List<TaggedWord>> tag = ((ParserGrammar)lp).loadTagger();
        List<Tree> treesList = new ArrayList<Tree>();        
		for (Tree selfTrainTree : selfTreeBank) {        	
        	List<? extends HasWord> sentence = selfTrainTree.yieldWords();
        	Tree parse = lp.apply(sentence);
        	//System.out.println(parse.pennString());
        	trainBank.add(parse);
        }
		
        // Retrain
        lp = LexicalizedParser.trainFromTreebank(trainBank,op);
        
        // Evaluate on the test set After Retraining
        evaluator = new EvaluateTreebank(lp);
        evaluator.testOnTreebank(testTreeBank);
	}
		
}
