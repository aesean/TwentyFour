package com.aesean.twentyfour;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
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

    private ICalculate24 mCalculate24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mA = (EditText) findViewById(R.id.et_a);
        mB = (EditText) findViewById(R.id.et_b);
        mC = (EditText) findViewById(R.id.et_c);
        mD = (EditText) findViewById(R.id.et_d);
        mResult = (TextView) findViewById(R.id.tv_result);

        mCalculate24 = new Calculate24Impl();
    }

    public void calculate(View view) {
        if (!check(mA, mB, mC, mD)) {
            return;
        }

        String a = mA.getText().toString();
        String b = mB.getText().toString();
        String c = mC.getText().toString();
        String d = mD.getText().toString();
        HashSet<String> hashSet = mCalculate24.calculateResult(Integer.valueOf(a), Integer.valueOf(b)
                , Integer.valueOf(c), Integer.valueOf(d));
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : hashSet) {
            stringBuilder.append(s).append("\n");
        }
        String result = stringBuilder.toString();
        setResult(result);
    }

    private void setResult(String result) {
        int colorId;
        if (TextUtils.isEmpty(result)) {
            mResult.setText(R.string.not_find_result);
            colorId = R.color.fail;
        } else {
            mResult.setText(result);
            colorId = R.color.success;
        }
        mResult.setTextColor(ResourcesCompat.getColor(getResources(), colorId, getTheme()));
    }

    private boolean check(EditText... editText) {
        boolean result = true;
        for (EditText view : editText) {
            result &= check(view);
        }
        return result;
    }

    private boolean check(EditText editText) {
        boolean pass = true;
        String s = editText.getText().toString();
        if (TextUtils.isEmpty(s)) {
            editText.setError(getString(R.string.not_null));
            pass = false;
        }
        if ("0".equals(s)) {
            editText.setError(getString(R.string.not_zero));
            pass = false;
        }
        return pass;
    }
}
