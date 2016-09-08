/* Copyright (C) 2002 Univ. of Massachusetts Amherst, Computer Science Dept.
   This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
   http://www.cs.umass.edu/~mccallum/mallet
   This software is provided under the terms of the Common Public License,
   version 1.0, as published by http://www.opensource.org.  For further
   information, see the file `LICENSE' included with this distribution. */




/** 
   @author Andrew McCallum <a href="mailto:mccallum@cs.umass.edu">mccallum@cs.umass.edu</a>
 */

package cc.mallet.fst;


import java.util.HashMap;
import java.util.logging.Logger;
import java.util.Set;
import java.util.HashSet;

import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Sequence;

import cc.mallet.util.MalletLogger;

/**
 * Evaluates a transducer model based on predictions of individual tokens.
 */
public class TokenAccuracyEvaluator extends TransducerEvaluator
{
	private static Logger logger = MalletLogger.getLogger(TokenAccuracyEvaluator.class.getName());

	private HashMap<String,Double> accuracy = new HashMap<String,Double>();
	// The Set of Training Tokens
	private Set<String> trainingTokens = new HashSet<String>();

	public TokenAccuracyEvaluator (InstanceList[] instanceLists, String[] descriptions) {
		super (instanceLists, descriptions);
	}
	
	public TokenAccuracyEvaluator (InstanceList instanceList1, String description1) {
		this (new InstanceList[] {instanceList1}, new String[] {description1});
	}
	
	public TokenAccuracyEvaluator (InstanceList instanceList1, String description1,
			InstanceList instanceList2, String description2) {
		this (new InstanceList[] {instanceList1, instanceList2}, new String[] {description1, description2});
	}
	
	public TokenAccuracyEvaluator (InstanceList instanceList1, String description1,
			InstanceList instanceList2, String description2,
			InstanceList instanceList3, String description3) {
		this (new InstanceList[] {instanceList1, instanceList2, instanceList3}, new String[] {description1, description2, description3});
	}

	public void evaluateInstanceList (TransducerTrainer trainer, InstanceList instances, String description) 
  {
		int numCorrectTokens;
		int totalTokens;
		int totalOOVTokens;
		int numCorrectOOVTokens;

		Transducer transducer = trainer.getTransducer();
		totalTokens = numCorrectTokens = totalOOVTokens = numCorrectOOVTokens = 0;
		for (int i = 0; i < instances.size(); i++) {
			Instance instance = instances.get(i);
			Sequence input = (Sequence) instance.getData();
			// If we are in training, add the tokens to the set trainingTokens
			if(description.equals("Training")){
				for (int j = 0; j < input.size(); j++) {
					trainingTokens.add(String.valueOf(input.get(j)));
				}
			}
			Sequence trueOutput = (Sequence) instance.getTarget();
			assert (input.size() == trueOutput.size());
			//System.err.println ("TokenAccuracyEvaluator "+i+" length="+input.size());
			Sequence predOutput = transducer.transduce (input);
			assert (predOutput.size() == trueOutput.size());

			// If we are in testing, then update totalOOVTokens and number of correct OOV Tokens
			if(description.equals("Testing")){
				boolean isOOV = false;
				for (int j = 0; j < trueOutput.size(); j++) {
					totalTokens++;
					if(trainingTokens.contains(String.valueOf(input.get(j)))){
						isOOV = false;
					}
					else{
						isOOV = true;
						totalOOVTokens += 1;
					}
					if (trueOutput.get(j).equals(predOutput.get(j))){
						numCorrectTokens++;
						if(isOOV){
							numCorrectOOVTokens += 1;
						}
					}
				}
			}
			else{
				for (int j = 0; j < trueOutput.size(); j++) {
					totalTokens++;
					if (trueOutput.get(j).equals(predOutput.get(j)))
						numCorrectTokens++;
				}
			}
			//System.err.println ("TokenAccuracyEvaluator "+i+" numCorrectTokens="+numCorrectTokens+" totalTokens="+totalTokens+" accuracy="+((double)numCorrectTokens)/totalTokens);
		}
		double acc = ((double)numCorrectTokens)/totalTokens;
		//System.err.println ("TokenAccuracyEvaluator accuracy="+acc);
		accuracy.put(description, acc);
		logger.info (description +" accuracy="+acc);
		if(description.equals("Testing")){
			if(totalOOVTokens == 0){
				logger.info("Out of Vocabulary Accuracy Not Defined");
			}
			else{
				double oovAccuracy = ((double) numCorrectOOVTokens/totalOOVTokens);
				logger.info("Out of Vocabulary Accuracy: " + oovAccuracy);
			}
			double percentageOOV = 100.0*((double)totalOOVTokens)/totalTokens;
			logger.info("Total number of Test Tokens: " + totalTokens + "\nOOV Tokens: " + totalOOVTokens + "\nPercentage: " + percentageOOV);

		}
	}

	/**
	 * Returns the accuracy from the last time test() or evaluate() was called
	 * @return
	 */
	public double getAccuracy (String description)
	{
		Double ret = accuracy.get(description);
		if (ret != null)
			return ret.doubleValue();
		throw new IllegalArgumentException ("No accuracy available for instance list \""+description+"\"");
	}
}
