package com.miles.twinpeaks.api.rest.receipt;

import com.miles.twinpeaks.api.rest.AbstractRestHandler;
import com.miles.twinpeaks.domain.receipt.Receipt;
import com.miles.twinpeaks.domain.receipt.ReceiptItemCategory;
import com.miles.twinpeaks.exception.DataFormatException;
import com.miles.twinpeaks.exception.ResourceNotFoundException;
import com.miles.twinpeaks.service.receipt.ReceiptItemCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/v1/fc")
@Api(tags = {"ReceiptItemCategories"})
public class ReceiptItemCategoryController extends AbstractRestHandler {
    @Autowired
    private ReceiptItemCategoryService ricService;

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single Receipt item.", notes = "You have to provide a valid Receipt Item Category ID.")
    public
    @ResponseBody
    ReceiptItemCategory get(@ApiParam(value = "The ID of the Receipt.", required = true)
                    @PathVariable("id") Long id,
                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            ReceiptItemCategory c = this.ricService.getCategory(id);
            checkResourceFound(c);
            return c;
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("resource not found");
        }
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a paginated list of all categories.",
            notes = "The list is paginated. You can provide a page number (default 0) and a page size (default 100)")
    public
    @ResponseBody
    Page<Receipt> getAll(@ApiParam(value = "The page number (zero-based)", required = true)
                                @RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                                @ApiParam(value = "The page size", required = true)
                                @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                HttpServletRequest request, HttpServletResponse response) {
        Page<Receipt> receipts = this.ricService.getAll(page, size);
        return receipts;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a Receipt resource.", notes = "Returns the URL of the new resource in the Location header.")
    public void create(@RequestBody ReceiptItemCategory category,
                              HttpServletRequest request, HttpServletResponse response) {
        ReceiptItemCategory createdCategory = this.ricService.createCategory(category);
        response.setHeader("Location", request.getRequestURL().append("/").append(createdCategory.getId()).toString());
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.PUT,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update a Receipt resource.", notes = "You have to provide a valid Receipt Item Category ID " +
            "in the URL and in the payload. The ID attribute can not be updated.")
    public void update(@ApiParam(value = "The ID of the existing Receipt item category resource.", required = true)
                              @PathVariable("id") Long id, @RequestBody ReceiptItemCategory Category,
                              HttpServletRequest request, HttpServletResponse response) {
        checkResourceFound(this.ricService.getCategory(id));
        if (id != Category.getId()) throw new DataFormatException("ID doesn't match!");
        this.ricService.updateCategory(Category);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a Receipt item category resource.", notes = "You have to provide a valid Receipt ID in the URL. " +
            "Once deleted the resource can not be recovered.")
    public void delete(@ApiParam(value = "The ID of the existing Receipt Item Category resource.", required = true)
                              @PathVariable("id") Long id, HttpServletRequest request,
                              HttpServletResponse response) {
        checkResourceFound(this.ricService.getCategory(id));
        this.ricService.deleteCategory(id);
    }
}
