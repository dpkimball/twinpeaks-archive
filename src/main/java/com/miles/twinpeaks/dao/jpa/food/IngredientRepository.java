package com.miles.twinpeaks.dao.jpa.food;

import com.miles.twinpeaks.domain.usda.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface IngredientRepository extends PagingAndSortingRepository<Ingredient, Long> {

    Page findAll(Pageable pageable);

    Ingredient findBySubstanceIgnoreCase(String substance);
}
