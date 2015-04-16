package com.grogshop.manage.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;
import com.grogshop.manage.R;
import com.grogshop.manage.adapter.DishAdapter;
import com.grogshop.manage.domain.Dish;
import com.grogshop.manage.domain.Order;
import com.grogshop.manage.domain.Waiter;
import com.grogshop.manage.util.PayUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 餐桌界面
 */
public class ShowDishActivity extends ActionBarActivity implements View.OnClickListener {
    public static final String DESKTOP_NAME = "name";
    public static final String DESKTOP_POSITION = "position";
    private static final String TAG = "ShowDishActivityTag";
    private DishAdapter mDishAdapter;
    private String mDesktopName;
    private int mDesktopPosition;
    private ListView mListView;
    private List<Dish> mOrderedList;
    private SharedPreferences mSharedPreferences;
    private Button mPayButton;
    private TextView mPriceTextView;
    private EditText orderPersonET;
    private Spinner mWaiterSpinner;
    SimpleDateFormat simpleDateFormat;
    String currentWaiterName;
    String currentMoney;
    Double doubleCurrentMoney;
    String currentOrderName;
    private RadioGroup mPayTypeRadioButton;
    private ProgressDialog mProgressDialog;
    BmobPay bmobPay;
    Dialog mChoosePaymentDialog;
    PayUtil mPayUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desktop);
        getIntentValue();
        initViewId();
        setListener();
        initData();
    }



    private void getIntentValue() {
        mSharedPreferences = getSharedPreferences("dish", MODE_PRIVATE);
        Intent intent = getIntent();
        if (null != intent) {
            mDesktopName = intent.getStringExtra(DESKTOP_NAME);
            if (null != mDesktopName) {
                mDesktopName = mDesktopName.replace("\n", "");
                mDesktopPosition = intent.getIntExtra(DESKTOP_POSITION, 0);
                mSharedPreferences.edit().putString("name", mDesktopName).commit();
                mSharedPreferences.edit().putInt("position", mDesktopPosition).commit();
            }
        }
    }

    private void initData() {
        setTitle(mDesktopName);
        mOrderedList = OrderDishActivity.getOrderedList();
        mDishAdapter = new DishAdapter(this, mOrderedList, mListView);
        mListView.setAdapter(mDishAdapter);
        countPrice();
    }

    private void countPrice() {
        double current_price;
        double sumprice = 0;
        for (int i = 0; i < mOrderedList.size(); i++) {
            current_price = mOrderedList.get(i).getPrice();
            sumprice += current_price;
        }
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("0.00");
        currentMoney = df.format(sumprice);
        mPriceTextView.setText(currentMoney + "元");
    }

    private void initViewId() {
        mListView = (ListView) this.findViewById(R.id.dish_list_view);
        mListView.setEmptyView(findViewById(R.id.dish_empty_view));
        mPayButton = (Button) findViewById(R.id.pay_button);
        mPriceTextView = (TextView) findViewById(R.id.order_price);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        bmobPay = new BmobPay(this);
        mPayUtil = new PayUtil(ShowDishActivity.this);
    }

    private void setListener() {
        mPayButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_dish, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDesktopName = mSharedPreferences.getString("name", "");
        mDesktopPosition = mSharedPreferences.getInt("position", 0);
        setTitle(mDesktopName);
        initData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.choose_dish:
                Intent intent = new Intent(this, OrderDishActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                if (mOrderedList != null) {
                    mOrderedList.clear();
                }
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_button:
                paymentOrder();
                break;
            case R.id.payment_button:
                surePayment();
                finish();
                break;
            default:
                break;
        }
    }

    private void paymentOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View v = LayoutInflater.from(this).inflate(R.layout.payment_dialog_layout, null);
        builder.setIcon(R.drawable.password_logo);
        builder.setTitle("支付");
        builder.setView(v);
        orderPersonET = (EditText) v.findViewById(R.id.order_name);
        mWaiterSpinner = (Spinner) v.findViewById(R.id.watier_spinner);
        mPayTypeRadioButton = (RadioGroup) v.findViewById(R.id.id_pay_type);
        initWaiterSpinner();
        Button mPayButton = (Button) v.findViewById(R.id.payment_button);
        mPayButton.setOnClickListener(this);
        builder.create();
        mChoosePaymentDialog = builder.show();
    }



    private void surePayment() {
        currentOrderName = orderPersonET.getText().toString().trim();
        Order order = new Order();
        order.setDesktopNumber(mDesktopName);
        order.setTime(simpleDateFormat.format(new Date()));
        order.setOrderName(null == currentOrderName ? "用餐者" : currentOrderName);
        doubleCurrentMoney = null == currentMoney ? 0.00 : Double.parseDouble(currentMoney);
        order.setTotalMoney(doubleCurrentMoney);
        order.setWatierName(null == currentWaiterName ? "自助服务" : currentWaiterName);
        if (mPayTypeRadioButton.getCheckedRadioButtonId() == R.id.paytype_alipay) {
            mPayUtil.PayByAli(currentOrderName, doubleCurrentMoney, mDesktopName, order);
        } else if (mPayTypeRadioButton.getCheckedRadioButtonId() == R.id.paytype_wxpay) {
            mPayUtil.PayByWX(currentOrderName, doubleCurrentMoney, mDesktopName, order);
        }
        closeAlertDialog();
        if (mOrderedList != null) {
            mOrderedList.clear();
        }
    }


    public void initWaiterSpinner() {
        BmobQuery<Waiter> query = new BmobQuery<>();
        query.addWhereNotEqualTo("type", "管理员");
        query.findObjects(this, new FindListener<Waiter>() {
            @Override
            public void onSuccess(List<Waiter> waiters) {
                final List<String> waiterName = new ArrayList<>();
                for (int i = 0; i < waiters.size(); i++) {
                    waiterName.add(waiters.get(i).getName());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ShowDishActivity.this, android.R.layout.simple_list_item_1, waiterName);
                mWaiterSpinner.setAdapter(arrayAdapter);
                Toast.makeText(ShowDishActivity.this, "查询服务员成功", Toast.LENGTH_SHORT).show();
                mWaiterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        currentWaiterName = waiterName.get(position);
                        Toast.makeText(ShowDishActivity.this, "waiter：" + currentWaiterName, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(ShowDishActivity.this, "您没有选择服务员", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(ShowDishActivity.this, "查询服务员失败" + s, Toast.LENGTH_SHORT).show();
            }
        });

    }


    void closeAlertDialog(){
        if(mChoosePaymentDialog == null && mChoosePaymentDialog.isShowing()){
            mChoosePaymentDialog.dismiss();
        }
    }
}
