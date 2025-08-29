package com.senati.apptiendavehiculos;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Registrar extends AppCompatActivity {

    private final String URL_DEV = "http://192.168.101.37:3001/api/v1/vehiculos";
    private final String URL_PROD = "https://crisp-mainly-mastodon.ngrok-free.app/api/v1/vehiculos";
    RequestQueue requestQueue;

    EditText edtMarca, edtModelo, edtColor, edtPrecio, edtPlaca;
    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        loadUI();
    }

    private void loadUI(){
        edtMarca = findViewById(R.id.edtMarca);
        edtModelo = findViewById(R.id.edtModelo);
        edtColor = findViewById(R.id.edtColor);
        edtPrecio = findViewById(R.id.edtPrecio);
        edtPlaca = findViewById(R.id.edtPlaca);
        btnGuardar = findViewById(R.id.btnGuardar);
    }

    private void sendDataWS(View view){
        requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("marca", edtMarca.getText().toString());
            jsonObject.put("modelo", edtModelo.getText().toString());
            jsonObject.put("color", edtColor.getText().toString());
            jsonObject.put("precio", edtPrecio.getText().toString());
            jsonObject.put("placa", edtPlaca.getText().toString());
        }catch (Exception e){
            Log.e("Error en JSON: ", e.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL_DEV,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try{
                            String id = jsonObject.getString("id");
                        }catch (Exception e){
                            Log.e("Error en JSON: ", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Error en WS: ", volleyError.toString());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}