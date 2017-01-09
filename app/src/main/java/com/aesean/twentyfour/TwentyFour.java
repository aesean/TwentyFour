package com.aesean.twentyfour;

import java.util.HashSet;

/**
 * TwentyFour
 *
 * @author xl
 * @version V1.0
 * @since 16/7/13
 */
@SuppressWarnings("WeakerAccess")
public class TwentyFour {
    private static final String LEFT_BRACKETS = "( ";
    private static final String RIGHT_BRACKETS = " )";

    /**
     * 禁止外部实例化
     */
    private TwentyFour() {
    }

    /**
     * 计算24
     */
    public static HashSet<String> calculateResult(int a, int b, int c, int d) {
        //这里要转为float是因为计算中可能会出现小数,int计算会出错
        float[] valueFloat = new float[4];
        valueFloat[0] = a;
        valueFloat[1] = b;
        valueFloat[2] = c;
        valueFloat[3] = d;

        float[][] arrange = arrange(valueFloat);
        HashSet<String> resultString = new HashSet<>();
        for (float[] anArrange : arrange) {
            start(resultString, anArrange);
        }
        return resultString;
    }

    /**
     * 计算24,原理就是无论最终结果是什么公式,拆开了看,一定是两种情况:
     * 1、两个数做了加减乘除后,然后跟第三个数做加减乘除,最后跟第四个数做加减乘除。
     * 2、两个数做加减乘除,剩余两个数做加减乘除,然后两个的结果做加减乘除
     * 所以,将这些情况全部遍历计算就可以了。
     * 如果最终结果=24,则输出结果。
     */
    public static void start(HashSet<String> resultString, float[] mNum) {
        float[] value0 = math(mNum[0], mNum[1]);
        // 1-1-1-1
        //当三个数与一个数计算的时候
        for (int i = 0; i < value0.length; i++) {
            float[] value1 = math(value0[i], mNum[2]);
            for (int i1 = 0; i1 < value1.length; i1++) {
                float[] value2 = math(value1[i1], mNum[3]);
                for (int i2 = 0; i2 < value2.length; i2++) {
                    if (value2[i2] == 24f) {
                        String p0, p1, p2;
                        if (i < 4) {
                            p0 = LEFT_BRACKETS + (int) mNum[0] + getSymbol(i) + (int) mNum[1] + RIGHT_BRACKETS;
                        } else {
                            p0 = LEFT_BRACKETS + (int) mNum[1] + getSymbol(i) + (int) mNum[0] + RIGHT_BRACKETS;
                        }

                        if (i1 < 4) {
                            p1 = LEFT_BRACKETS + p0 + getSymbol(i1) + (int) mNum[2] + RIGHT_BRACKETS;
                        } else {
                            p1 = LEFT_BRACKETS + (int) mNum[2] + getSymbol(i1) + p0 + RIGHT_BRACKETS;
                        }

                        if (i2 < 4) {
                            p2 = p1 + getSymbol(i2) + (int) mNum[3];
                        } else {
                            p2 = (int) mNum[3] + getSymbol(i2) + p1;
                        }
                        resultString.add(p2 + " = 24");
                    }
                }
            }
        }
        //当两个数与两个数计算的时候
        float[] rightTwo = math(mNum[2], mNum[3]);
        for (int i = 0; i < value0.length; i++) {
            for (int i1 = 0; i1 < rightTwo.length; i1++) {
                float[] result = math(value0[i], rightTwo[i1]);
                for (int i2 = 0; i2 < result.length; i2++) {
                    if (result[i2] == 24f) {
                        String p0, p1, p2;
                        if (i < 4) {
                            p0 = LEFT_BRACKETS + (int) mNum[0] + getSymbol(i) + (int) mNum[1] + RIGHT_BRACKETS;
                        } else {
                            p0 = LEFT_BRACKETS + (int) mNum[1] + getSymbol(i) + (int) mNum[0] + RIGHT_BRACKETS;
                        }

                        if (i1 < 4) {
                            p1 = LEFT_BRACKETS + (int) mNum[2] + getSymbol(i1) + (int) mNum[3] + RIGHT_BRACKETS;
                        } else {
                            p1 = LEFT_BRACKETS + (int) mNum[3] + getSymbol(i1) + (int) mNum[2] + RIGHT_BRACKETS;
                        }

                        if (i2 < 4) {
                            p2 = p0 + getSymbol(i2) + p1;
                        } else {
                            p2 = p1 + getSymbol(i2) + p0;
                        }
                        resultString.add(p2 + " = 24");
                    }
                }
            }
        }
    }

    private static String getSymbol(int index) {
        switch (index) {
            case 0:
                return " + ";
            case 1:
                return " - ";
            case 2:
                return " * ";
            case 3:
                return " / ";
            case 4:
                return " / ";
            case 5:
                return " - ";
            default:
                throw new RuntimeException("未知的index类型");
        }
    }

    private static float[] math(float a, float b) {
        float[] result = new float[6];
        result[0] = a + b;
        result[1] = a - b;
        result[2] = a * b;
        result[3] = a / b;
        result[4] = b / a;
        result[5] = b - a;
        return result;
    }

    /**
     * 将输入的数组排列输出,这里偷懒了,这里只支持长度为4的排列
     *
     * @param value 数组
     * @return 排列后的所有结果
     */
    private static float[][] arrange(float[] value) {
        float[][] result = new float[24][4];
        for (int i = 0; i < 24; i++) {
            result[i] = new float[4];
        }
        for (int i = 0; i < 24; i++) {
            float a, b, c, d;
            for (int j = 0; j < 4; j++) {
                a = value[j];

                float[] leftThree = new float[3];
                int offset = 0;
                for (int t = 0; t < 4; t++) {
                    if (t == j) {
                        offset++;
                        continue;
                    }
                    leftThree[t - offset] = value[t];
                }
                for (int k = 0; k < 3; k++) {
                    b = leftThree[k];

                    float[] leftTwo = new float[2];
                    int offset2 = 0;
                    for (int t = 0; t < 3; t++) {
                        if (t == k) {
                            offset2++;
                            continue;
                        }
                        leftTwo[t - offset2] = leftThree[t];
                    }
                    for (int l = 0; l < 2; l++) {
                        if (l == 0) {
                            c = leftTwo[0];
                            d = leftTwo[1];
                        } else {
                            c = leftTwo[1];
                            d = leftTwo[0];
                        }
                        result[i][0] = a;
                        result[i][1] = b;
                        result[i][2] = c;
                        result[i][3] = d;
                        i++;
                    }
                }
            }
        }
        return result;
    }
}
