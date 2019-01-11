package com.example.android.gametheory;

import android.content.Context;
import android.widget.Toast;

public class CustomUtils {

    // disply Toast
    public static void displayToast(Context context,  String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
