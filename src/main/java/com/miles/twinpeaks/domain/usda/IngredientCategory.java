package com.miles.twinpeaks.domain.usda;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ingredient_category", uniqueConstraints = {@UniqueConstraint(columnNames = {"ingcat_id"})})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class , property = "ingcat_id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class IngredientCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingcat_id", nullable = false)
    @JsonProperty("ingcat_id")
    private long id;

    @Column(nullable = false, unique = true)
    private String category;

    public IngredientCategory() {
    }

    public IngredientCategory(String category) {
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @ManyToMany(fetch=FetchType.LAZY, mappedBy = "categories")
    //@JsonBackReference(value = "categories")
    private List<Ingredient> ingredients = new ArrayList<>();

    @Override
    public String toString() {
        return "Ingredient Category {" +
                "id=" + id +
                ", category=" + category +
                '}';
    }
}
