package edu.education.androiddevelopmentcontest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private final String LOGIN_CRED_URL = "http://192.168.43.89/contest/index.php/backend/auth";

    private EditText Username;
    private EditText Password;
    private LazyLoader loader;
    private LinearLayout warning;
    private TextView warningText;
    private Button login;

    private RequestQueue credentialsQueue;
    private StringRequest loginCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Username = findViewById(R.id.username);
        Password = findViewById(R.id.password);
        loader = findViewById(R.id.progress);
        warning = findViewById(R.id.warning);
        warningText = findViewById(R.id.warningText);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setVisibility(View.INVISIBLE);
                warning.setVisibility(View.INVISIBLE);

                final String username = Username.getText().toString();
                final String password = Password.getText().toString();
                boolean flag = false;
                if (username.isEmpty() && password.isEmpty()) {
                    warningText.setText("Enter a valid Username & Password");
                    flag = true;
                } else if (username.isEmpty()) {
                    warningText.setText("Enter a valid Username");
                    flag = true;
                } else if (password.isEmpty()) {
                    warningText.setText("Enter a password");
                    flag = true;
                } else {
                    loader.setVisibility(View.VISIBLE);
                    credentialsQueue = Volley.newRequestQueue(Login.this);
                    loginCredentials = new StringRequest(Request.Method.POST, LOGIN_CRED_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject loginObject = new JSONObject(response);
                                        String status = loginObject.getString("status");
                                        loader.setVisibility(View.INVISIBLE);

                                        if (status.equals("stud")) {
                                            Intent homeActivity = new Intent(Login.this,Home.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("username",username);
                                            homeActivity.putExtras(bundle);
                                            startActivity(homeActivity);
                                        } else if (status.equals("teach")) {
                                            Intent admin = new Intent(Login.this,Dashboard.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("username",username);
                                            admin.putExtras(bundle);
                                            startActivity(admin);
                                        } else {
                                            warningText.setText("Invalid Credentials");
                                            warning.setVisibility(View.VISIBLE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG).show();
                            System.out.print(error.toString());
                            warningText.setText("Error Occured, Please try again..");
                            warning.setVisibility(View.VISIBLE);
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            params.put("username",username);
                            params.put("password",password);
                            return params;
                        }
                    };
                    credentialsQueue.add(loginCredentials);
                }

                if (flag) {
                    warning.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
