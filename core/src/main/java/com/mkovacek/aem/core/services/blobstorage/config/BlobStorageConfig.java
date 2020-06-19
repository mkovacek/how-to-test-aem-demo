package com.mkovacek.aem.core.services.blobstorage.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Blob Storage", description = "Blob Storage Folder Configuration")
public @interface BlobStorageConfig {

    @AttributeDefinition(name = "Product Images", description = "Full path to folder of product images")
    String productImagesFolderPath() default "https://dummyurl.com/images/products/";

}