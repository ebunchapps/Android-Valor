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
@Table(name = "Series")
public class Series extends Model implements Serializable{



    @Column(name = "seriesid")
    public String seriesid;

    @Column(name = "title")
    public String title;

    @Column(name = "price")
    public int price;

    @Column(name = "description")
    public String description;

    @Column(name = "qty")
    public String qty;

    public @Column(name = "bannerImage")
    String bannerImage;

    @Column(name = "thumb_bannerImage")
    public String thumb_bannerImage;

    @Column(name = "actualImage")
    public String actualImage;

    @Column(name = "thumb_actualImage")
    public String thumb_actualImage;

    public String getBrochure() {
        return brochure;
    }

    public void setBrochure(String brochure) {
        this.brochure = brochure;
    }

    public String getManual() {
        return manual;
    }

    public void setManual(String manual) {
        this.manual = manual;
    }

    @Column(name = "brochure")
    public String brochure;

    @Column(name = "manual")
    public String manual;

    @Column(name = "applicationId")
    public String applicationId;


    @Column(name = "application", onDelete = Column.ForeignKeyAction.CASCADE)
    public Application Application;


    public List<Addons> addons() {
        return getMany(Addons.class, "series");
    }

    public static List<Series> getAll() {
        return new Select().from(Series.class).execute();
    }


    public static void deleteAll() {
        new Delete().from(Series.class).execute();
    }

    public static List<Series> getAllByappId(String applicationId) {
        return new Select().from(Series.class).where("applicationId=?", applicationId)
                .execute();
    }

    public static List<Series> getAllBySeries() {
        return new Select().from(Series.class)
                .execute();
    }

    public static Series getAllByApplicationId(String applicationId) {
        return new Select().from(Series.class).where("applicationId=?", applicationId)
                .executeSingle();
    }

    public static Series getSeriesbySeriesId(String seriesid) {
        return new Select().from(Series.class).where("seriesid=?", seriesid)
                .executeSingle();
    }

}
