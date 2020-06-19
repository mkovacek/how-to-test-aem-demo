package com.mkovacek.aem.core.context.mocks;

import com.day.cq.commons.Externalizer;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Map;

public class MockExternalizer implements Externalizer {

    private static String LOCAL = "https://demo-aem-author-local.com";
    private static String AUTHOR = "https://demo-aem-author-local.com";
    private static String PUBLISH = "https://demo-aem-publish-local.com";

    public MockExternalizer() {
    }

    public MockExternalizer(final Map<String, String> config) {
        LOCAL = config.getOrDefault(Externalizer.LOCAL, LOCAL);
        AUTHOR = config.getOrDefault(Externalizer.AUTHOR, AUTHOR);
        PUBLISH = config.getOrDefault(Externalizer.PUBLISH, PUBLISH);
    }

    @Override
    public String externalLink(final ResourceResolver resourceResolver, final String s, final String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String externalLink(final ResourceResolver resourceResolver, final String s, final String s1, final String s2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String publishLink(final ResourceResolver resourceResolver, final String path) {
        return PUBLISH + path;
    }

    @Override
    public String publishLink(final ResourceResolver resourceResolver, final String scheme, final String path) {
        return PUBLISH.replaceAll("^https", scheme) + path;
    }

    @Override
    public String authorLink(final ResourceResolver resourceResolver, final String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String authorLink(final ResourceResolver resourceResolver, final String s, final String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String relativeLink(final SlingHttpServletRequest slingHttpServletRequest, final String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String absoluteLink(final SlingHttpServletRequest slingHttpServletRequest, final String s, final String s1) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public String absoluteLink(final ResourceResolver resourceResolver, final String s, final String s1) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public String absoluteLink(final String s, final String s1) {
        throw new UnsupportedOperationException();
    }

}