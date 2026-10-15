package com.miles.twinpeaks.domain.es5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.miles.twinpeaks.domain.usda.IngredientCategory;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.HashSet;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "watatic", type = "ingredients")
public class IngredientMeta {

    @Id
    private String id;
    private String substance;
    private String substanceUrl;
    private String asRegNo;
    private List<Crf21> crf21;
    private List<String> otherNames;
    private List<IngredientCategory> categories;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubstance() {
        return substance;
    }

    public void setSubstance(String substance) {
        this.substance = substance;
    }

    public String getSubstance_url() {
        return substanceUrl;
    }

    public void setSubstance_url(String substance_url) {
        this.substanceUrl = substance_url;
    }

    public String getAsRegNo() {
        return asRegNo;
    }

    public void setAsRegNo(String asRegNo) {
        this.asRegNo = asRegNo;
    }

    public List<Crf21> getCrf21() {
        return crf21;
    }

    public void setCrf21(List<Crf21> crf21) {
        this.crf21 = crf21;
    }

    public List<IngredientCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<IngredientCategory> categories) {
        this.categories = categories;
    }

    public List<String> getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(List<String> other_names) {
        this.otherNames = other_names;
    }

    public IngredientMeta() {
    }

    public com.miles.twinpeaks.domain.usda.Ingredient toRelationalRecord() {
        com.miles.twinpeaks.domain.usda.Ingredient ing = new com.miles.twinpeaks.domain.usda.Ingredient();
        if (this.id != null) {
            ing.setId(Long.parseLong(id));
        }
        ing.setCategories(new HashSet<>(categories));
        ing.setSubstance(substance);
        return ing;
    }
}
