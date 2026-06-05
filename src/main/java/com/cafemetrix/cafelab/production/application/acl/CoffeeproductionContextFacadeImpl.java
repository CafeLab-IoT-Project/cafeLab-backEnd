package com.cafemetrix.cafelab.production.application.acl;

import com.cafemetrix.cafelab.production.domain.model.aggregates.CoffeeLot;
import com.cafemetrix.cafelab.production.domain.model.aggregates.RoastProfile;
import com.cafemetrix.cafelab.production.domain.model.aggregates.Supplier;
import com.cafemetrix.cafelab.production.domain.model.commands.*;
import com.cafemetrix.cafelab.production.domain.model.queries.*;
import com.cafemetrix.cafelab.production.domain.services.*;
import com.cafemetrix.cafelab.production.interfaces.acl.CoffeeproductionContextFacade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoffeeproductionContextFacadeImpl implements CoffeeproductionContextFacade {
    private final SupplierCommandService supplierCommandService;
    private final SupplierQueryService supplierQueryService;
    private final CoffeeLotCommandService coffeeLotCommandService;
    private final CoffeeLotQueryService coffeeLotQueryService;
    private final RoastProfileCommandService roastProfileCommandService;
    private final RoastProfileQueryService roastProfileQueryService;

    public CoffeeproductionContextFacadeImpl(
            SupplierCommandService supplierCommandService,
            SupplierQueryService supplierQueryService,
            CoffeeLotCommandService coffeeLotCommandService,
            CoffeeLotQueryService coffeeLotQueryService,
            RoastProfileCommandService roastProfileCommandService,
            RoastProfileQueryService roastProfileQueryService) {
        this.supplierCommandService = supplierCommandService;
        this.supplierQueryService = supplierQueryService;
        this.coffeeLotCommandService = coffeeLotCommandService;
        this.coffeeLotQueryService = coffeeLotQueryService;
        this.roastProfileCommandService = roastProfileCommandService;
        this.roastProfileQueryService = roastProfileQueryService;
    }

    @Override
    public Long createSupplier(Long userId, String name, String email, Long phone, String location, List<String> specialties) {
        var createSupplierCommand = new CreateSupplierCommand(userId, name, email, phone, location, specialties);
        var supplier = supplierCommandService.handle(createSupplierCommand);
        return supplier.map(Supplier::getId).orElse(0L);
    }

    @Override
    public Long updateSupplier(Long supplierId, String name, String email, Long phone, String location, List<String> specialties) {
        var updateSupplierCommand = new UpdateSupplierCommand(supplierId, name, email, phone, location, specialties);
        var supplier = supplierCommandService.handle(updateSupplierCommand);
        return supplier.map(Supplier::getId).orElse(0L);
    }

    @Override
    public boolean deleteSupplier(Long supplierId) {
        var deleteSupplierCommand = new DeleteSupplierCommand(supplierId);
        return supplierCommandService.handle(deleteSupplierCommand);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierQueryService.handle(new GetAllSuppliersQuery());
    }

    @Override
    public Optional<Supplier> getSupplierById(Long supplierId) {
        return supplierQueryService.handle(new GetSupplierByIdQuery(supplierId));
    }

    @Override
    public List<Supplier> getSuppliersByUserId(Long userId) {
        return supplierQueryService.handle(new GetSuppliersByUserIdQuery(userId));
    }

    @Override
    public Long createCoffeeLot(Long userId, Long supplierId, String lotName, String coffeeType, 
                              String processingMethod, Integer altitude, Double weight, 
                              String origin, String status, List<String> certifications) {
        var createCoffeeLotCommand = new CreateCoffeeLotCommand(userId, supplierId, lotName, coffeeType, 
                processingMethod, altitude, weight, origin, status, certifications);
        var coffeeLot = coffeeLotCommandService.handle(createCoffeeLotCommand);
        return coffeeLot.map(CoffeeLot::getId).orElse(0L);
    }

    @Override
    public Long updateCoffeeLot(Long coffeeLotId, String lotName, String coffeeType, 
                              String processingMethod, Integer altitude, Double weight, 
                              String origin, String status, List<String> certifications) {
        var updateCoffeeLotCommand = new UpdateCoffeeLotCommand(coffeeLotId, lotName, coffeeType, 
                processingMethod, altitude, weight, origin, status, certifications);
        var coffeeLot = coffeeLotCommandService.handle(updateCoffeeLotCommand);
        return coffeeLot.map(CoffeeLot::getId).orElse(0L);
    }

    @Override
    public boolean deleteCoffeeLot(Long coffeeLotId) {
        var deleteCoffeeLotCommand = new DeleteCoffeeLotCommand(coffeeLotId);
        return coffeeLotCommandService.handle(deleteCoffeeLotCommand);
    }

    @Override
    public List<CoffeeLot> getAllCoffeeLots() {
        return coffeeLotQueryService.handle(new GetAllCoffeeLotsQuery());
    }

    @Override
    public Optional<CoffeeLot> getCoffeeLotById(Long coffeeLotId) {
        return coffeeLotQueryService.handle(new GetCoffeeLotByIdQuery(coffeeLotId));
    }

    @Override
    public List<CoffeeLot> getCoffeeLotsByUserId(Long userId) {
        return coffeeLotQueryService.handle(new GetCoffeeLotsByUserIdQuery(userId));
    }

    @Override
    public List<CoffeeLot> getCoffeeLotsBySupplierId(Long supplierId) {
        return coffeeLotQueryService.handle(new GetCoffeeLotsBySupplierIdQuery(supplierId));
    }

    @Override
    public boolean existsCoffeeLotById(Long coffeeLotId) {
        var getCoffeeLotByIdQuery = new GetCoffeeLotByIdQuery(coffeeLotId);
        var coffeeLot = coffeeLotQueryService.handle(getCoffeeLotByIdQuery);
        return coffeeLot.isPresent();
    }

    @Override
    public Long createRoastProfile(Long userId, String name, String type, Integer duration, 
                                 Double tempStart, Double tempEnd, Long coffeeLotId, Boolean isFavorite) {
        var createRoastProfileCommand = new CreateRoastProfileCommand(userId, name, type, duration, 
                tempStart, tempEnd, coffeeLotId, isFavorite);
        var roastProfile = roastProfileCommandService.handle(createRoastProfileCommand);
        return roastProfile.map(RoastProfile::getId).orElse(0L);
    }

    @Override
    public Long updateRoastProfile(Long roastProfileId, String name, String type, Integer duration, 
                                 Double tempStart, Double tempEnd, Long coffeeLotId, Boolean isFavorite) {
        var updateRoastProfileCommand = new UpdateRoastProfileCommand(roastProfileId, name, type, duration, 
                tempStart, tempEnd, coffeeLotId, isFavorite);
        var roastProfile = roastProfileCommandService.handle(updateRoastProfileCommand);
        return roastProfile.map(RoastProfile::getId).orElse(0L);
    }

    @Override
    public boolean deleteRoastProfile(Long roastProfileId) {
        var deleteRoastProfileCommand = new DeleteRoastProfileCommand(roastProfileId);
        return roastProfileCommandService.handle(deleteRoastProfileCommand);
    }

    @Override
    public List<RoastProfile> getAllRoastProfiles() {
        return roastProfileQueryService.handle(new GetAllRoastProfilesQuery());
    }

    @Override
    public Optional<RoastProfile> getRoastProfileById(Long roastProfileId) {
        return roastProfileQueryService.handle(new GetRoastProfileByIdQuery(roastProfileId));
    }

    @Override
    public List<RoastProfile> getRoastProfilesByUserId(Long userId) {
        return roastProfileQueryService.handle(new GetRoastProfilesByUserIdQuery(userId));
    }

    @Override
    public List<RoastProfile> getRoastProfilesByCoffeeLotId(Long coffeeLotId) {
        return roastProfileQueryService.handle(new GetRoastProfilesByCoffeeLotIdQuery(coffeeLotId));
    }
}
