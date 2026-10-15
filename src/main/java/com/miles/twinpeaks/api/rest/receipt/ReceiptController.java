/*
 * Derived from spring-boot-rest-example by Siamak Khoubyari.
 * Copyright (c) 2014-2018 Siamak Khoubyari <khoubyari@gmail.com>
 * Licensed under the MIT License; see LICENSE-UPSTREAM-MIT.txt.
 * TwinPeaks adapts package names, domain concepts, and application behavior.
 */
package com.miles.twinpeaks.api.rest.receipt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miles.twinpeaks.api.rest.AbstractRestHandler;
import com.miles.twinpeaks.domain.receipt.Receipt;
import com.miles.twinpeaks.domain.security.ApplicationUser;
import com.miles.twinpeaks.service.receipt.ReceiptService;
import com.miles.twinpeaks.service.security.UserDetailsServiceImpl;

import com.miles.twinpeaks.security.SecurityConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.miles.twinpeaks.exception.DataFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;


/*
 * Demonstrates how to set up RESTful API endpoints using Spring MVC
 */

@RestController
@RequestMapping(value = "/v1/Receipts")
@Api(tags = {"Receipts"})
public class ReceiptController extends AbstractRestHandler {

    @Autowired
    private ReceiptService ReceiptService;

    @Autowired
    private UserDetailsServiceImpl UserService;

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a Receipt resource.", notes = "Returns the URL of the new resource in the Location header.")
    public void createReceipt(@RequestBody Receipt Receipt,
                              HttpServletRequest request, HttpServletResponse response, Principal principal) {
        Receipt createdMBReceipt = this.ReceiptService.createReceipt(Receipt);
        response.setHeader("Location", request.getRequestURL().append("/").append(createdMBReceipt.getId()).toString());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a paginated list of all Receipts.",
            notes = "The list is paginated. You can provide a page number (default 0) and a page size (default 100)")
    public
    @ResponseBody
    Page<Receipt> getAllReceipt(@ApiParam(value = "The page number (zero-based)", required = true)
                                      @RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                                @ApiParam(value = "The page size", required = true)
                                      @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                HttpServletRequest request, HttpServletResponse response, Principal principal) {

        ApplicationUser user = UserService.loadUserWithIdByUsername(principal.getName());
        //Todo: flush out this logic
        if (SecurityConstants.WEBSITE_OXBOW.equals(user.getUsername())){
            return this.ReceiptService.getAllReceipts(page, size);
        }
        else {
            //these calls are coming from monadnock
            return this.ReceiptService.findAllByUserId(user.getId(), page, size);
        }
    }

    /*public static Long getCurrentUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String id = null;
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).;
        if (authentication != null)
            if (authentication.getPrincipal() instanceof UserDetails)
                id = ((UserDetails) authentication.getPrincipal()).getUsername();
            else if (authentication.getPrincipal() instanceof String)
                id = (String) authentication.getPrincipal();
        try {
            return Long.valueOf(id != null ? id : "1"); //anonymoususer
        } catch (NumberFormatException e) {
            return 1L;
        }
    }*/


    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single Receipt.", notes = "You have to provide a valid Receipt ID.")
    public
    @ResponseBody
    Receipt getReceipt(@ApiParam(value = "The ID of the Receipt.", required = true)
                             @PathVariable("id") Long id,
                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        Receipt r = this.ReceiptService.getReceipt(id);
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
    @ApiOperation(value = "Update a Receipt resource.", notes = "You have to provide a valid Receipt ID in the URL and in the payload. The ID attribute can not be updated.")
    public void updateReceipt(@ApiParam(value = "The ID of the existing Receipt resource.", required = true)
                                 @PathVariable("id") Long id, @RequestBody Receipt Receipt,
                                 HttpServletRequest request, HttpServletResponse response) {
        checkResourceFound(this.ReceiptService.getReceipt(id));
        if (id != Receipt.getId()) throw new DataFormatException("ID doesn't match!");
        this.ReceiptService.updateReceipt(Receipt);
    }

    //todo: @ApiImplicitParams, @ApiResponses
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a Receipt resource.", notes = "You have to provide a valid Receipt ID in the URL. Once deleted the resource can not be recovered.")
    public void deleteReceipt(@ApiParam(value = "The ID of the existing Receipt resource.", required = true)
                                 @PathVariable("id") Long id, HttpServletRequest request,
                                 HttpServletResponse response) {
        checkResourceFound(this.ReceiptService.getReceipt(id));
        this.ReceiptService.deleteReceipt(id);
    }
}
