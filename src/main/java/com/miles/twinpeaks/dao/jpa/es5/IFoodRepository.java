package com.miles.twinpeaks.dao.jpa.es5;

import com.miles.twinpeaks.domain.usda.api.Food;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface IFoodRepository extends ElasticsearchRepository<Food, String> {

    List<Food> findByName(String barcodevalue);

    Food findByNdbno(String Ndbno);

}
