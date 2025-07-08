package org.chaiware.emotion;
public enum EmotionType {
    NEUTRAL(-1),
    HAPPINESS(0),
    SADNESS(1),
    FEAR(2),
    ANGER(3),
    DISGUST(4),
    SURPRISE(5);

    private final int code;

    EmotionType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
