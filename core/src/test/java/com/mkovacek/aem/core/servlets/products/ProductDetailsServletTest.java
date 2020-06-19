package com.mkovacek.aem.core.servlets.products;

import com.day.cq.wcm.api.Page;
import com.mkovacek.aem.core.context.AppAemContextBuilder;
import com.mkovacek.aem.core.context.constants.TestConstants;
import com.mkovacek.aem.core.context.utils.ResourceUtil;
import com.mkovacek.aem.core.services.blobstorage.impl.BlobStorageServiceImpl;
import com.mkovacek.aem.core.services.products.impl.ProductDetailsServiceImpl;
import com.mkovacek.aem.core.services.products.impl.ProductLocalizationServiceImpl;
import com.mkovacek.aem.core.services.products.impl.ProductValidatorServiceImpl;
import com.mkovacek.aem.core.services.resourceresolver.impl.ResourceResolverServiceImpl;
import com.mkovacek.aem.core.services.response.impl.ResponseServiceImpl;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.testing.mock.sling.servlet.MockRequestPathInfo;
import org.apache.sling.testing.resourceresolver.MockResourceResolverFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(AemContextExtension.class)
class ProductDetailsServletTest {

    private static final AemContext context = new AppAemContextBuilder()
                                                  .loadResource(TestConstants.HR_HR_LANDING_PAGE_JSON, TestConstants.HR_HR_LANDING_PAGE_PATH)
                                                  .loadResource(TestConstants.DE_AT_LANDING_PAGE_JSON, TestConstants.DE_AT_LANDING_PAGE_PATH)
                                                  .loadResource(TestConstants.FR_FR_LANDING_PAGE_JSON, TestConstants.FR_FR_LANDING_PAGE_PATH)
                                                  .loadResource(TestConstants.PRODUCTS_JSON, TestConstants.PRODUCTS_PATH)
                                                  .registerService(ResourceResolverFactory.class, new MockResourceResolverFactory())
                                                  .registerInjectActivateService(new ResourceResolverServiceImpl())
                                                  .registerInjectActivateService(new ResponseServiceImpl())
                                                  .registerInjectActivateService(new BlobStorageServiceImpl(), Collections.singletonMap("productImagesFolderPath", "https://dummyurl.com/images/products/"))
                                                  .registerInjectActivateService(new ProductValidatorServiceImpl())
                                                  .registerInjectActivateService(new ProductLocalizationServiceImpl())
                                                  .registerInjectActivateService(new ResponseServiceImpl())
                                                  .registerInjectActivateService(new ProductDetailsServiceImpl())
                                                  .build();

    private static final MockRequestPathInfo requestPathInfo = context.requestPathInfo();
    private final ProductDetailsServlet servlet = context.registerInjectActivateService(new ProductDetailsServlet());
    private static final String CONTENT_RESOURCE_PATH = "root/productdetails";
    private static String NOT_FOUND_RESPONSE;
    private static String BAD_REQUEST_RESPONSE;

    @BeforeAll
    static void setUpBeforeAllTests() throws IOException {
        context.addModelsForPackage(TestConstants.SLING_MODELS_PACKAGES);
        requestPathInfo.setExtension("json");
        NOT_FOUND_RESPONSE = ResourceUtil.getExpectedResult(ProductDetailsServlet.class, "responses/not-found-response.json");
        BAD_REQUEST_RESPONSE = ResourceUtil.getExpectedResult(ProductDetailsServlet.class, "responses/bad-request-response.json");
    }

    @BeforeEach
    void setupBeforeEachTest() {
        context.response().resetBuffer();
        requestPathInfo.setSelectorString(ProductDetailsServlet.ALLOWED_SELECTOR);
        requestPathInfo.setSuffix("123456789");
        final Page page = context.pageManager().getPage(TestConstants.HR_HR_LANDING_PAGE_PATH);
        context.request().setResource(page.getContentResource(CONTENT_RESOURCE_PATH));
    }

    @Test
    @DisplayName("GIVEN landing page (en-HR) WHEN servlet is called with not valid selector THEN it returns bad request response in JSON format")
    void testNotValidSelector() throws ServletException, IOException {
        requestPathInfo.setSelectorString(ProductDetailsServlet.ALLOWED_SELECTOR + ".test");
        this.servlet.doGet(context.request(), context.response());

        assertAll(
            () -> assertEquals(HttpServletResponse.SC_BAD_REQUEST, context.response().getStatus()),
            () -> assertEquals(BAD_REQUEST_RESPONSE, context.response().getOutputAsString())
        );
    }

    @Test
    @DisplayName("GIVEN landing page (en-HR) WHEN servlet is called without productId suffix THEN it returns bad request response in JSON format")
    void testNoProductId() throws ServletException, IOException {
        requestPathInfo.setSuffix(StringUtils.EMPTY);
        this.servlet.doGet(context.request(), context.response());

        assertAll(
            () -> assertEquals(HttpServletResponse.SC_BAD_REQUEST, context.response().getStatus()),
            () -> assertEquals(BAD_REQUEST_RESPONSE, context.response().getOutputAsString())
        );
    }

    @Test
    @DisplayName("GIVEN landing page(en-HR) WHEN servlet is called with not existing productId THEN it returns not found response in JSON format")
    void testNotExistingProductId() throws ServletException, IOException {
        requestPathInfo.setSuffix("123abc");
        this.servlet.doGet(context.request(), context.response());

        assertAll(
            () -> assertEquals(HttpServletResponse.SC_OK, context.response().getStatus()),
            () -> assertEquals(NOT_FOUND_RESPONSE, context.response().getOutputAsString())
        );
    }

    @Test
    @DisplayName("GIVEN landing page(en-HR) WHEN servlet is called with existing productId THEN it returns an expected localized (fallback) product details response in JSON format")
    void testProductDetailsInCroatianMarket() throws ServletException, IOException {
        this.servlet.doGet(context.request(), context.response());
        final String expectedProductDetails = ResourceUtil.getExpectedResult(this.getClass(), "responses/product-123456789-hr-HR.json");

        assertAll(
            () -> assertEquals(HttpServletResponse.SC_OK, context.response().getStatus()),
            () -> assertEquals(expectedProductDetails, context.response().getOutputAsString())
        );
    }

    @Test
    @DisplayName("GIVEN landing page (de-AT) WHEN servlet is called with existing productId THEN it returns an expected localized product details response in JSON format")
    void testProductDetailsInAustrianMarket() throws ServletException, IOException {
        this.setPageResource(TestConstants.DE_AT_LANDING_PAGE_PATH);
        this.servlet.doGet(context.request(), context.response());
        final String expectedProductDetails = ResourceUtil.getExpectedResult(this.getClass(), "responses/product-123456789-at-DE.json");

        assertAll(
            () -> assertEquals(HttpServletResponse.SC_OK, context.response().getStatus()),
            () -> assertEquals(expectedProductDetails, context.response().getOutputAsString())
        );
    }

    @Test
    @DisplayName("GIVEN landing page (fr-FR) WHEN servlet is called with existing productId which is not valid for French market THEN it returns not found response in JSON format")
    void testProductDetailsInFrenchMarket() throws ServletException, IOException {
        this.setPageResource(TestConstants.FR_FR_LANDING_PAGE_PATH);
        this.servlet.doGet(context.request(), context.response());

        assertAll(
            () -> assertEquals(HttpServletResponse.SC_OK, context.response().getStatus()),
            () -> assertEquals(NOT_FOUND_RESPONSE, context.response().getOutputAsString())
        );
    }

    private void setPageResource(final String path) {
        final Page page = context.pageManager().getPage(path);
        context.request().setResource(page.getContentResource(CONTENT_RESOURCE_PATH));
    }
}