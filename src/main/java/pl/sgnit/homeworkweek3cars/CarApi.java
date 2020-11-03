package pl.sgnit.homeworkweek3cars;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cars")
public class CarApi {

    private List<Car> carList;

    public CarApi() {
        carList = new ArrayList<>();
        carList.add(new Car(1L, "Citroen", "C3", "red"));
        carList.add(new Car(2L, "Ford", "Mondeo", "blue"));
        carList.add(new Car(3L, "Audi", "Q3", "black"));
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        return new ResponseEntity<>(carList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();

        if (first.isPresent()) {
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/color/{colorName}")
    public ResponseEntity<List<Car>> getByColor(@PathVariable String colorName){
        List<Car> cars = carList.stream().filter(car -> car.getColor().equals(colorName)).collect(Collectors.toList());

        if(cars.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addCar(@RequestBody Car car) {
        boolean add = carList.add(car);

        if (add) {
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity updateCar(@RequestBody Car newCar) {
        Optional<Car> car = carList.stream().filter(car1 -> car1.getId()==newCar.getId()).findFirst();

        if (car.isPresent()) {
            carList.remove(car.get());
            carList.add(newCar);
            return new ResponseEntity(car.get(), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}/{property}/{value}")
    public ResponseEntity updateCarProperty(@PathVariable Long id, @PathVariable String property, @PathVariable String value) {
        Optional<Car> optionalCar = carList.stream().filter(car1 -> car1.getId()==id).findFirst();

        if(optionalCar.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Car car = optionalCar.get();
        boolean updated = true;

        switch (property) {
            case "mark":
                car.setMark(value);
                break;
            case "model":
                car.setModel(value);
                break;
            case "color":
                car.setColor(value);
                break;
            default:
                updated = false;
        }
        if (updated) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCar(@PathVariable Long id) {
        Optional<Car> optionalCar = carList.stream().filter(car -> car.getId()==id).findFirst();

        if(optionalCar.isPresent()) {
            carList.remove(optionalCar.get());
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
