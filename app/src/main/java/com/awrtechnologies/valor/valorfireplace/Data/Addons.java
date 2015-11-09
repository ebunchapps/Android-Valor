package com.awrtechnologies.valor.valorfireplace.Data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.List;

/**
 * Created by awr005 on 06/06/15.
 */

@Table(name = "Addons")
public class Addons extends Model implements Serializable{

    @Column(name = "addonsid")
    public String addonsid;

    @Column(name = "title")
    public String title;

    @Column(name = "description")
    public String description;

    @Column(name = "price")
    public int price;

    @Column(name = "qty")
    public  String qty;

    @Column(name = "image")
    public String image;

    @Column(name = "banner_image")
    public String banner_image;

    @Column(name = "category_id")
    public String category_id;



    @Column(name = "stock")
    public String stock;


    @Column(name = "ref_id")
    public String ref_id;

    @Column(name = "series", onDelete = Column.ForeignKeyAction.CASCADE)
    public Series Series;

    public static List<Addons> getAll() {
        return new Select().from(Addons.class).execute();
    }

    public static void deleteAll() {
        new Delete().from(Addons.class).execute();
    }


    public static List<Addons> getAllBycategoryId(String category_id) {
        return new Select().from(Addons.class).where("category_id=?", category_id)
                .execute();
    }

    public static Addons getAddonsBycategoryId(String category_id) {
        return new Select().from(Addons.class).where("category_id=?", category_id)
                .executeSingle();
    }


    public static Addons getAddonsByAddonsId(String addonsid) {
        return new Select().from(Addons.class).where("addonsid=?", addonsid)
                .executeSingle();
    }
}
