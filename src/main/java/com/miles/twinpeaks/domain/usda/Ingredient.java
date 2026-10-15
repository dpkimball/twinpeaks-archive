package com.miles.twinpeaks.domain.usda;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;


@Entity
@Table(name = "food_ingredient", uniqueConstraints = {@UniqueConstraint(columnNames = {"ing_id", "substance"})})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ing_id")
    private long id;

    @Column()
    private String substance;

    public long getId() {
        return id;
    }

    public String getSubstance() {
        return substance;
    }

    public void setSubstance(String substance) {
        this.substance = substance;
    }

    public Set<IngredientCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<IngredientCategory> categories) {
        this.categories = categories;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "food_ingredient_category",
            joinColumns = {@JoinColumn(name = "ing_id")},
            inverseJoinColumns = {@JoinColumn(name = "ingcat_id")})
    private Set<IngredientCategory> categories;

    public Ingredient() {
    }
}
