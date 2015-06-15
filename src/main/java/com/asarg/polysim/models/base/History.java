package com.asarg.polysim.models.base;


import com.asarg.polysim.models.base.FrontierElement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "History")
@XmlAccessorType(XmlAccessType.FIELD)
public class History {
    @XmlElement(name = "HistoryElement")
    private List<FrontierElement> history = null;

    public History() {
        history = new ArrayList<FrontierElement>();
    }

    public List<FrontierElement> getHistory() {
        return history;
    }

    public void setHistory(List<FrontierElement> history) {
        this.history = history;
    }
}