package org.chaiware.emotion.util;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for some text parsing algorithms
 */
public class ParsingUtility {

	/**
	 * Pareses text into sentences.
	 * 
	 * @param text {@link String} which represents the text
	 * @return {@link ArrayList} of {@link String} instances representing the sentences
	 */
	public static ArrayList<String> parseSentences(String text) {
		ArrayList<String> value = new ArrayList<String>();

		BreakIterator boundary = BreakIterator.getSentenceInstance();
		boundary.setText(text);
		int start = boundary.first();
		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
			String word = text.substring(start, end);
			value.add(word);
		}

		return value;
	}

	/**
	 * Pareses sentences into words.
	 * 
	 * @param text {@link String} which represents the sentence
	 * @return {@link ArrayList} of {@link String} instances representing the words
	 */
	public static ArrayList<String> parseWords(String text) {
		ArrayList<String> value = new ArrayList<String>();

		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(text);
		int start = boundary.first();
		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary
				.next()) {
			String word = text.substring(start, end);
			value.add(word);
		}

		return value;
	}

	/**
	 * Splits words by a given {@link String} argument (' ' or '-', to name two examples).
	 * 
	 * @param text {@link String} which represents the text
	 * @param splitter {@link String} which represents the splitting mark (' ' or '-', to name two examples)
	 * @return {@link List} of {@link String} instances representing the split words
	 */
	public static List<String> splitWords(String text, String splitter) {
		return Arrays.asList(text.split(splitter));
	}

	/**
	 * Returns true if the fist word begins with the second.
	 * 
	 * @param container {@link String} which represents the container word
	 * @param containee {@link String} which represents the containee word
	 * @return boolean, true if the fist word begins with the second
	 */
	public static boolean containsFirst(String container, String containee) {
		if (container.length() > containee.length()) {
			for (int i = 0; i < containee.length(); i++) {
				if (!(containee.charAt(i) == container.charAt(i))) {
					return false;
				}
			}

			return true;
		} else {
			return false;
		}
	}
}
