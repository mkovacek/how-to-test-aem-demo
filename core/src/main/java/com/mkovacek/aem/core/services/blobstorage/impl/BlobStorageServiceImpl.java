package com.mkovacek.aem.core.services.blobstorage.impl;

import com.mkovacek.aem.core.services.blobstorage.BlobStorageService;
import com.mkovacek.aem.core.services.blobstorage.config.BlobStorageConfig;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component(service = BlobStorageService.class, immediate = true)
@Designate(ocd = BlobStorageConfig.class)
public class BlobStorageServiceImpl implements BlobStorageService {

    @Activate
    private BlobStorageConfig blobStorageConfig;

    @Override
    public String getProductImageUrl(final String filename) {
        final String imageUrl = StringUtils.join(this.blobStorageConfig.productImagesFolderPath(), filename);
        try {
            return URLEncoder.encode(imageUrl, StandardCharsets.UTF_8.toString());
        } catch (final UnsupportedEncodingException e) {
            log.error("Error during encoding url", e);
        }
        return imageUrl;
    }

}