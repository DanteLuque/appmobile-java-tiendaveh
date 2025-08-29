package com.senati.apptiendavehiculos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void listar(View view){
        startActivity(new Intent(MainActivity.this, Listar.class));
    }

    public void registrar(View view){
        startActivity(new Intent(MainActivity.this, Registrar.class));
    }
}