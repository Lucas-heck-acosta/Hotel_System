package application;

public class Room
{

    int id;
    String type;
    double rate;

    boolean booked;

    Room(int id, String type, double rate, boolean booked)
    {
        setID(id);
        setType(type);
        setRate(rate);
        setBooked(booked);
    }

    void setBooked(boolean booked)
    {
        this.booked = booked;
    }
    public boolean getBooked()
    {
        return booked;
    }

    void setID(int id)
    {
        this.id = id;
    }
    public int getId()
    {
        return this.id;
    }

    void setType(String s)
    {
        this.type = s;
    }
    public String getType()
    {
        return this.type;
    }

    void setRate(double d)
    {
        this.rate = d;
    }
    public double getRate()
    {
        return this.rate;
    }
}
