package uol.co4224.campusfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MenuCategory extends AppCompatActivity implements RecyclerViewAdapterCategory.ItemClickListener, ApiResponse {

    String campus;
    String location;
    String userid;
    Date currentDate;
    Date date;
    Basket basket;
    SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat userFormat = new SimpleDateFormat("E, MMMM dd yyyy");
    SimpleDateFormat dayFormat = new SimpleDateFormat("E");

    ApiRetrieval apiRetrieval = new ApiRetrieval();
    String lastRetrievalCode;
    List<Category> categories = new ArrayList<>();

    TextView tvDate;
    TextView tvOpening;
    RecyclerView categoryList;
    RecyclerViewAdapterCategory adapter;
    RecyclerViewAdapterEmpty adapterEmpty;
    Button basketButton;
    Button nextDay;
    Button prevDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_category);


        campus = getIntent().getStringExtra("CAMPUS");
        location = getIntent().getStringExtra("LOCATION");
        userid = getIntent().getStringExtra("USER");
        currentDate = new Date();
        date = new Date();
        basket = (Basket) getIntent().getSerializableExtra("BASKET");

        getSupportActionBar().setTitle("CampusFood | " + location);

        tvDate = (TextView) findViewById(R.id.tvDate);
        tvOpening = (TextView) findViewById(R.id.tvOpening);
        basketButton = (Button) findViewById(R.id.btnBasket);
        basketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewBasket(v);
            }
        });
        nextDay = (Button) findViewById(R.id.btnNextDayCat);
        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                Date endDate = currentDate;
                c.setTime(endDate);
                c.add(Calendar.DATE, 14);
                if (c.getTime().after(date)) {
                    c.setTime(date);
                    c.add(Calendar.DATE, 1);
                    date = c.getTime();
                    updateLocationStatus();
                    basket = new Basket(campus, location, date, basket.getAllergenMatrix());
                    updateBasketButton();
                }
            }
        });
        prevDay = (Button) findViewById(R.id.btnPrevDayCat);
        prevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDate.before(date)) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    c.add(Calendar.DATE, -1);
                    date = c.getTime();
                    updateLocationStatus();
                    basket = new Basket(campus, location, date, basket.getAllergenMatrix());
                    updateBasketButton();
                }
            }
        });

        tvDate.setText(userFormat.format(date));
        updateBasketButton();

        apiRetrieval.delegate = this;
        lastRetrievalCode = "category";
        apiRetrieval.execute("category", location);
    }

    @Override
    public void processFinish(String response) {
        if (lastRetrievalCode.equals("category")) {
            categories = processResult(response);
            if (categories.size() != 1) {
                categoryList = findViewById(R.id.itemList);
                categoryList.setLayoutManager(new LinearLayoutManager(this));
                adapter = new RecyclerViewAdapterCategory(this, categories);
                adapter.setClickListener(this);
                categoryList.setAdapter(adapter);
            } else {
                List<String> empty = new ArrayList<>();
                empty.add("No Menu Found");
                categoryList = findViewById(R.id.itemList);
                categoryList.setLayoutManager(new LinearLayoutManager(this));
                adapterEmpty = new RecyclerViewAdapterEmpty(this, empty);
                categoryList.setAdapter(adapterEmpty);
            }
            updateLocationStatus();
        } else if (lastRetrievalCode.equals("opening_gen")) {
            try {
                JSONObject obj = new JSONObject(response);
                obj = obj.getJSONObject("ResultSets").getJSONArray("Table1").getJSONObject(0);
                boolean isopen = obj.getBoolean("IsOpen");
                int open = obj.getInt("OpenTime");
                int close = obj.getInt("CloseTime");

                String output = "";

                if (!isopen) output = "Closed";
                else {
                   output = "Open: ";
                   output += open / 2 + ":";
                   output += ((open % 2 == 0) ? "00 - " : "30 - ");
                   output += close / 2 + ":";
                   output += ((close % 2 == 0) ? "00" : "30");
                }
                tvOpening.setText(output);
            } catch (JSONException e) {

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (lastRetrievalCode.equals("opening_spec")) {
            try {
                JSONObject obj = new JSONObject(response);
                obj = obj.getJSONObject("ResultSets");
                if (obj.toString().equals("{}")) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    int dayofweek = 0;
                    switch (dayFormat.format(date)) {
                        case "Mon":
                            dayofweek = 0;
                            break;
                        case "Tue":
                            dayofweek = 1;
                            break;
                        case "Wed":
                            dayofweek = 2;
                            break;
                        case "Thu":
                            dayofweek = 3;
                            break;
                        case "Fri":
                            dayofweek = 4;
                            break;
                        case "Sat":
                            dayofweek = 5;
                            break;
                        case "Sun":
                            dayofweek = 6;
                            break;
                    }
                    apiRetrieval = new ApiRetrieval();
                    apiRetrieval.delegate = this;
                    lastRetrievalCode = "opening_gen";
                    apiRetrieval.execute("opening_gen", Integer.toString(dayofweek), location);
                } else {
                    JSONArray arr = obj.getJSONArray("Table1");
                    boolean isopen = arr.getJSONObject(0).getBoolean("IsOpen");
                    int open = arr.getJSONObject(0).getInt("OpenTime");
                    int close = arr.getJSONObject(0).getInt("CloseTime");
                    String output = "";

                    if (!isopen) output = "Closed";
                    else {
                        output = "Open: ";
                        output += open / 2 + ":";
                        output += ((open % 2 == 0) ? "00 - " : "30 - ");
                        output += close / 2 + ":";
                        output += ((close % 2 == 0) ? "00" : "30");
                    }
                    tvOpening.setText(output);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Category> processResult(String json) {
        List<Category> output = new ArrayList<>();
        output.add(new Category("Deals", -10));
        try {
            JSONObject obj1 = new JSONObject(json);
            JSONArray obj;
            obj = obj1.getJSONArray("Table1");
            for (int i = 0; i < obj.length(); i++) {
                output.add(new Category((String) obj.getJSONObject(i).get("CategoryName"), (int) obj.getJSONObject(i).get("CategoryOrder")));
            }
        } catch (JSONException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(output, new CategoryComparator());
        return output;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent i = new Intent(this, MenuItem.class);
        i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        i.putExtra("CAMPUS", campus);
        i.putExtra("LOCATION", location);
        i.putExtra("CATEGORY", categories.get(position).getName());
        i.putExtra("USER", userid);
        i.putExtra("DATE", sqlFormat.format(date));
        i.putExtra("BASKET", basket);
        startActivityForResult(i, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                basket = (Basket) data.getSerializableExtra("BASKET");
            }
        }
        updateBasketButton();
    }

    public void updateBasketButton() {
        basketButton.setText("View Basket - " + basket.getItemCount() + " items - £" + String.format("%.2f", basket.getTotal()));
    }

    public void updateLocationStatus() {
        tvDate.setText(userFormat.format(date));
        lastRetrievalCode = "opening_spec";
        apiRetrieval = new ApiRetrieval();
        apiRetrieval.delegate = this;
        apiRetrieval.execute("opening_spec", sqlFormat.format(date), location);
    }

    public void viewBasket(View v) {
        Intent intent = new Intent(this, BasketActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.putExtra("USER", userid);
        intent.putExtra("BASKET", basket);
        startActivity(intent);
    }
}