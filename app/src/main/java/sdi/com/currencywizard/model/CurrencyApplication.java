package sdi.com.currencywizard.model;

import android.app.Application;

import java.util.List;

/**
 * Created by twilightuser on 2/8/16.
 */
public class CurrencyApplication extends Application {

    String camera_currency;



    List<ScanResultList> scanList;

    public String getCamera_currency() {
        return camera_currency;
    }

    public void setCamera_currency(String camera_currency) {
        this.camera_currency = camera_currency;
    }

    public List<ScanResultList> getScanList() {
        return scanList;
    }

    public void setScanList(List<ScanResultList> scanList) {
        this.scanList = scanList;
    }
}
