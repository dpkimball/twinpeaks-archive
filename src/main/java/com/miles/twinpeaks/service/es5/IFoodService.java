package com.miles.twinpeaks.service.es5;

import com.miles.twinpeaks.domain.es5.IngredientMeta;
import com.miles.twinpeaks.domain.usda.api.Food;

import java.util.List;
import java.util.Optional;

public interface IFoodService {

    Food save(Food food);
    void delete(Food food);

    Food findByNdbno(String Ndbno);
    Iterable<Food> findAll();
    List<Food> findByName(String barcodevalue);

    void delete_ingredient(String ingredient_id);

    IngredientMeta save(IngredientMeta ingredient);
    List<IngredientMeta> findBySubstance(String substance);
    //List<IngredientMeta> searchBySubstance(String substance);
    List<IngredientMeta> findByOtherNames(String substance);
    Optional<IngredientMeta> findByIngredientId(String id);
    IngredientMeta searchSubstanceForMatch(String substance);
}
