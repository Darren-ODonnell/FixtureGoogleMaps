package com.example.googlemapsproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
}
