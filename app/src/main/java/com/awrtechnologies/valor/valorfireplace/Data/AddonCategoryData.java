package com.awrtechnologies.valor.valorfireplace.Data;

import java.io.Serializable;

/**
 * Created by m004 on 10/09/15.
 */
public class AddonCategoryData implements Serializable {
    String addonstitle;
    String addonsdesc;
    int addonsprice;

    public String getAddonstitle() {
        return addonstitle;
    }

    public void setAddonstitle(String addonstitle) {
        this.addonstitle = addonstitle;
    }

    public String getAddonsdesc() {
        return addonsdesc;
    }

    public void setAddonsdesc(String addonsdesc) {
        this.addonsdesc = addonsdesc;
    }

    public int getAddonsprice() {
        return addonsprice;
    }

    public void setAddonsprice(int addonsprice) {
        this.addonsprice = addonsprice;
    }
}
