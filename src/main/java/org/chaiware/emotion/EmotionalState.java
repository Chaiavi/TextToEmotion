package org.chaiware.emotion;

import java.util.*;


/**
 * Defines emotional content of the text.
 * <p>
 * That is:
 * <ul>
 * <li>General emotional weight
 * <li>General valence (emotion is positive or negative)
 * <li>Six specific emotional weights, defined by Ekman's categories: happiness
 * weight, sadness weight, fear weight, anger weight, disgust weight, surprise
 * weight. These specific emotions are defined by the class {@link Emotion}.
 * <li>Previous {@link EmotionalState} (so that whole emotional history of one
 * conversation can be accessed from the Processing applet ).
 * </ul>
 * <p>
 * Weights have values between 0 and 1 (0 for no emotion, 1 for full emotion,
 * 0.5 for the emotion of average intesity). Valence can be -1, 0, or 1
 * (negative, neutral, and positive emotion, respectively).
 * 
 */
public class EmotionalState {

	private String text;
	private double generalWeight = 0.0;
	private int valence = 0;
	private EmotionalState previous;
	private SortedSet<Emotion> emotions;


	public EmotionalState() {
		this("");
	}

	/**
	 * Class constructor which sets the text.
	 * 
	 * @param text String representing the text
	 */
	public EmotionalState(String text) {

		this.text = text;
		emotions = new TreeSet<Emotion>();
		emotions.add(new Emotion(1.0, Emotion.NEUTRAL));
	}

	/**
	 * Class constructor which sets the text, general emotional weight, emotional
	 * valence, and all of the emotional weights (in a form of a SortedSet).
	 * 
	 * @param text {@link String} representing the text
	 * @param emotions {@link SortedSet} containing all of the specific Ekman emotional
	 *            weights, defined by the {@link Emotion} class
	 * @param generalWeight double representing the general emotional weight
	 * @param valence int representing the emotinal valence
	 */
	public EmotionalState(String text, SortedSet<Emotion> emotions,
			double generalWeight, int valence) {

		this(text);
		this.generalWeight = generalWeight;
		this.valence = valence;
		this.emotions = emotions;
	}

	/**
	 * Returns {@link Emotion} with the highest weight.
	 * 
	 * @return Emotion with the highest weight
	 */
	public Emotion getStrongestEmotion() {
		return emotions.first();
	}

	/**
	 * Returns several emotions ({@link Emotion} instances) with the highest weight.
	 * 
	 * @param stop int representing the number of emotions which is to searched for
	 * @return list of emotions ({@link Emotion} instances) with the highest weight
	 */
	public List<Emotion> getFirstStrongestEmotions(int stop) {
		List<Emotion> value = new ArrayList();
		for (Emotion e : emotions) {
			if (stop <= 0) {
				break;
			}
			value.add(e);
			stop--;
		}

		return value;
	}

	/**
	 * Getter for the {@link Emotion} of happiness.
	 * 
	 * @return {@link Emotion} of happiness
	 */
	public Emotion getHappiness() {
		Emotion value = new Emotion(0.0, Emotion.HAPPINESS);
		for (Emotion e : emotions) {
			if (e.getType() == Emotion.HAPPINESS) {
				value = e;
			}
		}

		return value;
	}

	/**
	 * Getter for the happiness weight
	 * 
	 * @return double representing the happiness weight
	 */
	public double getHappinessWeight() {
		return getHappiness().getWeight();
	}

	/**
	 * Getter for the {@link Emotion} of sadness.
	 * 
	 * @return {@link Emotion} of sadness
	 */
	public Emotion getSadness() {
		Emotion value = new Emotion(0.0, Emotion.SADNESS);
		for (Emotion e : emotions) {
			if (e.getType() == Emotion.SADNESS) {
				value = e;
			}
		}

		return value;
	}

	/**
	 * Getter for the sadness weight
	 * 
	 * @return double representing the sadness weight
	 */
	public double getSadnessWeight() {
		return getSadness().getWeight();
	}

	/**
	 * Getter for the {@link Emotion} of fear.
	 * 
	 * @return {@link Emotion} of fear
	 */
	public Emotion getFear() {
		Emotion value = new Emotion(0.0, Emotion.FEAR);
		for (Emotion e : emotions) {
			if (e.getType() == Emotion.FEAR) {
				value = e;
			}
		}

		return value;
	}

	/**
	 * Getter for the fear weight
	 * 
	 * @return double representing the fear weight
	 */
	public double getFearWeight() {
		return getFear().getWeight();
	}

	/**
	 * Getter for the {@link Emotion} of anger.
	 * 
	 * @return {@link Emotion} of anger
	 */
	public Emotion getAnger() {
		Emotion value = new Emotion(0.0, Emotion.ANGER);
		for (Emotion e : emotions) {
			if (e.getType() == Emotion.ANGER) {
				value = e;
			}
		}

		return value;
	}

	/**
	 * Getter for the anger weight
	 * 
	 * @return double representing the anger weight
	 */
	public double getAngerWeight() {
		return getAnger().getWeight();
	}

	/**
	 * Getter for the {@link Emotion} of disgust.
	 * 
	 * @return {@link Emotion} of disgust
	 */
	public Emotion getDisgust() {
		Emotion value = new Emotion(0.0, Emotion.DISGUST);
		for (Emotion e : emotions) {
			if (e.getType() == Emotion.DISGUST) {
				value = e;
			}
		}

		return value;
	}

	/**
	 * Getter for the disgust weight
	 * 
	 * @return double representing the disgust weight
	 */
	public double getDisgustWeight() {
		return getDisgust().getWeight();
	}

	/**
	 * Getter for the {@link Emotion} of surprise
	 * 
	 * @return {@link Emotion} of surprise
	 */
	public Emotion getSurprise() {
		Emotion value = new Emotion(0.0, Emotion.SURPRISE);
		for (Emotion e : emotions) {
			if (e.getType() == Emotion.SURPRISE) {
				value = e;
			}
		}

		return value;
	}

	/**
	 * Getter for the surprise weight
	 * 
	 * @return double representing the surprise weight
	 */
	public double getSurpriseWeight() {
		return getSurprise().getWeight();
	}

	/**
	 * Getter for the previous {@link EmotionalState}
	 * 
	 * @return previous {@link EmotionalState}
	 */
	public EmotionalState getPrevious() {
		return previous;
	}

	/**
	 * Setter for the previous {@link EmotionalState}
	 * 
	 * @param previous
	 *            previous {@link EmotionalState}
	 */
	public void setPrevious(EmotionalState previous) {
		this.previous = previous;
	}

	/**
	 * Getter for the emotional valence
	 * 
	 * @return emotional valence
	 */
	public int getValence() {
		return valence;
	}

	/**
	 * Getter for the general emotional weight
	 * 
	 * @return general emotional weight
	 */
	public double getGeneralWeight() {
		return generalWeight;
	}

	/**
	 * Getter for the text used as an interpretation resource
	 *
	 * @return {@link String} representing the text
	 */

	public String getText() {
		return text;
	}

	/**
	 * Transforms emotional data into a descriptive sentence ('toString' method)
	 * 
	 * @return String description of a emotinal data
	 */
	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner("\n");
		sj.add("Text: " + text);
		sj.add("General weight: " + generalWeight);
		sj.add("Valence: " + valence);
		sj.add("Happiness weight: " + getHappinessWeight());
		sj.add("Sadness weight: " + getSadnessWeight());
		sj.add("Anger weight: " + getAngerWeight());
		sj.add("Fear weight: " + getFearWeight());
		sj.add("Disgust weight: " + getDisgustWeight());
		sj.add("Surprise weight: " + getSurpriseWeight());

		return sj.toString();
	}
}
