package hu.finominfo.carrental.services;

import hu.finominfo.carrental.Application;
import hu.finominfo.carrental.data.Car;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.Resource;

public class External {

    public static final int MAX_PARALLEL_FOREIGN_REQUESTS = 200;
    public static final AtomicLong FOREIGN_REQUESTS = new AtomicLong(0);
    public static final AtomicLong FOREIGN_REQUESTS_IN_PROGRESS = new AtomicLong(0);
    public static final AtomicLong FOREIGN_FAILED_BECAUSE_OF_OVERLOAD = new AtomicLong(0);
    public static final AtomicLong FOREIGN_FAILED_BECAUSE_OF_SYNTAX = new AtomicLong(0);
    public static final AtomicLong FOREIGN_FAILED_BECAUSE_OF_FOREIGN = new AtomicLong(0);
    public static final AtomicLong FOREIGN_SUCCESS = new AtomicLong(0);
    private volatile int id;
    private volatile Boolean result = false;
    private volatile String error = null;
    
    @Resource(name="cars")
    private List<Car> cars;

    private final static Random RANDOM = new Random(0xfd3456234565L);

    public External(String carId) {
        FOREIGN_REQUESTS.incrementAndGet();
        FOREIGN_REQUESTS_IN_PROGRESS.incrementAndGet();
        if (!checkNumOfParallelRequests()) {
            FOREIGN_REQUESTS_IN_PROGRESS.decrementAndGet();
            return;
        }
        if (!checkId(carId)) {
            FOREIGN_REQUESTS_IN_PROGRESS.decrementAndGet();
            return;
        }
        waitSomeTime();
        makeResult();
        FOREIGN_REQUESTS_IN_PROGRESS.decrementAndGet();
    }

    private boolean checkNumOfParallelRequests() {
        if (FOREIGN_REQUESTS_IN_PROGRESS.get() > MAX_PARALLEL_FOREIGN_REQUESTS) {
            error = "The server is overloaded. Try again later.";
            FOREIGN_FAILED_BECAUSE_OF_OVERLOAD.incrementAndGet();
            return false;
        }
        return true;
    }

    private boolean checkId(String carId) {
        try {
            id = Integer.valueOf(carId);
        } catch (Throwable t) {
            error = "The given id is wrong. It shoud be a number between 0 and " + (cars.size() - 1);
            FOREIGN_FAILED_BECAUSE_OF_SYNTAX.incrementAndGet();
            return false;
        }
        if (0 > id || id >= cars.size()) {
            error = "The given id is wrong. It shoud be a number between 0 and " + (cars.size() - 1);
            FOREIGN_FAILED_BECAUSE_OF_SYNTAX.incrementAndGet();
            return false;
        } else {
            return true;
        }
    }

    private void waitSomeTime() {
        try {
            Thread.sleep(200 + RANDOM.nextInt(1000));
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
    }

    private void makeResult() {
        if (cars.get(id).getCarDetails().getBrand().isForeignUsageEnabled()) {
            result = true;
            FOREIGN_SUCCESS.incrementAndGet();
        } else {
            result = false;
            error = "Foreign usage is not enabled for this car.";
            FOREIGN_FAILED_BECAUSE_OF_FOREIGN.incrementAndGet();
        }
    }

    public External() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getId() {
        return id;
    }

    public Boolean getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

}
