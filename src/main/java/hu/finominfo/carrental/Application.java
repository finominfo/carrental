package hu.finominfo.carrental;

import hu.finominfo.carrental.client.CountersGUI;
import hu.finominfo.carrental.client.MainTester;
import hu.finominfo.carrental.data.Car;
import hu.finominfo.carrental.enums.Brand;
import hu.finominfo.carrental.enums.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.SwingUtilities;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

    public static volatile ConfigurableApplicationContext runningApp;
    public static volatile int port;
    public static final List<Car> CARS;
    public static final int NUMBER_OF_CARS = 100;
    public static final RestTemplate REST_TEMPLATE = new RestTemplate();
    public static final Random RANDOM = new Random(0xac365478eef2565L);

    static {
        CARS = new ArrayList<>();
        int sizeOfBrands = Brand.values().length;
        int sizeOfColors = Color.values().length;
        for (int i = 0; i < NUMBER_OF_CARS; i++) {
            CARS.add(new Car(i,
                    Brand.values()[RANDOM.nextInt(sizeOfBrands)],
                    Color.values()[RANDOM.nextInt(sizeOfColors)]));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CountersGUI::createAndShowGUI);
        runningApp = SpringApplication.run(Application.class, args);
    }

    @Component
    class MyListener implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

        @Override
        public void onApplicationEvent(final EmbeddedServletContainerInitializedEvent event) {
            port = event.getEmbeddedServletContainer().getPort();
            new MainTester(port).run();
        }
    }

}
