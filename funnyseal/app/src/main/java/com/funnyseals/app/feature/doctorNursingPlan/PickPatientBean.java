package com.funnyseals.app.feature.doctorNursingPlan;

public class PickPatientBean {

    private String id;

    private String name;

    public PickPatientBean(String name)
    {
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
