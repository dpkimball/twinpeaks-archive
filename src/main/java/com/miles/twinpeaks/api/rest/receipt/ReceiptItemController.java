package com.miles.twinpeaks.api.rest.receipt;

import com.miles.twinpeaks.api.rest.AbstractRestHandler;
import com.miles.twinpeaks.domain.OxbowItemResponse;
import com.miles.twinpeaks.domain.receipt.Receipt;
import com.miles.twinpeaks.domain.receipt.ReceiptItem;
import com.miles.twinpeaks.domain.usda.api.Food;
import com.miles.twinpeaks.domain.usda.api.Response;
import com.miles.twinpeaks.domain.usda.api.USDAFood;
import com.miles.twinpeaks.exception.DataFormatException;
import com.miles.twinpeaks.domain.report.FoodPackage;
import com.miles.twinpeaks.service.food.FoodCollectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/*
 * Demonstrates how to set up RESTful API endpoints using Spring MVC
 */

@RestController
@RequestMapping(value = "/v1/ReceiptItems")
@Api(tags = {"ReceiptItems"})
public class ReceiptItemController extends AbstractRestHandler {
    @Autowired
    private com.miles.twinpeaks.service.receipt.ReceiptService ReceiptService;
    @Autowired
    private com.miles.twinpeaks.service.receipt.ReceiptItemService ReceiptItemService;
    @Autowired
    private FoodCollectionService foodCollectionService;

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a Receipt resource.", notes = "Returns the URL of the new resource in the Location header.")
    public void createReceiptItem(@RequestBody ReceiptItem ReceiptItem,
                                  HttpServletRequest request, HttpServletResponse response) {
        Receipt Receipt = ReceiptItem.getReceipt();
        ReceiptItem item = this.ReceiptItemService.createReceiptItem(ReceiptItem);
        response.setHeader("Location", request.getRequestURL().append("/").append(item.getId()).toString());
    }


    @RequestMapping(value = "/{id}/{item_id}",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single Receipt item.", notes = "You have to provide a valid Receipt item ID.")
    public
    @ResponseBody
    OxbowItemResponse getItem(@ApiParam(value = "The ID of the Receipt.", required = true)
                              @PathVariable("id") Long id,
                              @PathVariable("item_id") Long item_id,
                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        Receipt Receipt = this.ReceiptService.getReceipt(id);
        checkResourceFound(Receipt);

        // verify that we can find the line item itself
        Optional<ReceiptItem> ri = this.ReceiptItemService.getReceiptItem(item_id);
        checkResourceFound(ri);

        USDAFood dbFood;
        ReceiptItem item;
        if (ri.isPresent()) {
            item = ri.get();
            dbFood = item.getUsda_food();
            if (dbFood != null) {
                //look for dictionary
                /*
                Food esFood;
                try {
                    esFood = this.esFoodService.findByNdbno(Long.toString(dbFood.getNdbno()));
                }
                catch(Exception ex){
                    log.error(ex.getMessage());
                    //todo handle exceptions better
                    this.esFoodService.delete_ingredient(Long.toString(dbFood.getNdbno()));
                    esFood = null;
                }*/
                //scan for product
                FoodPackage fp = this.foodCollectionService.SeekFoodPackage(dbFood.getBarcodevalue());
                // link the lookup to the item; this will now link to the elastic search record
                item.setUsda_food(dbFood);
                this.ReceiptItemService.updateReceiptItem(item);

                return new OxbowItemResponse(item, fp.getEsFood(), fp.getDbFood());
            }
            return new OxbowItemResponse(item, null, null);

        } else {
            return new OxbowItemResponse();
        }
    }

    private Food getFoodMetaData(long id) {
        Food esFood;RestTemplate restTemplate = new RestTemplate();
        String url = String.format("https://api.nal.usda.gov/ndb/reports/?ndbno=%s&type=b&format=json&api_key=%s", id, System.getenv("USDA_API_KEY"));
        try {
            Response usda_response = restTemplate.getForObject(url, Response.class, 200);
            esFood = usda_response.getReport().getFood();
            esFood.setNdbno(Long.toString(id));
        }
        catch(Exception ex){
            log.error(ex.getMessage());
            return null;
        }
        return esFood;
    }

    @RequestMapping(value = "/{id}/{item_id}",
            method = RequestMethod.PUT,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update a Receipt resource.", notes = "You have to provide a valid Receipt Item ID in the URL and in the payload. The ID attribute can not be updated.")
    public void updateItem(@ApiParam(value = "The ID of the existing Receipt Item resource.", required = true)
                           @PathVariable("id") Long id,
                           @PathVariable("item_id") Long item_id,
                           @RequestBody ReceiptItem ReceiptItem,
                           HttpServletRequest request, HttpServletResponse response) {

        // verify that we can find the parent (Receipt)
        Receipt r = this.ReceiptService.getReceipt(id);
        checkResourceFound(r);

        // verify that we can find the line item itself
        Optional<ReceiptItem> ri = this.ReceiptItemService.getReceiptItem(item_id);

        // make sure the path lines up to json that was sent
        if (item_id != ReceiptItem.getId()) throw new DataFormatException("Item ID doesn't match!");
        if (id != ReceiptItem.getReceipt().getId()) throw new DataFormatException("ID doesn't match!");

        this.ReceiptItemService.updateReceiptItem(ri.get());
    }
}
