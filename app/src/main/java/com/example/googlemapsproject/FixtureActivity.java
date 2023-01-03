package com.example.googlemapsproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.googlemapsproject.APIs.TokenSingleton;
import com.example.googlemapsproject.Models.Club;
import com.example.googlemapsproject.Models.Fixture;

import com.example.googlemapsproject.Services.FixtureRepository;
import com.example.googlemapsproject.databinding.ActivityFixtureBinding;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import com.example.bottomnavigationproper.StringReferences;


public class FixtureActivity extends FragmentActivity implements FixtureAdapter.OnNoteListener {
    List<Fixture> fixtures;
    List<Club> clubs;
    FixtureAdapter adapter;
    int position;
    RecyclerView recyclerView;
    ActivityFixtureBinding binding;
    ClubLocationViewModel viewModel;
    ActivityResultLauncher<Intent> testLauncher;
    FixtureRepository fixtureRepository;

    Spinner fixtureDateSpin, homeTeamSpin, awayTeamSpin;
    Club homeTeamSelected, awayTeamSelected;
    String dateSelected;


    Fixture fixture;
    LatLng destination;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFixtureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initSpinners();

        fixtureRepository = new FixtureRepository();
        fixtures = new ArrayList<>();
        clubs = new ArrayList<>();

        adapter = new FixtureAdapter(fixtures, this);
        createRecyclerView();
        initViewModel();

        initVoiceRecognition();



        //Waiting for response from UpdateActivity
        ActivityResultContracts.StartActivityForResult selectMatchingRouteStartActivityForResult
                = new ActivityResultContracts.StartActivityForResult();
        ActivityResultCallback<ActivityResult> selectMatchingActivityResultCallback = new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) { // On Update
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intent2 = result.getData();
                    Bundle bundle = intent2.getExtras();
                    Fixture fixture2 = (Fixture)bundle.getSerializable("updatedTask");
                    fixtures.set(position, fixture2);

                    fixtureRepository.update(TokenSingleton.getInstance().getBearerTokenString(), fixture2);
                    adapter.notifyDataSetChanged();
                }
                if(result.getResultCode() == Activity.RESULT_CANCELED){// on Delete
                    fixtureRepository.delete(TokenSingleton.getInstance().getBearerTokenString(), fixtures.get(position));
                    fixtures.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        };

        testLauncher = registerForActivityResult(selectMatchingRouteStartActivityForResult,
                selectMatchingActivityResultCallback);

    }

    private void initSpinners() {
        homeTeamSpin = findViewById(R.id.spinnerHome2);
        awayTeamSpin = findViewById(R.id.spinnerAway2);
        fixtureDateSpin = findViewById(R.id.spinnerDate2);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ClubLocationViewModel.class);
        viewModel.init();
        viewModel.getFixturesResponseLiveData().observe(this, new Observer<List<Fixture>>() {
            @Override
            public void onChanged(List<Fixture> fixtureList) {
                if (fixtureList != null) {
                    fixtures = fixtureList;
                    adapter.setValues(fixtureList);
                    adapter.notifyDataSetChanged();
                }

            }
        });
        viewModel.getClubResponseLiveData().observe(this, new Observer<List<Club>>() {
            @Override
            public void onChanged(List<Club> clubList) {
                if (clubList != null) {
                    clubs = clubList;
                    init();
                }

            }
        });

        viewModel.getFixtures();

        viewModel.getClubs();

    }

    public void createRecyclerView(){

//        Fi user = CurrentUser.getInstance().getUser();
//        fixtureList = dbHandler.getTasks(user);

        recyclerView = findViewById(R.id.fixtureRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setValues(fixtures);

        registerForContextMenu(recyclerView);
    }

    private void showOnMaps(LatLng destination, Fixture fixture) {
        if(destination  != null){
            weatherByLatLon(destination, fixture);
        }
    }

    @Override
    public void onSpeechInput(int position) {
        this.position = position;
        fixture = fixtures.get(position);
        Log.d("speechResult", fixture.getAwayTeam().getName());
        destination = getLocationFromAddress(fixture, fixture.getHomeTeam().getPitches());
    }

    @Override
    public void onNoteClick(int position){
        this.position = position;
        Fixture fixture = fixtures.get(position);

        Intent intent = new Intent(this, UpdateActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedFixture", fixture);

        bundle.putParcelableArrayList("clubs", (ArrayList<? extends Parcelable>) clubs);
        intent.putExtras(bundle);
        testLauncher.launch(intent);
    }

    @Override
    public void onDelete(int position){
        this.position = position;

    }

    @Override
    public void onDone(int position){
        this.position = position;

    }


public LatLng getLocationFromAddress(Fixture fixture, String address) {
    String url = "https://maps.googleapis.com/maps/api/geocode/json?address="
            + Uri.encode(address) + "&sensor=true&key="+"AIzaSyBA7kvUFGPo9y4nynMC0G7XWFRO86b4UOQ";
    RequestQueue queue = Volley.newRequestQueue(this);
    JsonObjectRequest stateReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            JSONObject location;
            try {
                // Get JSON Array called "results" and then get the 0th
                // complete object as JSON
                location = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

                // Get the value of the attribute whose name is
                // "formatted_string"
                if (location.getDouble("lat") != 0 && location.getDouble("lng") != 0) {
                    LatLng latLng = new LatLng(location.getDouble("lat"), location.getDouble("lng"));

                    destination = latLng;
                    showOnMaps(destination, fixture);
                    //Do what you want
                }
            } catch (JSONException e1) {
                e1.printStackTrace();

            }
        }

    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("Error.Response", error.toString());
        }
    });
    // add it to the queue
    queue.add(stateReq);
    return destination;

}

    private void weatherByLatLon(LatLng destination, Fixture fixture){
        String fixtureDate = "2023-01-05";
        OkHttpClient client=new OkHttpClient();

        Log.d("speechResult", destination.toString());

        String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=53.30&longitude=-6.32&hourly=temperature_2m&start_date=2023-01-05&end_date=2023-01-05";
//        String weatherUrl ="https://api.open-meteo.com/v1/forecast?"+
//                "latitude="+destination.latitude+"&"+
//                "longitude="+destination.longitude+"&"+
//                "hourly=temperature_2m&"+
//                "start_date=" + fixtureDate + "&"+
//                "end_date=" + fixtureDate;
        Log.d("url-name", weatherUrl);

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest stateReq = new JsonObjectRequest(Request.Method.GET, weatherUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    try {
                        JSONObject hourlyUnits = response.getJSONObject("hourly_units");
                        String tempUnits = hourlyUnits.getString("temperature_2m");
                        JSONObject hourly = response.getJSONObject("hourly");
                        JSONArray temperature_2m = hourly.getJSONArray("temperature_2m");
                        Double temp = (Double) temperature_2m.get(10);

                        Intent intent = new Intent(FixtureActivity.this, MapsActivity.class);

                        if(destination != null){
                            adapter.setLocation(destination.latitude,destination.longitude);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("currentFixture", fixture);
                            bundle.putSerializable("lat", destination.latitude);
                            bundle.putSerializable("long", destination.longitude);
                            bundle.putDouble("temp", temp);
                            bundle.putSerializable("tempUnits", tempUnits);

                            intent.putExtras(bundle);
                            startActivity(intent);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
        });
        // add it to the queue
        queue.add(stateReq);

    }

    private void init(){
        setHomeTeamSpin();
        setAwayTeamSpin();
        setFixtureList();

    }

    public void setHomeTeamSpin(){
        ArrayAdapter<Club> adapter =
                new ArrayAdapter<Club>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, clubs);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        homeTeamSpin.setAdapter(adapter);

        homeTeamSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                homeTeamSelected = (Club)parent.getItemAtPosition(pos);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        adapter.notifyDataSetChanged();
    }

    public void setAwayTeamSpin(){
        ArrayAdapter<Club> adapter =
                new ArrayAdapter<Club>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, clubs);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        awayTeamSpin.setAdapter(adapter);

        awayTeamSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                awayTeamSelected = (Club)parent.getItemAtPosition(pos);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        adapter.notifyDataSetChanged();
    }

    public void setFixtureList() {
        List<String> fixtureDates = new ArrayList<>();

        for (int i = 1; i < 10; i++) {
            StringBuilder str = new StringBuilder("2023/");
            str.append("0").append(i);
            str.append("/01");
            fixtureDates.add(str.toString());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, fixtureDates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fixtureDateSpin.setAdapter(adapter);

        fixtureDateSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                dateSelected = (String) parent.getItemAtPosition(pos);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private ImageView micButton;


    public void initVoiceRecognition(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        editText = findViewById(R.id.text);
        micButton = findViewById(R.id.button);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                editText.setText("");
                editText.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                micButton.setImageResource(R.drawable.ic_mic_black_off);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String dataStr = data.get(0);
                Log.d("speechResult", dataStr);
                int num = validateInput(dataStr);
                Log.d("speechResult", Integer.toString(num));
                if(num != -1){
                    onSpeechInput(num);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    micButton.setImageResource(R.drawable.ic_mic_black_on);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });


    }

    private int validateInput(String dataStr) {
        Soundexer soundexer = new Soundexer();
        List<String> clubs = new ArrayList<>();
        for(Fixture fixture: fixtures){
            String opp = fixture.getAwayTeam().getName();
            if (opp.equals(StringReferences.JUDES)) {
                opp = fixture.getHomeTeam().getName();
            }
            clubs.add(opp);
        }
        HashMap<Integer, List<String>> response = soundexer.getWordRatings(dataStr, clubs);
        int max = Collections.max(response.keySet());
        List<String> words = response.get(max);
        String word = words.get(0);
        Log.d("speechResult", word);

        int position = clubs.indexOf(word);
        return position;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }


}
