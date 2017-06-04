package hu.finominfo.carrental.services;

import static hu.finominfo.carrental.Application.NUMBER_OF_CARS;
import static hu.finominfo.carrental.Application.RANDOM;
import hu.finominfo.carrental.data.Car;
import hu.finominfo.carrental.enums.Brand;
import hu.finominfo.carrental.enums.Color;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CarConfig {
  
    @Bean(name = "cars")
    public List<Car> getCars() {
        List<Car> cars = new ArrayList<>();
        int sizeOfBrands = Brand.values().length;
        int sizeOfColors = Color.values().length;
        for (int i = 0; i < NUMBER_OF_CARS; i++) {
            cars.add(new Car(i,
                    Brand.values()[RANDOM.nextInt(sizeOfBrands)],
                    Color.values()[RANDOM.nextInt(sizeOfColors)]));
        }
        return cars;
    }
  
}
