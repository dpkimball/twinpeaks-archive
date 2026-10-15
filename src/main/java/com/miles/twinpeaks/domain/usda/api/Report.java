package com.miles.twinpeaks.domain.usda.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Report {
    private String sr;
    private String type;
    private Food food;
    public Report(){}

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }

    public String getType() {
        return type;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Report(String sr, String type, Food food){
        this.sr = sr;
        this.type = type;
        this.food = food;
    }
}
