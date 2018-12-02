package com.funnyseals.app.model;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/12/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SportsPlan {
    private String sportsName;
    private String sportsTracks;
    private String sportsNote;

    public SportsPlan(String sportsName, String sportsTracks, String sportsNote) {
        this.sportsName = sportsName;
        this.sportsTracks = sportsTracks;
        this.sportsNote = sportsNote;
    }

    public String getSportsName() {
        return sportsName;
    }

    public void setSportsName(String sportsName) {
        this.sportsName = sportsName;
    }

    public String getSportsTracks() {
        return sportsTracks;
    }

    public void setSportsTracks(String sportsTracks) {
        this.sportsTracks = sportsTracks;
    }

    public String getSportsNote() {
        return sportsNote;
    }

    public void setSportsNote(String sportsNote) {
        this.sportsNote = sportsNote;
    }
}
