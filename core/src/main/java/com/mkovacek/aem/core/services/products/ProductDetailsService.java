package com.mkovacek.aem.core.services.products;

import org.apache.sling.api.resource.Resource;
import com.mkovacek.aem.core.models.products.ProductDetailsModel;
import com.mkovacek.aem.core.records.response.Response;

public interface ProductDetailsService {

    Response<ProductDetailsModel> getProductDetails(String id, Resource resource);

}