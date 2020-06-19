package com.mkovacek.aem.core.services.response.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkovacek.aem.core.records.response.Response;
import com.mkovacek.aem.core.records.response.Status;
import com.mkovacek.aem.core.services.response.ResponseService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.servlets.post.JSONResponse;
import org.osgi.service.component.annotations.Component;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component(service = ResponseService.class, immediate = true)
public class ResponseServiceImpl implements ResponseService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Response<String> badRequest = new Response<>(new Status(false, "Bad Request"), StringUtils.EMPTY);
    private static final Response<String> internalServerError = new Response<>(new Status(false, "Internal Server Error"), StringUtils.EMPTY);

    @Override
    public void setJsonContentType(final SlingHttpServletResponse response) {
        response.setContentType(JSONResponse.RESPONSE_CONTENT_TYPE);
        response.setCharacterEncoding(CharEncoding.UTF_8);
    }

    @Override
    public void sendOk(final SlingHttpServletResponse response, final Response<?> data) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(data));
    }

    @Override
    public void sendBadRequest(final SlingHttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write(objectMapper.writeValueAsString(badRequest));
    }

    @Override
    public void sendInternalServerError(final SlingHttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(objectMapper.writeValueAsString(internalServerError));
    }

    @Override
    public boolean areSelectorsValid(final String selector, final String... allowedSelectors) {
        return StringUtils.equalsAny(selector, allowedSelectors);
    }

    @Override
    public String getSuffix(final SlingHttpServletRequest request) {
        return Optional.ofNullable(request)
                   .map(req -> req.getRequestPathInfo().getSuffix())
                   .filter(StringUtils::isNotBlank)
                   .map(suffix -> StringUtils.removeStart(suffix, "/"))
                   .orElse(StringUtils.EMPTY);
    }

}