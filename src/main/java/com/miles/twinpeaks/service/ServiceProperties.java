/*
 * Derived from spring-boot-rest-example by Siamak Khoubyari.
 * Copyright (c) 2014-2018 Siamak Khoubyari <khoubyari@gmail.com>
 * Licensed under the MIT License; see LICENSE-UPSTREAM-MIT.txt.
 * TwinPeaks adapts package names, domain concepts, and application behavior.
 */
package com.miles.twinpeaks.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/*
 * demonstrates how service-specific properties can be injected
 */
@ConfigurationProperties(prefix = "receipt.service", ignoreUnknownFields = false)
@Component
public class ServiceProperties {

    @NotNull // you can also create configurationPropertiesValidator
    private String name = "Empty";

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
