package com.awrtechnologies.valor.valorfireplace.Data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by m004 on 09/06/15.
 */

@Table(name = "DealerInfo")
public class DealerInfo extends Model {

    @Column(name = "name")
    public String name;

    @Column(name = "email")
    public String email;

    @Column(name = "phone")
    public String phone;

    @Column(name = "address")
    public String address;

    @Column(name = "image")
    public String image;


    public static DealerInfo dealerinfo() {
        return new Select().from(DealerInfo.class)
                .executeSingle();
    }

}
