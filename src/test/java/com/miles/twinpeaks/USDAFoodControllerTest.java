package com.miles.twinpeaks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miles.twinpeaks.domain.es5.Crf21;
import com.miles.twinpeaks.domain.es5.IngredientMeta;
import com.miles.twinpeaks.domain.usda.IngredientCategory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import java.util.*;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class USDAFoodControllerTest {

    private static final String FOOD_LOCATION_PATTERN = "http://localhost/v1/food/ingredient/[0-9]+";

    @Autowired
    WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private int getResourceIntIdFromUrl(String locationUrl) {
        String[] parts = locationUrl.split("/");
        return Integer.valueOf(parts[parts.length - 1]);
    }

    @Test
    public void postIngredient() throws Exception {
        IngredientMeta m = new IngredientMeta();
        m.setSubstance("ACESULFAME POTASSIUM");
        m.setAsRegNo("55589-62-3");
        List<Crf21> crf21 = new ArrayList<>();
        Crf21 c = new Crf21();
        c.setCode("172.800");
        c.setHref("https://www.accessdata.fda.gov/scripts/cdrh/cfdocs/cfCFR/CFRSearch.cfm?fr=172.800");
        crf21.add(c);
        m.setCrf21(crf21);
        List<IngredientCategory> cats = new ArrayList<>();
        IngredientCategory ic = new IngredientCategory();
        ic.setCategory("NON-NUTRITIVE SWEETENER");
        cats.add(ic);
        m.setCategories(cats);
        String[] other_names = {"POTASSIUM ACESULFAME", "ACESULFAME K","POTASSIUM 6-METHYL-1,2,3-OXATHIAZIN-4(3H)-ONE 2,2-DIOXIDE","ACESULFAME POTASSIUM", "1,2,3-OXATHIAZIN-4(3H)-ONE, 6-METHYL-","2,2-DIOXIDE, POTASSIUM SALT", "ACESULFAME-K", "ACESULFAM-K"};
        m.setOtherNames(Arrays.asList(other_names));
        m.setSubstance_url("https://www.accessdata.fda.gov/scripts/fdcc/index.cfm?set=FoodSubstances&id=ACESULFAMEPOTASSIUM");

        /*
        {
          'substance': 'N-ACETYL-L-METHIONINE',
          'substance_url': 'https://www.accessdata.fda.gov/scripts/fdcc/index.cfm?set=FoodSubstances&id=ACETYLMETHIONINE',
          'categories': [
            {
              'category': 'NUTRIENT SUPPLEMENT'
            }
          ],
          'as_reg_no': '65-82-7',
          'crf_21': [
            {
              'code': '172.372',
              'href': 'https://www.accessdata.fda.gov/scripts/cdrh/cfdocs/cfCFR/CFRSearch.cfm?fr=172.372'
            }
          ],
          'other_names': [
            'N-ACETYL-L-METHIONINE',
            'ACETYLMETHIONINE',
            'L-METHIONINE, N-ACETYL-',
            'METHIONINE, N-ACETYL-, L-',
            'N-ACETYLMETHIONINE',
            'N-ACETYLMETHIONINE, L-'
          ]
        }
        String JSON_DATA = "{'substance': 'ACESULFAME POTASSIUM', " +
                "'substance_url': 'https://www.accessdata.fda.gov/scripts/fdcc/index.cfm?set=FoodSubstances&id=ACESULFAMEPOTASSIUM'," +
                " 'categories': [{'category': 'NON-NUTRITIVE SWEETENER'}], 'as_reg_no': '55589-62-3'," +
                " 'crf_21': [{'code': '172.800', 'href': 'https://www.accessdata.fda.gov/scripts/cdrh/cfdocs/cfCFR/CFRSearch.cfm?fr=172.800'}]," +
                " 'other_names': ['POTASSIUM ACESULFAME', 'ACESULFAME K'," +
                " 'POTASSIUM 6-METHYL-1,2,3-OXATHIAZIN-4(3H)-ONE 2,2-DIOXIDE'," +
                " 'ACESULFAME POTASSIUM', '1,2,3-OXATHIAZIN-4(3H)-ONE, 6-METHYL-, " +
                "2,2-DIOXIDE, POTASSIUM SALT', 'ACESULFAME-K', 'ACESULFAM-K']}";

        final JSONObject obj = new JSONObject(JSON_DATA);*/

        byte[] json = toJson(m);

        //CREATE
        MvcResult result = mvc.perform(post("/v1/food/ingredient")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern(FOOD_LOCATION_PATTERN))
                .andReturn();
        int id = getResourceIntIdFromUrl(result.getResponse().getRedirectedUrl());

        mvc.perform(get("/v1/food/ingredient/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)));
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
