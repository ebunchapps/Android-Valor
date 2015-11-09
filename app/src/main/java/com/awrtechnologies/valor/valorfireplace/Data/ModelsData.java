package com.awrtechnologies.valor.valorfireplace.Data;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by awr001 on 05/09/15.
 */
@Table(name = "ModelsData")
public class ModelsData  extends Models {


    @Column(name = "series_id")
    public String series_id;

    @Column(name = "description")
    public String description;

    @Column(name = "image")
    public String image;

    @Column(name = "banner_image")
    public String banner_image;

    @Column(name = "title")
    public String title;

    @Column(name = "stock")
    public int stock;


    @Column(name = "price")
    public int price;

    @Column(name = "ref_id")
    public String ref_id;

    @Column(name = "modelId")
    public String modelId;




    public static ModelsData getAllByApplicationId(String series_id) {
        return new Select().from(ModelsData.class).where("series_id=?", series_id)
                .executeSingle();
    }

    public static List<ModelsData> getAllBySeriesId(String series_id) {
        return new Select().from(ModelsData.class).where("series_id=?", series_id)
                .execute();
    }

    public static ModelsData getModelByModelID(String modelId) {
        return new Select().from(ModelsData.class).where("modelId=?", modelId)
                .executeSingle();
    }
    public static ModelsData getModelStock() {
        return new Select().from(ModelsData.class).executeSingle();
    }
    public static void deleteAll() {
        new Delete().from(ModelsData.class).execute();
    }

}