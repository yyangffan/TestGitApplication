package com.example.yf.testgitapplication.print_demo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 打印调用该类方法
 */
public class PrintUtil {
    public static BluetoothService mService = null;
    private static PrintUtil printUtil;
    public static int postion = -1;
    public static List<Map<String, Object>> mListMap;
    private OnHandlerBack mOnHandlerBack;
    private Context mContext;
    /**
     * 创建一个Handler实例，用于接收BluetoothService类返回回来的消息
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mOnHandlerBack != null) {
                mOnHandlerBack.onHandlerBack(msg);
            }
        }

    };

    private PrintUtil(Context context) {
        mContext = context;
        mService = new BluetoothService(context, mHandler);
        PrintUtils.setService(mService);
        PrintUtils.setLineByteSize(context);
    }

    public void setOnHandlerBack(OnHandlerBack onHandlerBack) {
        mOnHandlerBack = onHandlerBack;
    }

    public static PrintUtil getInstance(Context context) {
        if (printUtil == null) {
            synchronized (PrintUtil.class) {
                if (printUtil == null) {
                    printUtil = new PrintUtil(context);
                }
            }
        }
        return printUtil;
    }

    public static int getPostion() {
        return postion;
    }

    public static void setPostion(int postion) {
        PrintUtil.postion = postion;
    }

    public static List<Map<String, Object>> getmListMap() {
        return mListMap;
    }

    public static void setmListMap(List<Map<String, Object>> listMap) {
        mListMap = new ArrayList<>();
        PrintUtil.mListMap.addAll(listMap);
    }

    public BluetoothService getmService() {
        return mService;
    }

    //打印格式
    public  void printTest() {
        PrintUtils.selectCommand(PrintUtils.RESET);
        PrintUtils.selectCommand(PrintUtils.LINE_SPACING_DEFAULT);
        PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
        PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
        PrintUtils.printText("****#18小二外卖****\n");
        PrintUtils.selectCommand(PrintUtils.NORMAL);
        PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
        PrintUtils.printText("**HealthOnly轻食店**");
        PrintUtils.selectCommand(PrintUtils.BOLD);
        PrintUtils.printText("银联小二外卖超“惠”吃，云闪付随机立减");
        PrintUtils.printText(PrintUtils.printLine("--","--","--",true));
        PrintUtils.selectCommand(PrintUtils.NORMAL);
        PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
        PrintUtils.printText("下单时间:2016-02-16 11:46\n");
        PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
        PrintUtils.printText("备注：天气很热，外卖小哥辛苦了，路上慢点。可乐是送小哥，麻烦店家冰一下，谢谢。\n");
        PrintUtils.selectCommand(PrintUtils.NORMAL);
//        PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
        PrintUtils.printText(PrintUtils.printLine("--","商品名称","--\n",true));
        PrintUtils.selectCommand(PrintUtils.BOLD_CANCEL);
        PrintUtils.printText(PrintUtils.printThreeData("面", "1", "0.00"));
        PrintUtils.printText(PrintUtils.printThreeData("米饭", "1", "6.00"));
        PrintUtils.printText(PrintUtils.printThreeData("铁板烧", "1", "26.00"));
        PrintUtils.printText(PrintUtils.printThreeData("一个测试", "1", "226.00"));
        PrintUtils.printText(PrintUtils.printThreeData("牛肉面啊啊", "1", "2226.00"));
        PrintUtils.printText(PrintUtils.printThreeData("牛肉面啊啊啊牛肉面啊啊啊", "888", "98886.00\n"));
        PrintUtils.selectCommand(PrintUtils.NORMAL);
//        PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
        PrintUtils.printText(PrintUtils.printLine("--","其它","--",true));
        PrintUtils.printText(PrintUtils.printTwoData("餐盒费", "2.00"));
        PrintUtils.printText(PrintUtils.printTwoData("配送费", "24.5"));
        PrintUtils.printText(PrintUtils.printTwoData("优惠合计", "-20.00"));
        PrintUtils.printText(PrintUtils.printLine("--","--","--",true));
        PrintUtils.selectCommand(PrintUtils.NORMAL);
        PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
        PrintUtils.printText(PrintUtils.printTwoData("", "原价82.50"));
        PrintUtils.selectCommand(PrintUtils.NORMAL);
        PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
        PrintUtils.printText(PrintUtils.printTwoData("用户在线支付", "42.459"));
        PrintUtils.printText(PrintUtils.printLine("--","--","--",true));
        PrintUtils.selectCommand(PrintUtils.ALIGN_LEFT);
        PrintUtils.printText("订单号：1241hkljgsdlk\n");
        PrintUtils.selectCommand(PrintUtils.DOUBLE_HEIGHT_WIDTH);
        PrintUtils.printText("天津市河东区天创企业孵化基地\n\n13712342421\n\n王先生");
        PrintUtils.selectCommand(PrintUtils.NORMAL);
        PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
        PrintUtils.printText(PrintUtils.printLine("--","#18完","--\n",true));
        PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
        PrintUtils.printText("小二外卖");
        PrintUtils.printText("\n\n\n\n\n\n");
    }

    //打印格式测试
    public static void toDayin() {
        byte[] now = {0x1d, 0x21, 0x11};
        PrintUtils.selectCommand(now);
        PrintUtils.selectCommand(PrintUtils.ALIGN_CENTER);
        PrintUtils.printText("**HealthOnly轻食店**");
        PrintUtils.printText("银联小二外卖超“惠”吃，云闪付随机立减");
        PrintUtils.printText("\n\n\n");
    }

    //打印图形
    @SuppressLint("SdCardPath")
    public static void printImage() {
        byte[] sendData = null;
        PrintPic pg = new PrintPic();
        pg.initCanvas(384);
        pg.initPaint();
        pg.drawImage(0, 0, "/mnt/sdcard/icon.jpg");
        sendData = pg.printDraw();
        mService.write(sendData);   //打印byte流数据
    }

    /*接口访问获取数据*/
    public static void toGetData(String order_number) {

    }

    public interface OnHandlerBack {
        void onHandlerBack(Message msg);
    }
}
