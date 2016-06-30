package edu.orissermaroix.vocabpaper;

/**
 * Created by VieViee31 on 30/06/2016.
 */
public class MyWord {
    protected String french = null;
    protected String english = null;

    public MyWord(String english, String french) {
        this.french = french;
        this.english = english;
    }

    public String getEnglish() {
        return english;
    }

    public String getFrench() {
        return french;
    }
}
