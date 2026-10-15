package com.miles.twinpeaks.dao.jpa.receipt;

import com.miles.twinpeaks.domain.receipt.ReceiptItem;
import org.springframework.data.repository.CrudRepository;


public interface IReceiptItemRepository extends CrudRepository<ReceiptItem, Long> {

}
