/*
 * Derived from spring-boot-rest-example by Siamak Khoubyari.
 * Copyright (c) 2014-2018 Siamak Khoubyari <khoubyari@gmail.com>
 * Licensed under the MIT License; see LICENSE-UPSTREAM-MIT.txt.
 * TwinPeaks adapts package names, domain concepts, and application behavior.
 */
package com.miles.twinpeaks.service.receipt;

import com.miles.twinpeaks.domain.receipt.Receipt;
import com.miles.twinpeaks.dao.jpa.receipt.IReceiptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.actuate.metrics.CounterService;
//import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
 * Sample service to demonstrate what the API would use to get things done
 */
@Service
public class ReceiptService {

    private static final Logger log = LoggerFactory.getLogger(ReceiptService.class);

    @Autowired
    private IReceiptRepository ReceiptRepository;

/*    @Autowired
    CounterService counterService;

    @Autowired
    GaugeService gaugeService;
*/
    public ReceiptService() {
    }

    public Receipt createReceipt(Receipt Receipt) {
        return ReceiptRepository.save(Receipt);
    }

    public Receipt getReceipt(long id) {
        Optional<Receipt> r = ReceiptRepository.findById(id);
        if (r.isPresent()) {
            Receipt receipt = r.get();
            if (receipt != null)
                receipt.getReceiptItems();
                return receipt;
        }
        return null;
    }

    public void updateReceipt(Receipt Receipt) {
        ReceiptRepository.save(Receipt);
    }

    public void deleteReceipt(Long id) {
        ReceiptRepository.deleteById(id);
    }

    //http://goo.gl/7fxvVf
    public Page<Receipt> getAllReceipts(Integer page, Integer size) {
        Page pageOfReceipts = ReceiptRepository.findAll(new PageRequest(page, size));
        // example of adding to the /metrics
        if (size > 50) {
            //counterService.increment("oxbow.ReceiptService.getAll.largePayload");
        }
        return pageOfReceipts;
    }
    public Page<Receipt> findAllByUserId(Long id, Integer page, Integer size) {
        Page<Receipt>receipts = ReceiptRepository.findAllByUserId(id, new PageRequest(page, size));
        // example of adding to the /metrics
        if (size > 50) {
            //counterService.increment("oxbow.ReceiptService.getAll.largePayload");
        }
        return receipts;
    }
}
