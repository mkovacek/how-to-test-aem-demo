package com.mkovacek.aem.core.servlets.products;

import com.mkovacek.aem.core.models.products.ProductDetailsModel;
import com.mkovacek.aem.core.records.response.Response;
import com.mkovacek.aem.core.services.products.ProductDetailsService;
import com.mkovacek.aem.core.services.response.ResponseService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import java.io.IOException;

@Slf4j
@Component(service = Servlet.class)
@SlingServletResourceTypes(
    resourceTypes = ProductDetailsServlet.RESOURCE_TYPE,
    selectors = ProductDetailsServlet.ALLOWED_SELECTOR,
    extensions = ProductDetailsServlet.JSON,
    methods = HttpConstants.METHOD_GET)
public class ProductDetailsServlet extends SlingSafeMethodsServlet {

    public static final String ALLOWED_SELECTOR = "productdetails";
    static final String RESOURCE_TYPE = "demo/components/productdetails";
    static final String JSON = "json";

    @Reference
    private transient ResponseService responseService;

    @Reference
    private transient ProductDetailsService productDetailsService;

    @Override
    public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            this.responseService.setJsonContentType(response);
            final String selector = request.getRequestPathInfo().getSelectorString();
            final String productId = this.responseService.getSuffix(request);

            if (this.responseService.areSelectorsValid(selector, ALLOWED_SELECTOR) && StringUtils.isNotBlank(productId)) {
                final Resource resource = request.getResource();
                final Response<ProductDetailsModel> data = this.productDetailsService.getProductDetails(productId, resource);
                this.responseService.sendOk(response, data);
            } else {
                this.responseService.sendBadRequest(response);
            }
        } catch (final Exception e) {
            log.error("Exception during handling request", e);
            this.responseService.sendInternalServerError(response);
        }
    }

}