package com.example.googlemapsproject;


import android.location.Address;
import android.location.Geocoder;
import android.os.StrictMode;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottomnavigationproper.StringReferences;
import com.example.googlemapsproject.Models.Club;
import com.example.googlemapsproject.Models.Fixture;
import com.google.android.gms.maps.model.LatLng;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FixtureAdapter extends RecyclerView.Adapter<FixtureAdapter.FixtureResultsHolder> {
    private List<Fixture> values;
    private OnNoteListener onNoteListener;

    String clubName = "CLG Na Fianna";
    String apiKey = "AIzaSyBA7kvUFGPo9y4nynMC0G7XWFRO86b4UOQ";

    String weatherUrl = "https://api.open-meteo.com/v1/forecast?";

    FixtureResultsHolder holder;

    double lat;
    double lng;
    public FixtureAdapter(List<Fixture> values, OnNoteListener onNoteListener) {
        this.values = values;
        this.onNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public FixtureResultsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fixture_row, parent, false);

        return new FixtureResultsHolder(itemView, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FixtureResultsHolder holder, int position) {
        this.holder = holder;
        Fixture fixture = values.get(position);


        if (fixture != null) {
            String opp = fixture.getAwayTeam().getName();
            String home = "home";
            if (opp.equals(StringReferences.JUDES)) {
                home = "away";
                opp = fixture.getHomeTeam().getName();
            }
            holder.location.setText(home);
            holder.opposition.setText(opp);
            holder.fixtureDate.setText(fixture.getFixtureDate());

//            LatLng latLng = new LatLng(53, -6);

//            WeatherByLatLon(fixture.getFixtureDate());
        }
    }
//    private void WeatherByLatLon(String fixtureDate){
//        fixtureDate = "2022-12-30";
//        OkHttpClient client=new OkHttpClient();
//
//        weatherUrl ="https://api.open-meteo.com/v1/forecast?"+
//                "latitude="+lat+"&"+
//                "longitude="+lng+"&"+
//                "hourly=temperature_2m&"+
//                "start_date=" + fixtureDate + "&"+
//                "end_date=" + fixtureDate;
//        Log.d("url-name", weatherUrl);
//
//        Request request=new Request.Builder()
//                .url(weatherUrl)
//                .get().build();
//        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                String data=response.body().string();
//                ResponseBody body = response.body();
//
//                JSONObject json = null;
//                try {
//                    json = new JSONObject(data);
//                    JSONObject hourlyUnits = json.getJSONObject("hourly_units");
//                    String tempUnits = hourlyUnits.getString("temperature_2m");
//                    JSONObject hourly = json.getJSONObject("hourly");
//                    JSONArray temperature_2m = hourly.getJSONArray("temperature_2m");
//                    Double temp = (Double) temperature_2m.get(10);
//
//                    holder.tempUnits.setText(tempUnits);
//                    holder.temp.setText(Double.toString(temp) + " " + tempUnits);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public void setValues(List<Fixture> allTasks) {
        this.values = allTasks;
    }

    public void setLocation(double latitude, double longitude) {
        this.lat = latitude;
        this.lng = longitude;
    }

    // implements View.OnClickListener
    class FixtureResultsHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener {
        private TextView fixtureDate;
        private TextView location;
        private TextView opposition;


        OnNoteListener onNoteListener;

        public FixtureResultsHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            fixtureDate = itemView.findViewById(R.id.fixture_date);
            fixtureDate.setKeyListener(null);
            location = itemView.findViewById(R.id.location);
            location.setKeyListener(null);
            opposition = itemView.findViewById(R.id.opposition);
            opposition.setKeyListener(null);

            this.onNoteListener = onNoteListener;

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem edit = menu.add(Menu.NONE,1,1,"Edit");
            MenuItem delete = menu.add(Menu.NONE,2,2,"Delete");
            MenuItem done = menu.add(Menu.NONE,3,3,"Update Status");



            edit.setOnMenuItemClickListener(onChange);
            delete.setOnMenuItemClickListener(onChange);
            done.setOnMenuItemClickListener(onChange);
        }
        private final MenuItem.OnMenuItemClickListener onChange = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 1:
                        onNoteListener.onSpeechInput(getAdapterPosition());
                        Toast.makeText(itemView.getContext(),"Edit",Toast.LENGTH_LONG).show();
                        return true;
                    case 2:
                        onNoteListener.onDelete(getAdapterPosition());
                        Toast.makeText(itemView.getContext(), "Deleted",Toast.LENGTH_LONG).show();
                        return true;
                    case 3:
                        onNoteListener.onDone(getAdapterPosition());
                        Toast.makeText(itemView.getContext(), "Status Updated",Toast.LENGTH_LONG).show();
                        return true;
                    case 4:
                        onNoteListener.onNoteClick(getAdapterPosition());
                        Toast.makeText(itemView.getContext(), "Deleted",Toast.LENGTH_LONG).show();
                        return true;
                }
                return false;
            }
        };
    }

    public interface OnNoteListener{
        void onSpeechInput(int position);
        void onNoteClick(int position);
        void onDelete(int position);
        void onDone(int position);
    }




}
