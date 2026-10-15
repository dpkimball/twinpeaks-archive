package com.miles.twinpeaks.dao.jpa.food;

import com.miles.twinpeaks.domain.usda.Ingredient;
import com.miles.twinpeaks.domain.usda.IngredientCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface IngredientCategoryRepository extends PagingAndSortingRepository<IngredientCategory, Long> {
    Page findAll(Pageable pageable);
}
