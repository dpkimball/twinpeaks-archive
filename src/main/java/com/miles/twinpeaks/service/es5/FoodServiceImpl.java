package com.miles.twinpeaks.service.es5;

import com.miles.twinpeaks.dao.jpa.es5.IFoodIngredientRepository;
import com.miles.twinpeaks.dao.jpa.es5.IFoodRepository;
import com.miles.twinpeaks.domain.es5.IngredientMeta;
import com.miles.twinpeaks.domain.usda.api.Food;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodServiceImpl implements IFoodService {

    private IFoodRepository foodRepository;
    private IFoodIngredientRepository foodIngredientRepository;

    @Autowired
    public void setFoodRepository(IFoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Autowired
    public void setFoodIngredientRepository(IFoodIngredientRepository foodIngredientRepository) {
        this.foodIngredientRepository = foodIngredientRepository;
    }

    public Food save(Food food) {
        return foodRepository.save(food);
    }

    public IngredientMeta save(IngredientMeta ingredient) {
        return foodIngredientRepository.save(ingredient);
    }

    public void delete(Food food) {
        foodRepository.delete(food);
    }

    public void delete_ingredient(String ingredient_id) {
        foodIngredientRepository.deleteById(ingredient_id);
    }

    public Food findByNdbno(String Ndbno) {
        return foodRepository.findByNdbno(Ndbno);
    }

    public Iterable<Food> findAll() {
        return foodRepository.findAll();
    }

    public List<Food> findByName(String barcodevalue) {
        return foodRepository.findByName(barcodevalue);
    }

    public List<IngredientMeta> findBySubstance(String substance) {
        substance = org.apache.lucene.queryparser.flexible.standard.QueryParserUtil.escape(substance);
        //todo move to exact search - fix index (not analyzed)
        List<IngredientMeta> substances = foodIngredientRepository.findBySubstance(substance);
        return substances;
    }

    public IngredientMeta searchSubstanceForMatch(String substance) {
        //todo move to exact search - fix index (not analyzed)
        List<IngredientMeta> substances = this.findBySubstance(substance);
        for(IngredientMeta im : substances){
            if (im.getSubstance().equalsIgnoreCase(substance)){
                return im;
            }
        }
        substances = this.findByOtherNames(substance);
        for(IngredientMeta im : substances) {
            if (im.getSubstance().equalsIgnoreCase(substance)) {
                return im;
            }
        }

        return null;
    }

    public Optional<IngredientMeta> findByIngredientId(String id) {
        return foodIngredientRepository.findById(id);
    }

    public List<IngredientMeta> findByOtherNames(String substance) {
        substance = org.apache.lucene.queryparser.flexible.standard.QueryParserUtil.escape(substance);
        return foodIngredientRepository.findByOtherNames(substance);
    }
    /*
    public List<IngredientMeta> searchBySubstance(String substance) {
        //BoolQueryBuilder keywordBuilder = boolQuery();
        //keywords.forEach(k -> keywordBuilder.should(matchPhraseQuery("text", k)));

        //BoolQueryBuilder stopWordBuilder = boolQuery();
        //stopWords.forEach(s -> stopWordBuilder.should(matchPhraseQuery("text", s)));

        BoolQueryBuilder queryBuilder = boolQuery().must(matchPhraseQuery("other_names", substance));
        List<IngredientMeta> results = foodIngredientRepository.search(queryBuilder);

        return results;
    }
    public long getHitsCount(String query, String filter, Project project) {
        try {
            MatchQueryBuilder QueryBuilder = QueryBuilders.matchQuery(filter, smartEscapeQuery(query));
            org.elasticsearch.index.query.QueryBuilder qb = QueryBuilders
                    .boolQuery()
                    .must(QueryBuilder);

            SearchRequestBuilder requestBuilder;
            requestBuilder = client.prepareSearch()
                    .setIndices(getDomainIndexId(project))
                    .setTypes(getProjectTypeId(project))
                    .setSearchType(SEARCH_TYPE)
                    .setQuery(qb);


            SearchResponse response = requestBuilder.execute().actionGet(ES_TIMEOUT_MS);
            SearchHits hits = response.getHits();
            if (hits.getTotalHits() > 0) {
                return hits.getTotalHits();
            }else{
                return 0l;
            }
        } catch (IndexMissingException ex) {

        }

    }*/
}
