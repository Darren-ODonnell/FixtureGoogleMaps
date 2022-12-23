package com.example.googlemapsproject;


import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottomnavigationproper.StringReferences;
import com.example.googlemapsproject.Models.Fixture;


import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

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

    String imageUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json" +
            "&query="+ clubName +
            "&radius=10000" +
            "&key=" + apiKey;
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
        Fixture fixture = values.get(position);


if(fixture != null) {
    String opp = fixture.getAwayTeam().getName();
    String home = "home";
    if(opp.equals(StringReferences.JUDES)) {
        home = "away";
        opp = fixture.getHomeTeam().getName();
    }
    holder.location.setText(home);
    holder.opposition.setText(opp);
    holder.fixtureDate.setText(fixture.getFixtureDate());
    holder.image.setText("Image");

}

    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public void setValues(List<Fixture> allTasks) {
        this.values = allTasks;
    }

    // implements View.OnClickListener
    class FixtureResultsHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener {
        private TextView fixtureDate;
        private TextView image;
        private TextView location;
        private TextView opposition;

        OnNoteListener onNoteListener;

        public FixtureResultsHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            fixtureDate = itemView.findViewById(R.id.fixture_date);
            fixtureDate.setKeyListener(null);
            image = itemView.findViewById(R.id.image);
            image.setKeyListener(null);
            location = itemView.findViewById(R.id.location);
            location.setKeyListener(null);
            opposition = itemView.findViewById(R.id.opposition);
            opposition.setKeyListener(null);


            this.onNoteListener = onNoteListener;

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
        }
        //        @Override
//        public boolean onLongClick(View v) {
//            int position = this.getLayoutPosition();
//            Task task = values.get(position);
//
//            Intent intent = new Intent(v.getContext(),UpdateActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putParcelable("currentTask", task);
//            intent.putExtras(bundle);
//            v.getContext().startActivity(intent);
//        }
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
                        onNoteListener.onNoteClick(getAdapterPosition());
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
                }
                return false;
            }
        };
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
        void onDelete(int position);
        void onDone(int position);
    }




}
