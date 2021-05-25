package edu.education.androiddevelopmentcontest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.education.androiddevelopmentcontest.adapters.InflateAdminFormulaAdapter;
import edu.education.androiddevelopmentcontest.adapters.InflatorAdminReferenceAdapter;
import edu.education.androiddevelopmentcontest.classes.AdminFormula;
import edu.education.androiddevelopmentcontest.classes.AdminReference;

public class AdminReferences extends AppCompatActivity implements InflatorAdminReferenceAdapter.onItemClickListener,InflateAdminFormulaAdapter.onFormulaClickListener {

    private final String GET_FORMULA_URL = "http://192.168.43.89/contest/index.php/admin/getAdminLecture";
    private final String GET_REFERENCE_URL = "http://192.168.43.89/contest/index.php/admin/getAdminReference";
    private final String DELETE_FORMULA_URL = "http://192.168.43.89/contest/index.php/admin/deleteFormula";
    private final String DELETE_REFERENCE_URL = "http://192.168.43.89/contest/index.php/admin/deleteReference";

    private InflateAdminFormulaAdapter adapter;
    private ArrayList<AdminFormula> details;

    private InflatorAdminReferenceAdapter referenceAdapter;
    private ArrayList<AdminReference> references;

    private RequestQueue formulaQueue;
    private StringRequest formulaRequest;
    private JSONArray formulaArray;
    private JSONObject formulaObject;

    private RequestQueue referenceQueue;
    private StringRequest referenceRequest;
    private JSONArray referenceArray;
    private JSONObject referenceObject;

    private RequestQueue deleteFormulaQueue;
    private StringRequest deleteFormulaRequest;

    private RequestQueue deleteReferenceQueue;
    private StringRequest deleteReferenceRequest;

    private RecyclerView recyclerView;
    private RecyclerView referenceView;
    private LinearLayout addReferenceAndFormula;

    private String TASKId;
    private Bundle bundle;

    @Override
    protected void onStart() {
        super.onStart();

        if (!references.isEmpty()) {
            references.clear();
            new FetchReference().execute();
        }

        if (details!=null) {
            details.clear();
            new FetchFormula().execute();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reference);

        bundle = getIntent().getExtras();
        TASKId = bundle.getString("id");

        recyclerView = findViewById(R.id.formulas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminReferences.this));

        referenceView = findViewById(R.id.videos);
        referenceView.setHasFixedSize(true);
        referenceView.setLayoutManager(new LinearLayoutManager(AdminReferences.this));

        addReferenceAndFormula = findViewById(R.id.addFormulaReference);
        addReferenceAndFormula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminReferences.this,AddAdminReferenceFormula.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",TASKId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        new FetchReference().execute();
        new FetchFormula().execute();
    }

    @Override
    public void onDeleteVideo(int position) {
        //Toast.makeText(getApplicationContext(),"Delete",Toast.LENGTH_LONG).show();
        final edu.education.androiddevelopmentcontest.classes.AdminReference adminReference = references.get(position);

        deleteReferenceQueue = Volley.newRequestQueue(AdminReferences.this);
        deleteReferenceRequest = new StringRequest(Request.Method.POST, DELETE_REFERENCE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String res = jsonObject.getString("status");
                            if (res.equals("ok")) {
                                refreshReference();
                            } else {
                                Toast.makeText(getApplicationContext(),"Failed to delete the Formula",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",adminReference.getId());
                params.put("taskid",TASKId);
                return params;
            }
        };

        deleteReferenceQueue.add(deleteReferenceRequest);
    }

    private void refreshReference() {
        if (!references.isEmpty()) {
            references.clear();
        }

        new FetchReference().execute();
    }

    @Override
    public void onDeleteFormula(int position) {
        //Toast.makeText(getApplicationContext(),"Delete F", Toast.LENGTH_LONG).show();
        final AdminFormula formula = details.get(position);
        deleteFormulaQueue = Volley.newRequestQueue(AdminReferences.this);
        deleteFormulaRequest = new StringRequest(Request.Method.POST, DELETE_FORMULA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String res = jsonObject.getString("status");
                            if (res.equals("ok")) {
                                reloadFormula();
                            } else {
                                Toast.makeText(getApplicationContext(),"Failed to delete Fprmula",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",formula.getId());
                params.put("taskid",TASKId);
                return params;
            }
        };
        deleteFormulaQueue.add(deleteFormulaRequest);
    }

    private void reloadFormula() {
        if (! details.isEmpty()) {
            details.clear();
        }
        new FetchFormula().execute();
    }

    public class FetchFormula extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            details = new ArrayList<>();
            formulaQueue = Volley.newRequestQueue(AdminReferences.this);
            formulaRequest = new StringRequest(Request.Method.POST, GET_FORMULA_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                formulaArray = jsonObject.getJSONArray("lecture");
                                for (int i=0;i < formulaArray.length(); i++) {
                                    formulaObject = formulaArray.getJSONObject(i);
                                    details.add(new AdminFormula(formulaObject.getString("id"),formulaObject.getString("title"),formulaObject.getString("formula")));
                                }
                                adapter = new InflateAdminFormulaAdapter(AdminReferences.this,details);
                                recyclerView.setAdapter(adapter);
                                adapter.setOnClickListener(AdminReferences.this);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("id",TASKId);
                    return params;
                }
            };

            formulaQueue.add(formulaRequest);
            return null;
        }
    }

    public class FetchReference extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            references = new ArrayList<>();
            referenceQueue = Volley.newRequestQueue(AdminReferences.this);
            referenceRequest = new StringRequest(Request.Method.POST, GET_REFERENCE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                referenceArray = jsonObject.getJSONArray("reference");
                                //Toast.makeText(getApplicationContext(),referenceArray.toString(),Toast.LENGTH_LONG).show();
                                for (int i=0; i<referenceArray.length();i++) {
                                    referenceObject = referenceArray.getJSONObject(i);
                                    //Toast.makeText(getApplicationContext(),referenceObject.toString(),Toast.LENGTH_LONG).show();
                                    references.add(new AdminReference(referenceObject.getString("id"),referenceObject.getString("title"),referenceObject.getString("videoid")));
                                }
                                Toast.makeText(getApplicationContext(),String.valueOf(references.size()),Toast.LENGTH_LONG).show();
                                referenceAdapter = new InflatorAdminReferenceAdapter(AdminReferences.this,references);
                                referenceView.setAdapter(referenceAdapter);
                                referenceAdapter.setOnItemClickListener(AdminReferences.this);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("id",TASKId);
                    return params;
                }
            };

            referenceQueue.add(referenceRequest);

            return null;
        }
    }
}
