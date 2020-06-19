package com.mkovacek.aem.core.services.products;

import org.apache.sling.api.resource.ValueMap;

import java.util.Locale;

public interface ProductLocalizationService {

    String getLocalizedProductDetail(final ValueMap valueMap, final String propertyName, final Locale locale);

}
