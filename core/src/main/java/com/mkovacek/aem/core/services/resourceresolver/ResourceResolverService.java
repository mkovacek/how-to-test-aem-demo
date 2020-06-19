package com.mkovacek.aem.core.services.resourceresolver;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;

public interface ResourceResolverService {

    ResourceResolver getResourceResolver(String user) throws LoginException;

}