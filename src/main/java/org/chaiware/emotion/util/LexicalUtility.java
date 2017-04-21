package org.chaiware.emotion.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.chaiware.emotion.AffectWord;
import org.chaiware.util.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for some text processing algorithms (Singleton)
 */
public class LexicalUtility {

    private static Logger logger = LoggerFactory.getLogger(LexicalUtility.class);
	private static LexicalUtility instance;

	private String FILENAME_LEXICON = "/data/lex/lexicon.txt";
	private String FILENAME_EMOTICONS = "/data/lex/lexicon_emoticons.txt";
	private String FILENAME_PROPERTIES = "/data/lex/keywords.xml";

	private List<AffectWord> affectWords;
	private List<AffectWord> emoticons;

	private List<String> negations;
	private List<String> intensityModifiers;

	private final double NORMALISATOR = 1;

	private LexicalUtility() throws IOException {
		affectWords = new ArrayList();
		emoticons = new ArrayList();
		PropertiesManager pm = new PropertiesManager(FILENAME_PROPERTIES);
		negations = ParsingUtility.splitWords(pm.getProperty("negations"), ", ");
		intensityModifiers = ParsingUtility.splitWords(pm.getProperty("intensity.modifiers"), ", ");
		parseLexiconFile(affectWords, FILENAME_LEXICON);
		parseLexiconFile(emoticons, FILENAME_EMOTICONS);

		logger.debug("Lexical Utility Instantiated");
	}

	/**
	 * Returns the Singleton instance of the {@link LexicalUtility}.
	 * 
	 * @return the instance of {@link LexicalUtility}
	 * @throws IOException
	 */
	public static LexicalUtility getInstance() throws IOException {
		if (instance == null) {
			instance = new LexicalUtility();
		}

		return instance;
	}

	private void parseLexiconFile(List<AffectWord> wordList, String fileName) throws IOException {

      try (BufferedReader in = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(fileName), "UTF8"))) {

        String line = in.readLine();
        while (line != null) {
          AffectWord record = parseLine(line);
          wordList.add(record);
          line = in.readLine();
        }

        logger.debug("Parsed lexicon file: {}", fileName);
      }
    }

	/**
	 * Parses one line of the Lexicon and returns the {@link AffectWord}
	 * 
	 * @param line {@link String} representing the line of the Lexicon
	 * @return {@link AffectWord}
	 */
	private AffectWord parseLine(String line) {

		AffectWord value;
		String[] text = line.split(" ");
		String word = text[0];
		double generalWeight = Double.parseDouble(text[1]);
		double happinessWeight = Double.parseDouble(text[2]);
		double sadnessWeight = Double.parseDouble(text[3]);
		double angerWeight = Double.parseDouble(text[4]);
		double fearWeight = Double.parseDouble(text[5]);
		double disgustWeight = Double.parseDouble(text[6]);
		double surpriseWeight = Double.parseDouble(text[7]);
		value = new AffectWord(word, generalWeight, happinessWeight,
				sadnessWeight, angerWeight, fearWeight, disgustWeight, surpriseWeight, NORMALISATOR);

		return value;
	}

	/**
	 * Returns the instance of {@link AffectWord} for the given word.
	 * 
	 * @param word {@link String} representing the word
	 * @return {@link AffectWord}
	 */
	public AffectWord getAffectWord(String word) {
		for (AffectWord affectWord : affectWords) {
			if (affectWord.getWord().equals(word)) {
				return affectWord.clone();
			}
		}

		return null;
	}

	/**
	 * Returns the instance of {@link AffectWord} for the given word, which is emoticon.
	 * 
	 * @param word {@link String} representing the word
	 * @return {@link AffectWord}
	 */
	public AffectWord getEmoticonAffectWord(String word) {
		for (AffectWord affectWordEmoticon : emoticons) {
			if (affectWordEmoticon.getWord().equals(word)) {
				return affectWordEmoticon.clone();
			}
		}

		for (AffectWord affectWordEmoticon : emoticons) {
			String emoticon = affectWordEmoticon.getWord();
			if (ParsingUtility.containsFirst(word, emoticon)) {
				affectWordEmoticon.setStartsWithEmoticon(true);
				return affectWordEmoticon.clone();
			}
		}

		return null;
	}

	/**
	 * Returns all instances of {@link AffectWord} which represent emoticons for the given sentence.
	 * 
	 * @param sentence {@link String} representing the sentence
	 * @return the list of {@link AffectWord} instances
	 */
	public List<AffectWord> getEmoticonWords(String sentence) {

		List<AffectWord> value = new ArrayList ();
		for (AffectWord emoticon : emoticons) {
			String emoticonWord = emoticon.getWord();
			if (sentence.contains(emoticonWord)) {
				emoticon.setStartsWithEmoticon(true);
				value.add(emoticon);
			}
		}

		return value;
	}

	/**
	 * Returns all instances of {@link AffectWord}
	 * 
	 * @return the list of {@link AffectWord} instances
	 */
	public List<AffectWord> getAffectWords() {
		return affectWords;
	}

	/**
	 * Returns true if the word is a negation.
	 * 
	 * @param word {@link String} which represents a word
	 * @return boolean, true is the word is a negation
	 */
	public boolean isNegation(String word) {
		return negations.contains(word);
	}

	/**
	 * Returns true if the word is an intensity modifier.
	 * 
	 * @param word {@link String} which represents a word
	 * @return boolean, true is the word is an intensity modifier
	 */
	public boolean isIntensityModifier(String word) {
		return intensityModifiers.contains(word);
	}
	
	/**
	 * Returns true if the word and the negation are in the same
	 * part of the sentence, i.e. divided by a interpunction mark.
	 * 
	 * @param word {@link String} which represents a word
	 * @return boolean, true is the word is an intensity modifier
	 */
	public static boolean inTheSamePartOfTheSentence(String negation, String word, String sentence) {
		int i = sentence.indexOf(negation);
		int j = sentence.indexOf(word);
		if (i < j) {
			i += negation.length();
		} else {
			int tmp = i;
			i = j + word.length();
			j = tmp;
		}

		for (int k = i; k < j; k++) {
			if ((sentence.charAt(k) == ',') ||
				(sentence.charAt(k) == '.') ||
				(sentence.charAt(k) == ';') ||
				(sentence.charAt(k) == ':') ||
				(sentence.charAt(k) == '-')) {
				return false;
			}
		}

		return true;
	}
}
