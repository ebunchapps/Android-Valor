package com.awrtechnologies.valor.valorfireplace.Data;

import java.io.Serializable;

/**
 * Created by m004 on 07/09/15.
 */
public class SelectedAddonData implements Serializable {

    String AddonCategoryId;
    String AddonsID;

    public String getAddonCategoryId() {
        return AddonCategoryId;
    }

    public void setAddonCategoryId(String addonCategoryId) {
        AddonCategoryId = addonCategoryId;
    }

    public String getAddonsID() {
        return AddonsID;
    }

    public void setAddonsID(String addonsID) {
        AddonsID = addonsID;
    }
}
