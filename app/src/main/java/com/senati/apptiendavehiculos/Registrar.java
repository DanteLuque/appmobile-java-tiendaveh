package com.senati.apptiendavehiculos;

import static com.senati.apptiendavehiculos.utils.FieldsUtils.isEmpty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.senati.apptiendavehiculos.config.Config;
import com.senati.apptiendavehiculos.utils.ToastUtils;

import org.json.JSONObject;

public class Registrar extends AppCompatActivity {
    private final String URL_VEHICULOS = Config.getVehiculosEndpoint();
    RequestQueue requestQueue;
    EditText edtMarca, edtModelo, edtColor, edtPrecio, edtPlaca;
    Button btnGuardar;
    boolean isEdit = false;
    Vehiculo vehiculoEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        loadUI();

        //detectar modo edición
        Intent i = getIntent();
        if (i != null && i.hasExtra("isEdit")) {
            isEdit = i.getBooleanExtra("isEdit", false);
            vehiculoEdit = (Vehiculo) i.getSerializableExtra("vehiculo");
        }

        if (isEdit && vehiculoEdit != null) {
            // Pre-llenado
            edtMarca.setText(vehiculoEdit.marca);
            edtModelo.setText(vehiculoEdit.modelo);
            edtColor.setText(vehiculoEdit.color);
            edtPrecio.setText(String.valueOf(vehiculoEdit.precio));
            edtPlaca.setText(vehiculoEdit.placa);
            btnGuardar.setText("Actualizar");
        }
    }

    private void loadUI(){
        edtMarca = findViewById(R.id.edtMarca);
        edtModelo = findViewById(R.id.edtModelo);
        edtColor = findViewById(R.id.edtColor);
        edtPrecio = findViewById(R.id.edtPrecio);
        edtPlaca = findViewById(R.id.edtPlaca);
        btnGuardar = findViewById(R.id.btnGuardar);
    }

    public void sendDataWS(View view){
        if(!validateFields()) return;

        requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        try {
            double precio = Double.parseDouble(edtPrecio.getText().toString().trim());

            jsonObject.put("marca", edtMarca.getText().toString().trim());
            jsonObject.put("modelo", edtModelo.getText().toString().trim());
            jsonObject.put("color", edtColor.getText().toString().trim());
            jsonObject.put("precio", precio);
            jsonObject.put("placa", edtPlaca.getText().toString().trim());
        }catch (Exception e){
            Log.e("Error JSON", e.toString());
        }

        if (isEdit && vehiculoEdit != null) {
            doPatch(jsonObject, vehiculoEdit.id);
        } else {
            doPost(jsonObject);
        }
    }

    //crear
    private void doPost(JSONObject body) {
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                URL_VEHICULOS,
                body,
                response -> {
                    ToastUtils.showToastShort(this, "Vehículo registrado correctamente 🚗");
                    clearFields();
                    startActivity(new Intent(this, Listar.class));
                },
                error -> handleVolleyError(error, "registrar")
        );

        requestQueue.add(req);
    }

    //actualizar
    private void doPatch(JSONObject body, int id) {
        String url = Config.getVehiculoEndpointById(id);
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.PATCH,
                url,
                body,
                response -> {
                    ToastUtils.showToastShort(this, "Vehículo actualizado ✅");
                    startActivity(new Intent(this, Listar.class));
                },
                error -> handleVolleyError(error, "actualizar")
        );
        requestQueue.add(req);
    }

    //manejo de errores
    private void handleVolleyError(com.android.volley.VolleyError error, String accion) {
        String errorMessage = "Ocurrió un error al " + accion;
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String body = new String(error.networkResponse.data, "UTF-8");
                JSONObject jsonError = new JSONObject(body);
                if (jsonError.has("message")) errorMessage = jsonError.getString("message");
                else if (jsonError.has("error")) errorMessage = jsonError.getString("error");
            } catch (Exception e) {
                Log.e("Registrar", "Error parseando respuesta: " + e.getMessage());
            }
        }
        ToastUtils.showToastShort(this, "❌ " + errorMessage);
    }

    private boolean validateFields() {
        if (isEmpty(edtMarca)) return false;
        if (isEmpty(edtModelo)) return false;
        if (isEmpty(edtColor)) return false;
        if (isEmpty(edtPrecio)) return false;
        if (isEmpty(edtPlaca)) return false;

        return true;
    }
    private void clearFields() {
        edtMarca.setText("");
        edtModelo.setText("");
        edtColor.setText("");
        edtPrecio.setText("");
        edtPlaca.setText("");
    }
}