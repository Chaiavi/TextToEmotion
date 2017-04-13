package org.chaiware;

import org.chaiware.emotion.EmotionalState;

/**
 * An abstract class which contains data textualy interpreted by the
 * <p>
 * For example, it's subclass {@link EmotionalState} contains
 * emotional information about the piece of text.
 */
public abstract class SynesketchState {

	protected String text;

	/**
	 * Class constructor that sets text which is to be synestheticaly
	 * interpreted
	 * 
	 * @param text
	 *            {@link String} representing the text which is to be synestheticaly
	 *            interpreted
	 */

	public SynesketchState(String text) {
		this.text = text;
	}

	/**
	 * Getter for the text used as an interpretation resource
	 * 
	 * @return {@link String} representing the text
	 */

	public String getText() {
		return text;
	}

}
