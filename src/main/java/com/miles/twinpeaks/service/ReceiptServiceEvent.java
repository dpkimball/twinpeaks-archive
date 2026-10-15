/*
 * Derived from spring-boot-rest-example by Siamak Khoubyari.
 * Copyright (c) 2014-2018 Siamak Khoubyari <khoubyari@gmail.com>
 * Licensed under the MIT License; see LICENSE-UPSTREAM-MIT.txt.
 * TwinPeaks adapts package names, domain concepts, and application behavior.
 */
package com.miles.twinpeaks.service;

import org.springframework.context.ApplicationEvent;

/**
 * This is an optional class used in publishing application events.
 * This can be used to inject events into the Spring Boot audit management endpoint.
 */
public class ReceiptServiceEvent extends ApplicationEvent {

    public ReceiptServiceEvent(Object source) {
        super(source);
    }

    public String toString() {
        return "My ReceiptService Event";
    }
}
