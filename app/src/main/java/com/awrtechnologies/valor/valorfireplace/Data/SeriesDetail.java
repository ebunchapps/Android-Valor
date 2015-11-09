package com.awrtechnologies.valor.valorfireplace.Data;

import java.io.Serializable;

/**
 * Created by m004 on 08/06/15.
 */
public class SeriesDetail implements Serializable {

    String applicationdesc;
    String seriestitle;
    String seriesdesc;
    int seriesprice;
    String background;
    String addonstitle;
    String addonsdesc;
    int addonsprice;
    boolean imagecheck;
    String modeltitle;
    String modeldesc;
    int modelstock;
    int modelprice;
    boolean addonscheck;
    public int type=0;
    String categoryname;
    String categoryID;
    String addonId;

    public String getAddonId() {
        return addonId;
    }

    public void setAddonId(String addonId) {
        this.addonId = addonId;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isAddonscheck() {
        return addonscheck;
    }

    public void setAddonscheck(boolean addonscheck) {
        this.addonscheck = addonscheck;
    }

    public boolean isImagecheck() {
        return imagecheck;
    }

    public void setImagecheck(boolean imagecheck) {
        this.imagecheck = imagecheck;
    }

    public String getApplicationdesc() {
        return applicationdesc;
    }

    public void setApplicationdesc(String applicationdesc) {
        this.applicationdesc = applicationdesc;
    }


    public String getSeriesdesc() {
        return seriesdesc;
    }

    public void setSeriesdesc(String seriesdesc) {
        this.seriesdesc = seriesdesc;
    }

    public String getSeriestitle() {
        return seriestitle;
    }

    public void setSeriestitle(String seriestitle) {
        this.seriestitle = seriestitle;
    }


    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

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

    public int getSeriesprice() {
        return seriesprice;
    }

    public void setSeriesprice(int seriesprice) {
        this.seriesprice = seriesprice;
    }

    public int getAddonsprice() {
        return addonsprice;
    }

    public void setAddonsprice(int addonsprice) {
        this.addonsprice = addonsprice;
    }


    public String getModeltitle() {
        return modeltitle;
    }

    public void setModeltitle(String modeltitle) {
        this.modeltitle = modeltitle;
    }

    public String getModeldesc() {
        return modeldesc;
    }

    public void setModeldesc(String modeldesc) {
        this.modeldesc = modeldesc;
    }

    public int getModelstock() {
        return modelstock;
    }

    public void setModelstock(int modelstock) {
        this.modelstock = modelstock;
    }

    public int getModelprice() {
        return modelprice;
    }

    public void setModelprice(int modelprice) {
        this.modelprice = modelprice;
    }

}
