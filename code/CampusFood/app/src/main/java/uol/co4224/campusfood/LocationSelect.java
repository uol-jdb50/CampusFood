package uol.co4224.campusfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationSelect extends AppCompatActivity implements RecyclerViewAdapterString.ItemClickListener, ApiResponse {

    String campusName = "";
    String userid;
    int allergenMatrix;
    RecyclerView recyclerView;
    RecyclerViewAdapterString adapter;
    RecyclerViewAdapterEmpty adapterEmpty;
    ApiRetrieval apiRetrieval = new ApiRetrieval();
    List<String> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select);

        campusName = getIntent().getStringExtra("CAMPUS");
        userid = getIntent().getStringExtra("USER");
        allergenMatrix = getIntent().getIntExtra("ALLERGEN", 0);
        getSupportActionBar().setTitle("CampusFood | " + campusName);
        apiRetrieval.delegate = this;
        apiRetrieval.execute("locations", campusName);
    }

    @Override
    public void processFinish(String response) {
        locations = processResult(response);
        recyclerView = findViewById(R.id.locationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (!locations.isEmpty()) {
            adapter = new RecyclerViewAdapterString(this, locations);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        } else {
            List<String> empty = new ArrayList<>();
            empty.add("No Locations Found");
            adapterEmpty = new RecyclerViewAdapterEmpty(this, empty);
            recyclerView.setAdapter(adapterEmpty);
        }
    }

    public List<String> processResult(String json) {
        List<String> output = new ArrayList<>();
        try {
            JSONArray obj = new JSONArray(json);
            for (int i = 0; i < obj.length(); i++) {
                output.add((String) obj.getJSONObject(i).get("Name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return output;
    }

    @Override
    public void onItemClick(View view, int position, int request) {
        Intent i = new Intent(this, MenuCategory.class);
        i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        i.putExtra("CAMPUS", campusName);
        i.putExtra("LOCATION", locations.get(position));
        i.putExtra("USER", userid);
        i.putExtra("BASKET", new Basket(campusName, locations.get(position), new Date(), allergenMatrix));
        startActivity(i);
    }
}