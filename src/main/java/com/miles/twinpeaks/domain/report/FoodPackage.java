package com.miles.twinpeaks.domain.report;

import com.miles.twinpeaks.domain.usda.api.Food;
import com.miles.twinpeaks.domain.usda.api.USDAFood;

public class FoodPackage {
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

    public FoodPackage(){}
    public FoodPackage(Food esFood, USDAFood dbFood) {
        this.esFood = esFood;
        this.dbFood = dbFood;
    }
}
