package com.footballai.engineering.service.exception;



public class InvalidFeatureVectorException
        extends RuntimeException {

    public InvalidFeatureVectorException(
            String message
    ) {
        super(message);
    }
}