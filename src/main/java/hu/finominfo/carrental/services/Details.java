package hu.finominfo.carrental.services;

import hu.finominfo.carrental.Application;
import hu.finominfo.carrental.data.CarDetails;

public class Details {

    private final String id;
    private final CarDetails carDetails;
    private final String error;

    public Details(String id) {
        this.id = id;
        CarDetails myCarDetails;
        String myError;
        try {
            myCarDetails = Application.CARS.get(Integer.valueOf(id)).getCarDetails();
            myError = "";
        } catch (Throwable t) {
            myCarDetails = null;
            myError = "The given id is wrong. It shoud be a number between 0 and " + (Application.CARS.size() - 1);
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
