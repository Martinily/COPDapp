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
    private String instrumentName;
    private String instrumentTracks;
    private String instrumentNote;

    public InstrumentPlan(String instrumentName, String instrumentNote, String instrumentTracks) {
        this.instrumentName = instrumentName;
        this.instrumentNote = instrumentNote;
        this.instrumentTracks = instrumentTracks;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName() {
        this.instrumentName = instrumentName;
    }

    public String getInstrumentTracks() {
        return instrumentTracks;
    }

    public void setInstrumentTracks() {
        this.instrumentTracks = instrumentTracks;
    }

    public String getInstrumentNote() {
        return instrumentNote;
    }

    public void setInstrumentNote() {
        this.instrumentNote = instrumentNote;
    }
}
