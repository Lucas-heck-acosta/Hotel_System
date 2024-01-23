package application;

import java.time.LocalDate;

public class ListInfo
{

    private int id;
    private LocalDate date;
    private int numDays;
    private String clientName;
    private int numRooms;

    public ListInfo(int id, LocalDate date, int numDays, String clientName, int numRooms) {
        this.id = id;
        this.date = date;
        this.numDays = numDays;
        this.clientName = clientName;
        this.numRooms = numRooms;
    }

    // Getter methods for the fields
    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getNumDays() {
        return numDays;
    }

    public String getClientName() {
        return clientName;
    }

    public int getNumRooms() {
        return numRooms;
    }
}
