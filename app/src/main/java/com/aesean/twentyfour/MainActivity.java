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
    private EditText mT;
    private TextView mResult;

    private ICalculateAny mCalculateAny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mA = findViewById(R.id.et_a);
        mB = findViewById(R.id.et_b);
        mC = findViewById(R.id.et_c);
        mD = findViewById(R.id.et_d);
        mT = findViewById(R.id.et_t);
        mResult = findViewById(R.id.tv_result);

        mCalculateAny = new CalculateAnyImpl();
    }

    public void calculate(View view) {
        if (!check(mA, mB, mC, mD, mT)) {
            return;
        }

        String a = mA.getText().toString();
        String b = mB.getText().toString();
        String c = mC.getText().toString();
        String d = mD.getText().toString();
        String t = mT.getText().toString();
        HashSet<String> hashSet = mCalculateAny.calculateResult(Integer.valueOf(t), Integer.valueOf(a), Integer.valueOf(b)
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
