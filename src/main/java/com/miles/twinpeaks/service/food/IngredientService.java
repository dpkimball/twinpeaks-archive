package com.miles.twinpeaks.service.food;

import com.miles.twinpeaks.dao.jpa.food.IngredientCategoryRepository;
import com.miles.twinpeaks.dao.jpa.food.IngredientRepository;
import com.miles.twinpeaks.domain.usda.Ingredient;
import com.miles.twinpeaks.domain.usda.IngredientCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IngredientService {

    private static final Logger log = LoggerFactory.getLogger(IngredientService.class);

    @Autowired
    private IngredientRepository repo;

    @Autowired
    private IngredientCategoryRepository ingcateRepo;

    public IngredientService() {
    }

    public Ingredient createIngredient(Ingredient ingredient) {
        return repo.save(ingredient);
    }

    public IngredientCategory createCategory(IngredientCategory category) {
        return ingcateRepo.save(category);
    }

    public Ingredient getIngredient(long id) {
        Optional<Ingredient> r = repo.findById(id);
        if (r.isPresent()) {
            Ingredient ing = r.get();
            return ing;
        }
        return null;
    }

    public Iterable<IngredientCategory> getCategories() {
        return ingcateRepo.findAll();
    }

    public Iterable<Ingredient> getIngredients() {
        return repo.findAll();
    }

    public Ingredient getSubstance(String substance){
        //look in the database (postgres and see if this object exists
        Ingredient ing = repo.findBySubstanceIgnoreCase(substance);
        return  ing;
    }
}
