package com.miles.twinpeaks.dao.jpa.food;

import com.miles.twinpeaks.domain.usda.api.USDAFood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Repository can be used to delegate CRUD operations against the data source: http://goo.gl/P1J8QH
 */
public interface IUSDAFoodRepository extends PagingAndSortingRepository<USDAFood, Long> {
    //Receipt findReceiptByStoreID(int mb_store_id);
    Page findAll(Pageable pageable);
    List<USDAFood> findByBarcodevalueContaining(String barcodevalue);
}
