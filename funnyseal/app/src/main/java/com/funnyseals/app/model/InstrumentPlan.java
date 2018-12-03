package com.funnyseals.app.model;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/12/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class InstrumentPlan {
    private String mInstrumentName;
    private String mInstrumentTracks;
    private String mInstrumentNote;

    public InstrumentPlan(String instrumentName, String instrumentNote, String instrumentTracks) {
        this.mInstrumentName = instrumentName;
        this.mInstrumentNote = instrumentNote;
        this.mInstrumentTracks = instrumentTracks;
    }

    public String getInstrumentName() {
        return mInstrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.mInstrumentName = instrumentName;
    }

    public String getInstrumentTracks() {
        return mInstrumentTracks;
    }

    public void setInstrumentTracks(String instrumentTracks) {
        this.mInstrumentTracks = instrumentTracks;
    }

    public String getInstrumentNote() {
        return mInstrumentNote;
    }

    public void setInstrumentNote(String instrumentNote) {
        this.mInstrumentNote = instrumentNote;
    }
}
