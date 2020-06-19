package com.mkovacek.aem.core.services.products.impl;

import com.mkovacek.aem.core.models.products.VariantsModel;
import com.mkovacek.aem.core.services.products.ProductValidatorService;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component(service = ProductValidatorService.class, immediate = true)
public class ProductValidatorServiceImpl implements ProductValidatorService {

    @Override
    public boolean isValid(final Locale locale, final String[] countries, final Date validFrom) {
        if (ObjectUtils.allNotNull(locale, countries, validFrom) && StringUtils.isNotEmpty(locale.getCountry())) {
            final ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
            final ZonedDateTime validFromInZonedDateTime = validFrom.toInstant().atZone(ZoneId.systemDefault());
            return Arrays.asList(countries).contains(StringUtils.upperCase(locale.getCountry())) && (!validFromInZonedDateTime.isAfter(now));
        }
        return false;
    }

    @Override
    public List<VariantsModel> getValidVariants(final List<VariantsModel> variants, final Locale locale) {
        return Optional.ofNullable(variants)
                   .orElse(Collections.emptyList())
                   .stream()
                   .filter(variant -> variant.isValid(locale))
                   .collect(Collectors.toList());
    }

}