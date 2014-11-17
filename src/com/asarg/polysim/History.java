package com.asarg.polysim;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "History")
@XmlAccessorType (XmlAccessType.FIELD)
public class History
{
    public History(){
        history = new ArrayList<FrontierElement>();
    }

    @XmlElement(name = "HistoryElement")
    private List<FrontierElement> history = null;

    public List<FrontierElement> getHistory() {
        return history;
    }

    public void setHistory(List<FrontierElement> history) {
        this.history = history;
    }
}