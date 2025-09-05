package com.senati.apptiendavehiculos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.senati.apptiendavehiculos.config.Config;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class Listar extends AppCompatActivity {

    ListView lstVehiculos;
    private ArrayList<Vehiculo> dataVehiculos = new ArrayList<>();
    private final String URL_VEHICULOS = Config.getVehiculosEndpoint();
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
                URL_VEHICULOS,
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
            ArrayList<String> listaStrings = new ArrayList<>();
            dataVehiculos.clear();

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject o = jsonArray.getJSONObject(i);
                Vehiculo v = new Vehiculo();
                v.id = o.getInt("id");
                v.marca = o.getString("marca");
                v.modelo = o.getString("modelo");
                v.color = o.getString("color");
                v.precio = o.getDouble("precio");
                v.placa = o.getString("placa");

                dataVehiculos.add(v);
                listaStrings.add(v.marca + " " + v.modelo + " (" + v.placa + ")");
            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaStrings);
            lstVehiculos.setAdapter(adapter);

            lstVehiculos.setOnItemClickListener((parent, view, position, id) -> {
                Vehiculo selected = dataVehiculos.get(position);
                Intent iEdit = new Intent(Listar.this, Registrar.class);
                iEdit.putExtra("isEdit", true);
                iEdit.putExtra("vehiculo", selected); // Serializable
                startActivity(iEdit);
            });

        }catch (Exception error){
            Log.e("Render", error.toString());
        }
    }
}