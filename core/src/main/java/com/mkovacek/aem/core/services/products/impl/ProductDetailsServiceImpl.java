package com.mkovacek.aem.core.services.products.impl;

import com.day.cq.wcm.api.PageManager;
import com.mkovacek.aem.core.models.products.ProductDetailsModel;
import com.mkovacek.aem.core.records.response.Response;
import com.mkovacek.aem.core.records.response.Status;
import com.mkovacek.aem.core.services.products.ProductDetailsService;
import com.mkovacek.aem.core.services.resourceresolver.ResourceResolverService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Locale;
import java.util.Optional;

@Slf4j
@Component(service = ProductDetailsService.class, immediate = true)
public class ProductDetailsServiceImpl implements ProductDetailsService {

    private static final String PIM_READER = "pimReader";
    private static final Response<ProductDetailsModel> notFoundResponse = new Response<>(new Status(true, "Product Details not found"), null);
    private static final Response<ProductDetailsModel> errorResponse = new Response<>(new Status(false, "Error during fetching product details"), null);

    @Reference
    private ResourceResolverService resourceResolverService;

    @Override
    public Response<ProductDetailsModel> getProductDetails(final String id, final Resource resource) {
        try (final ResourceResolver resourceResolver = this.resourceResolverService.getResourceResolver(PIM_READER)) {
            final Locale locale = resourceResolver.adaptTo(PageManager.class).getContainingPage(resource).getLanguage(false);
            //usually this would be implemented with query
            final String productPath = StringUtils.join("/var/commerce/products/demo/", id);
            return Optional.ofNullable(resourceResolver.getResource(productPath))
                       .map(productResource -> productResource.adaptTo(ProductDetailsModel.class))
                       .map(productDetailsModel -> productDetailsModel.setLocale(locale))
                       .filter(ProductDetailsModel::isValid)
                       .map(productDetailsModel -> new Response<>(new Status(true), productDetailsModel))
                       .orElse(notFoundResponse);
        } catch (final Exception e) {
            log.error("Exception during fetching product details", e);
        }
        return errorResponse;
    }

}