package com.miles.twinpeaks.api.rest.food;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.miles.twinpeaks.api.rest.AbstractRestHandler;
import com.miles.twinpeaks.domain.usda.api.Food;
import com.miles.twinpeaks.service.es5.FoodServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/v1/Foods")
@Api(tags = {"Foods"})
public class FoodSearchController extends AbstractRestHandler {

    @Autowired
    private FoodServiceImpl FoodService;
    /*
        @RequestMapping(value = "",
                method = RequestMethod.POST,
                consumes = {"application/json", "application/xml"},
                produces = {"application/json", "application/xml"})
        @ResponseStatus(HttpStatus.CREATED)
        @ApiOperation(value = "Create a Food resource.", notes = "Returns the URL of the new resource in the Location header.")
        public void createFood(@RequestBody Food Food,
                                     HttpServletRequest request, HttpServletResponse response) {
            Food createdMBFood = this.FoodService.save(Food);
            response.setHeader("Location", request.getRequestURL().append("/").append(createdMBFood.getId()).toString());
        }
    */
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a paginated list of all Foods.",
            notes = "The list is paginated. You can provide a page number (default 0) and a page size (default 100)")
    public
    @ResponseBody
    Iterable<Food> getAllFood(@ApiParam(value = "The page number (zero-based)", required = true)
                              @RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                              @ApiParam(value = "The page size", required = true)
                              @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                              HttpServletRequest request, HttpServletResponse response) {
        Iterable<Food> foods = this.FoodService.findAll();
        return foods;
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single Food.", notes = "You have to provide a valid Food ID.")
    public
    @ResponseBody
    Food getFood(@ApiParam(value = "The ID of the Food.", required = true)
                 @PathVariable("id") Long id,
                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        Food r = this.FoodService.findByNdbno(id.toString());
        checkResourceFound(r);
        byte[] test = toJson(r);
        //todo: http://goo.gl/6iNAkz
        return r;
    }
    private byte[] toJson(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        try {
            String s = map.writeValueAsString(r);
            return s.getBytes();
        }
        catch (Exception ex){
            String exd = ex.getMessage();
        }
        return null;

    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.PUT,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update a Food resource.", notes = "You have to provide a valid Food ID in the URL and in the payload. The ID attribute can not be updated.")
    public void updateFood(@ApiParam(value = "The ID of the existing Food resource.", required = true)
                           @PathVariable("id") Long id, @RequestBody Food Food,
                           HttpServletRequest request, HttpServletResponse response) {
        checkResourceFound(this.FoodService.findByNdbno(id.toString()));
        //if (id != Food.getId().toString()) throw new DataFormatException("ID doesn't match!");
        this.FoodService.save(Food);
    }

    //todo: @ApiImplicitParams, @ApiResponses
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a Food resource.", notes = "You have to provide a valid Food ID in the URL. Once deleted the resource can not be recovered.")
    public void deleteFood(@ApiParam(value = "The ID of the existing Food resource.", required = true)
                           @PathVariable("id") Long id, HttpServletRequest request,
                           HttpServletResponse response) {
        Food food = this.FoodService.findByNdbno(id.toString());
        checkResourceFound(food);
        this.FoodService.delete(food);
    }
}
