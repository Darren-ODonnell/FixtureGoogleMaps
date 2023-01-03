package com.example.googlemapsproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.googlemapsproject.Models.Club;
import com.example.googlemapsproject.Models.Fixture;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateActivity extends AppCompatActivity {

    Fixture currentFixture;

    Spinner homeTeamSpin, awayTeamSpin, fixtureDateSpin;

    String dateSelected;

    Club homeTeamSelected, awayTeamSelected;

    List<Club> clubs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        initSpinners();

        Intent i = getIntent();

        currentFixture = (Fixture) i.getSerializableExtra("selectedFixture");
        clubs = i.getParcelableArrayListExtra("clubs");

        init();

        findViewById(R.id.updateButton).setOnClickListener(v -> updateTask());

        findViewById(R.id.deleteButton).setOnClickListener(v -> deleteTask());
        }

    private void initSpinners() {
        homeTeamSpin = findViewById(R.id.spinnerHome);
        awayTeamSpin = findViewById(R.id.spinnerAway);
        fixtureDateSpin = findViewById(R.id.spinnerDate);
    }

    private void deleteTask() {
        Intent i = new Intent(this, FixtureActivity.class);
        setResult(Activity.RESULT_CANCELED, i);
        finish();

    }

    private boolean validateDate(String deadline){
        Pattern pattern = Pattern.compile("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$");
        Matcher matcher = pattern.matcher(deadline);
        return matcher.find();
    }

    private void updateTask() {

        Fixture fixture = initTask();
        if(validateDate(fixture.getFixtureDate())) {
                fixture.setId(currentFixture.getId());
                // result returning to TaskActivity
                Intent i = new Intent(this, FixtureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("updatedTask", fixture);
                i.putExtras(bundle);
                setResult(RESULT_OK, i);
                finish();
            }
        }




    private Fixture initTask() {
        return new Fixture();
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
        for(int i = 0; i < clubs.size(); i++){
            if(clubs.get(i).getPitches().equalsIgnoreCase(currentFixture.getHomeTeam().getPitches())){
                homeTeamSpin.setSelection(i);
            }
        }
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

        for(int i = 0; i < clubs.size(); i++){
            if(clubs.get(i).getPitches().equalsIgnoreCase(currentFixture.getAwayTeam().getPitches())){
                awayTeamSpin.setSelection(i);
            }
        }
    }

    public void setFixtureList(){
        List<String> fixtureDates = new ArrayList<>();

        for(int i = 1; i < 10; i++){
            StringBuilder str = new StringBuilder("2023/");
            str.append("0").append(i);
            str.append("/01");
            fixtureDates.add(str.toString());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, fixtureDates);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        fixtureDateSpin.setAdapter(adapter);

        fixtureDateSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                dateSelected = (String)parent.getItemAtPosition(pos);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
