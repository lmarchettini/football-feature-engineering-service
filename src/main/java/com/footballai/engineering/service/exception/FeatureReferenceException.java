package com.footballai.engineering.service.exception;


public class FeatureReferenceException
        extends RuntimeException {

    public FeatureReferenceException(
            String message
    ) {
        super(message);
    }
}