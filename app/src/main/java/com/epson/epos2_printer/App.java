package com.epson.epos2_printer;

import android.app.Application;
import android.util.Log;

import com.epson.epos2.discovery.DeviceInfo;
import com.epson.epos2.discovery.Discovery;
import com.epson.epos2.discovery.DiscoveryListener;
import com.epson.epos2.discovery.FilterOption;
import com.epson.epos2_printer.printer.ShowMsg;

/**
 * @author thisfeng
 * @date 2017/12/19-下午6:21
 */

public class App extends Application {

    private static String printerTarget = "";

    @Override
    public void onCreate() {
        super.onCreate();

        initDiscoveryDevice();
    }

    /**
     * 需要通过查找打印设备获取打印机 Target
     */
    private void initDiscoveryDevice() {
        FilterOption mFilterOption = new FilterOption();
        mFilterOption.setDeviceType(Discovery.TYPE_PRINTER);
        mFilterOption.setEpsonFilter(Discovery.FILTER_NAME);
        try {
            Discovery.start(this, mFilterOption, new DiscoveryListener() {
                @Override
                public void onDiscovery(DeviceInfo deviceInfo) {
//                    item.put("PrinterName", deviceInfo.getDeviceName());
//                    item.put("Target", deviceInfo.getTarget());
                    printerTarget = deviceInfo.getTarget();
                    int i = 0;
                    Log.d("target:", printerTarget + "第几次打印:" + i++);
                   /* try {
                        if (!TextUtils.isEmpty(printerTarget)) {
                            Discovery.stop();
                            Log.d("tarDiscoveryget:", "Discovery stop ");
                        }
                    } catch (Epos2Exception e) {
                        if (e.getErrorStatus() != Epos2Exception.ERR_PROCESSING) {
                            ShowMsg.showException(e, "Discovery stop fail", getApplicationContext());
                        }
                    }*/
                }
            });
        } catch (Exception e) {
            ShowMsg.showException(e, "search usb printer start failure", this);
        }
    }

    public static String getPrinterTarget() {
        return printerTarget;
    }

}
