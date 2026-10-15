package com.miles.twinpeaks.service.food;

import com.miles.twinpeaks.dao.jpa.food.IngredientCategoryRepository;
import com.miles.twinpeaks.dao.jpa.food.IngredientRepository;
import com.miles.twinpeaks.domain.es5.IngredientMap;
import com.miles.twinpeaks.domain.es5.IngredientMeta;
import com.miles.twinpeaks.domain.usda.Ingredient;
import com.miles.twinpeaks.domain.usda.IngredientCategory;
import com.miles.twinpeaks.domain.usda.api.Food;
import com.miles.twinpeaks.domain.usda.api.Response;
import com.miles.twinpeaks.domain.usda.api.USDAFood;
import com.miles.twinpeaks.domain.report.FoodPackage;
import com.miles.twinpeaks.service.es5.IFoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This service creates a dictionary of a food product.  The service will retrieve data from various services
 * and consolidate the information into a single record for the API to retrieve.  This will speed up
 * and reduce the overhead of calling a various services to research a food item.
 */
@Service
public class FoodCollectionService {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    public FoodCollectionService() {
    }

    @Autowired
    private IFoodService esFoodService;
    @Autowired
    private USDAFoodService dbFoodService;



    /*
    POSTGRES CALLS
    ---------------------------------------------------------------------------------------
    @Autowired
    private IngredientRepository ingSQLRepo;
    @Autowired
    private IngredientCategoryRepository ingCatSQLRepo;
    public Ingredient createIngredient(Ingredient ingredient) {
        return ingSQLRepo.save(ingredient);
    }

    public IngredientCategory createCategory(IngredientCategory category) {
        return ingCatSQLRepo.save(category);
    }

    public Ingredient getIngredient(long id) {
        Optional<Ingredient> r = ingSQLRepo.findById(id);
        if (r.isPresent()) {
            Ingredient ing = r.get();
            return ing;
        }
        return null;
    }

    public Iterable<IngredientCategory> getCategories() {
        return ingCatSQLRepo.findAll();
    }

    public Iterable<Ingredient> getIngredients() {
        return ingSQLRepo.findAll();
    }

    public Ingredient getSubstance(String substance){
        //look in the database (postgres and see if this object exists
        Ingredient ing = ingSQLRepo.findBySubstanceIgnoreCase(substance);
        return  ing;
    }
        /* Postgres CALLS
    ---------------------------------------------------------------------------------------

     */

    /**
     * Retrieve a report of the food.  This will contain information about the food item including
     * a deep search of each ingredient in the food.
     *
     * @param barcode
     * @return
     */
    public FoodPackage SeekFoodPackage(String barcode) {
        //see if we can find the item in our index
        USDAFood dbFood = this.dbFoodService.getUSDAFoodByUPC(barcode);

        // if no look up then it doesn't exist
        if (dbFood == null) {
            return null;
        }

        // retrieve the food data from the elastic search engine
        // This is a registered food item in the USDA database
        // This will bring back basic information and more if it is already researched
        Food esFood = getFoodMetaData(dbFood.getNdbno());
        if (esFood != null) {
            // retrieve the string of ingredients labeled with the food
            com.miles.twinpeaks.domain.usda.api.Ingredient ing = esFood.getIng();

            // we may have already done this; so save the transaction if not needed
            boolean save = false;

            // every food should have an ingredients
            // todo:
            if (ing.getDesc() == null) {
                log.error(String.format("Unable to retrieve ingredient information for food %s", dbFood.getName()));
            }

            //is there already a map/meta data for this ingredient
            List<IngredientMap> meta = ing.getMeta();
            if (meta == null) {
                meta = new ArrayList<>();
                ing.setMeta(meta);
            }
            // split the ingredients by a comma
            // we will need to do a deep analysis on these ingredients
            // parse the concatenated string from usda and map it to their other service info
            String[] ingredients = ing.getDesc().split(",");

            //todo - we need to build in versioning and updating logic
            // we can start adding more services to do a thorough investigation on the ingredient
            for (int i = 0; i < ingredients.length; i++) {
                IngredientMap map = new IngredientMap();
                String label_ing = ingredients[i].trim();

                //attempt to match the ingredient with the additive index
                // this will retrieve category data about this ingredient
                // todo: move to keyword and other names
                IngredientMeta research_meta = this.esFoodService.searchSubstanceForMatch(label_ing);

                //we need to line up the label with the research (formatting and not normalized)
                map.setIndex(i);
                map.setIngredient_label(label_ing.trim());
                //now save the other meta data
                map.setMeta(research_meta);
                meta.add(map);
                save = true;
            }

            // we need to save the document?
            if (save) {
                //save the record in elastic search
                this.esFoodService.save(esFood);
            }
        }
        return new FoodPackage(esFood, dbFood);
    }

    /**
     * Calls the USDA Food API service to retrieve data about a particular food product.  We need to call
     * the api by using their id
     *
     * @param id ndbno id
     * @return serialized USDA Food domain object
     */
    private Food getFoodMetaData(long id) {
        Food esFood;
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("https://api.nal.usda.gov/ndb/reports/?ndbno=%s&type=b&format=json&api_key=%s", id, System.getenv("USDA_API_KEY"));
        try {
            Response usda_response = restTemplate.getForObject(url, Response.class, 200);
            esFood = usda_response.getReport().getFood();
            esFood.setNdbno(Long.toString(id));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return null;
        }
        return esFood;
    }

    /*private Food getSubstanceAddedToFood(String url) {
        Food esFood;
        RestTemplate restTemplate = new RestTemplate();
        try {
            Response usda_response = restTemplate.getForObject(url, Response.class, 200);
            esFood = usda_response.getReport().getFood();
            esFood.setNdbno(Long.toString(id));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return null;
        }
        return esFood;
    }*/
}
