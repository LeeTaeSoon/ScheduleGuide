package com.example.nrst1.scheduleguide;

/**
 * Created by nrst1 on 2017-05-30.
 */

public class Attandance {
    private String name;
    private String phoneNumber;

    Attandance() {}
    Attandance(String name, String phoneNumber) { this.name = name; this.phoneNumber = phoneNumber; }

    void setName(String name) { this.name = name; }
    void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    String getName() { return this.name; }
    String getPhoneNumber() { return this.phoneNumber; }
}
