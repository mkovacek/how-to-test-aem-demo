package com.mkovacek.aem.core.models.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mkovacek.aem.core.services.products.ProductLocalizationService;
import com.mkovacek.aem.core.services.products.ProductValidatorService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Slf4j
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@Model(adaptables = {Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductDetailsModel {

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    @Getter
    private String id;

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    @Getter
    private String categoryId;

    @ChildResource
    @Getter
    private List<ImageModel> images;

    @ChildResource
    private List<VariantsModel> variants;

    @Self
    private ValueMap valueMap;

    @OSGiService
    private ProductLocalizationService productLocalizationService;

    @OSGiService
    private ProductValidatorService productValidatorService;

    @Getter
    @JsonProperty("variants")
    private List<VariantsModel> validVariants = new ArrayList<>();

    @Getter
    private String name = StringUtils.EMPTY;

    @Getter
    private String description = StringUtils.EMPTY;

    @JsonIgnore
    public boolean isValid() {
        return !this.validVariants.isEmpty();
    }

    @JsonIgnore
    public ProductDetailsModel setLocale(final Locale locale) {
        this.setLocalizedValues(locale);
        this.validateAndSortVariants(locale);
        return this;
    }

    private void setLocalizedValues(final Locale locale) {
        this.name = this.productLocalizationService.getLocalizedProductDetail(this.valueMap, "name.", locale);
        this.description = this.productLocalizationService.getLocalizedProductDetail(this.valueMap, "description.", locale);
    }

    private void validateAndSortVariants(final Locale locale) {
        this.validVariants = this.productValidatorService.getValidVariants(this.variants, locale);
        this.validVariants.sort(Comparator.comparing(VariantsModel::getSortOrder));
    }

}