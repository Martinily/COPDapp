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
    private String mSportsName;
    private String mSportsTracks;
    private String mSportsNote;

    public SportsPlan (String sportsName, String sportsTracks, String sportsNote) {
        this.mSportsName = sportsName;
        this.mSportsTracks = sportsTracks;
        this.mSportsNote = sportsNote;
    }

    public String getSportsName () {
        return mSportsName;
    }

    public void setSportsName (String sportsName) {
        this.mSportsName = sportsName;
    }

    public String getSportsTracks () {
        return mSportsTracks;
    }

    public void setSportsTracks (String sportsTracks) {
        this.mSportsTracks = sportsTracks;
    }

    public String getSportsNote () {
        return mSportsNote;
    }

    public void setSportsNote (String sportsNote) {
        this.mSportsNote = sportsNote;
    }
}
