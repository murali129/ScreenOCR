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

        ocrString = removespacesAndNewLines(ocrString);

        if(ocrString.split("=").length != 0){
            return ocrString.split("=")[0];
        }else if(ocrString.split(":").length != 0){
            return ocrString.split(":")[0];
        }else if(ocrString.split("z").length != 0){
            return ocrString.split(":")[0];
        }else{
            return "";
        }
    }

    // the below one is bad code, lot of hard coding
    public String getSolution(String ocrString){
        Log.d("Input", ocrString);
        ocrString = getMathStatement(ocrString);
        Log.d("Input", ocrString);
        try{
            String string = ocrString;
            for (int i = 0; i <string.length(); i++) {
                 if(Integer.valueOf(string.charAt(i))==8212){
                     if(i!=string.length()-1){
                         string = string.substring(0,i)+'-'+string.substring(i+1);
                     }
                     else{
                         string = string.substring(0,i) +'-';
                     }
                 }
            }
            return String.valueOf(eval(string));
        }catch(Exception e){
            return "loading...";
        }
    }

    public double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('X')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}
