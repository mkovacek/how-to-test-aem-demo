package com.mkovacek.aem.core.services.products.impl;

import com.mkovacek.aem.core.services.products.ProductLocalizationService;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;

import java.util.Locale;

@Component(service = ProductLocalizationService.class, immediate = true)
public class ProductLocalizationServiceImpl implements ProductLocalizationService {

    @Override
    public String getLocalizedProductDetail(final ValueMap valueMap, final String propertyName, final Locale locale) {
        return valueMap.get(StringUtils.join(propertyName, locale.getLanguage()), this.getFallbackValue(valueMap, propertyName));
    }

    private String getFallbackValue(final ValueMap valueMap, final String propertyName) {
        return valueMap.get(StringUtils.join(propertyName, Locale.ENGLISH.getLanguage()), StringUtils.EMPTY);
    }

}