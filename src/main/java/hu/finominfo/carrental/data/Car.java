package hu.finominfo.carrental.data;

import hu.finominfo.carrental.enums.Brand;
import hu.finominfo.carrental.enums.Color;
import hu.finominfo.carrental.enums.EuropeanCountry;
import hu.finominfo.carrental.enums.Usage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Car {

    private final Lock lock;
    private final long id;
    private final CarDetails carDetails;
    private final List<EuropeanCountry> targetCountries;
    private volatile long bookedUpTo = 0;
    private volatile Usage usage = null;

    public Car(long id, Brand brand, Color color) {
        this.id = id;
        this.carDetails = new CarDetails(brand, color);
        this.targetCountries = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    public long getId() {
        return id;
    }

    public CarDetails getCarDetails() {
        return carDetails;
    }

    public long getBookedUpTo() {
        return bookedUpTo;
    }

    public List<EuropeanCountry> getTargetCountries() {
        return targetCountries;
    }

    public Usage getUsage() {
        return usage;
    }

    public Lock getLock() {
        return lock;
    }

    public void setBookedUpTo(long bookedUpTo) {
        this.bookedUpTo = bookedUpTo;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }
}
