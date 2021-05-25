package edu.education.androiddevelopmentcontest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddAdminReferenceFormula extends AppCompatActivity {

    private final String UPLOAD_FORMULA_URL = "http://192.168.43.89/contest/index.php/admin/addFormula";
    private final String UPLOAD_REFERENCE_URL = "http://192.168.43.89/contest/index.php/admin/addReference";

    private String INFLATER_ELEMENT[] = {"Formula (Wording)","Reference (Link)"};

    private Spinner dropdown;
    private EditText content;
    private EditText title;
    private LinearLayout addContent;
    private LinearLayout warning;
    private TextView warningText;

    private String TASKId;
    private String TITLE;
    private String CONTENT;

    private Bundle bundle;

    private ArrayAdapter<String> adapter;

    private RequestQueue addQueue;
    private StringRequest addRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin_reference_formula);

        dropdown = findViewById(R.id.dropdown);
        content = findViewById(R.id.content);
        title = findViewById(R.id.title);
        addContent = findViewById(R.id.addContent);
        warning = findViewById(R.id.warning);
        warningText = findViewById(R.id.warningText);

        bundle = getIntent().getExtras();
        TASKId = bundle.getString("id");

        Toast.makeText(getApplicationContext(),TASKId, Toast.LENGTH_LONG).show();

        adapter = new ArrayAdapter<>(this,R.layout.dropdownlister,R.id.lister,INFLATER_ELEMENT);
        adapter.setDropDownViewResource(R.layout.dropdownlister);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    content.setHint("Formula (Wording Content)");
                } else if (position == 1) {
                    content.setHint(R.string.formula_hint);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TITLE = title.getText().toString();
                CONTENT = content.getText().toString();

                if (TITLE.isEmpty() || CONTENT.isEmpty()) {
                    warningText.setText("Found some Missing Fields");
                    warning.setVisibility(View.VISIBLE);
                } else {
                    if (dropdown.getSelectedItemPosition() == 0) {
                        //Toast.makeText(getApplicationContext(),"ONE",Toast.LENGTH_LONG).show();
                        addFormula(UPLOAD_FORMULA_URL);
                    } else if (dropdown.getSelectedItemPosition() == 1) {
                        //Toast.makeText(getApplicationContext(),"TWO",Toast.LENGTH_LONG).show();
                        addFormula(UPLOAD_REFERENCE_URL);
                    }
                }
            }
        });
    }

    private void addFormula(String url) {
        addQueue = Volley.newRequestQueue(AddAdminReferenceFormula.this);
        addRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String res = jsonObject.getString("status");
                            if (res.equals("ok")) {
                                onBackPressed();
                            } else {
                                warningText.setText("Failed to Upload");
                                warning.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                warning.setVisibility(View.VISIBLE);
                warningText.setText("Error Occured while Updating");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",TASKId);
                params.put("title",TITLE);
                params.put("content",CONTENT);
                return params;
            }
        };

        addQueue.add(addRequest);
    }
}
