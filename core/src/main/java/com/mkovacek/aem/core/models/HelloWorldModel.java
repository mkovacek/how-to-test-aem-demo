package com.mkovacek.aem.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;

import java.util.Optional;

import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;

@Model(adaptables = Resource.class)
public class HelloWorldModel {

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "No resourceType")
    protected String resourceType;

    @OSGiService
    private SlingSettingsService settings;
    @SlingObject
    private Resource currentResource;
    @SlingObject
    private ResourceResolver resourceResolver;

    private String message;

    @PostConstruct
    protected void init() {
        final PageManager pageManager = this.resourceResolver.adaptTo(PageManager.class);
        final String currentPagePath = Optional.ofNullable(pageManager)
                                     .map(pm -> pm.getContainingPage(this.currentResource))
                                     .map(Page::getPath).orElse("");

        this.message = "Hello World!\n"
                      + "Resource type is: " + this.resourceType + "\n"
                      + "Current page is:  " + currentPagePath + "\n"
                      + "This is instance: " + this.settings.getSlingId() + "\n";
    }

    public String getMessage() {
        return this.message;
    }

}
