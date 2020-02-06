package com.example.masterlogistica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class login extends AppCompatActivity {

    private EditText rut,pass;
    private Button btnLogin;

    ProgressBar carga;
    ScrollView contenido;

    String tra_rut,tra_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rut = (EditText) findViewById(R.id.txtRut);
        pass = (EditText) findViewById(R.id.txtPass);
        btnLogin = (Button) findViewById(R.id.log);
        contenido = (ScrollView) findViewById(R.id.login_form);
        carga = (ProgressBar) findViewById(R.id.login_progress);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                contenido.setVisibility(View.GONE);
                carga.setVisibility(View.VISIBLE);
                tra_rut = rut.getText().toString().trim();
                tra_pass = pass.getText().toString().trim();

                StringRequest RequestAsigF = new StringRequest(Request.Method.POST,"https://www.aestudiar.cl/Transportistas/index.php/TransportistasAPP_Controller/login",new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("0")){
                            contenido.setVisibility(View.VISIBLE);
                            carga.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Rut y/o Contraseña incorrecta!", Toast.LENGTH_SHORT).show();
                        }else{
                            JSONObject j = null;
                            try {
                                j = new JSONObject(response);
                                JSONArray lista = j.getJSONArray("datos");

                                for (int i = 0; i < lista.length(); i++) {
                                    JSONObject employee = lista.getJSONObject(i);

                                    SharedPreferences prefs = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("tra_nombre", employee.getString("tra_nombre"));
                                    editor.putString("tra_patente", employee.getString("tra_patente"));
                                    editor.putInt("tra_id", employee.getInt("tra_id"));

                                    editor.commit();

                                    Intent intencion = new Intent(getApplicationContext(), MainActivity.class);

                                    contenido.setVisibility(View.VISIBLE);
                                    carga.setVisibility(View.GONE);

                                    startActivity(intencion);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                contenido.setVisibility(View.VISIBLE);
                                carga.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Rut y/o Contraseña incorrecta!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getApplicationContext(),"ERROR DE CONECCIÓN: ",Toast.LENGTH_SHORT).show();
                        contenido.setVisibility(View.VISIBLE);
                        carga.setVisibility(View.GONE);
                    }
                }){
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();

                        params.put("tra_rut",tra_rut);
                        params.put("tra_pass",tra_pass);
                        return params;
                    }
                };

                RequestQueue requestAsigF = Volley.newRequestQueue(getApplicationContext());
                requestAsigF.add(RequestAsigF);
            }
        });



    }
}
