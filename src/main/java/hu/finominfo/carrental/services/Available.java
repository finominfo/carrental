package hu.finominfo.carrental.services;

import hu.finominfo.carrental.Application;
import java.util.ArrayList;
import java.util.List;

public class Available {

    private final List<Long> available;

    public Available() {
        this.available = new ArrayList<>();
        long now = System.currentTimeMillis();
        Application.CARS.stream().filter((car) -> (car.getBookedUpTo() < now)).forEachOrdered((car) -> {
            available.add(car.getId());
        });
    }

    public List<Long> getAvailable() {
        return available;
    }
}