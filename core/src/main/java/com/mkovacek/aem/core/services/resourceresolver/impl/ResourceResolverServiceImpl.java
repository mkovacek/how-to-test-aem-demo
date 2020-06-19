package com.mkovacek.aem.core.services.resourceresolver.impl;

import com.mkovacek.aem.core.services.resourceresolver.ResourceResolverService;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collections;
import java.util.Map;

@Component(service = ResourceResolverService.class, immediate = true)
public class ResourceResolverServiceImpl implements ResourceResolverService {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public ResourceResolver getResourceResolver(final String user) throws LoginException {
        return this.resourceResolverFactory.getServiceResourceResolver(this.getAuthenticationInfo(user));
    }

    private Map<String, Object> getAuthenticationInfo(final String user) {
        return Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, user);
    }

}