package com.mkovacek.aem.core.models.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mkovacek.aem.core.services.products.ProductValidatorService;

import lombok.Getter;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.Date;
import java.util.Locale;

@Model(adaptables = {Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class VariantsModel {

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    @Getter
    private String id;

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    @Getter
    private String name;

    @ValueMapValue
    @Default(intValues = 0)
    @JsonIgnore
    @Getter
    private int sortOrder;

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    private String[] availableIn;

    @ValueMapValue
    @Default(longValues = 0)
    private Date validFrom;

    @OSGiService
    private ProductValidatorService productValidatorService;

    @JsonIgnore
    public boolean isValid(final Locale locale) {
        return this.productValidatorService.isValid(locale, this.availableIn, this.validFrom);
    }

}