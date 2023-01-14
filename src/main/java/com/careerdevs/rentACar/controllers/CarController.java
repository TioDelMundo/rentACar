package com.careerdevs.rentACar.controllers;

import com.careerdevs.rentACar.models.Branch;
import com.careerdevs.rentACar.models.Car;
import com.careerdevs.rentACar.models.CarType;
import com.careerdevs.rentACar.services.BranchService;
import com.careerdevs.rentACar.services.CarService;
import com.careerdevs.rentACar.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarService carService;
    @Autowired
    private BranchService branchService;

    @Autowired
    private CustomerService customerService;

    // Will likely get rid of since all cars should be associated with a Branch
    @GetMapping("/")
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> allCarsInCompany = carService.findAll();

        return new ResponseEntity<>(allCarsInCompany, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getById(@PathVariable Long id) {
        Car sedan = carService.findCar(id);

        return new ResponseEntity<>(sedan, HttpStatus.OK);
    }

    @GetMapping("branch/{branchId}")
    public ResponseEntity<Set<Car>> getCarsByBranchId(@PathVariable Long branchId) {
        Branch branch = branchService.findBranch(branchId);
        Set<Car> carsInBranches = carService.getBranchCars(branch);

        return new ResponseEntity<>(carsInBranches, HttpStatus.OK);
    }

    //TODO: Get all cars by carType
//    @GetMapping("/style/{carType}")
//    public ResponseEntity<List<Car>> getAllCarsByType (@PathVariable String carType) {
//
//    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCarByCustomerId (@PathVariable Long customerId) {
        Optional<Car> car = carService.findByCustomerid(customerId);

        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Car> createNewCar(@RequestBody Car newCarData) {
        Car createdCar = carService.saveCar(newCarData);

        return new ResponseEntity<>(createdCar, HttpStatus.CREATED);
    }

    @PostMapping("/{carType}/{id}")
    public ResponseEntity<Car> updateCarTypeById(@RequestBody Car updatedCarData, @PathVariable String carType, @PathVariable Long id) {
        Car car = carService.findCar(id);
        CarType type = carService.setCarType(carType);

        if (car.getCarType() == null) {
            car.setCarType(type);
        }
        carService.saveCar(car);

        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @PostMapping("branch/{branchId}")
    public ResponseEntity<Car> updateCarToBranch(@RequestBody Car newCarData, @PathVariable Long branchId) {
        Branch branch = branchService.findBranch(branchId);
        newCarData.setBranch(branch);

        return new ResponseEntity<>(carService.saveCar(newCarData), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Car> deleteCarById(@PathVariable Long carId) {
        Car car = carService.deleteCar(carId);

        return new ResponseEntity<>(car, HttpStatus.OK);
    }
}
