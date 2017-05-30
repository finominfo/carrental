package hu.finominfo.carrental.client;

import hu.finominfo.carrental.Application;
import static hu.finominfo.carrental.Application.RANDOM;
import hu.finominfo.carrental.services.Availability;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestClientException;

public class MainTester implements ListenableFutureCallback<ResponseEntity<String>>, Runnable {

    private final int port;
    private final AsyncRestTemplate asynchRestTemplate;
    private final AtomicInteger operationNumber;
    private final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);
    private volatile Availability lastAvailable = null;
    private static volatile boolean running = true;
    
    public static void startAutoTesting() {
        running = true;
    }

    public static void stopAutoTesting() {
        running = false;
    }

    public MainTester(int port) {
        this.port = port;
        asynchRestTemplate = new AsyncRestTemplate();
        operationNumber = new AtomicInteger(0);
    }
    

    @Override
    public void run() {
        if (Application.runningApp == null) {
            makeSchedule(1);
            return;
        }
        if (!Application.runningApp.isActive()) {
            return;
        }
        if (!running) {
            executor.submit(this);
            return;
        }
        switch (operationNumber.getAndIncrement()) {
            case 0:
                getAvailables();
                break;
            case 1:
                makeBookings();
                break;
            default:
                operationNumber.set(0);
                executor.submit(this);
                break;
        }
    }

    private void makeBookings() throws RestClientException {
        if (lastAvailable != null) {
            for (long i : lastAvailable.getAvailable()) {
                String usage = RANDOM.nextBoolean() ? "foreign" : "domestic";
                asynchRestTemplate.getForEntity(
                        "http://localhost:" + port + "/book?id=" + RANDOM.nextInt(100) + "&time=" + (5 + RANDOM.nextInt(5)) + "&usage=" + usage + "&countries=hungary",
                        String.class).addCallback(MainTester.this);
            }
        }
        makeSchedule(1);
    }

    private void getAvailables() {
        try {
            asynchRestTemplate.getForEntity("http://localhost:" + port + "/available",
                    Availability.class).addCallback(new ListenableFutureCallback<ResponseEntity<Availability>>() {
                        @Override
                        public void onSuccess(ResponseEntity<Availability> t) {
                            lastAvailable = t.getBody();
                            makeSchedule(1);
                        }

                        @Override
                        public void onFailure(Throwable thrwbl) {
                            System.out.println(thrwbl.getMessage());
                            makeSchedule(1);
                        }
                    });
        } catch (Throwable t) {
            System.out.println(t.getMessage());
            makeSchedule(1);
        }
    }

    private void makeSchedule(int seconds) {
        executor.schedule(this, seconds, TimeUnit.SECONDS);
    }

    @Override
    public void onSuccess(ResponseEntity<String> t) {
        System.out.println(t.getBody());
    }

    @Override
    public void onFailure(Throwable thrwbl) {
        System.out.println(thrwbl.getMessage());
    }
}
