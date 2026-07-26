import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Train {
    private int trainNumber;
    private String trainName;
    private String source;
    private String destination;
    private int availableSeats;
    private double ticketPrice;

    public Train(int trainNumber, String trainName, String source, String destination, int totalSeats, double ticketPrice) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.availableSeats = totalSeats;
        this.ticketPrice = ticketPrice;
    }

    public int getTrainNumber() { return trainNumber; }
    public String getTrainName() { return trainName; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public int getAvailableSeats() { return availableSeats; }
    public double getTicketPrice() { return ticketPrice; }

    public boolean bookSeats(int count) {
        if (count <= availableSeats) {
            availableSeats -= count;
            return true;
        }
        return false;
    }

    public void cancelSeats(int count) {
        availableSeats += count;
    }

    public void displayTrainDetails() {
        System.out.printf("%-10d %-20s %-15s %-15s %-10d $%.2f%n",
                trainNumber, trainName, source, destination, availableSeats, ticketPrice);
    }
}

class Ticket {
    private static int idCounter = 1001;
    private int bookingId;
    private String passengerName;
    private Train train;
    private int seatsBooked;
    private double totalFare;

    public Ticket(String passengerName, Train train, int seatsBooked) {
        this.bookingId = idCounter++;
        this.passengerName = passengerName;
        this.train = train;
        this.seatsBooked = seatsBooked;
        this.totalFare = seatsBooked * train.getTicketPrice();
    }

    public int getBookingId() { return bookingId; }
    public Train getTrain() { return train; }
    public int getSeatsBooked() { return seatsBooked; }

    public void printTicket() {
        System.out.println("\n================ TICKET CONFIRMATION ================");
        System.out.println("Booking ID     : " + bookingId);
        System.out.println("Passenger Name : " + passengerName);
        System.out.println("Train Name     : " + train.getTrainName() + " (#" + train.getTrainNumber() + ")");
        System.out.println("Route          : " + train.getSource() + " -> " + train.getDestination());
        System.out.println("Seats Booked   : " + seatsBooked);
        System.out.println("Total Fare     : $" + totalFare);
        System.out.println("=====================================================\n");
    }
}

public class TrainBookingApp {
    private static List<Train> trains = new ArrayList<>();
    private static List<Ticket> tickets = new ArrayList<>();

    public static void main(String[] args) {
        initializeTrains();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("================================================");
        System.out.println("    WELCOME TO TRAIN TICKET BOOKING SYSTEM      ");
        System.out.println("================================================");

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. View Available Trains");
            System.out.println("2. Book a Ticket");
            System.out.println("3. View My Ticket");
            System.out.println("4. Cancel a Ticket");
            System.out.println("5. Exit");
            System.out.print("Select an option (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear newline buffer

            switch (choice) {
                case 1:
                    viewTrains();
                    break;
                case 2:
                    bookTicket(scanner);
                    break;
                case 3:
                    viewTicket(scanner);
                    break;
                case 4:
                    cancelTicket(scanner);
                    break;
                case 5:
                    running = false;
                    System.out.println("\nThank you for using the booking system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice! Please select between 1 and 5.");
            }
        }
        scanner.close();
    }

    private static void initializeTrains() {
        trains.add(new Train(101, "Express Bullet", "Central", "Northside", 50, 45.00));
        trains.add(new Train(102, "Superfast Flyer", "West End", "East Coast", 30, 60.00));
        trains.add(new Train(103, "Night Coastal", "Central", "South Bay", 20, 75.00));
    }

    private static void viewTrains() {
        System.out.println("\n%-10s %-20s %-15s %-15s %-10s %-10s".formatted("Train No", "Train Name", "Source", "Destination", "Available", "Price"));
        System.out.println("-------------------------------------------------------------------------------");
        for (Train train : trains) {
            train.displayTrainDetails();
        }
    }

    private static void bookTicket(Scanner scanner) {
        viewTrains();
        System.out.print("\nEnter Train Number to book: ");
        int trainNo = scanner.nextInt();
        scanner.nextLine();

        Train selectedTrain = null;
        for (Train t : trains) {
            if (t.getTrainNumber() == trainNo) {
                selectedTrain = t;
                break;
            }
        }

        if (selectedTrain == null) {
            System.out.println("Train with ID " + trainNo + " not found!");
            return;
        }

        System.out.print("Enter Passenger Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Number of Seats: ");
        int seats = scanner.nextInt();

        if (selectedTrain.bookSeats(seats)) {
            Ticket ticket = new Ticket(name, selectedTrain, seats);
            tickets.add(ticket);
            System.out.println("\nBooking Successful!");
            ticket.printTicket();
        } else {
            System.out.println("\nBooking Failed! Not enough seats available.");
        }
    }

    private static void viewTicket(Scanner scanner) {
        System.out.print("\nEnter Booking ID: ");
        int bookingId = scanner.nextInt();

        for (Ticket ticket : tickets) {
            if (ticket.getBookingId() == bookingId) {
                ticket.printTicket();
                return;
            }
        }
        System.out.println("No booking found with ID: " + bookingId);
    }

    private static void cancelTicket(Scanner scanner) {
        System.out.print("\nEnter Booking ID to cancel: ");
        int bookingId = scanner.nextInt();

        Ticket foundTicket = null;
        for (Ticket ticket : tickets) {
            if (ticket.getBookingId() == bookingId) {
                foundTicket = ticket;
                break;
            }
        }

        if (foundTicket != null) {
            foundTicket.getTrain().cancelSeats(foundTicket.getSeatsBooked());
            tickets.remove(foundTicket);
            System.out.println("Ticket with Booking ID " + bookingId + " has been successfully cancelled.");
        } else {
            System.out.println("No booking found with ID: " + bookingId);
        }
    }
}