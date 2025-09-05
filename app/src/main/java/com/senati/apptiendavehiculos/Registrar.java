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
import com.senati.apptiendavehiculos.utils.ToastUtils;

import org.json.JSONObject;

public class Registrar extends AppCompatActivity {

    private final String URL_VEHICULOS = Config.getVehiculosEndpoint();
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

    public void sendDataWS(View view){
        if(!validateFields()) return;

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
                URL_VEHICULOS,
                jsonObject,
                response -> {
                    ToastUtils.showToastShort(this, "Vehículo registrado correctamente 🚗");
                    clearFields();
                    startActivity(new Intent(this, Listar.class));
                },
                error -> {
                    String errorMessage = "Ocurrió un error al registrar";

                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String body = new String(error.networkResponse.data, "UTF-8");
                            JSONObject jsonError = new JSONObject(body);
                            if (jsonError.has("error")) errorMessage = jsonError.getString("error");
                        } catch (Exception e) {
                            Log.e("Registrar", "Error parseando respuesta: " + e.getMessage());
                        }
                    }
                    ToastUtils.showToastShort(this, "❌ " + errorMessage);
                }
        );
        requestQueue.add(jsonObjectRequest);
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