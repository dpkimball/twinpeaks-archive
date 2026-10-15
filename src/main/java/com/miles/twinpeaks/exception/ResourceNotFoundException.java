/*
 * Derived from spring-boot-rest-example by Siamak Khoubyari.
 * Copyright (c) 2014-2018 Siamak Khoubyari <khoubyari@gmail.com>
 * Licensed under the MIT License; see LICENSE-UPSTREAM-MIT.txt.
 * TwinPeaks adapts package names, domain concepts, and application behavior.
 */
package com.miles.twinpeaks.exception;

/**
 * For HTTP 404 errros
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

}
