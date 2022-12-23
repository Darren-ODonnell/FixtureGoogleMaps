package com.example.googlemapsproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googlemapsproject.Models.Club;
import com.example.googlemapsproject.Models.Fixture;
import com.example.googlemapsproject.databinding.ActivityMapsBinding;

import com.google.android.gms.maps.SupportMapFragment;
import com.example.googlemapsproject.databinding.ActivityFixtureBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FixtureActivity extends FragmentActivity implements FixtureAdapter.OnNoteListener {
    List<Fixture> fixtures;
    FixtureAdapter adapter;
    int position;
    RecyclerView recyclerView;
    ActivityFixtureBinding binding;
    ClubLocationViewModel viewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFixtureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fixtures = new ArrayList<>();

        initViewModel();

        createRecyclerView();

        registerForContextMenu(recyclerView);

        initVoiceRecognition();

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

        viewModel.getFixtures();

    }

    public void createRecyclerView(){

//        Fi user = CurrentUser.getInstance().getUser();
//        fixtureList = dbHandler.getTasks(user);
        adapter = new FixtureAdapter(fixtures, this);
        recyclerView = findViewById(R.id.fixtureRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setValues(fixtures);
    }


    @Override
    public void onNoteClick(int position) {
        this.position = position;
        Fixture fixture = fixtures.get(position);
        Intent intent = new Intent(this, MapsActivity.class);


        Bundle bundle = new Bundle();
        bundle.putSerializable("currentFixture", fixture);
        intent.putExtras(bundle);
        startActivity(intent);

//        Task task = taskList.get(position);
//        Intent intent = new Intent(this, UpdateActivity.class);
//
//
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("currentTask", task);
//        intent.putExtras(bundle);
//        testLauncher.launch(intent);

    }

    @Override
    public void onDelete(int position){
        this.position = position;
//        Task task = taskList.get(position);
//
//        dbHandler.delete(task);
//        taskList = dbHandler.getAllTasksForCurrentUser();
//        adapter.setValues(taskList);
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDone(int position){
        this.position = position;
//        Task task = taskList.get(position);
//        Task newTask = task;
//        newTask.updateStatus();//Mark as done
//
//        dbHandler.update(task, newTask);
//        taskList = dbHandler.getAllTasksForCurrentUser();
//        adapter.setValues(taskList);
//        adapter.notifyDataSetChanged();
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
                int num = validateInput(dataStr);
                if(num != -1){
                    onNoteClick(num);
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
        return NLP.parseNumber(dataStr);
    }

    public void handleCommand(){

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
