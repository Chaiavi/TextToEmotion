package org.chaiware.app;

import java.io.IOException;

import org.chaiware.emotion.EmotionalState;
import org.chaiware.emotion.Empathyscope;

public class TestSentence {
	
	public static void main(String[] args) throws IOException {
		String line = "I love you so very much";
		EmotionalState sentenceState = Empathyscope.getInstance().feel(line);
		System.out.println(sentenceState.toString());
	}

}
