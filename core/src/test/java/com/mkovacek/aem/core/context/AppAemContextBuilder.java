package com.mkovacek.aem.core.context;

import io.wcm.testing.mock.aem.junit5.AemContext;

import org.apache.sling.testing.mock.sling.ResourceResolverType;

import java.util.HashMap;
import java.util.Map;

public class AppAemContextBuilder {

    private final AemContext aemContext;

    public AppAemContextBuilder() {
        this.aemContext = new AemContext(ResourceResolverType.RESOURCERESOLVER_MOCK);
    }

    public AppAemContextBuilder(final ResourceResolverType resourceResolverType) {
        this.aemContext = new AemContext(resourceResolverType);
    }

    public AemContext build() {
        return this.aemContext;
    }

    public AppAemContextBuilder loadResource(final String classPathResource, final String destinationPath) {
        this.aemContext.load().json(classPathResource, destinationPath);
        return this;
    }

    public <T> AppAemContextBuilder registerService(final Class<T> serviceClass, final T service) {
        this.aemContext.registerService(serviceClass, service, new HashMap<>());
        return this;
    }

    public <T> AppAemContextBuilder registerInjectActivateService(final T osgiService) {
        this.aemContext.registerInjectActivateService(osgiService, new HashMap<>());
        return this;
    }

    public <T> AppAemContextBuilder registerInjectActivateService(final T osgiService, final Map<String, Object> config) {
        this.aemContext.registerInjectActivateService(osgiService, config);
        return this;
    }

}