package com.DVLA.testapp.app;

import android.support.annotation.BoolRes;
import android.util.Log;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Edem on 07-Aug-17.
 */
public class vehRecord {
    public String LicensePlate;
    public DateTime ApkInvalidDate;

    public String getLicensePlate(){
        return LicensePlate;
    }

    public DateTime getApkInvalidDate(){
        return ApkInvalidDate;
    }

    public String isValidApk() {
        if(getApkInvalidDate() == null) {return "Niet gevonden";}
        DateTime now = new DateTime().withTimeAtStartOfDay();
        if(getApkInvalidDate().isAfter(now)) {
            return "Nog Geldig";
        } else {
            return "Verlopen";
        }
    }

    public boolean getValidApk(){
        if(getApkInvalidDate() == null) {return false;}
        DateTime now = new DateTime().withTimeAtStartOfDay();
        if(getApkInvalidDate().isAfter(now)) {
            return true;
        }
        else{
            return false;
        }
    }


}

