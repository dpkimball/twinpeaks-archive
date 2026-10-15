package com.miles.twinpeaks;

/**
 * Uses JsonPath: http://goo.gl/nwXpb, Hamcrest and MockMVC
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miles.twinpeaks.api.rest.receipt.ReceiptController;

import com.miles.twinpeaks.domain.receipt.*;
import com.miles.twinpeaks.domain.security.ApplicationUser;
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class ReceiptControllerTest {

    private static final String RECEIPT_LOCATION_PATTERN = "http://localhost/v1/Receipts/[0-9]+";
    private static final String RECEIPT_ITEM_LOCATION_PATTERN = "http://localhost/v1/ReceiptItems/[0-9]+";
    private static final String CATEGORY_LOCATION_PATTERN = "http://localhost/v1/fc/[0-9]+";

    @InjectMocks
    ReceiptController controller;

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
        Receipt r1 = mockReceipt(true, "Create");
        byte[] r1Json = toJson(r1);

        //CREATE
        MvcResult result = mvc.perform(post("/v1/Receipts")
                .content(r1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern(RECEIPT_LOCATION_PATTERN))
                .andReturn();
        long id = getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

        //RETRIEVE
        mvc.perform(get("/v1/Receipts/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)))
                //.andExpect(jsonPath("$.user_id", is((int) r1.getUser_id())))
                .andExpect(jsonPath("$.total_items", is(r1.getTotal_items())))
                .andExpect(jsonPath("$.tax_total", is(r1.getTax_total().doubleValue())))
                .andExpect(jsonPath("$.subpurchase_total", is(r1.getSubpurchase_total().doubleValue())))
                //.andExpect(jsonPath("$.create_date", isA(String)))
                .andExpect(jsonPath("$.purchase_total", is(r1.getPurchase_total().doubleValue())));

        //DELETE
        mvc.perform(delete("/v1/Receipts/" + id))
                .andExpect(status().isNoContent());

        //RETRIEVE should fail
        mvc.perform(get("/v1/Receipts/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //@Test//(expected = JsonMappingException.class)
    public void givenBidirectionRelation_whenSerializing_thenException()
            throws JsonProcessingException {

        long user_id = 100;

        Receipt r = new Receipt();
        r.setPurchase_date(new Date());
        r.setSubpurchase_total(BigDecimal.valueOf(50).movePointLeft(2));
        r.setTax_total(BigDecimal.valueOf(50).movePointLeft(2));
        r.setTrace_no(100);
        ApplicationUser user = new ApplicationUser();
        user.setUsername("test");
        user.setPassword("test password");
        r.setUser(user);
        r.setPurchase_total(BigDecimal.valueOf(200).movePointLeft(2));

        Store s = new Store();
        s.setAddress("301 Constitution Ave");
        s.setCity("Littleton");
        s.setState("MA");
        s.setZip("01460");
        s.setPhone("1-978-486-0828");
        s.setNumber("77");
        r.setStore(s);

        CreditAccount ca = new CreditAccount();
        ca.setAccount("SYNTHETIC-ACCOUNT");
        r.setCredit_account(ca);

        ReceiptItemCategory grocery = new ReceiptItemCategory("Grocery");
        ReceiptItem item;

        item = new ReceiptItem();
        item.setCategory(grocery);
        item.setLabel("OSPRAY CRAN POMEG");
        item.setDescription("");
        item.setPrice(BigDecimal.valueOf(399).movePointLeft(2));
        item.setTaxable(false);
        r.addReceiptItem(item);
    }

    @Test
    public void shouldCreateAndUpdateAndDelete() throws Exception {

        Receipt r1 = mockReceipt(true, "Update");
        byte[] r1Json = toJson(r1);

        //CREATE
        MvcResult result = mvc.perform(post("/v1/Receipts")
                .content(r1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern(RECEIPT_LOCATION_PATTERN))
                .andReturn();
        long id = getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

        // update the dto to have the id now
        r1.setId(id);

        Store s = new Store();
        s.setAddress("302 Constitution Ave");
        s.setCity("Littleton");
        s.setState("MA");
        s.setZip("01460");
        s.setPhone("1-978-486-0828");
        s.setNumber("77");
        r1.setStore(s);

        byte[] r2Json = toJson(r1);

        //UPDATE
        result = mvc.perform(put("/v1/Receipts/" + id)
                .content(r2Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        //RETRIEVE updated
        mvc.perform(get("/v1/Receipts/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)))
                //.andExpect(jsonPath("$.user_id", is((int) r1.getUser_id())))
                .andExpect(jsonPath("$.total_items", is(r1.getTotal_items())));

        //DELETE
        mvc.perform(delete("/v1/Receipts/" + id))
                .andExpect(status().isNoContent());
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

    @Test
    public void mockOneCategoryReceiptItemFlow() throws Exception {

        Receipt r1 = mockReceipt(false, "One Receipt");
        ReceiptItem ri = new ReceiptItem();
        ri.setCategory(mockCategory("Mock Dairy"));
        ri.setLabel("Mock OSPRAY CRAN POMEG");
        ri.setDescription("");
        ri.setPrice(BigDecimal.valueOf(399).movePointLeft(2));
        ri.setTaxable(false);
        r1.addReceiptItem(ri);

        byte[] r1Json = toJson(r1);
        //CREATE
        MvcResult result = mvc.perform(post("/v1/Receipts")
                .content(r1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern(RECEIPT_LOCATION_PATTERN))
                .andReturn();
        getResourceIdFromUrl(result.getResponse().getRedirectedUrl());
    }

    //@Test
    //todo: wire up
    public void mockReceiptItem() throws Exception {

        Receipt r1 = mockReceipt(false, "Receipt Item");
        byte[] r1Json = toJson(r1);
        //CREATE
        MvcResult result = mvc.perform(post("/v1/Receipts")
                .content(r1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern(RECEIPT_LOCATION_PATTERN))
                .andReturn();
        long id = getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

        // update the dto to have the id now
        r1.setId(id);

        ReceiptItem ri = new ReceiptItem();
        ri.setReceipt(r1);
        ri.setCategory(new ReceiptItemCategory("TEST 1"));
        ri.setLabel("OSPRAY CRAN POMEG");
        ri.setDescription("");
        ri.setPrice(BigDecimal.valueOf(399).movePointLeft(2));
        ri.setTaxable(false);

        byte[] json = toJson(ri);

        //CREATE
        result = mvc.perform(post("/v1/ReceiptItems")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern(RECEIPT_ITEM_LOCATION_PATTERN))
                .andReturn();

        id = getResourceIntIdFromUrl(result.getResponse().getRedirectedUrl());
        ri.setId(id);
        ri.setCategory(new ReceiptItemCategory("TEST 2"));

        byte[] r2Json = toJson(ri);

        //UPDATE
        result = mvc.perform(put("/v1/ReceiptItems/" + id)
                .content(r2Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        //RETRIEVE updated
        mvc.perform(get("/v1/ReceiptItems/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)))
                //.andExpect(jsonPath("$.storeID", is(r2.getStore().getId())))
                .andExpect(jsonPath("$.label", is(ri.getLabel())))
                .andExpect(jsonPath("$.category", is(ri.getCategory())));

        //DELETE
        mvc.perform(delete("/v1/ReceiptItems/" + id))
                .andExpect(status().isNoContent());
    }

    private Receipt mockReceipt(boolean add_items, String mock) throws Exception{

        long user_id = 100;

        Receipt r = new Receipt();
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        r.setPurchase_date(sdf.parse("10/03/2016 13:10:38"));
        r.setSubpurchase_total(BigDecimal.valueOf(16312).movePointLeft(2));
        r.setTax_total(BigDecimal.valueOf(1050).movePointLeft(2));
        r.setTrace_no(100);
        //r.setUser_id(user_id);
        r.setPurchase_total(BigDecimal.valueOf(17312).movePointLeft(2));

        Store s = new Store();
        s.setAddress("301 Constitution Ave");
        s.setCity("Littleton");
        s.setState("MA");
        s.setZip("01460");
        s.setPhone("1-978-486-0828");
        s.setNumber("77");
        r.setStore(s);

        CreditAccount ca = new CreditAccount();
        ca.setAccount("SYNTHETIC-ACCOUNT");
        r.setCredit_account(ca);

        ReceiptItemCategory grocery;
        ReceiptItem ri;

        if (add_items) {
            grocery = mockCategory(mock + " Grocery");
            ri = new ReceiptItem();
            ri.setCategory(grocery);
            ri.setLabel("OSPRAY CRAN POMEG");
            ri.setDescription("");
            ri.setPrice(BigDecimal.valueOf(399).movePointLeft(2));
            ri.setTaxable(false);
            r.addReceiptItem(ri);

            int items_count = 6;
            int count = 1;
            ri = new ReceiptItem();
            while (count < items_count) {
                ri.setCategory(grocery);
                ri.setLabel("BUMBEE SOLID WHITE");
                ri.setDescription("1 @ 2/ 3.00");
                ri.setPrice(BigDecimal.valueOf(150).movePointLeft(2));
                ri.setTaxable(false);
                ri.setBarcodetype(1);
                ri.setBarcodevalue("08660533");
                r.addReceiptItem(ri);
                count++;
            }
            items_count = 6;
            count = 1;
            while (count < items_count) {
                ri = new ReceiptItem();
                ri.setCategory(grocery);
                ri.setLabel("POLAR SLT CRL 2L PMA");
                ri.setDescription("1 @ 5/ 5.00");
                ri.setDeposit(BigDecimal.valueOf(50).movePointLeft(2));
                ri.setPrice(BigDecimal.valueOf(100).movePointLeft(2));
                ri.setTaxable(false);
                r.addReceiptItem(ri);
                count++;
            }

            ri.setCategory(mockCategory(mock + " Meat"));
            ri.setLabel("MB AB CHICKENTENDER");
            ri.setDescription("");
            ri.setPrice(BigDecimal.valueOf(475).movePointLeft(2));
            ri.setTaxable(false);
            r.addReceiptItem(ri);

            ri = new ReceiptItem();
            ri.setCategory(mockCategory(mock + " Deli"));
            ri.setLabel("HOFFMAN SHARP");
            ri.setDescription("");
            ri.setPrice(BigDecimal.valueOf(509).movePointLeft(2));
            ri.setTaxable(false);
            r.addReceiptItem(ri);

            ri = new ReceiptItem();
            ri.setCategory(mockCategory(mock + " Produce"));
            ri.setLabel("BRUSSEL SPROUTS");
            ri.setDescription("1.12 LB @ 1 LB / 1.99");
            ri.setPrice(BigDecimal.valueOf(223).movePointLeft(2));
            ri.setTaxable(false);
            r.addReceiptItem(ri);

            ri = new ReceiptItem();
            ri.setCategory(mockCategory(mock + " Bakery"));
            ri.setLabel("TUSCAN PANE");
            ri.setDescription("");
            ri.setPrice(BigDecimal.valueOf(329).movePointLeft(2));
            ri.setTaxable(false);
            r.addReceiptItem(ri);

            ri = new ReceiptItem();
            ri.setCategory(mockCategory(mock + " Dairy"));
            ri.setLabel("JOE TABOULE SALAD");
            ri.setDescription("");
            ri.setPrice(BigDecimal.valueOf(299).movePointLeft(2));
            ri.setTaxable(false);
            r.addReceiptItem(ri);
        }

        r.setTotal_items(r.getReceiptItems().size());
        return r;
    }

    private byte[] toJson(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        String s = map.writeValueAsString(r);
        return s.getBytes();
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
