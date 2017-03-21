package com.uptous.controller.utils;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * FileName : Validation.java
 * Dependencies :
 * Description : Validation on views.
 * Classes : Validation.java
 */
public class Validation {

    // return true if the input field is valid, based on the parameter passed
    public static boolean isFieldEmpty(EditText ed) {
        if (ed != null) {
            String uname = ed.getText().toString().trim();
            if (uname.equals("") || uname.length() <= 0)
                return true;
        }
        return false;
    }


    // Regular Expression
    // you can change the expression based on your need
    public static boolean isEmailValid(String mail) {

        String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern p = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(mail);
        if (m.matches() && mail.trim().length() > 0) {
            return true;
        }

        return false;
    }


}