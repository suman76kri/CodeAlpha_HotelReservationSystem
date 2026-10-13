import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Room {
    private int roomNumber;
    private String category;
    private double pricePerNight;
    private boolean isAvailable;

    public Room(int roomNumber, String category, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getCategory() {
        return category;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        String status = isAvailable ? "AVAILABLE" : "BOOKED";
        return String.format("| Room %-4d | %-12s | Rs. %-8.2f | %-10s |", roomNumber, category, pricePerNight, status);
    }
}

class Booking {
    private static int idCounter = 101;
    private int bookingId;
    private String guestName;
    private Room room;
    private int nights;
    private double totalAmount;

    public Booking(String guestName, Room room, int nights) {
        this.bookingId = idCounter++;
        this.guestName = guestName;
        this.room = room;
        this.nights = nights;
        this.totalAmount = room.getPricePerNight() * nights;
        this.room.setAvailable(false);
    }

    public int getBookingId() {
        return bookingId;
    }

    public Room getRoom() {
        return room;
    }

    public void displayReceipt() {
        System.out.println("----------------------------------------------");
        System.out.println("              BOOKING RECEIPT                 ");
        System.out.println("----------------------------------------------");
        System.out.println("Booking ID     : " + bookingId);
        System.out.println("Guest Name     : " + guestName);
        System.out.println("Room Number    : " + room.getRoomNumber() + " (" + room.getCategory() + ")");
        System.out.println("Duration       : " + nights + " Night(s)");
        System.out.printf("Total Paid     : Rs. %.2f\n", totalAmount);
        System.out.println("Status         : Confirmed & Paid");
        System.out.println("----------------------------------------------");
    }
}

public class HotelReservationSystem {
    private static List<Room> rooms = new ArrayList<>();
    private static List<Booking> bookings = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeRooms();

        while (true) {
            System.out.println("\n==============================================");
            System.out.println("        CODEALPHA HOTEL RESERVATION           ");
            System.out.println("==============================================");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel a Reservation");
            System.out.println("4. View All Bookings");
            System.out.println("5. Exit");
            System.out.print("Select an option (1-5): ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                continue;
            }

            switch (choice) {
                case 1:
                    displayAvailableRooms();
                    break;
                case 2:
                    bookRoom();
                    break;
                case 3:
                    cancelBooking();
                    break;
                case 4:
                    viewAllBookings();
                    break;
                case 5:
                    System.out.println("Thank you for using CodeAlpha Hotel Reservation System!");
                    return;
                default:
                    System.out.println("Invalid choice. Please select an option between 1 and 5.");
            }
        }
    }

    private static void initializeRooms() {
        rooms.add(new Room(101, "Standard", 1500.0));
        rooms.add(new Room(102, "Standard", 1500.0));
        rooms.add(new Room(201, "Deluxe", 2500.0));
        rooms.add(new Room(202, "Deluxe", 2500.0));
        rooms.add(new Room(301, "Suite", 4500.0));
    }

    private static void displayAvailableRooms() {
        System.out.println("\n----------------- AVAILABLE ROOMS -----------------");
        System.out.println("+-----------+--------------+--------------+------------+");
        System.out.println("| Room No   | Category     | Price/Night  | Status     |");
        System.out.println("+-----------+--------------+--------------+------------+");
        boolean anyAvailable = false;
        for (Room r : rooms) {
            if (r.isAvailable()) {
                System.out.println(r);
                anyAvailable = true;
            }
        }
        System.out.println("+-----------+--------------+--------------+------------+");
        if (!anyAvailable) {
            System.out.println("No rooms currently available.");
        }
    }

    private static void bookRoom() {
        displayAvailableRooms();
        System.out.print("\nEnter Room Number to book: ");
        int roomNum;
        try {
            roomNum = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid room number format.");
            return;
        }

        Room selectedRoom = null;
        for (Room r : rooms) {
            if (r.getRoomNumber() == roomNum && r.isAvailable()) {
                selectedRoom = r;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("Error: Room not found or already occupied!");
            return;
        }

        System.out.print("Enter Guest Name: ");
        String guestName = scanner.nextLine().trim();
        if (guestName.isEmpty()) {
            System.out.println("Guest name cannot be empty.");
            return;
        }

        System.out.print("Enter Number of Nights: ");
        int nights;
        try {
            nights = Integer.parseInt(scanner.nextLine().trim());
            if (nights <= 0) {
                System.out.println("Nights must be at least 1.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number of nights.");
            return;
        }

        double total = selectedRoom.getPricePerNight() * nights;
        System.out.printf("Total Payable Amount: Rs. %.2f\n", total);
        System.out.print("Simulate Payment Process? (Y/N): ");
        String payConfirm = scanner.nextLine().trim();

        if (payConfirm.equalsIgnoreCase("Y")) {
            Booking booking = new Booking(guestName, selectedRoom, nights);
            bookings.add(booking);
            System.out.println("\nPayment Successful!");
            booking.displayReceipt();
        } else {
            System.out.println("Booking cancelled by user. Payment not completed.");
        }
    }

    private static void cancelBooking() {
        System.out.print("\nEnter Booking ID to cancel: ");
        int bookingId;
        try {
            bookingId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid booking ID format.");
            return;
        }

        Booking foundBooking = null;
        for (Booking b : bookings) {
            if (b.getBookingId() == bookingId) {
                foundBooking = b;
                break;
            }
        }

        if (foundBooking != null) {
            foundBooking.getRoom().setAvailable(true);
            bookings.remove(foundBooking);
            System.out.println("Booking ID " + bookingId + " has been successfully cancelled and refunded.");
        } else {
            System.out.println("Error: Booking ID not found.");
        }
    }

    private static void viewAllBookings() {
        if (bookings.isEmpty()) {
            System.out.println("\nNo active reservations currently.");
            return;
        }
        System.out.println("\n---------------- ACTIVE RESERVATIONS ----------------");
        for (Booking b : bookings) {
            b.displayReceipt();
        }
    }
}