package com.miles.twinpeaks.service.receipt;

import com.miles.twinpeaks.dao.jpa.receipt.IReceiptItemCategoryRepository;
import com.miles.twinpeaks.domain.receipt.Receipt;
import com.miles.twinpeaks.domain.receipt.ReceiptItemCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class ReceiptItemCategoryService {

    private static final Logger log = LoggerFactory.getLogger(ReceiptItemCategoryService.class);

    @Autowired
    private IReceiptItemCategoryRepository repo;

    public ReceiptItemCategoryService() {}

    public ReceiptItemCategory createCategory(ReceiptItemCategory category) {
        return repo.save(category);
    }

    public ReceiptItemCategory getCategory(long id) {

        ReceiptItemCategory c = repo.findById(id).get();
        return c;
    }

    public Page<Receipt> getAll(Integer page, Integer size) {
        Page pageOfCategories = repo.findAll(new PageRequest(page, size));
        // example of adding to the /metrics
        if (size > 50) {
            //counterService.increment("oxbow.ReceiptService.getAll.largePayload");
        }
        return pageOfCategories;
    }

    public void updateCategory(ReceiptItemCategory category) {
        repo.save(category);
    }

    public void deleteCategory(Long id) {
        repo.deleteById(id);
    }
}
