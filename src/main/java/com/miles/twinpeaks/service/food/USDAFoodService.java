package com.miles.twinpeaks.service.food;

import com.miles.twinpeaks.dao.jpa.food.IUSDAFoodRepository;
import com.miles.twinpeaks.domain.usda.api.USDAFood;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class USDAFoodService {

    private static final Logger log = LoggerFactory.getLogger(USDAFoodService.class);

    @Autowired
    private IUSDAFoodRepository USDAFoodRepository;


    public USDAFoodService() {
    }

    public USDAFood createUSDAFood(USDAFood USDAFood) {
        return USDAFoodRepository.save(USDAFood);
    }

    public USDAFood getUSDAFood(long id) {
        Optional<USDAFood> r = USDAFoodRepository.findById(id);
        if (r.isPresent()) {
            USDAFood food = r.get();
            //if (food != null)
            //    food.getUSDAFoodItems();
            //    return food;
            return food;
        }
        return null;
    }

    public USDAFood getUSDAFoodByUPC(String barcodevalue) {
        List<USDAFood> records_matching_upc = USDAFoodRepository.findByBarcodevalueContaining(barcodevalue);
        if (records_matching_upc == null){
            return null;
        }
        else if (records_matching_upc.size() == 0 ){
            return records_matching_upc.get(0);
        }
        else {
            //todo we need to throw an error
            return records_matching_upc.get(0);
        }
    }

    public void updateUSDAFood(USDAFood USDAFood) {
        USDAFoodRepository.save(USDAFood);
    }

    public void deleteUSDAFood(Long id) {
        USDAFoodRepository.deleteById(id);
    }

    //http://goo.gl/7fxvVf
    public Page<USDAFood> getAllUSDAFoods(Integer page, Integer size) {
        Page pageOfUSDAFoods = USDAFoodRepository.findAll(new PageRequest(page, size));
        // example of adding to the /metrics
        if (size > 50) {
            //counterService.increment("oxbow.USDAFoodService.getAll.largePayload");
        }
        return pageOfUSDAFoods;
    }
}
