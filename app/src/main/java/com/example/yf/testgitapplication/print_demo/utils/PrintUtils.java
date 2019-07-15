package com.example.yf.testgitapplication.print_demo.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.superc.yf_lib.utils.ShareUtil;
import com.zj.btsdk.BluetoothService;

import java.nio.charset.Charset;

public class PrintUtils {

    public static final byte[] RESET = {0x1b, 0x40}; //    复位打印机
    public static final byte[] ALIGN_LEFT = {0x1b, 0x61, 0x00};//    左对齐
    public static final byte[] ALIGN_CENTER = {0x1b, 0x61, 0x01}; //    中间对齐
    public static final byte[] ALIGN_RIGHT = {0x1b, 0x61, 0x02};//     右对齐
    public static final byte[] BOLD = {0x1b, 0x45, 0x01};//    选择加粗模式
    public static final byte[] BOLD_CANCEL = {0x1b, 0x45, 0x00};//    取消加粗模式
    public static final byte[] DOUBLE_HEIGHT_WIDTH = {0x1d, 0x21, 0x11};    //    宽高加倍
    public static final byte[] DOUBLE_WIDTH = {0x1d, 0x21, 0x10};   //    宽加倍
    public static final byte[] DOUBLE_HEIGHT = {0x1d, 0x21, 0x01};//    高加倍
    public static final byte[] NORMAL = {0x1d, 0x21, 0x00};//    字体不放大
    public static final byte[] LINE_SPACING_DEFAULT = {0x1b, 0x32}; //    设置默认行间距
    public static int LINE_BYTE_SIZE = 32; //    打印纸一行最大的字节
    private static int LEFT_LENGTH = LINE_BYTE_SIZE / 2;//    打印三列时，中间一列的中心线距离打印纸左侧的距离
    private static int RIGHT_LENGTH = LINE_BYTE_SIZE / 2;//    打印三列时，中间一列的中心线距离打印纸右侧的距离
    private static final int LEFT_TEXT_MAX_LENGTH = 5; //    打印三列时，第一列汉字最多显示几个文字
    private static BluetoothService mService = null;

    public static void setService(BluetoothService service) {
        mService = service;
    }

    /**
     * 打印文字
     *
     * @param text 要打印的文字
     */
    public static void printText(String text) {
        mService.sendMessage(text, "GBK");
    }

    /**
     * 设置打印格式
     *
     * @param command 格式指令
     */
    public static void selectCommand(byte[] command) {
        mService.write(command);
    }

    /**
     * 获取数据长度
     *
     * @param msg
     * @return
     */
    @SuppressLint("NewApi")
    private static int getBytesLength(String msg) {
        return msg.getBytes(Charset.forName("GB2312")).length;
    }
    /**
     * 打印两列
     *
     * @param leftText  左侧文字
     * @param rightText 右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public static String printTwoData(String leftText, String rightText) {
        StringBuilder sb = new StringBuilder();
        int leftTextLength = getBytesLength(leftText);
        int rightTextLength = getBytesLength(rightText);
        sb.append(leftText);
        // 计算两侧文字中间的空格
        int marginBetweenMiddleAndRight = LINE_BYTE_SIZE - leftTextLength - rightTextLength;
        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }
        sb.append(rightText);
        return sb.toString();
    }

    /**
     * 打印三列
     *
     * @param leftText   左侧文字
     * @param middleText 中间文字
     * @param rightText  右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public static String printThreeData(String leftText, String middleText, String rightText) {
        return printLine(leftText, middleText, rightText, false);
    }

    /**
     * 左边-----
     *
     * @param middleText 中间文字
     *                   右边-----
     */
    @SuppressLint("NewApi")
    public static String printLine(String leftText, String middleText, String rightText, boolean isLine) {
        StringBuilder sb = new StringBuilder();
        // 左边最多显示 LEFT_TEXT_MAX_LENGTH 个汉字 + 两个点
        if (leftText.length() > LEFT_TEXT_MAX_LENGTH) {
            leftText = leftText.substring(0, LEFT_TEXT_MAX_LENGTH) + "..";
        }
        LEFT_LENGTH = getLineByteSize() / 2;
        RIGHT_LENGTH = getLineByteSize() / 2;
        int leftTextLength = getBytesLength(leftText);
        int middleTextLength = getBytesLength(middleText);
        int rightTextLength = getBytesLength(rightText);
        sb.append(leftText);
        // 计算左侧文字和中间文字的空格长度
        int marginBetweenLeftAndMiddle = LEFT_LENGTH - leftTextLength - middleTextLength / 2;
        for (int i = 0; i < marginBetweenLeftAndMiddle; i++) {
            sb.append(isLine ? "-" : " ");
        }
        sb.append(middleText);
        // 计算右侧文字和中间文字的空格长度
        int marginBetweenMiddleAndRight = RIGHT_LENGTH - middleTextLength / 2 - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(isLine ? "-" : " ");
        }
        // 打印的时候发现，最右边的文字总是偏右一个字符，所以需要删除一个空格
        sb.delete(sb.length() - 1, sb.length()).append(rightText);
        return sb.toString();
    }

    public static void setLineByteSize(Context context) {
        boolean is_fiftyeifht = (boolean) ShareUtil.getInstance(context).get("is_fiftyeifht", true);
        LINE_BYTE_SIZE = is_fiftyeifht ? 32 : 48;
    }

    public static int getLineByteSize() {
        return LINE_BYTE_SIZE;
    }
}
