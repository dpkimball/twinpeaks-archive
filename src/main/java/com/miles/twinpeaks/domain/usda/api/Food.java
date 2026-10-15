package com.miles.twinpeaks.domain.usda.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "watatic", type = "foods")
public class Food {

    @Id
    private String ndbno;
    private String name;
    private String manu;
    private String ds;
    private String ru;
    private Ingredient ing;
    private List<Nutrient> nutrients;

    public String getNdbno() {
        return ndbno;
    }

    public void setNdbno(String ndbno) {
        this.ndbno = ndbno;
    }

    public String getName() {
        return name;
    }

    public String getRu() {
        return ru;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getManu() {
        return manu;
    }

    public void setManu(String manu) {
        this.manu = manu;
    }

    public String getDs() {
        return ds;
    }

    public void setDs(String ds) {
        this.ds = ds;
    }

    public Ingredient getIng() {
        return ing;
    }

    public void setIng(Ingredient ing) {
        this.ing = ing;
    }

    public List<Nutrient> getNutrients() {
        return nutrients;
    }

    public void setNutrients(List<Nutrient> nutrients) {
        this.nutrients = nutrients;
    }

    public Food() {
    }

}
