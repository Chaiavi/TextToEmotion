package org.chaiware.emotion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.chaiware.emotion.util.HeuristicsUtility;
import org.chaiware.emotion.util.LexicalUtility;
import org.chaiware.emotion.util.ParsingUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines logic for transferring textual affect information, emotional
 * manifestations recognised in text into visual output.<br/>
 * This class is a singleton.
 */
public class Empathyscope {

    private static Logger logger = LoggerFactory.getLogger(Empathyscope.class);
	private static Empathyscope instance;
	private LexicalUtility lexUtil;

	private Empathyscope() throws IOException {
		lexUtil = LexicalUtility.getInstance();
		logger.info("Empathy Scope Instantiated");
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
	 * Textual affect sensing behavior, the main NLP algorithm which uses
	 * the Lexicon and several heuristic rules.
	 * 
	 * @param text String representing the text to be analysed
	 * @return {@link EmotionalState} which represents data recognised from the text
	 * @throws IOException
	 */
	public EmotionalState feel(String text) throws IOException {

		text = text.replace('\n', ' ');
		List<AffectWord> affectWords = new ArrayList();
		List<String> sentences = ParsingUtility.parseSentences(text);

		for (String sentence : sentences) {
			
			logger.debug("- " + sentence);
			
			// we imply 6 heuristic rules to adjust emotive weights of the words:
			// (1) more exclamation signs in a sentence => more intensive emotive weights
			double exclamationQoef = HeuristicsUtility.computeExclamationQoef(sentence.toLowerCase());

			// (2) an exclamation mark next to a question mark => emotion of surprise
			if (HeuristicsUtility.hasExclamationQuestionMarks(sentence)) {
				AffectWord emoWordSurprise = new AffectWord("?!");
				emoWordSurprise.setSurpriseWeight(1.0);
				affectWords.add(emoWordSurprise);
			}
			
			boolean hasNegation = false;
			
			List<String> splitWords = ParsingUtility.splitWords(sentence," ");
			String previousWord = "";
			String negation = "";
			
			for (String splitWord : splitWords) {
				
				AffectWord emoWord = lexUtil.getEmoticonAffectWord(splitWord);
				if (emoWord == null)
					emoWord = lexUtil.getEmoticonAffectWord(splitWord.toLowerCase());

				if (emoWord != null) {
					// (3) more emoticons with more 'emotive' signs (e.g. :DDDD)
					// => more intensive emotive weights
										
					double emoticonCoef = HeuristicsUtility.computeEmoticonQoef(splitWord, emoWord);
					if (emoticonCoef == 1.0)
						emoticonCoef = HeuristicsUtility.computeEmoticonQoef(splitWord.toLowerCase(), emoWord);
					emoWord.adjustWeights(exclamationQoef * emoticonCoef);
					affectWords.add(emoWord);
				} else {

					List<String> words = ParsingUtility.parseWords(splitWord);
					
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
							double capsLockCoef = HeuristicsUtility.computeUpperCasedQoef(word);

							// (6) previous word is a intensity modifier (e.g.
							// "extremely") => more intensive emotive weights
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
		TreeSet<Emotion> emotions = new TreeSet();
		int generalValence = 0;

        double valence = 0.0;
        double generalWeight = 0.0;
        double happinessWeight = 0.0;
        double sadnessWeight = 0.0;
        double angerWeight = 0.0;
        double fearWeight = 0.0;
        double disgustWeight = 0.0;
        double surpriseWeight = 0.0;

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
