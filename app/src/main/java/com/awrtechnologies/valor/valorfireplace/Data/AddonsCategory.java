package com.awrtechnologies.valor.valorfireplace.Data;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by awr001 on 05/09/15.
 */

@Table(name = "AddonsCategory")

public class AddonsCategory  extends  Models{


    @Column(name = "addoncatid")
    public String addoncatid;


    @Column(name = "model_id")
    public String model_id;

    @Column(name = "name")
    public String name;



    public static AddonsCategory  getBymodelId(String model_id) {
        return new Select().from(AddonsCategory.class).where("model_id=?", model_id)
                .executeSingle();
    }

    public static  List<AddonsCategory> getAllBymodelId(String model_id) {
        return new Select().from(AddonsCategory.class).where("model_id=?", model_id)
                .execute();
    }


    public static AddonsCategory getCatByCatID(String addoncatid) {
        return new Select().from(AddonsCategory.class).where("addoncatid=?", addoncatid)
                .executeSingle();
    }

    public static void deleteAll() {
        new Delete().from(AddonsCategory.class).execute();
    }


}
