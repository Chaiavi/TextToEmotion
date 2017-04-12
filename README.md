# TextToEmotion
Parses text in order to recognize the emotions in it

TextToEmotions reads the block of text, parses emotions from it, then returns a response object containing the percent value propability of each emotion.

The library's core logic was taken from Synesketch http://krcadinac.com/synesketch/, which has a GPL v2.0 license.

It was then much changed to be fit as a reusable library with Maven support, and the code containing the graphics presentation of the emotions was removed as it is not relevant to the users which want only the core parsing.

In short, just throw text to this library and it will analyze it then return the emotions exhibited in the text, the library is not 100% accurate of course but is very reliable.