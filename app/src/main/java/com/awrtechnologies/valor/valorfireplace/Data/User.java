package com.awrtechnologies.valor.valorfireplace.Data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by m004 on 06/06/15.
 */

@Table(name="User")
public class User extends Model {


    @Column(name = "image")
    String image;

    @Column(name = "userid")
    String userid;

    @Column(name = "name")
    String name;

    @Column(name = "email")
    String email;

    @Column(name = "contact")
    String contact;

    @Column(name = "securityNo")
    String securityNo;

    @Column(name = "fees")
    String fees;

    @Column(name = "city")
    String city;

    @Column(name = "country")
    String country;

    @Column(name = "address")
    String address;

    @Column(name = "dealerId")
    String dealerId;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSecurityNo() {
        return securityNo;
    }

    public void setSecurityNo(String securityNo) {
        this.securityNo = securityNo;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static User getUser() {
        return new Select().from(User.class).executeSingle();
    }
}
