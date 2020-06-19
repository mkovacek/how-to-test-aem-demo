package com.mkovacek.aem.core.services.response;

import com.mkovacek.aem.core.records.response.Response;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import java.io.IOException;

public interface ResponseService {

    void setJsonContentType(SlingHttpServletResponse response);

    void sendOk(SlingHttpServletResponse response, Response<?> data) throws IOException;

    void sendBadRequest(SlingHttpServletResponse response) throws IOException;

    void sendInternalServerError(SlingHttpServletResponse response) throws IOException;

    boolean areSelectorsValid(String selector, String... allowedSelectors);

    String getSuffix(SlingHttpServletRequest request);

}