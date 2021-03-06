package org.chaiware.emotion;

/**
 * Represents one emotion, with its type and weight.
 * <p>
 * Emotion types are the ones defined by Ekman: happiness, sadness, fear, anger,
 * disgust, surprise. These types are defines by the static attributes of this class.
 */

public class Emotion implements Comparable<Emotion> {

	public static int NEUTRAL = -1;
	public static int HAPPINESS = 0;
	public static int SADNESS = 1;
	public static int FEAR = 2;
	public static int ANGER = 3;
	public static int DISGUST = 4;
	public static int SURPRISE = 5;

	private double weight;
	private int type;

	/**
	 * Class constructor which sets weight and type of the emotion.
	 * 
	 * @param weight double which represents the intensity of the emotion (can take
	 *            values between 0 and 1)
	 * @param type type of the emotion (happiness, sadness, fear, anger, disgust, or surprise)
	 */
	public Emotion(double weight, int type) {
		this.weight = weight;
		this.type = type;
	}

	/**
	 * Compares weights of current object and the one from the argument.
	 * 
	 * @param emotion {@link Emotion} which is to compared to the current one
	 * @return integer representing the result
	 */
	@Override
	public int compareTo(Emotion emotion) {
		int value = (int) (100 * (emotion.getWeight() - weight));
		// make sure each emotion will be considered, even if it is weight-even with another one
		if (value == 0)
			return 1;

		return value;
	}

	/**
	 * Getter for the emotion type
	 * 
	 * @return emotion type (integer constant defined by this class)
	 */
	public int getType() {
		return type;
	}

	/**
	 * Setter for the emotion type
	 * 
	 * @param type emotion type (integer constant defined by this class)
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Getter for the emotional weight
	 * 
	 * @return double representing the emotional weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Setter for the emotional weight
	 * 
	 * @param value double representing the emotional weight
	 */
	public void setWeight(double value) {
		this.weight = value;
	}

	/**
	 * Returns a string representation of the object.
	 * 
	 * @return a string representation of the object
	 */
	public String toString() {
		return "Type number: " + type + ", weight: " + weight;
	}
}
