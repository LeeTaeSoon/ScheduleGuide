package com.example.nrst1.scheduleguide;

/**
 * Created by AhnJeongHyeon on 2017. 6. 9..
 */

public class KangNam {
    private String name;
    private String address;

    KangNam(String name, String address){
        this.name=name;
        this.address=address;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
