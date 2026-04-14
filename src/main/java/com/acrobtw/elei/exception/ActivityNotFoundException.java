package com.acrobtw.elei.exception;

public class ActivityNotFoundException extends RuntimeException {
    public ActivityNotFoundException(Long id) {
        super("Activity with id " + id + " not found");
    }
}
