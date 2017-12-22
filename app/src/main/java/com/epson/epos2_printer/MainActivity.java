package com.epson.epos2_printer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.epson.epos2.Epos2CallbackCode;
import com.epson.epos2.Log;


/**
 * test
 * epson的底层已经为我们封装了printer方法，里面很多需要通过指令集操作的都被一一封装成了逐一的方法。详情请看打印
 */
public class MainActivity extends Activity implements View.OnClickListener, ReceiveListener {
    ReceiptPrinter receiptPrinter;

    private Context mContext = null;
    private EditText mEditTarget = null;
    private Spinner mSpnSeries = null;
    private Spinner mSpnLang = null;
    private Printer mPrinter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        int[] target = {
                R.id.btnDiscovery,
                R.id.btnSampleReceipt,
                R.id.btnSampleCoupon
        };

        for (int i = 0; i < target.length; i++) {
            Button button = (Button) findViewById(target[i]);
            button.setOnClickListener(this);
        }

        mSpnSeries = (Spinner) findViewById(R.id.spnModel);
        ArrayAdapter<SpnModelsItem> seriesAdapter = new ArrayAdapter<SpnModelsItem>(this, android.R.layout.simple_spinner_item);
        seriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_m10), Printer.TM_M10));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_m30), Printer.TM_M30));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_p20), Printer.TM_P20));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_p60), Printer.TM_P60));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_p60ii), Printer.TM_P60II));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_p80), Printer.TM_P80));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t20), Printer.TM_T20));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t60), Printer.TM_T60));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t70), Printer.TM_T70));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t81), Printer.TM_T81));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t82), Printer.TM_T82));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t83), Printer.TM_T83));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t88), Printer.TM_T88));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t90), Printer.TM_T90));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t90kp), Printer.TM_T90KP));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_u220), Printer.TM_U220));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_u330), Printer.TM_U330));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_l90), Printer.TM_L90));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_h6000), Printer.TM_H6000));
        mSpnSeries.setAdapter(seriesAdapter);
        mSpnSeries.setSelection(0);

        mSpnLang = (Spinner) findViewById(R.id.spnLang);
        ArrayAdapter<SpnModelsItem> langAdapter = new ArrayAdapter<SpnModelsItem>(this, android.R.layout.simple_spinner_item);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_ank), Printer.MODEL_ANK));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_japanese), Printer.MODEL_JAPANESE));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_chinese), Printer.MODEL_CHINESE));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_taiwan), Printer.MODEL_TAIWAN));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_korean), Printer.MODEL_KOREAN));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_thai), Printer.MODEL_THAI));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_southasia), Printer.MODEL_SOUTHASIA));
        mSpnLang.setAdapter(langAdapter);
        mSpnLang.setSelection(2);

        try {
            Log.setLogSettings(mContext, Log.PERIOD_TEMPORARY, Log.OUTPUT_STORAGE, null, 0, 1, Log.LOGLEVEL_LOW);
        } catch (Exception e) {
            ShowMsg.showException(e, "setLogSettings", mContext);
        }
        mEditTarget = (EditText) findViewById(R.id.edtTarget);
        findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String json = "#CENTER\n" +
                        "在線訂座入座終端編號(重印)\n" +
                        "分店：紅磡漁港\n" +
                        "終端編號：STATION\n";

                ReceiptPrinter.getInstance().runPrintReceiptSequence(MainActivity.this, json);

            }
        });
    }


    /**
     * 获取打印的目标对象
     */
    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        if (data != null && resultCode == RESULT_OK) {
            String target = data.getStringExtra(getString(R.string.title_target));
            if (target != null) {
                EditText mEdtTarget = (EditText) findViewById(R.id.edtTarget);
                mEdtTarget.setText(target);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.btnDiscovery:
                intent = new Intent(this, DiscoveryActivity.class);
                startActivityForResult(intent, 0);
                break;

            case R.id.btnSampleReceipt:
                updateButtonState(false);
                //打印收据文本
                if (!runPrintReceiptSequence()) {
                    updateButtonState(true);
                }
                break;

            case R.id.btnSampleCoupon:
                updateButtonState(false);
                //打印优惠卷 图片
                if (!runPrintCouponSequence()) {
                    updateButtonState(true);
                }
                break;

            default:
                // Do nothing
                break;
        }
    }

    private boolean runPrintReceiptSequence() {
        /* 初始化对象 */
        if (!initializeObject()) {
            return false;
        }
        /* 创建打印 数据 */
        if (!createReceiptData()) {
            finalizeObject();
            return false;
        }
        /*打印*/
        if (!printData()) {
            finalizeObject();
            return false;
        }

        return true;
    }

    private boolean createReceiptData() {
        String method = "";
//        Bitmap logoData = BitmapFactory.decodeResource(getResources(), R.drawable.store);

        StringBuffer textData = new StringBuffer("BIG5");//繁体字符集BIG5  中文 GBK
        final int barcodeWidth = 2;
        final int barcodeHeight = 100;

        if (mPrinter == null) {
            return false;
        }

        try {
            //语言需要
            mPrinter.addTextLang(Printer.LANG_ZH_TW);
            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            mPrinter.addTextSize(Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT);
            mPrinter.addText("在線訂座入座終端編號    174.81\n");
            mPrinter.addText("在線訂座入座終端編號2    174.81\n");

 /*           method = "addImage";
            //打印添加一个bitmap图片
          *//*  mPrinter.addImage(logoData, 0, 0,
                    logoData.getWidth(),
                    logoData.getHeight(),
                    Printer.COLOR_1,
                    Printer.MODE_MONO,
                    Printer.HALFTONE_DITHER,
                    Printer.PARAM_DEFAULT,
                    Printer.COMPRESS_AUTO);*//*

            method = "addFeedLine";
            mPrinter.addFeedLine(1);//添加一行
            textData.append("THE STORE 123 (555) 555 – 5555\n");
            textData.append("STORE DIRECTOR – John Smith\n");
            textData.append("\n");
            textData.append("7/01/07 16:58 6153 05 0191 134\n");
            textData.append("ST# 21 OP# 001 TE# 01 TR# 747\n");
            textData.append("------------------------------\n");
            method = "addText";
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length()); //添加完之前加入的内容后删除
            textData.append("400 OHEIDA 3PK SPRINGF  9.99 R\n");
            textData.append("410    9.99 R\n");
            textData.append("445 EMERIL /PAN 17.99 R\n");
            textData.append("438 CANDYMAKER ASSORT   4.99 R\n");
            textData.append("474 TRIPOD              8.99 R\n");
            textData.append("433 BLK LOGO PRNTED ZO  7.99 R\n");
            textData.append("458 AQUA MICROTERRY SC  6.99 R\n");
            textData.append("493 30L BLK FF DRESS   16.99 R\n");
            textData.append("407 LEVITATING DESKTOP  7.99 R\n");
            textData.append("441 **Blue Overprint P  2.99 R\n");
            textData.append("476 REPOSE 4PCPM CHOC   5.49 R\n");
            textData.append("461 WESTGATE BLACK 25  59.99 R\n");
            textData.append("------------------------------\n");
            method = "addText";
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            textData.append("SUBTOTAL                160.38\n");
            textData.append("TAX                      14.43\n");
            method = "addText";
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());

            method = "addTextSize";
            mPrinter.addTextSize(2, 2);//打印下一个字符集的时候定义字体大小
            method = "addText";
            mPrinter.addText("TOTAL    174.81\n");
            method = "addTextSize";
            mPrinter.addTextSize(1, 1);//继续以默认字体继续打印以下内容
            method = "addFeedLine";
            mPrinter.addFeedLine(1);//空一行


            textData.append("CASH     200.00\n");
            textData.append("CHANGE      25.19\n");
            textData.append("------------------------------\n");
            method = "addText";
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());

            textData.append("Purchased item total number\n");
            textData.append("Sign Up and Save !\n");
            textData.append("取號時間取號時間 取號時間取號時間 取號時間取號時間 !\n");
            textData.append("With Preferred Saving Card\n");
            method = "addText";
            mPrinter.addText(textData.toString());
            mPrinter.addText("发达的完全放弃");
            textData.delete(0, textData.length());
            method = "addFeedLine";
            mPrinter.addFeedLine(2);
            method = "addBarcode";
                   //BARCODE_CODE39 类型 数字长度为11～15  ；HRI_BELOW 位置  ;字体还是FONT_A默认大小最好看,FONT_C 小一点
                mPrinter.addBarcode("012094578902215",
                        Printer.BARCODE_CODE39,
                        Printer.HRI_BELOW,
                        Printer.FONT_A,
                        2,
                        100); //Width:2 to 6 ; Height:1 to 255

            mPrinter.addText("取號時間取號時間 取號時間取號時間 取號時間取號時間\n ");
            mPrinter.addFeedLine(2);//空两行
            method = "addCut";
            mPrinter.addCut(Printer.CUT_FEED);//裁切命令*/

        } catch (Exception e) {
            ShowMsg.showException(e, method, mContext);
            return false;
        }

        textData = null;

        return true;
    }

    private boolean runPrintCouponSequence() {
        //初始化打印
        if (!initializeObject()) {
            return false;
        }

        if (!createCouponData()) {
            finalizeObject();
            return false;
        }

        if (!printData()) {
            finalizeObject();
            return false;
        }

        return true;
    }

    private boolean createCouponData() {
        String method = "";
        Bitmap coffeeData = BitmapFactory.decodeResource(getResources(), R.drawable.coffee);
        Bitmap wmarkData = BitmapFactory.decodeResource(getResources(), R.drawable.wmark);

        final int barcodeWidth = 2;
        final int barcodeHeight = 64;
        /**
         * 页面区域宽高
         * */
        final int pageAreaHeight = 500;
        final int pageAreaWidth = 500;
        final int fontAHeight = 24;
        final int fontAWidth = 12;

        /**
         * 条形码宽高定义
         * */
        final int barcodeWidthPos = 110;
        final int barcodeHeightPos = 70;

        if (mPrinter == null) {
            return false;
        }

        try {
            method = "addPageBegin";
            mPrinter.addPageBegin();

            method = "addPageArea";
            mPrinter.addPageArea(0, 0, pageAreaWidth, pageAreaHeight);

            method = "addPageDirection";
            mPrinter.addPageDirection(Printer.DIRECTION_TOP_TO_BOTTOM);

            method = "addPagePosition";
            mPrinter.addPagePosition(0, coffeeData.getHeight());

            method = "addImage";
            mPrinter.addImage(coffeeData, 0, 0, coffeeData.getWidth(), coffeeData.getHeight(), Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, 3, Printer.PARAM_DEFAULT);

            method = "addPagePosition";
            mPrinter.addPagePosition(0, wmarkData.getHeight());

            method = "addImage";
            mPrinter.addImage(wmarkData, 0, 0, wmarkData.getWidth(), wmarkData.getHeight(), Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT);

            method = "addPagePosition";
            mPrinter.addPagePosition(fontAWidth * 4, (pageAreaHeight / 2) - (fontAHeight * 2));

            method = "addTextSize";
            mPrinter.addTextSize(3, 3);

            method = "addTextStyle";
            mPrinter.addTextStyle(Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.TRUE, Printer.PARAM_DEFAULT);

            method = "addTextSmooth";
            mPrinter.addTextSmooth(Printer.TRUE);

            method = "addText";
            mPrinter.addText("FREE  Coffee\n");

            method = "addPagePosition";
            mPrinter.addPagePosition((pageAreaWidth / barcodeWidth) - barcodeWidthPos, coffeeData.getHeight() + barcodeHeightPos);

            method = "addBarcode";
            mPrinter.addBarcode("01234567890", Printer.BARCODE_UPC_A, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, barcodeWidth, barcodeHeight);

            method = "addPageEnd";
            mPrinter.addPageEnd();

            method = "addCut";
            mPrinter.addCut(Printer.CUT_FEED);
        } catch (Exception e) {
            ShowMsg.showException(e, method, mContext);
            return false;
        }

        return true;
    }

    /**
     * 打印数据
     */
    private boolean printData() {
        if (mPrinter == null) {
            return false;
        }
        //连接打印设备
        if (!connectPrinter()) {
            return false;
        }
        //当前的打印状态
        PrinterStatusInfo status = mPrinter.getStatus();

        dispPrinterWarnings(status);

        //打印机不可用时弹窗提示
        if (!isPrintable(status)) {
            ShowMsg.showMsg(makeErrorMessage(status), mContext);
            try {
                //断开
                mPrinter.disconnect();
            } catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        try {
            //通过打印对象发送已经添加在mPrinter中的数据
            mPrinter.sendData(Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            ShowMsg.showException(e, "sendData", mContext);
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        return true;
    }

    /**
     * 初始化打印对象  返回true 是否生成
     */
    private boolean initializeObject() {
        try {
            // ((SpnModelsItem) mSpnLang.getSelectedItem()).getModelConstant()
//            mPrinter = new Printer(((SpnModelsItem) mSpnSeries.getSelectedItem()).getModelConstant(),
            mPrinter = new Printer(Printer.TM_T88, Printer.MODEL_ANK, mContext);
            //

        } catch (Exception e) {
            ShowMsg.showException(e, "Printer", mContext);
            return false;
        }

        mPrinter.setReceiveEventListener(this);

        return true;
    }

    /**
     * 完成打印，清空命令缓冲，关闭释放打印对象
     */
    private void finalizeObject() {
        if (mPrinter == null) {
            return;
        }

        mPrinter.clearCommandBuffer();

        mPrinter.setReceiveEventListener(null);

        mPrinter = null;
    }

    /**
     * 连接打印机
     *
     * @return
     */
    private boolean connectPrinter() {
        boolean isBeginTransaction = false;

        if (mPrinter == null) {
            return false;
        }

        try {
            //连接 USB设备地址我的 EPSON TM-T88IV型号地址: USB:/dev/bus/usb/004/002  必须通过开启搜索设备设置连接机型
//            mPrinter.connect(mEditTarget.getText().toString(), Printer.PARAM_DEFAULT);
            mPrinter.connect(App.getPrinterTarget(), Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            ShowMsg.showException(e, "connect fail", mContext);
            return false;
        }

        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        } catch (Exception e) {
            ShowMsg.showException(e, "beginTransaction", mContext);
        }

        if (isBeginTransaction == false) {
            try {
                mPrinter.disconnect();
            } catch (Epos2Exception e) {
                // Do nothing
                return false;
            }
        }

        return true;
    }


    /**
     * 断开当前打印
     */
    private void disconnectPrinter() {
        if (mPrinter == null) {
            return;
        }

        try {
            mPrinter.endTransaction();
        } catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    ShowMsg.showException(e, "endTransaction", mContext);
                }
            });
        }

        try {
            mPrinter.disconnect();
        } catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    ShowMsg.showException(e, "disconnect", mContext);
                }
            });
        }

        finalizeObject();
    }

    /**
     * 打印是否可用
     */
    private boolean isPrintable(PrinterStatusInfo status) {
        if (status == null) {
            return false;
        }

        if (status.getConnection() == Printer.FALSE) {
            return false;
        } else if (status.getOnline() == Printer.FALSE) {
            return false;
        } else {
            ;//print available
        }

        return true;
    }

    /**
     * 错误信息
     */
    private String makeErrorMessage(PrinterStatusInfo status) {
        String msg = "";

        if (status.getOnline() == Printer.FALSE) {
            msg += getString(R.string.handlingmsg_err_offline);
        }
        if (status.getConnection() == Printer.FALSE) {
            msg += getString(R.string.handlingmsg_err_no_response);
        }
        if (status.getCoverOpen() == Printer.TRUE) {
            msg += getString(R.string.handlingmsg_err_cover_open);
        }
        if (status.getPaper() == Printer.PAPER_EMPTY) {
            msg += getString(R.string.handlingmsg_err_receipt_end);
        }
        if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON) {
            msg += getString(R.string.handlingmsg_err_paper_feed);
        }
        if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR) {
            msg += getString(R.string.handlingmsg_err_autocutter);
            msg += getString(R.string.handlingmsg_err_need_recover);
        }
        if (status.getErrorStatus() == Printer.UNRECOVER_ERR) {
            msg += getString(R.string.handlingmsg_err_unrecover);
        }
        if (status.getErrorStatus() == Printer.AUTORECOVER_ERR) {
            if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_head);
            }
            if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_motor);
            }
            if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_battery);
            }
            if (status.getAutoRecoverError() == Printer.WRONG_PAPER) {
                msg += getString(R.string.handlingmsg_err_wrong_paper);
            }
        }
        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0) {
            msg += getString(R.string.handlingmsg_err_battery_real_end);
        }

        return msg;
    }

    /**
     * 打印机警告
     */
    private void dispPrinterWarnings(PrinterStatusInfo status) {
        EditText edtWarnings = (EditText) findViewById(R.id.edtWarnings);
        String warningsMsg = "";

        if (status == null) {
            return;
        }

        if (status.getPaper() == Printer.PAPER_NEAR_END) {
            warningsMsg += getString(R.string.handlingmsg_warn_receipt_near_end);
        }

        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_1) {
            warningsMsg += getString(R.string.handlingmsg_warn_battery_near_end);
        }

        edtWarnings.setText(warningsMsg);
    }

    /**
     * 更新button状态
     */
    private void updateButtonState(boolean state) {
        Button btnReceipt = (Button) findViewById(R.id.btnSampleReceipt);
        Button btnCoupon = (Button) findViewById(R.id.btnSampleCoupon);
        btnReceipt.setEnabled(state);
        btnCoupon.setEnabled(state);
    }


    /**
     * 打印结果监听
     */
    @Override
    public void onPtrReceive(final Printer printerObj, final int code, final PrinterStatusInfo status, final String printJobId) {
        runOnUiThread(new Runnable() {
            @Override
            public synchronized void run() {
                ShowMsg.showResult(code, makeErrorMessage(status), mContext);

                dispPrinterWarnings(status);

                updateButtonState(true);

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
                //执行完之后断开当前打印
                disconnectPrinter();
//                    }
//                }).start();
            }
        });
    }
}
