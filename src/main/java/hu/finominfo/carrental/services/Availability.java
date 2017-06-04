package hu.finominfo.carrental.services;

import hu.finominfo.carrental.data.Car;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

public class Availability {

    @Resource(name = "cars")
    private List<Car> cars;

    private final List<Long> available;

    public Availability() {
        this.available = new ArrayList<>();
        long now = System.currentTimeMillis();
        cars.stream().filter((car) -> (car.getBookedUpTo() < now)).forEachOrdered((car) -> {
            available.add(car.getId());
        });
    }

    public List<Long> getAvailable() {
        return available;
    }
}
