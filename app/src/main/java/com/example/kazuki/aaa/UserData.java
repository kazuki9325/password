package com.example.kazuki.aaa;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kazuki on 2017/06/04.
 */

public class UserData extends RealmObject {

    private String address;
    private String password;
    @PrimaryKey
    private int id;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
