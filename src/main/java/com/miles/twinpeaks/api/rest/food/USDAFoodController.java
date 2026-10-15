package com.miles.twinpeaks.api.rest.food;

import com.miles.twinpeaks.api.rest.AbstractRestHandler;
import com.miles.twinpeaks.domain.es5.IngredientMeta;
import com.miles.twinpeaks.domain.usda.Ingredient;
import com.miles.twinpeaks.domain.OxbowFoodResponse;
import com.miles.twinpeaks.domain.usda.IngredientCategory;
import com.miles.twinpeaks.domain.usda.api.Food;
import com.miles.twinpeaks.domain.usda.api.USDAFood;
import com.miles.twinpeaks.domain.usda.api.Response;
import com.miles.twinpeaks.service.es5.IFoodService;
import com.miles.twinpeaks.service.food.IngredientService;
import com.miles.twinpeaks.service.food.USDAFoodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/*
 * Demonstrates how to set up RESTful API endpoints using Spring MVC
 */

@RestController
@RequestMapping(value = "/v1/food")
@Api(tags = {"USDAFood"})
public class USDAFoodController extends AbstractRestHandler {
    @Autowired
    private IFoodService esFoodSearchService;
    @Autowired
    private USDAFoodService dbFoodService;
    @Autowired
    private IngredientService ingredientService;


    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a USDAFood resource.", notes = "Returns the URL of the new resource in the Location header.")
    public void createFoodItem(@RequestBody USDAFood USDAFood,
                               HttpServletRequest request, HttpServletResponse response) {
        //USDAFood USDAFood = this.foodService.getFood();
        USDAFood food = this.dbFoodService.createUSDAFood(USDAFood);
        response.setHeader("Location", request.getRequestURL().append("/").append(food.getUsda_food_id()).toString());
    }


    @RequestMapping(value = "/ingredient",
            method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a USDAFood resource.", notes = "Returns the URL of the new resource in the Location header.")
    public void createIngredient(@RequestBody IngredientMeta ingredient,
                                 HttpServletRequest request, HttpServletResponse response) {

        //find the ingredient
        Ingredient dbIng = this.ingredientService.getSubstance(ingredient.getSubstance());
        if (dbIng != null) {
            String id = Long.toString(dbIng.getId());
            Optional<IngredientMeta> cache = this.esFoodSearchService.findByIngredientId(id);
            if (cache == null) {
                ingredient.setId(id);
                this.esFoodSearchService.save(ingredient);
            }
        }

        //covert the record into relation db model to save
        Ingredient ing = ingredient.toRelationalRecord();

        Iterable<IngredientCategory> cacheCategories = this.ingredientService.getCategories();
        boolean found = false;
        for (IngredientCategory ingredientCategory : ing.getCategories()) {
            found = false;
            String ic = ingredientCategory.getCategory();
            for (IngredientCategory cacheCat : cacheCategories) {
                if (ic.equalsIgnoreCase(cacheCat.getCategory())) {
                    ingredientCategory.setId(cacheCat.getId());
                    found = true;
                    break;
                }
            }
            if (!found) {
                this.ingredientService.createCategory(ingredientCategory);
            }
        }

        //save record in the relation database
        Ingredient i = this.ingredientService.getSubstance(ingredient.getSubstance());
        if (i == null) {
            i = this.ingredientService.createIngredient(ing);
        }
        ingredient.setId(Long.toString(i.getId()));

        //save the record into elastic search
        this.esFoodSearchService.save(ingredient);

        response.setHeader("Location", request.getRequestURL().append("/").append(ing.getId()).toString());
    }

    @RequestMapping(value = "/ingredient/{id}",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single ingredient item.", notes = "You have to provide a valid ingredient ID.")
    public
    @ResponseBody
    Ingredient getIngredient(@ApiParam(value = "The ID of the ingredient.", required = true)
                             @PathVariable("id") Long id,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        Ingredient ing = this.ingredientService.getIngredient(id);
        checkResourceFound(ing);
        return ing;
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single USDAFood item.", notes = "You have to provide a valid USDAFood item ID.")
    public
    @ResponseBody
    OxbowFoodResponse getUSDAFood(@ApiParam(value = "The ID of the USDAFood.", required = true)
                                  @PathVariable("id") Long id,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        Food esFood = this.esFoodSearchService.findByNdbno(id.toString());
        USDAFood dbFood = this.dbFoodService.getUSDAFood(id);
        checkResourceFound(esFood);
        return new OxbowFoodResponse(esFood, dbFood);
    }

    @RequestMapping(value = "/search/{upc}",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single USDAFood item.", notes = "You have to provide a valid USDAFood item ID.")
    public
    @ResponseBody
    Food searchUSDAFood(@ApiParam(value = "The bar code value of the item.", required = true)
                        @PathVariable("upc") String upc,
                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        USDAFood lookupFood = this.dbFoodService.getUSDAFoodByUPC(upc);

        checkResourceFound(lookupFood);

        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("https://api.nal.usda.gov/ndb/reports/?ndbno=%s&type=b&format=json&api_key=%s", lookupFood.getNdbno(), System.getenv("USDA_API_KEY"));
        Response usda_response = restTemplate.getForObject(url, Response.class, 200);
        Food food = usda_response.getReport().getFood();
        if (food != null) {
            //save the record in elastic search
            this.esFoodSearchService.save(food);
            //save the record in our database
            return food;
        }

        return null;
    }
/*
    @RequestMapping(value = "/{id}/{item_id}",
            method = RequestMethod.PUT,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update a USDAFood resource.", notes = "You have to provide a valid USDAFood Item ID in the URL and in the payload. The ID attribute can not be updated.")
    public void updateItem(@ApiParam(value = "The ID of the existing USDAFood Item resource.", required = true)
                           @PathVariable("id") Long id,
                           @PathVariable("item_id") Long item_id,
                           @RequestBody USDAFood USDAFood,
                           HttpServletRequest request, HttpServletResponse response) {

        // verify that we can find the parent (USDAFood)
        USDAFood r = this.foodService.getUSDAFood(id);
        checkResourceFound(r);

        // verify that we can find the line item itself
        //Optional<USDAFood> ri = this.foodService.getFoodItem(item_id);

        // make sure the path lines up to json that was sent
        if (item_id != USDAFood.getUsda_food_id()) throw new DataFormatException("Item ID doesn't match!");
        //if (id != this.USDAFood.get().getId()) throw new DataFormatException("ID doesn't match!");

        //this.foodService.updateFoodItem(ri.get());
    }
    */
}
