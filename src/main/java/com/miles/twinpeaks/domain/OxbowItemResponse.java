package com.miles.twinpeaks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.miles.twinpeaks.domain.receipt.ReceiptItem;
import com.miles.twinpeaks.domain.usda.api.Food;
import com.miles.twinpeaks.domain.usda.api.USDAFood;
import com.miles.twinpeaks.domain.report.FoodPackage;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OxbowItemResponse extends FoodPackage {

    private ReceiptItem item;
    /*
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
    }*/

    public OxbowItemResponse(){}

    public OxbowItemResponse(ReceiptItem ri, Food esFood, USDAFood dbFood){
        this.item = ri;
        this.setEsFood(esFood);
        this.setDbFood(dbFood);
    }
}
