package com.aesean.twentyfour;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashSet;

public class MainActivity extends BaseActivity {

    private EditText mA;
    private EditText mB;
    private EditText mC;
    private EditText mD;
    private TextView mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mA = (EditText) findViewById(R.id.et_a);
        mB = (EditText) findViewById(R.id.et_b);
        mC = (EditText) findViewById(R.id.et_c);
        mD = (EditText) findViewById(R.id.et_d);
        mResult = (TextView) findViewById(R.id.tv_result);
    }

    @SuppressWarnings("deprecation")
    public void calculate(View view) {
        boolean pass = true;
        String a = mA.getText().toString();
        String b = mB.getText().toString();
        String c = mC.getText().toString();
        String d = mD.getText().toString();
        if (TextUtils.isEmpty(a)) {
            mA.setError(getString(R.string.not_null));
            pass = false;
        }
        if ("0".equals(a)) {
            mA.setError(getString(R.string.not_zero));
            pass = false;
        }
        if (TextUtils.isEmpty(b)) {
            mB.setError(getString(R.string.not_null));
            pass = false;
        }
        if ("0".equals(a)) {
            mB.setError(getString(R.string.not_zero));
            pass = false;
        }
        if (TextUtils.isEmpty(c)) {
            mC.setError(getString(R.string.not_null));
            pass = false;
        }
        if ("0".equals(a)) {
            mC.setError(getString(R.string.not_zero));
            pass = false;
        }
        if (TextUtils.isEmpty(d)) {
            mD.setError(getString(R.string.not_null));
            pass = false;
        }
        if ("0".equals(a)) {
            mD.setError(getString(R.string.not_zero));
            pass = false;
        }
        if (!pass) {
            return;
        }

        HashSet<String> hashSet = TwentyFour.calculateResult(Integer.valueOf(a), Integer.valueOf(b)
                , Integer.valueOf(c), Integer.valueOf(d));
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : hashSet) {
            stringBuilder.append(s).append("\n");
        }
        String result = stringBuilder.toString();
        int colorId;
        if (TextUtils.isEmpty(result)) {
            mResult.setText(R.string.not_find_result);
            colorId = R.color.fail;
        } else {
            mResult.setText(result);
            colorId = R.color.success;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mResult.setTextColor(getResources().getColor(colorId, getTheme()));
        } else {
            mResult.setTextColor(getResources().getColor(colorId));
        }
    }
}
