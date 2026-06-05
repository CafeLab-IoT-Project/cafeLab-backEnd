package com.cafemetrix.cafelab.monitoring.application.internal.outboundservices.acl;

import com.cafemetrix.cafelab.production.interfaces.acl.CoffeeproductionContextFacade;
import org.springframework.stereotype.Service;

@Service
public class ExternalProductionService {
    private final CoffeeproductionContextFacade coffeeProductionContextFacade;

    public ExternalProductionService(CoffeeproductionContextFacade coffeeProductionContextFacade) {
        this.coffeeProductionContextFacade = coffeeProductionContextFacade;
    }

    public boolean existsCoffeeLot(Long coffeeLotId) {
        if (coffeeLotId == null) {
            return false;
        }
        return coffeeProductionContextFacade.getCoffeeLotById(coffeeLotId).isPresent();
    }



}
