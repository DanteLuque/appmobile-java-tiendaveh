package com.senati.apptiendavehiculos.utils;

import android.widget.EditText;

public class FieldsUtils {
    public static boolean isEmpty(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError("Este campo es obligatorio");
            editText.requestFocus();
            return true;
        }
        return false;
    }
}
