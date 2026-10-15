package com.miles.twinpeaks.domain.usda.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.miles.twinpeaks.domain.es5.IngredientMap;
import com.miles.twinpeaks.domain.es5.IngredientMeta;

import java.util.List;
import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingredient {

    private String desc;
    private String upd;
    private List<IngredientMap> meta;

    public String getDesc() {
        return desc;
    }

    public List<IngredientMap> getMeta() {
        return meta;
    }

    public void setMeta(List<IngredientMap> meta) {
        this.meta = meta;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUpd() {
        return upd;
    }

    public void setUpd(String upd) {
        this.upd = upd;
    }

    public Ingredient(){}

}
