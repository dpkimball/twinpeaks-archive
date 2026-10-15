package com.miles.twinpeaks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.miles.twinpeaks.domain.usda.api.Food;
import com.miles.twinpeaks.domain.usda.api.USDAFood;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OxbowFoodResponse {

    private Food esFood;
    private USDAFood dbFood;

    public Food getEsFood() {
        return esFood;
    }

    public void setEsFood(Food esFood) {
        this.esFood = esFood;
    }

    public USDAFood getDbFood() {
        return dbFood;
    }

    public void setDbFood(USDAFood dbFood) {
        this.dbFood = dbFood;
    }

    public OxbowFoodResponse(){}

    public OxbowFoodResponse(Food esFood, USDAFood dbFood){
        this.esFood = esFood;
        this.dbFood = dbFood;
    }
}
