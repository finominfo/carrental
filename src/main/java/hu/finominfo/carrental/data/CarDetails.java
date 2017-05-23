package hu.finominfo.carrental.data;

import hu.finominfo.carrental.enums.Brand;
import hu.finominfo.carrental.enums.Color;

public class CarDetails {

    private final Brand brand;
    private final Color color;

    public CarDetails(Brand brand, Color color) {
        this.brand = brand;
        this.color = color;
    }

    public Brand getBrand() {
        return brand;
    }

    public Color getColor() {
        return color;
    }

}
