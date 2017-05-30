package hu.finominfo.carrental.services;

import hu.finominfo.carrental.Application;
import hu.finominfo.carrental.data.Car;
import hu.finominfo.carrental.data.CarDetails;
import java.util.List;
import javax.annotation.Resource;

public class Details {

    private final String id;
    private final CarDetails carDetails;
    private final String error;
    
    @Resource(name="cars")
    private List<Car> cars;

    public Details(String id) {
        this.id = id;
        CarDetails myCarDetails;
        String myError;
        try {
            myCarDetails = cars.get(Integer.valueOf(id)).getCarDetails();
            myError = "";
        } catch (Throwable t) {
            myCarDetails = null;
            myError = "The given id is wrong. It shoud be a number between 0 and " + (cars.size() - 1);
        }
        this.carDetails = myCarDetails;
        this.error = myError;
    }

    public String getId() {
        return id;
    }

    public CarDetails getCarDetails() {
        return carDetails;
    }

    public String getError() {
        return error;
    }

}
