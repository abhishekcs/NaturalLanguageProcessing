import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PreprocessData {
	// Got from http://www.darke.k12.oh.us/curriculum/la/suffixes.pdf
	private static final String[] listOfSuffixes = { "ible", "able", "ness", "ment", "ing", "ogy", "ion", "tion", "ity",
			"est", "ies", "ful", "ive", "ous", "ic", "al", "en", "es", "or", "ed", "ly", "er", "y", "s" };

	public enum ProcessType {
		PREFIXES, SUFFIXES, ALL, NON_ORTHOGRAPHIC
	};

	private static boolean isFeasibleStartingCharacter(char c) {
		char[] unfeasibleChars = { '[', ']', '@', '\n' };
		for (int i = 0; i < unfeasibleChars.length; i++) {
			if (c == unfeasibleChars[i]) {
				return false;
			}
		}
		return true;
	}

	// Process single word, posTag pair to generate all features
	private static String processSingleWord(String word, String posTag, ProcessType pType) {
		if (pType.equals(ProcessType.NON_ORTHOGRAPHIC)) {
			return word + " " + posTag + "\n";
		} else {
			String ret = word + " ";
			if (pType.equals(ProcessType.PREFIXES) || pType.equals(ProcessType.ALL)) {
				if (Character.isDigit(word.charAt(0))) {
					ret = ret + "dig ";
				} else if (Character.isUpperCase(word.charAt(0))) {
					ret = ret + "caps ";
				}
				String[] hyphenTokens = word.split("-");
				if (hyphenTokens.length >= 2) {
					ret = ret + "hyph ";
				}
			}
			if (pType.equals(ProcessType.SUFFIXES) || pType.equals(ProcessType.ALL)) {
				for (int i = 0; i < listOfSuffixes.length; i++) {
					if (word.endsWith(listOfSuffixes[i])) {
						ret = ret + listOfSuffixes[i] + " ";
						break;
					}
				}
			}
			ret = ret + posTag + "\n";

			return ret;
		}
	}

	// Converts a singe file to format required by Mallet
	public static void processSingleFile(String inFileName, String outFileName, ProcessType pType) {
		BufferedReader inr = null;
		BufferedWriter ouw = null;
		try {
			inr = new BufferedReader(new FileReader(inFileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Input File Not Found \n Exiting");
			System.exit(1);
		}
		try {
			File file = new File(outFileName);
			file.getParentFile().mkdirs();
			ouw = new BufferedWriter(new FileWriter(outFileName, true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String line;
		try {
			while ((line = inr.readLine()) != null) {
				// System.out.println(line);
				//System.out.println(line + " " + line.length());
				if(line.equals("")){
					ouw.write("\n");
				}
				else{
					String[] tokens = line.split("\\s+");
					for (int i = 0; i < tokens.length; i++) {
						if (tokens[i].equals("")) {
							continue;
						}
						char startingChar = tokens[i].charAt(0);
	
						if (startingChar == '=') {
							ouw.write("\n");
							break;
						} else if (isFeasibleStartingCharacter(startingChar)) {
							String[] wordPosPair = tokens[i].split("/");
							if (wordPosPair.length == 2) {
								String preProc = processSingleWord(wordPosPair[0], wordPosPair[1], pType);
								// System.out.println(preProc);
								ouw.write(preProc);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			inr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ouw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Converts a directory having WSJ structure into a structure required for Mallet
	public static void preProcessDirectory(String inDirectoryName, String outDirectoryName, ProcessType pType) {
		File dir = new File(inDirectoryName);
		File[] listOfFiles = dir.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				;
			} else if (listOfFiles[i].isDirectory()) {
				String subDirName = listOfFiles[i].getName();
				File subDir = new File(inDirectoryName + subDirName + "/");
				File[] listOfFilesInSubDir = subDir.listFiles();
				for (int j = 0; j < listOfFilesInSubDir.length; j++) {
					if (listOfFilesInSubDir[j].isFile()) {
						String fname = listOfFilesInSubDir[j].getName();
						processSingleFile(inDirectoryName + "/" + subDirName + "/" + fname,
								outDirectoryName + "/" + subDirName + ".pos", pType);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		// String inFileName = "/Users/abhisheksinha/NLP/pos/atis/atis3.pos";
		// String outFileName =
		// "/Users/abhisheksinha/NLP/preprocessed_pos/atis/atis3.pos";
		// String outFileOrthographic =
		// "/Users/abhisheksinha/NLP/preprocessed_ortho_pos/atis/atis3.pos";
		// //processSingleFile(inFileName, outFileName,
		// ProcessType.NON_ORTHOGRAPHIC);
		// outFileOrthographic =
		// "/Users/abhisheksinha/NLP/preprocessed_ortho_pref_pos/atis/atis3.pos";
		// processSingleFile(inFileName, outFileOrthographic,
		// ProcessType.PREFIXES);
		// outFileOrthographic =
		// "/Users/abhisheksinha/NLP/preprocessed_ortho_suff_pos/atis/atis3.pos";
		// processSingleFile(inFileName, outFileOrthographic,
		// ProcessType.SUFFIXES);
//		String inDirectoryName = "/Users/abhisheksinha/NLP/pos/wsj/";
//		String outDirectoryName = "/Users/abhisheksinha/NLP/preprocessed_pos/wsj/";
//		preProcessDirectory(inDirectoryName, outDirectoryName, ProcessType.NON_ORTHOGRAPHIC);
//		outDirectoryName = "/Users/abhisheksinha/NLP/preprocessed_ortho_pos/wsj/";
//		preProcessDirectory(inDirectoryName, outDirectoryName, ProcessType.ALL);
//		outDirectoryName = "/Users/abhisheksinha/NLP/preprocessed_ortho_pref_pos/wsj/";
//		preProcessDirectory(inDirectoryName, outDirectoryName, ProcessType.PREFIXES);
//		outDirectoryName = "/Users/abhisheksinha/NLP/preprocessed_ortho_suff_pos/wsj/";
//		preProcessDirectory(inDirectoryName, outDirectoryName, ProcessType.SUFFIXES);
		if(args.length != 4){
			System.out.println("4 arguments needed : Input Path, Out of Path, Dataset, Type of Processing");
		}
		String inName = args[0];
		String outName = args[1];
		String dataSet = args[2];
		if(dataSet.equals("atis")){
			processSingleFile(inName, outName, ProcessType.valueOf(args[3]));
		}
		else{
			preProcessDirectory(inName, outName, ProcessType.valueOf(args[3]));
		}
	}
}
