package com.miles.twinpeaks.dao.jpa.es5;

import com.miles.twinpeaks.domain.es5.IngredientMeta;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface IFoodIngredientRepository extends ElasticsearchRepository<IngredientMeta, String> {

    List<IngredientMeta> findBySubstance(String substance);

    List<IngredientMeta> findByOtherNames(String substance);

    List<IngredientMeta> search(QueryBuilder query);

    //IngredientMeta searchSubstanceForMatch(String substance);

}
