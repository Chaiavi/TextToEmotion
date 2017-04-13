package synesketch.app;

import java.io.IOException;

import synesketch.emotion.EmotionalState;
import synesketch.emotion.Empathyscope;

public class TestSentence {
	
	public static void main(String[] args) throws IOException {
		String line = "I love you so very much";
		EmotionalState sentenceState = Empathyscope.getInstance().feel(line);
		System.out.println(sentenceState.toString());
	}

}
