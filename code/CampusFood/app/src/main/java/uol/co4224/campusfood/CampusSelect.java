package uol.co4224.campusfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CampusSelect extends AppCompatActivity implements RecyclerViewAdapterString.ItemClickListener, ApiResponse {

    Button btnAllergen;
    Button btnOrders;
    RecyclerView recyclerView;
    RecyclerViewAdapterString adapter;
    ApiRetrieval apiRetrieval = new ApiRetrieval();
    List<String> campuses = new ArrayList<>();
    String userid = "";
    int allergenMatrix = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_select);
        getSupportActionBar().setTitle("CampusFood | Select a Campus");

        userid = checkUserExists();
        btnAllergen = (Button) findViewById(R.id.btnAllergens);
        btnAllergen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAllergens(v);
            }
        });
        btnOrders = (Button) findViewById(R.id.btnOrders);
        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOrders(v);
            }
        });

        // Campus List
        apiRetrieval.delegate = this;
        apiRetrieval.execute("campuses");
    }

    @Override
    public void onItemClick(View view, int position, int request) {
        Intent i = new Intent(this, LocationSelect.class);
        i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        i.putExtra("CAMPUS", campuses.get(position));
        i.putExtra("USER", userid);
        i.putExtra("ALLERGEN", allergenMatrix);
        startActivity(i);
    }

    @Override
    public void processFinish(String response) {
        campuses = processResult(response);

        recyclerView = findViewById(R.id.campusList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapterString(this, campuses);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            allergenMatrix = data.getIntExtra("ALLERGEN", 0);
            try {
                File file2 = new File(this.getFilesDir(), "allergen.txt");
                BufferedWriter writer2 = new BufferedWriter(new FileWriter(file2, false));
                writer2.write(Integer.toString(allergenMatrix));
                writer2.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> processResult(String json) {
        List<String> output = new ArrayList<>();
        try {
            JSONArray obj = new JSONArray(json);
            for (int i = 0; i < obj.length(); i++) {
                output.add((String) obj.getJSONObject(i).get("CampusName"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return output;
    }

    public void viewAllergens(View v) {
        Intent i = new Intent(this, UserSettingsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        i.putExtra("ALLERGEN", allergenMatrix);
        startActivityForResult(i, 1);
    }

    public void viewOrders(View v) {
        Intent i = new Intent(this, OrdersActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        i.putExtra("USER", userid);
        startActivity(i);
    }

    public String checkUserExists() {
        String user = "";
        BufferedReader br;
        try {
            File file = new File(this.getFilesDir(), "user.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            br = new BufferedReader(new FileReader(new File(this.getFilesDir(), "user.txt")));
            String line;
            while ((line = br.readLine()) != null) {
                user = line;
            }
            if (user.equals("")) {
                user = UUID.randomUUID().toString();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
                writer.write(user);
                writer.close();
                System.out.println("New user: " + user);
            } else {
                System.out.println("Current user found: " + user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File file2 = new File(this.getFilesDir(), "allergen.txt");
            if (!file2.exists()) {
                file2.createNewFile();
            }
            br = new BufferedReader(new FileReader(new File(this.getFilesDir(), "allergen.txt")));
            String line2;
            while ((line2 = br.readLine()) != null) {
                allergenMatrix = Integer.parseInt(line2);
            }
            if (allergenMatrix == 0) {
                BufferedWriter writer2 = new BufferedWriter(new FileWriter(file2, false));
                writer2.write("0");
                writer2.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}