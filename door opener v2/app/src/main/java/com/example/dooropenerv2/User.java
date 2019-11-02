package com.example.dooropenerv2;

import java.util.ArrayList;

public class User {
    private String name;
    private ArrayList<String> ownedRooms;
    private ArrayList<String>tempRooms;

    public User(String name, ArrayList<String> ownedRooms,ArrayList<String> tempRooms) {
        this.name = name;
        this.ownedRooms = ownedRooms;
        this.tempRooms = tempRooms;
    }

    public void lendRoom(String roomName, User borrower) {
        if (this.ownedRooms.contains(roomName)) {
            borrower.tempRooms.add(roomName);
        }
    }

    public void revokeRoom(String roomName, User borrower) {
        if (this.ownedRooms.contains(roomName) && borrower.tempRooms.contains(roomName)) {
            borrower.tempRooms.remove(roomName);
        }
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public ArrayList<String> setOwnedRooms(){
        return this.ownedRooms;
    }

    public void getOwnedRooms(ArrayList<String> tempRooms){
        this.ownedRooms = ownedRooms;
    }

    public ArrayList<String> setTempRooms(){
        return this.tempRooms;
    }

    public void getTempRooms(ArrayList<String> tempRooms){
        this.tempRooms = tempRooms;
    }
}