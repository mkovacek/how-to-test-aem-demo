package com.mkovacek.aem.core.services.products;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.mkovacek.aem.core.models.products.VariantsModel;

public interface ProductValidatorService {

    boolean isValid(Locale locale, String[] countries, Date validFrom);

    List<VariantsModel> getValidVariants(List<VariantsModel> variants, Locale locale);

}