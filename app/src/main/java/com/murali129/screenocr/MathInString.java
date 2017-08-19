package com.murali129.screenocr;

/**
 * Created by muralikrishna on 19/08/17.
 */

import android.util.Log;

public class MathInString {

    private String removespacesAndNewLines(String input){
        return input.replaceAll("\\s", "");
    }

    private String getMathStatement(String ocrString){
        Log.d("Input", ocrString);
        ocrString = removespacesAndNewLines(ocrString);
        return ocrString.split("=")[0];
    }

    private String getAnswerFromMathExpression(String mathStatement){
        return "";
    }

    public String getSolution(String ocrString){
        ocrString = getMathStatement(ocrString);
        return getAnswerFromMathExpression(ocrString);
    }

}
