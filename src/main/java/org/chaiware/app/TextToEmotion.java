package org.chaiware.app;

import java.io.IOException;

import org.chaiware.emotion.EmotionalState;
import org.chaiware.emotion.Empathyscope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This class is the class you should use in order to parse your text for emotions, it contains static methods */
public class TextToEmotion {

	private static Logger logger = LoggerFactory.getLogger(TextToEmotion.class);

	/** For internal use, in order to try the app and see that it is working while developing it */
	public static void main(String[] args ) throws Exception {
		if (args.length < 1) {
			logger.info("Please send an argument with the string you want to sense for emotion");
			System.exit(0);
		}
		String text = args[0];

		logger.info(textToEmotion(text).toString());
	}

	/** Use this method in order to analyze any text for emotions */
	public static EmotionalState textToEmotion(String text) throws Exception {

		EmotionalState sentenceState = new EmotionalState();
		try {
			sentenceState = Empathyscope.getInstance().feel(text);
		} catch (IOException e) {
			logger.debug("TextToEmotion failure", e);
			throw new Exception("TextToEmotion failed to start (probably due to failure of loading its internal files)");
		}

		return sentenceState;
	}
}
