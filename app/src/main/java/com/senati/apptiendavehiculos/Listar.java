package com.senati.apptiendavehiculos;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Listar extends AppCompatActivity {

    ListView lstVehiculos;
    private final String URL_DEV = "http://192.168.101.37:3001/api/v1/vehiculos";
    private final String URL_PROD = "https://crisp-mainly-mastodon.ngrok-free.app/api/v1/vehiculos";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        loadUI();
        getData();
    }

    private void loadUI(){
        lstVehiculos = findViewById(R.id.lstVehiculos);
    }

    /**
     * Obtiene los datos del webservices - vehiculos
     */
    private void getData(){
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_DEV,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("Datos recibidos: ", jsonArray.toString());
                        renderData(jsonArray);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Error en el servicio: ", volleyError.toString());
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void renderData(JSONArray jsonArray){
        try{
            ArrayAdapter adapter;
            ArrayList<String> listaVehiculos = new ArrayList<>();

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                listaVehiculos.add(
                                jsonObject.getString("marca")+" "+
                                jsonObject.getString("modelo")
                );
            }

            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaVehiculos);
            lstVehiculos.setAdapter(adapter);
        }catch (Exception error){
            Log.e("Error en el renderizado de los datos: ", error.toString());
        }
    }
}