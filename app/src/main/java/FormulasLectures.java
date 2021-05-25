package edu.education.androiddevelopmentcontest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubePlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.education.androiddevelopmentcontest.adapters.InflateFormulaAdapter;
import edu.education.androiddevelopmentcontest.adapters.InflatorReferenceAdapter;
import edu.education.androiddevelopmentcontest.classes.GetFormula;
import edu.education.androiddevelopmentcontest.classes.GetReference;

public class FormulasLectures extends AppCompatActivity implements InflatorReferenceAdapter.OnItemClickListener {

    private final String GET_FORMULA_URL = "http://192.168.43.89/contest/index.php/backend/getFormula";
    private final String GET_REQUEST_URL = "http://192.168.43.89/contest/index.php/backend/getReference";
    private final String COMPLETE_TASK_URL = "http://192.168.43.89/contest/index.php/backend/completeFormula";

    private RecyclerView formulaView;
    private RecyclerView lectureView;
    private Button CompleteTask;

    private InflateFormulaAdapter formulaAdapter;
    private InflatorReferenceAdapter referenceAdapter;

    private ArrayList<GetFormula> formulaDetails;
    private ArrayList<GetReference> referencesDetails;

    private RequestQueue formulaQueue;
    private StringRequest formulaRequest;
    private JSONArray formulaArray;
    private JSONObject formulaObject;

    private RequestQueue referenceQueue;
    private StringRequest referenceRequest;
    private JSONArray referenceArray;
    private JSONObject referenceObject;

    private RequestQueue completeQueue;
    private StringRequest completeRequest;

    private String USERNAME;
    private String TASKID;
    private String Title;
    private String Formula;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulas_lectures);

        bundle = getIntent().getExtras();

        USERNAME = bundle.getString("username");
        TASKID = bundle.getString("taskid");

        formulaView = findViewById(R.id.formulas);
        formulaView.setNestedScrollingEnabled(false);
        formulaView.setHasFixedSize(true);
        formulaView.setLayoutManager(new LinearLayoutManager(this));

        lectureView = findViewById(R.id.videos);
        lectureView.setNestedScrollingEnabled(false);
        lectureView.setHasFixedSize(true);
        lectureView.setLayoutManager(new LinearLayoutManager(this));

        CompleteTask = findViewById(R.id.complete);
        CompleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),TASKID,Toast.LENGTH_LONG).show();
                completeQueue = Volley.newRequestQueue(FormulasLectures.this);
                completeRequest = new StringRequest(Request.Method.POST, COMPLETE_TASK_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(),response + " " + TASKID,Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Complete Error",Toast.LENGTH_LONG).show();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("username",USERNAME);
                        params.put("taskid",TASKID);
                        return params;
                    }
                };

                completeQueue.add(completeRequest);
            }
        });

        new FetchFormula().execute();
        new FetchReferece().execute();
    }

    @Override
    public void onIemClick(int position) {
        GetReference getReference = referencesDetails.get(position);
        Toast.makeText(getApplicationContext(),getReference.getVideoid(),Toast.LENGTH_LONG).show();
        Intent intent = new Intent(FormulasLectures.this,YoutubeVideoPlayer.class);
        Bundle bundle = new Bundle();
        bundle.putString("videoid",getReference.getVideoid());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public class FetchFormula extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            formulaDetails = new ArrayList<>();

            formulaQueue = Volley.newRequestQueue(FormulasLectures.this);
            formulaRequest = new StringRequest(Request.Method.POST, GET_FORMULA_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                formulaArray = jsonObject.getJSONArray("formula");

                                for (int i=0;i<formulaArray.length();i++) {
                                    formulaObject = formulaArray.getJSONObject(i);

                                    Title = formulaObject.getString("title");
                                    Formula = formulaObject.getString("formula");

                                    formulaDetails.add(new GetFormula(formulaObject.getString("id"),formulaObject.getString("taskid"),Title,Formula));
                                }

                                formulaAdapter = new InflateFormulaAdapter(FormulasLectures.this,formulaDetails);
                                formulaView.setAdapter(formulaAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("taskid",TASKID);
                    return params;
                }
            };

            formulaQueue.add(formulaRequest);
            return null;
        }
    }
    public class FetchReferece extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            referencesDetails = new ArrayList<>();

            referenceQueue = Volley.newRequestQueue(FormulasLectures.this);
            referenceRequest = new StringRequest(Request.Method.POST, GET_REQUEST_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                referenceArray = jsonObject.getJSONArray("reference");

                                for (int i=0;i<referenceArray.length();i++) {
                                    referenceObject = referenceArray.getJSONObject(i);

                                    referencesDetails.add(new GetReference(referenceObject.getString("id"),referenceObject.getString("taskid"),referenceObject.getString("title"),referenceObject.getString("view")));
                                }

                                referenceAdapter = new InflatorReferenceAdapter(FormulasLectures.this,referencesDetails);
                                lectureView.setAdapter(referenceAdapter);
                                referenceAdapter.setOnItemClickListener(FormulasLectures.this);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Reference Error",Toast.LENGTH_LONG).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("taskid",TASKID);
                    return params;
                }
            };
            referenceQueue.add(referenceRequest);
            return null;
        }
    }
}
