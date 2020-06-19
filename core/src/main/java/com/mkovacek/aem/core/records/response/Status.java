package com.mkovacek.aem.core.records.response;

import org.apache.commons.lang3.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Status {

    private boolean success;
    private String message;

    public Status(final boolean success) {
        this.success = success;
        this.message = StringUtils.EMPTY;
    }

}