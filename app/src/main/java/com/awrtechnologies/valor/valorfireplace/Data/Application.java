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
@Table(name = "Application")
public class Application extends Model implements Serializable{

    @Column(name = "appid")
    public  String appid;

    @Column(name = "title")
    public String title;

    @Column(name = "description")
    public  String description;

    @Column(name = "image")
    public  String image;

    @Column(name = "banner_image")
    public String banner_image;



    public static List<Application> getAll() {
        return new Select().from(Application.class).execute();
    }

    public static void deleteAll() {
        new Delete().from(Application.class).execute();
    }

    public List<Series> series() {
        return getMany(Series.class, "application");
    }

    public static Application getAllByApplicationId(String appid) {
        return new Select().from(Application.class).where("appid=?", appid)
                .executeSingle();
    }
    public static Application getApplicationName() {
        return new Select().from(Application.class).executeSingle();
    }


}
