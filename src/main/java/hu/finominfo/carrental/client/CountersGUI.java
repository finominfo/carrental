package hu.finominfo.carrental.client;

import hu.finominfo.carrental.Application;
import hu.finominfo.carrental.services.Book;
import hu.finominfo.carrental.services.External;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CountersGUI extends JPanel {

    private static final String START_TEXT = "Start auto testing";
    private static final String STOP_TEXT = "Stop auto testing";
    private final Lock lock = new ReentrantLock();
    private final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);
    private final JLabel empty1;
    private final JLabel empty2;
    private final JLabel empty3;
    private final JLabel empty4;
    private final JLabel empty5;
    private final JLabel empty6;

    private final JLabel maxParallelBookingRequests;
    private final JLabel maxParallelBookingRequestsValue;
    private final JLabel bookingRequests;
    private final JLabel bookingRequestsValue;
    private final JLabel bookingRequestsInProgress;
    private final JLabel bookingRequestsInProgressValue;
    private final JLabel bookingRequestsFailedBecauseOfOverload;
    private final JLabel bookingRequestsFailedBecauseOfOverloadValue;
    private final JLabel bookingRequestsFailedBecauseOfSyntax;
    private final JLabel bookingRequestsFailedBecauseOfSyntaxValue;
    private final JLabel bookingRequestsFailedBecauseOfForeign;
    private final JLabel bookingRequestsFailedBecauseOfForeignValue;
    private final JLabel bookingRequestsFailedBecauseOfAlreadyBooked;
    private final JLabel bookingRequestsFailedBecauseOfAlreadyBookedValue;
    private final JLabel bookingSuccess;
    private final JLabel bookingSuccessValue;

    private final JLabel maxParallelForeignRequests;
    private final JLabel maxParallelForeignRequestsValue;
    private final JLabel foreignRequests;
    private final JLabel foreignRequestsValue;
    private final JLabel foreignRequestsInProgress;
    private final JLabel foreignRequestsInProgressValue;
    private final JLabel foreignRequestsFailedBecauseOfOverload;
    private final JLabel foreignRequestsFailedBecauseOfOverloadValue;
    private final JLabel foreignRequestsFailedBecauseOfSyntax;
    private final JLabel foreignRequestsFailedBecauseOfSyntaxValue;
    private final JLabel foreignRequestsFailedBecauseOfForeign;
    private final JLabel foreignRequestsFailedBecauseOfForeignValue;
    private final JLabel foreignSuccess;
    private final JLabel foreignSuccessValue;
    private final JButton stopStart;

    public CountersGUI(LayoutManager layout) {
        super(layout);
        empty1 = new JLabel();
        empty2 = new JLabel();
        empty3 = new JLabel();
        empty4 = new JLabel();
        empty5 = new JLabel();
        empty6 = new JLabel();

        maxParallelBookingRequests = new JLabel("Max Parallel Booking Requests");
        maxParallelBookingRequestsValue = new JLabel(String.valueOf(Book.MAX_PARALLEL_BOOKING_REQUESTS));
        bookingRequests = new JLabel("Booking Requests");
        bookingRequestsValue = new JLabel();
        bookingRequestsInProgress = new JLabel("Booking Requests In Progress");
        bookingRequestsInProgressValue = new JLabel();
        bookingRequestsFailedBecauseOfOverload = new JLabel("Booking Requests Failed - Server Overload");
        bookingRequestsFailedBecauseOfOverloadValue = new JLabel();
        bookingRequestsFailedBecauseOfSyntax = new JLabel("Booking Requests Failed - Syntax Error");
        bookingRequestsFailedBecauseOfSyntaxValue = new JLabel();
        bookingRequestsFailedBecauseOfForeign = new JLabel("Booking Requests Failed - Foreign Disabled");
        bookingRequestsFailedBecauseOfForeignValue = new JLabel();
        bookingRequestsFailedBecauseOfAlreadyBooked = new JLabel("Booking Requests Failed - Already Booked");
        bookingRequestsFailedBecauseOfAlreadyBookedValue = new JLabel();
        bookingSuccess = new JLabel("Booking Success");
        bookingSuccessValue = new JLabel();

        maxParallelForeignRequests = new JLabel("Max Parallel Foreign Requests");
        maxParallelForeignRequestsValue = new JLabel(String.valueOf(External.MAX_PARALLEL_FOREIGN_REQUESTS));
        foreignRequests = new JLabel("Foreign Requests");
        foreignRequestsValue = new JLabel();
        foreignRequestsInProgress = new JLabel("Foreign Requests In Progress");
        foreignRequestsInProgressValue = new JLabel();
        foreignRequestsFailedBecauseOfOverload = new JLabel("Foreign Requests Failed - Server Overload");
        foreignRequestsFailedBecauseOfOverloadValue = new JLabel();
        foreignRequestsFailedBecauseOfSyntax = new JLabel("Foreign Requests Failed - Syntax Error");
        foreignRequestsFailedBecauseOfSyntaxValue = new JLabel();
        foreignRequestsFailedBecauseOfForeign = new JLabel("Foreign Requests Failed - Foreign Disabled");
        foreignRequestsFailedBecauseOfForeignValue = new JLabel();
        foreignSuccess = new JLabel("Foreign Success");
        foreignSuccessValue = new JLabel();

        stopStart = new JButton(STOP_TEXT);
        stopStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    lock.lock();
                    if (stopStart.getText().equals(STOP_TEXT)) {
                        MainTester.stopAutoTesting();
                        stopStart.setText(START_TEXT);
                    } else {
                        MainTester.startAutoTesting();
                        stopStart.setText(STOP_TEXT);
                    }
                } finally {
                    lock.unlock();
                }
            }
        });

    }

    public void init() {
        add(maxParallelBookingRequests);
        add(maxParallelBookingRequestsValue);
        add(bookingRequests);
        add(bookingRequestsValue);
        add(bookingRequestsInProgress);
        add(bookingRequestsInProgressValue);
        add(bookingRequestsFailedBecauseOfOverload);
        add(bookingRequestsFailedBecauseOfOverloadValue);
        add(bookingRequestsFailedBecauseOfSyntax);
        add(bookingRequestsFailedBecauseOfSyntaxValue);
        add(bookingRequestsFailedBecauseOfForeign);
        add(bookingRequestsFailedBecauseOfForeignValue);
        add(bookingRequestsFailedBecauseOfAlreadyBooked);
        add(bookingRequestsFailedBecauseOfAlreadyBookedValue);
        add(bookingSuccess);
        add(bookingSuccessValue);
        add(empty1);
        add(empty2);

        add(maxParallelForeignRequests);
        add(maxParallelForeignRequestsValue);
        add(foreignRequests);
        add(foreignRequestsValue);
        add(foreignRequestsInProgress);
        add(foreignRequestsInProgressValue);
        add(foreignRequestsFailedBecauseOfOverload);
        add(foreignRequestsFailedBecauseOfOverloadValue);
        add(foreignRequestsFailedBecauseOfSyntax);
        add(foreignRequestsFailedBecauseOfSyntaxValue);
        add(foreignRequestsFailedBecauseOfForeign);
        add(foreignRequestsFailedBecauseOfForeignValue);
        add(foreignSuccess);
        add(foreignSuccessValue);
        add(empty3);
        add(empty4);

        add(stopStart);

        executor.submit(new Runnable() {
            @Override
            public void run() {
                if (Application.runningApp == null || Application.runningApp.isActive()) {
                    refreshLabels();
                    executor.schedule(this, 1, TimeUnit.SECONDS);
                }
            }
        });
    }

    private void refreshLabels() {
        bookingRequestsValue.setText(String.valueOf(Book.BOOKING_REQUESTS.get()));
        bookingRequestsInProgressValue.setText(String.valueOf(Book.BOOKING_REQUESTS_IN_PROGRESS.get()));
        bookingRequestsFailedBecauseOfOverloadValue.setText(String.valueOf(Book.BOOKING_FAILED_BECAUSE_OF_OVERLOAD.get()));
        bookingRequestsFailedBecauseOfSyntaxValue.setText(String.valueOf(Book.BOOKING_FAILED_BECAUSE_OF_SYNTAX.get()));
        bookingRequestsFailedBecauseOfForeignValue.setText(String.valueOf(Book.BOOKING_FAILED_BECAUSE_OF_FOREIGN.get()));
        bookingRequestsFailedBecauseOfAlreadyBookedValue.setText(String.valueOf(Book.BOOKING_FAILED_BECAUSE_OF_ALREADY_BOOKED.get()));
        bookingSuccessValue.setText(String.valueOf(Book.BOOKING_SUCCESS.get()));

        foreignRequestsValue.setText(String.valueOf(External.FOREIGN_REQUESTS.get()));
        foreignRequestsInProgressValue.setText(String.valueOf(External.FOREIGN_REQUESTS_IN_PROGRESS.get()));
        foreignRequestsFailedBecauseOfOverloadValue.setText(String.valueOf(External.FOREIGN_FAILED_BECAUSE_OF_OVERLOAD.get()));
        foreignRequestsFailedBecauseOfSyntaxValue.setText(String.valueOf(External.FOREIGN_FAILED_BECAUSE_OF_SYNTAX.get()));
        foreignRequestsFailedBecauseOfForeignValue.setText(String.valueOf(External.FOREIGN_FAILED_BECAUSE_OF_FOREIGN.get()));
        foreignSuccessValue.setText(String.valueOf(External.FOREIGN_SUCCESS.get()));
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Car rental counters");
        final CountersGUI gui = new CountersGUI(new GridLayout(0, 2, 15, 5));
        gui.init();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Closing...");
                Application.runningApp.close();
                e.getWindow().dispose();
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(gui);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
