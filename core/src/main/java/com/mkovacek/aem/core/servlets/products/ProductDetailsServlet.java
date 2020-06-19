package com.mkovacek.aem.core.servlets.products;

import com.adobe.cq.export.json.ExporterConstants;
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
    resourceTypes = "demo/components/content/productdetails",
    selectors = ProductDetailsServlet.ALLOWED_SELECTOR,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION,
    methods = HttpConstants.METHOD_GET)
public class ProductDetailsServlet extends SlingSafeMethodsServlet {

    static final String ALLOWED_SELECTOR = "productdetails";

    @Reference
    private transient ResponseService responseService;

    @Reference
    private transient ProductDetailsService productDetailsService;

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            this.responseService.setJsonContentType(response);
            if (this.isValidRequest(request)) {
                final String productId = this.responseService.getSuffix(request);
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

    private boolean isValidRequest(final SlingHttpServletRequest request) {
        return this.responseService.areSelectorsValid(request.getRequestPathInfo().getSelectorString(), ALLOWED_SELECTOR)
                   && StringUtils.isNotBlank(this.responseService.getSuffix(request));
    }

}