package com.DVLA.testapp.app;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by Edem on 16-Aug-17.
 */

public class HttpRequest extends AsyncTask<Object, Void, vehRecord> {
    vehRecView vehView;

    @Override
    protected vehRecord doInBackground(Object... params) {
        this.vehView = (vehRecView) params[0];

        if (this.vehView.Param.length() == 0) {
            vehRecord result = new vehRecord();
            result.LicensePlate = "Not Found";
            return result;
        }

        BufferedReader inBuffer = null;
        String url = "https://opendata.rdw.nl/resource/uxre-t94a.json?kenteken=" + this.vehView.Param;
        vehRecord result;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(request);
            inBuffer = new BufferedReader(
                    new InputStreamReader(
                            httpResponse.getEntity().getContent()));
            StringBuffer stringBuffer = new StringBuffer("");
            String line = "";
            String newLine = System.getProperty("line.separator");
            while ((line = inBuffer.readLine()) != null) {
                stringBuffer.append(line + newLine);
            }
            inBuffer.close();

            JSONArray jsonArray = new JSONArray(stringBuffer.toString());

            Log.i("Test", jsonArray.toString());
            result = getVehicle(jsonArray);

        } catch (Exception e) {
            Log.i("Err", e.getMessage());
            result = new vehRecord();
        } finally {
            if (inBuffer != null) {
                try {
                    inBuffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    protected void onPostExecute(vehRecord vehicle) {
        vehView.LicensePlate.setText(vehicle.getLicensePlate());
        vehView.ApkInvalidDate.setText(DateTimeFormat.forPattern("dd/MM/YYYY").print(vehicle.getApkInvalidDate()));
        vehView.ValidApk.setText(vehicle.isValidApk());
        vehView.LoadingFrame.setVisibility(View.GONE);
        try {
            if (!vehicle.getValidApk()) {
                if (!VehRecRepo.vehRecordList.isEmpty()) {
                    for (int i = 0; VehRecRepo.vehRecordList.size() > i; i++) {
                        if (VehRecRepo.vehRecordList.get(i).getLicensePlate().equals(vehicle.getLicensePlate())) {

                            break;
                        } else {

                            VehRecRepo.vehRecordList.add(vehicle);

                        }
                    }

                }
                else {
                    VehRecRepo.vehRecordList.add(vehicle);
                }
            }
        } catch (Exception e) {
            Log.i("Error", e.getMessage());
        }

    }

    public vehRecord getVehicle(JSONArray json) {
        try {
            vehRecord vehicle = new vehRecord();
            vehicle.LicensePlate = json.getJSONObject(0).get("kenteken").toString();
            String date = json.getJSONObject(0).get("vervaldatum_keuring").toString();
            DateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
            DateFormat outputFormat = new SimpleDateFormat("dd/MM/YYYY");
            DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/YYYY");
            Date newDate = inputFormat.parse(date);
            String outputText = outputFormat.format(newDate);
            vehicle.ApkInvalidDate = dtf.parseDateTime(outputText);
            return vehicle;

        } catch (Exception e) {
            Log.i("Error", e.getMessage());
        }
        return new vehRecord();
    }
}
