package com.miles.twinpeaks.domain.es5;

import org.springframework.data.annotation.Id;

import java.util.List;

public class IngredientMap {

    @Id
    private String id;

    private String ingredient_label;

    private Integer index;

    private IngredientMeta meta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIngredient_label() {
        return ingredient_label;
    }

    public void setIngredient_label(String ingredient_label) {
        this.ingredient_label = ingredient_label;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public IngredientMeta getMeta() {
        return meta;
    }

    public void setMeta(IngredientMeta meta) {
        this.meta = meta;
    }

    public IngredientMap() {
    }
}
