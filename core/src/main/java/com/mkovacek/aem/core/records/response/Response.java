package com.mkovacek.aem.core.records.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response<T> {

    private Status status;
    private T data;

}