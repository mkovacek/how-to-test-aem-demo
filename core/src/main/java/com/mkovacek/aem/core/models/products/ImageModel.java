package com.mkovacek.aem.core.models.products;


import com.mkovacek.aem.core.services.blobstorage.BlobStorageService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Slf4j
@Model(adaptables = {Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageModel {

    @ValueMapValue
    @Default(values = StringUtils.EMPTY)
    private String filename;

    @OSGiService
    private BlobStorageService blobStorageService;

    @Getter
    private String url = StringUtils.EMPTY;

    @PostConstruct
    protected void init() {
        try {
            this.url = this.blobStorageService.getProductImageUrl(this.filename);
        } catch (final Exception e) {
            log.error("Exception in post construct", e);
        }
    }

}