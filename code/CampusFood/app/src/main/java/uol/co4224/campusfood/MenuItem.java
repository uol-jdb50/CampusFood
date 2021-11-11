package uol.co4224.campusfood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MenuItem extends AppCompatActivity implements RecyclerViewAdapterItem.ItemClickListener, RecyclerViewAdapterDeal.ItemClickListener, ApiResponse {

    String campus;
    String location;
    String category;
    String userid;
    String lastRetrievalCode;
    Date date;
    Basket basket;
    SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat userFormat = new SimpleDateFormat("E, MMMM dd yyyy");

    ApiRetrieval apiRetrieval = new ApiRetrieval();
    List<Item> items = new ArrayList<>();
    List<Deal> deals = new ArrayList<>();

    TextView tvDate;
    TextView tvOpening;
    RecyclerView categoryList;
    RecyclerViewAdapterItem adapter;
    RecyclerViewAdapterDeal adapterDeal;
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
        category = getIntent().getStringExtra("CATEGORY");
        userid = getIntent().getStringExtra("USER");
        try {
            date = sqlFormat.parse(getIntent().getStringExtra("DATE"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        prevDay = (Button) findViewById(R.id.btnPrevDayCat);
        nextDay.setEnabled(false);
        prevDay.setEnabled(false);

        tvDate.setText(userFormat.format(date));
        updateBasketButton();

        apiRetrieval.delegate = this;
        if (category.equals("Deals")) {
            lastRetrievalCode = "deals";
            apiRetrieval.execute("deals", sqlFormat.format(date), location);
        } else {
            lastRetrievalCode = "item";
            apiRetrieval.execute("item", category, sqlFormat.format(date), location);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("BASKET", basket);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void processFinish(String response) {
        if (category.equals("Deals")) {
            deals = processResult(response);
            if (!deals.isEmpty()) {
                categoryList = findViewById(R.id.itemList);
                categoryList.setLayoutManager(new LinearLayoutManager(this));
                adapterDeal = new RecyclerViewAdapterDeal(this, deals);
                adapterDeal.setClickListener(this);
                categoryList.setAdapter(adapterDeal);
            } else {
                List<String> empty = new ArrayList<>();
                empty.add("No Deals Found");
                categoryList = findViewById(R.id.itemList);
                categoryList.setLayoutManager(new LinearLayoutManager(this));
                adapterEmpty = new RecyclerViewAdapterEmpty(this, empty);
                categoryList.setAdapter(adapterEmpty);
            }
        } else if (lastRetrievalCode.equals("item")) {
            items = processResult(response);
            if (items != null) {
                lastRetrievalCode = "offers";
                apiRetrieval = new ApiRetrieval();
                apiRetrieval.delegate = this;
                apiRetrieval.execute("offers", sqlFormat.format(date), location);
            } else {
                List<String> empty = new ArrayList<>();
                empty.add("No Items Found");
                categoryList = findViewById(R.id.itemList);
                categoryList.setLayoutManager(new LinearLayoutManager(this));
                adapterEmpty = new RecyclerViewAdapterEmpty(this, empty);
                categoryList.setAdapter(adapterEmpty);
            }
        } else {
            processResult(response);
            categoryList = findViewById(R.id.itemList);
            categoryList.setLayoutManager(new LinearLayoutManager(this));
            adapter = new RecyclerViewAdapterItem(this, items);
            adapter.setClickListener(this);
            categoryList.setAdapter(adapter);
        }
    }

    public <T> List<T> processResult(String json) {
        if (category.equals("Deals")) {
            try {
                List<Deal> output = new ArrayList<>();
                JSONObject d = new JSONObject(json);
                JSONArray da = d.getJSONArray("Table1");
                for (int i = 0; i < da.length(); i++) {
                    output.add(new Deal((String) da.getJSONObject(i).get("DealString"), (String) da.getJSONObject(i).get("Name"), (String) da.getJSONObject(i).get("Description"), sqlFormat.parse((String) da.getJSONObject(i).get("EndDate")), (double) da.getJSONObject(i).get("Price")));
                }
                return (List<T>) output;
            } catch (JSONException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (lastRetrievalCode.equals("item")) {
                List<Item> output = new ArrayList<>();
                try {
                    JSONObject obj1 = new JSONObject(json);
                    JSONArray obj;
                    obj = obj1.getJSONArray("Table1");
                    for (int i = 0; i < obj.length(); i++) {
                        output.add(new Item((String) obj.getJSONObject(i).get("ItemID"), (String) obj.getJSONObject(i).get("Name"), (String) obj.getJSONObject(i).get("Description"), category, (double) obj.getJSONObject(i).get("Price"), (int) obj.getJSONObject(i).get("AllergenMatrix"), (int) obj.getJSONObject(i).get("ListOrder")));
                    }
                    Collections.sort(output, new ItemComparator());
                    return (List<T>) output;
                } catch (JSONException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (lastRetrievalCode.equals("offers")) {
                try {
                    JSONObject obj1 = (new JSONObject(json)).getJSONObject("ResultSets");
                    JSONArray obj;
                    obj = obj1.getJSONArray("Table1");
                    for (int i = 0; i < obj.length(); i++) {
                        for (Item t: items) {
                            if (obj.getJSONObject(i).getString("ItemID").equals(t.getId())) {
                                t.setPrice(obj.getJSONObject(i).getDouble("OfferPrice"));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void onItemClick(View view, int position, int request) {
        if (basket.getCampus().equals(campus) && basket.getLocation().equals(location)) {
            if (category.equals("Deals")) {
                Intent i = new Intent(this, MenuDeal.class);
                i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                i.putExtra("BASKET", basket);
                i.putExtra("DEAL", deals.get(position));
                startActivityForResult(i, 1);
            } else {
                basket.addItem(items.get(position));
                Toast.makeText(this, "Added " + items.get(position).getName() + " to your basket", Toast.LENGTH_SHORT);
                updateBasketButton();
            }
        }
    }

    public void updateBasketButton() {
        basketButton.setText("View Basket - " + basket.getItemCount() + " items - £" + String.format("%.2f", basket.getTotal()));
    }

    public void viewBasket(View v) {
        Intent intent = new Intent(this, BasketActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.putExtra("USER", userid);
        intent.putExtra("BASKET", basket);
        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (basket.getCampus().equals(campus) && basket.getLocation().equals(location)) {
            if (category.equals("Deals")) {
                Intent i = new Intent(this, MenuDeal.class);
                i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                i.putExtra("DEAL", deals.get(position));
                i.putExtra("BASKET", basket);
                startActivityForResult(i, 1);
            } else {
                basket.addItem(items.get(position));
                Toast.makeText(this, "Added " + items.get(position).getName() + " to your basket", Toast.LENGTH_SHORT);
                updateBasketButton();
            }
        }
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
}