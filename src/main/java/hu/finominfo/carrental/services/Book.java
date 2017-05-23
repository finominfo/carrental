package hu.finominfo.carrental.services;

import hu.finominfo.carrental.Application;
import hu.finominfo.carrental.data.Car;
import hu.finominfo.carrental.enums.EuropeanCountry;
import hu.finominfo.carrental.enums.Usage;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Book {

    public static final int MAX_BOOKING_TIME_IN_SECONDS = 1000;
    public static final int MAX_PARALLEL_BOOKING_REQUESTS = 500;
    public static final AtomicLong BOOKING_REQUESTS = new AtomicLong(0);
    public static final AtomicLong BOOKING_REQUESTS_IN_PROGRESS = new AtomicLong(0);
    public static final AtomicLong BOOKING_FAILED_BECAUSE_OF_OVERLOAD = new AtomicLong(0);
    public static final AtomicLong BOOKING_FAILED_BECAUSE_OF_SYNTAX = new AtomicLong(0);
    public static final AtomicLong BOOKING_FAILED_BECAUSE_OF_FOREIGN = new AtomicLong(0);
    public static final AtomicLong BOOKING_FAILED_BECAUSE_OF_ALREADY_BOOKED = new AtomicLong(0);
    public static final AtomicLong BOOKING_SUCCESS = new AtomicLong(0);
    private volatile int id = -1;
    private volatile int bookingTime = 0;
    private volatile Usage usage = null;
    private volatile Set<EuropeanCountry> countries = new HashSet<>();
    private volatile boolean successfulBooking = false;
    private volatile String error = null;

    public Book(String id, String periodInSeconds, String usageStr, String countriesStr) {
        BOOKING_REQUESTS.incrementAndGet();
        BOOKING_REQUESTS_IN_PROGRESS.incrementAndGet();
        if (!checkNumOfParallelRequests()) {
            BOOKING_REQUESTS_IN_PROGRESS.decrementAndGet();
            return;
        }
        if (!checkId(id)) {
            BOOKING_REQUESTS_IN_PROGRESS.decrementAndGet();
            return;
        }
        if (!checkPeriod(periodInSeconds)) {
            BOOKING_REQUESTS_IN_PROGRESS.decrementAndGet();
            return;
        }
        if (!checkBookingTime()) {
            BOOKING_REQUESTS_IN_PROGRESS.decrementAndGet();
            return;
        }
        if (!checkUsage(usageStr)) {
            BOOKING_REQUESTS_IN_PROGRESS.decrementAndGet();
            return;
        }
        if (!checkCountries(countriesStr)) {
            BOOKING_REQUESTS_IN_PROGRESS.decrementAndGet();
            return;
        }
        if (!checkFree()) {
            BOOKING_REQUESTS_IN_PROGRESS.decrementAndGet();
            return;
        }
        if (!checkExternal()) {
            BOOKING_REQUESTS_IN_PROGRESS.decrementAndGet();
            return;
        }
        tryToBook();
        BOOKING_REQUESTS_IN_PROGRESS.decrementAndGet();
    }

    private boolean checkNumOfParallelRequests() {
        if (BOOKING_REQUESTS_IN_PROGRESS.get() > MAX_PARALLEL_BOOKING_REQUESTS) {
            error = "The server is overloaded. Try again later.";
            BOOKING_FAILED_BECAUSE_OF_OVERLOAD.incrementAndGet();
            return false;
        }
        return true;
    }

    private boolean checkId(String carId) {
        try {
            id = Integer.valueOf(carId);
        } catch (Throwable t) {
            error = "The given id is wrong. It shoud be a number between 0 and " + (Application.CARS.size() - 1);
            BOOKING_FAILED_BECAUSE_OF_SYNTAX.incrementAndGet();
            return false;
        }
        if (0 > id || id >= Application.CARS.size()) {
            error = "The given id is wrong. It shoud be a number between 0 and " + (Application.CARS.size() - 1);
            BOOKING_FAILED_BECAUSE_OF_SYNTAX.incrementAndGet();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkPeriod(String periodInSeconds) {
        try {
            bookingTime = Integer.valueOf(periodInSeconds);
            return true;
        } catch (Throwable t) {
            error = "The given period is wrong. It shoud be a number between 1 and " + MAX_BOOKING_TIME_IN_SECONDS + " seconds.";
            BOOKING_FAILED_BECAUSE_OF_SYNTAX.incrementAndGet();
            return false;
        }
    }

    private boolean checkBookingTime() {
        if (bookingTime < 1 || bookingTime > MAX_BOOKING_TIME_IN_SECONDS) {
            error = "The given period is wrong. It shoud be a number between 1 and " + MAX_BOOKING_TIME_IN_SECONDS + " seconds.";
            BOOKING_FAILED_BECAUSE_OF_SYNTAX.incrementAndGet();
            return false;
        }
        return true;
    }

    private boolean checkUsage(String usageStr) {
        try {
            usage = Usage.get(usageStr);
            return true;
        } catch (Throwable ex) {
            error = ex.getMessage();
            BOOKING_FAILED_BECAUSE_OF_SYNTAX.incrementAndGet();
            return false;
        }
    }

    private boolean checkCountries(String countriesStr) {
        try {
            String[] countriesArray = countriesStr.split(",");
            for (String country : countriesArray) {
                countries.add(EuropeanCountry.get(country));
            }
        } catch (Throwable t) {
            error = t.getMessage();
            BOOKING_FAILED_BECAUSE_OF_SYNTAX.incrementAndGet();
            return false;
        }
        if (countries.isEmpty()) {
            error = "You should add at least one country when foreign usage is specified.";
            BOOKING_FAILED_BECAUSE_OF_SYNTAX.incrementAndGet();
            return false;
        }
        return true;
    }

    private boolean checkFree() {
        long bookedUpTo = Application.CARS.get(id).getBookedUpTo();
        long now = System.currentTimeMillis();
        if (now < bookedUpTo) {
            error = "The car will probably be free in " + (int) ((bookedUpTo - now + 500) / 1000) + " seconds.";
            BOOKING_FAILED_BECAUSE_OF_ALREADY_BOOKED.incrementAndGet();
            return false;
        }
        return true;
    }

    private boolean checkExternal() {
        if (!usage.equals(Usage.FOREIGN)) {
            return true;
        }
        External external = Application.REST_TEMPLATE.getForObject("http://localhost:" + Application.port + "/external?id=" + Application.CARS.get(id).getId(), External.class);
        if (!external.getResult()) {
            BOOKING_FAILED_BECAUSE_OF_FOREIGN.incrementAndGet();
            error = external.getError();
            return false;
        }
        return true;
    }

    private void tryToBook() {
        long now = System.currentTimeMillis();
        Car car = Application.CARS.get(id);
        if (car.getBookedUpTo() < now) {
            try {
                car.getLock().lock();
                if (car.getBookedUpTo() < now) {
                    car.setBookedUpTo(now + bookingTime * 1000L);
                    car.getTargetCountries().clear();
                    car.getTargetCountries().addAll(countries);
                    successfulBooking = true;
                    BOOKING_SUCCESS.incrementAndGet();
                }
            } finally {
                car.getLock().unlock();
            }
        }
        if (!successfulBooking) {
            BOOKING_FAILED_BECAUSE_OF_ALREADY_BOOKED.incrementAndGet();
            error = "The car will probably be free in " + (int) ((car.getBookedUpTo() - now + 500) / 1000) + " seconds.";
        }
    }

    public int getId() {
        return id;
    }

    public int getBookingTime() {
        return bookingTime;
    }

    public Usage getUsage() {
        return usage;
    }

    public Set<EuropeanCountry> getCountries() {
        return countries;
    }

    public boolean isSuccessfulBooking() {
        return successfulBooking;
    }

    public String getError() {
        return error;
    }

}
