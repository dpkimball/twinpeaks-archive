package com.miles.twinpeaks.service.receipt;

import com.miles.twinpeaks.dao.jpa.receipt.IReceiptItemRepository;
import com.miles.twinpeaks.domain.receipt.ReceiptItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ReceiptItemService {

    private static final Logger log = LoggerFactory.getLogger(ReceiptItemService.class);

    @Autowired
    private IReceiptItemRepository ReceiptItemRepository;

    public ReceiptItemService() {}

    public ReceiptItem createReceiptItem(ReceiptItem ReceiptItem) {
        return ReceiptItemRepository.save(ReceiptItem);
    }

    public Optional<ReceiptItem> getReceiptItem(long id) {

        Optional<ReceiptItem> r = ReceiptItemRepository.findById(id);
        return r;
    }

    public void updateReceiptItem(ReceiptItem ReceiptItem) {
        ReceiptItemRepository.save(ReceiptItem);
    }

    public void deleteReceiptItem(Long id) {
        ReceiptItemRepository.deleteById(id);
    }
}
