/*
 * Derived from spring-boot-rest-example by Siamak Khoubyari.
 * Copyright (c) 2014-2018 Siamak Khoubyari <khoubyari@gmail.com>
 * Licensed under the MIT License; see LICENSE-UPSTREAM-MIT.txt.
 * TwinPeaks adapts package names, domain concepts, and application behavior.
 */
package com.miles.twinpeaks.exception;

/**
 * for HTTP 400 errors
 */
public final class DataFormatException extends RuntimeException {
    public DataFormatException() {
        super();
    }

    public DataFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataFormatException(String message) {
        super(message);
    }

    public DataFormatException(Throwable cause) {
        super(cause);
    }
}
