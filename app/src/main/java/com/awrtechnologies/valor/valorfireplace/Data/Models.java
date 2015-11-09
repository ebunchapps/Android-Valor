package com.awrtechnologies.valor.valorfireplace.Data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by awr001 on 02/09/15.
 */
@Table(name = "Models")
public class Models extends Model {

    @Column(name = "modelName")
    public String modelName;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }


}
