package com.sofiane.envol.ownersquarehangout.Entities;

/**
 * Created by HP-450G3 on 20/06/2016.
 */
public class BookingForOwner {

   private String date_book;
    private String id_book;
    private String idPlace;
    private String resto_book;
    private String seats;
    private String time_book;
    private String name_user ;

    private String deleted ;
    private String info ;

    @SuppressWarnings("unused")
    public BookingForOwner() {

    }

    public BookingForOwner(String date_book, String id_book, String resto_book, String seats, String time_book, String name_user, String deleted, String info) {
        this.date_book = date_book;
        this.id_book = id_book;
        this.resto_book = resto_book;
        this.seats = seats;
        this.time_book = time_book;
        this.name_user = name_user;
        this.deleted = deleted;
        this.info = info;
    }

    public String getDate_book() {
        return date_book;
    }

    public void setDate_book(String date_book) {
        this.date_book = date_book;
    }

    public String getId_book() {
        return id_book;
    }

    public void setId_book(String id_book) {
        this.id_book = id_book;
    }

    public String getResto_book() {
        return resto_book;
    }

    public void setResto_book(String resto_book) {
        this.resto_book = resto_book;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getTime_book() {
        return time_book;
    }

    public void setTime_book(String time_book) {
        this.time_book = time_book;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(String idPlace) {
        this.idPlace = idPlace;
    }
}