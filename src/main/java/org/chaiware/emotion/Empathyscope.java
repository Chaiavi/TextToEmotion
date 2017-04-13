package org.chaiware.emotion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.chaiware.emotion.util.HeuristicsUtility;
import org.chaiware.emotion.util.LexicalUtility;
import org.chaiware.emotion.util.ParsingUtility;

/**
 * Defines logic for transferring textual affect information, emotional
 * manifestations recognised in text into visual output.
 */
public class Empathyscope {

	private static Empathyscope instance;
	private LexicalUtility lexUtil;

	private Empathyscope() throws IOException {
		lexUtil = LexicalUtility.getInstance();
	}

	/**
	 * Returns the Singleton instance of the {@link Empathyscope}.
	 * 
	 * @return {@link Empathyscope} instance
	 * @throws IOException
	 */
	public static Empathyscope getInstance() throws IOException {
		if (instance == null) {
			instance = new Empathyscope();
		}

		return instance;
	}

	/**
	 * Textual affect sensing behavior, the main NLP alghoritm which uses
	 * Synesketch Lexicon and several heuristic rules.
	 * 
	 * @param text String representing the text to be analysed
	 * @return {@link EmotionalState} which represents data recognised from the text
	 * @throws IOException
	 */
	public EmotionalState feel(String text) throws IOException {

		text = text.replace('\n', ' ');
		List<AffectWord> affectWords = new ArrayList<AffectWord>();
		List<String> sentences = ParsingUtility.parseSentences(text);

		for (String sentence : sentences) {
			
			System.out.println("- " + sentence);
			
			// we imploy 6 heuristic rules to adjust emotive weights of the words:
			// (1) more exclamation signs in a sentence => more intensive emotive weights
			double exclamationQoef = HeuristicsUtility.computeExclaminationQoef(sentence.toLowerCase());

			// (2) an exclamation mark next to a question mark => emotion of surprise
			if (HeuristicsUtility.hasExclaminationQuestionMarks(sentence)) {
				AffectWord emoWordSurprise = new AffectWord("?!");
				emoWordSurprise.setSurpriseWeight(1.0);
				affectWords.add(emoWordSurprise);
			}
			
			boolean hasNegation = false;
			
			List<String> splittedWords = ParsingUtility.splitWords(sentence," ");
			String previousWord = "";
			String negation = "";
			
			for (String splittedWord : splittedWords) {
				
				AffectWord emoWord = lexUtil.getEmoticonAffectWord(splittedWord);
				if (emoWord == null)
					emoWord = lexUtil.getEmoticonAffectWord(splittedWord.toLowerCase());

				if (emoWord != null) {
					// (3) more emoticons with more 'emotive' signs (e.g. :DDDD)
					// => more intensive emotive weights
										
					double emoticonCoef = HeuristicsUtility.computeEmoticonQoef(splittedWord, emoWord);
					if (emoticonCoef == 1.0)
						emoticonCoef = HeuristicsUtility.computeEmoticonQoef(splittedWord.toLowerCase(), emoWord);
					emoWord.adjustWeights(exclamationQoef * emoticonCoef);
					affectWords.add(emoWord);
				} else {

					List<String> words = ParsingUtility.parseWords(splittedWord);
					
					for (String word : words) {
						
						// (4) negation in a sentence => 
						// flip valence of the affect words in it
						if (HeuristicsUtility.isNegation(word.toLowerCase())) {
							negation = word;
							hasNegation = true;
						}

						emoWord = lexUtil.getAffectWord(word.toLowerCase());
						if (emoWord == null)
							emoWord = lexUtil.getEmoticonAffectWord(word.toLowerCase());
						if (emoWord != null) {
							
							// (5) word is upper case => more intensive emotive weights
							double capsLockCoef = HeuristicsUtility.computeCapsLockQoef(word);

							// (6) previous word is a intensity modifier (e.g.
							// "extremly") => more intensive emotive weights
							double modifierCoef = HeuristicsUtility.computeModifier(previousWord);
							
							// change the affect word!
							if ((hasNegation) && (LexicalUtility.inTheSamePartOfTheSentence(negation, emoWord.getWord(), sentence))) {
								emoWord.flipValence();
							}

							emoWord.adjustWeights(exclamationQoef * capsLockCoef * modifierCoef);
							affectWords.add(emoWord);
						}

						previousWord = word;
					}
				}
			}
		}

		return createEmotionalState(text, affectWords);
	}

	private EmotionalState createEmotionalState(String text, List<AffectWord> affectWords) {
		TreeSet<Emotion> emotions = new TreeSet<Emotion>();
		int generalValence = 0;
		double valence, generalWeight, happinessWeight, sadnessWeight, angerWeight, fearWeight, disgustWeight, surpriseWeight;

		valence = 0.0;
		generalWeight = 0.0;
		happinessWeight = 0.0;
		sadnessWeight = 0.0;
		angerWeight = 0.0;
		fearWeight = 0.0;
		disgustWeight = 0.0;
		surpriseWeight = 0.0;

		// compute weights. maximum weights for the particular emotion are taken.
		for (AffectWord affectWord : affectWords) {
			valence += affectWord.getGeneralValence();
			if (affectWord.getGeneralWeight() > generalWeight)
				generalWeight = affectWord.getGeneralWeight();
			if (affectWord.getHappinessWeight() > happinessWeight)
				happinessWeight = affectWord.getHappinessWeight();
			if (affectWord.getSadnessWeight() > sadnessWeight)
				sadnessWeight = affectWord.getSadnessWeight();
			if (affectWord.getAngerWeight() > angerWeight)
				angerWeight = affectWord.getAngerWeight();
			if (affectWord.getFearWeight() > fearWeight)
				fearWeight = affectWord.getFearWeight();
			if (affectWord.getDisgustWeight() > disgustWeight)
				disgustWeight = affectWord.getDisgustWeight();
			if (affectWord.getSurpriseWeight() > surpriseWeight)
				surpriseWeight = affectWord.getSurpriseWeight();
		}

		if (valence > 0)
			generalValence = 1;
		else if (valence < 0)
			generalValence = -1;

		if (happinessWeight > 0)
			emotions.add(new Emotion(happinessWeight, Emotion.HAPPINESS));
		if (sadnessWeight > 0)
			emotions.add(new Emotion(sadnessWeight, Emotion.SADNESS));
		if (angerWeight > 0)
			emotions.add(new Emotion(angerWeight, Emotion.ANGER));
		if (fearWeight > 0)
			emotions.add(new Emotion(fearWeight, Emotion.FEAR));
		if (disgustWeight > 0)
			emotions.add(new Emotion(disgustWeight, Emotion.DISGUST));
		if (surpriseWeight > 0)
			emotions.add(new Emotion(surpriseWeight, Emotion.SURPRISE));
		if (emotions.isEmpty())
			emotions.add(new Emotion((0.2 + generalWeight) / 1.2, Emotion.NEUTRAL));

		return new EmotionalState(text, emotions, generalWeight, generalValence);
	}
}
