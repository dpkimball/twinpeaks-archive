package com.miles.twinpeaks.dao.jpa.receipt;

import com.miles.twinpeaks.domain.receipt.ReceiptItemCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


public interface IReceiptItemCategoryRepository extends CrudRepository<ReceiptItemCategory, Long> {
    Page findAll(Pageable pageable);
}
