package hu.finominfo.carrental;

import static hu.finominfo.carrental.Application.NUMBER_OF_CARS;
import static hu.finominfo.carrental.Application.RANDOM;
import hu.finominfo.carrental.data.Car;
import hu.finominfo.carrental.enums.Brand;
import hu.finominfo.carrental.enums.Color;
import hu.finominfo.carrental.services.Details;
import hu.finominfo.carrental.services.Availability;
import hu.finominfo.carrental.services.Book;
import hu.finominfo.carrental.services.External;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    
    private static final String NULL = "null";
    private static final String MINUS = "-1";

    @RequestMapping(value = "/external", method = GET)
    public Callable<External> external(@RequestParam(value = "id", defaultValue = NULL) String id) {
        return () -> new External(id);
    }

    @RequestMapping(value = "/available", method = GET)
    public Callable<Availability> available() {
        return () -> new Availability();
    }

    @RequestMapping(value = "/details", method = GET)
    public Callable<Details> details(@RequestParam(value = "id", defaultValue = MINUS) String id) {
        return () -> new Details(id);
    }

    @RequestMapping(value = "/book", method = GET)
    public Callable<Book> book(@RequestParam(value = "id", defaultValue = MINUS) String id,
            @RequestParam(value = "time", defaultValue = MINUS) String time,
            @RequestParam(value = "usage", defaultValue = NULL) String usage,
            @RequestParam(value = "countries", defaultValue = NULL) String countries) {
        return () -> new Book(id, time, usage, countries);
    }

    @RequestMapping(value = "/**", method = GET)
    public Callable<String> details() {
        return () -> "{usage:[\n" 
                + "\"localhost:8080/available\", \n"
                + "\"localhost:8080/details?id=[id]\", \n"
                + "\"localhost:8080/external?id=[id]\", \n"
                + "\"localhost:8080/book?id=[id]&time=[time]&usage=[domestic/foreign]&countries=[one or more european country]\", \n"
                + "]}";
    }
    
}
