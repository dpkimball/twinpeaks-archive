package com.miles.twinpeaks;

/**
 * Uses JsonPath: http://goo.gl/nwXpb, Hamcrest and MockMVC
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miles.twinpeaks.api.rest.receipt.ReceiptItemCategoryController;
import com.miles.twinpeaks.domain.receipt.ReceiptItemCategory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class ReceiptItemCategoryControllerTest {

    private static final String CATEGORY_LOCATION_PATTERN = "http://localhost/v1/fc/[0-9]+";

    @InjectMocks
    ReceiptItemCategoryController controller;

    @Autowired
    WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void shouldCreateRetrieveDelete() throws Exception {
        ReceiptItemCategory r1 = mockCategory("Deli");

        long id = r1.getId();

        //RETRIEVE
        mvc.perform(get("/v1/fc/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.category", is(r1.getCategory())));

        //DELETE
        mvc.perform(delete("/v1/fc/" + id))
                .andExpect(status().isNoContent());

        //RETRIEVE should fail
        mvc.perform(get("/v1/fc/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateAndUpdateAndDelete() throws Exception {
        Set<ReceiptItemCategory> persistedCategories = new HashSet<>();
        for (String c : categories) {
            ReceiptItemCategory r1 = mockCategory(c);
            byte[] r1Json = toJson(r1);
            //CREATE
            MvcResult result = mvc.perform(post("/v1/fc")
                    .content(r1Json)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(redirectedUrlPattern(CATEGORY_LOCATION_PATTERN))
                    .andReturn();
            long id = getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

            r1.setCategory(c + " updated");
            r1Json = toJson(r1);

            //UPDATE
            result = mvc.perform(put("/v1/fc/" + id)
                    .content(r1Json)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andReturn();

            //RETRIEVE updated
            mvc.perform(get("/v1/fc/" + id)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is((int) id)))
                    .andExpect(jsonPath("$.category", is(r1.getCategory())));

            //DELETE
            mvc.perform(delete("/v1/fc/" + id))
                    .andExpect(status().isNoContent());
        }
    }

    private long getResourceIdFromUrl(String locationUrl) {
        String[] parts = locationUrl.split("/");
        return Long.valueOf(parts[parts.length - 1]);
    }
    private int getResourceIntIdFromUrl(String locationUrl) {
        String[] parts = locationUrl.split("/");
        return Integer.valueOf(parts[parts.length - 1]);
    }

    private String[] categories = {"Meat", "Deli", "Produce", "Dairy", "Bakery", "Grocery"};

    private ReceiptItemCategory mockCategory(String category) throws Exception {

            ReceiptItemCategory ric = new ReceiptItemCategory();
            ric.setCategory(category);
            byte[] json = toJson(ric);
            //CREATE
            MvcResult result = mvc.perform(post("/v1/fc")
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(redirectedUrlPattern(CATEGORY_LOCATION_PATTERN))
                    .andReturn();
            int id = getResourceIntIdFromUrl(result.getResponse().getRedirectedUrl());
            ric.setId(id);
            return ric;
    }

    private byte[] toJson(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(r).getBytes();
    }

    // match redirect header URL (aka Location header)
    private static ResultMatcher redirectedUrlPattern(final String expectedUrlPattern) {
        return new ResultMatcher() {
            public void match(MvcResult result) {
                Pattern pattern = Pattern.compile("\\A" + expectedUrlPattern + "\\z");
                assertTrue(pattern.matcher(result.getResponse().getRedirectedUrl()).find());
            }
        };
    }
}
