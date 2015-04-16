package com.grogshop.manage.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;
import com.grogshop.manage.domain.Order;
import com.grogshop.manage.ui.ShowDishActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by jiamengyu on 4/13/2015.
 */
public class PayUtil {
    protected static final String TAG = "PayUtilTag";
    BmobPay bmobPay;
    Context mContext;
    protected ProgressDialog mProgressDialog;

    public PayUtil(Activity context) {
        mContext = context;
        bmobPay = new BmobPay(context);
    }

    public void PayByWX(String name, final double price, final String desktopNum, final Order order) {
        showProgressDialog("正在获取订单..");
        if (name == null || "".equals(name)) {
            name = (desktopNum == null ? "Temp" : desktopNum) + "客人";
        }
        final String payName = name + "点菜消费";
        final String finalName = name;
        bmobPay.payByWX(price, payName, new PayListener() {
            @Override
            public void orderId(String orderId) {
                Toast.makeText(mContext, "获取订单成功!请等待跳转到支付页面", Toast.LENGTH_LONG).show();
                Log.e(TAG, payName + "'orderid is " + orderId + "\n\n");
            }

            @Override
            public void succeed() {
                Toast.makeText(mContext, "支付成功", Toast.LENGTH_LONG).show();
                Log.e(TAG, payName + "支付成功\n\n");
                closeProgressDialog();
                submitDishOrder(order);
            }

            @Override
            public void fail(int code, String reason) {
                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    new AlertDialog.Builder(mContext).setTitle("提示")
                            .setMessage("检测到您没有安装微信支付插件，无法进行微信支付，请选择是否安装（插件已打包在本地，无需消耗流量）")
                            .setPositiveButton("安装", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    installBmobPayPlugin("BmobPayPlugin.apk");
                                }
                            }).setNegativeButton("使用支付宝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PayByAli(finalName, price, desktopNum, order);
                        }
                    }).create().show();
                } else if (code == -2) {
                    Toast.makeText(mContext, "支付中断！！", Toast.LENGTH_LONG).show();
                }

                Log.e(TAG, payName + "支付失败\n\n");
                closeProgressDialog();
            }

            @Override
            public void unknow() {
                Toast.makeText(mContext, "支付结果未知，请稍后手动查询", Toast.LENGTH_LONG).show();
                Log.e(TAG, payName + "支付情况未知。\n\n");
                closeProgressDialog();
            }
        });
    }
    public void submitDishOrder(final Order order) {
        order.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, order.getOrderName() + "，您的订单已提交", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(mContext, "订单提交失败" + s, Toast.LENGTH_SHORT).show();

            }
        });
    }
    protected void installBmobPayPlugin(String apkName) {
        File file = null;
        try {
            InputStream inputStream = mContext.getAssets().open(apkName);
            file = new File(Environment.getExternalStorageDirectory() + File.separator + inputStream);
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = inputStream.read(temp)) > 0) {
                fileOutputStream.write(temp, 0, i);
            }
            inputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + file), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    public void PayByAli(String name, double price, String desktopNum, final Order order) {
        showProgressDialog("正在获取订单..");
        if (name == null || "".equals(name)) {
            name = (desktopNum == null ? "Temp" : desktopNum) + "桌客人";
        }
        final String payName = name + "点菜消费";
        bmobPay.pay(price, payName, new PayListener() {
            //无论成功与否  返回订单号
            @Override
            public void orderId(String orderId) {
                Toast.makeText(mContext, "获取订单成功!请等待跳转到支付页面", Toast.LENGTH_LONG).show();
                Log.e(TAG, payName + "'orderid is " + orderId + "\n\n");
            }

            @Override
            public void succeed() {
                Toast.makeText(mContext, "支付成功", Toast.LENGTH_LONG).show();
                Log.e(TAG, payName + "支付成功\n\n");
                closeProgressDialog();
                submitDishOrder(order);
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int i, String s) {
                Toast.makeText(mContext, "支付失败", Toast.LENGTH_LONG).show();
                Log.e(TAG, payName + "支付失败\n\n");
                closeProgressDialog();
            }

            //由于网络等原因，支付结果未知（小概率事件），出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(mContext, "支付结果未知，请稍后手动查询", Toast.LENGTH_LONG).show();
                Log.e(TAG, payName + "支付情况未知。\n\n");
                closeProgressDialog();
            }
        });
    }

    void showProgressDialog(String msg) {
        if (null == mProgressDialog) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setTitle("请稍等");
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    void closeProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
