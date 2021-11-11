package uol.co4224.campusfood;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BasketActivity extends AppCompatActivity implements RecyclerViewAdapterItem.ItemClickListener, ApiResponse {

    RecyclerView itemList;
    RecyclerViewAdapterItem adapter;
    RecyclerViewAdapterEmpty adapterEmpty;
    EditText entryName;
    TextView tvLocation;
    TextView tvCollectDate;
    Button placeOrder;
    TextView tvTotal;

    Basket basket;
    String userid;

    SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat userFormat = new SimpleDateFormat("E, MMMM dd yyyy");

    ApiRetrieval apiRetrieval = new ApiRetrieval();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        basket = (Basket) getIntent().getSerializableExtra("BASKET");
        userid = getIntent().getStringExtra("USER");
        getSupportActionBar().setTitle("CampusFood | Your Basket");
        itemList = (RecyclerView) findViewById(R.id.itemList);
        entryName = (EditText) findViewById(R.id.entryName);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvCollectDate = (TextView) findViewById(R.id.tvCollectDate);
        placeOrder = (Button) findViewById(R.id.btnCheckout);
        if (basket.getBasket().isEmpty()) {
            placeOrder.setEnabled(false);
        } else {
            placeOrder.setEnabled(true);
        }
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (!basket.getBasket().isEmpty()) {
                    placeOrder(v);
                }
            }
        });
        tvTotal = (TextView) findViewById(R.id.tvTotal);

        tvLocation.setText("Collect from: " + basket.getLocation() + ", " + basket.getCampus());
        tvCollectDate.setText("Collect on: " + userFormat.format(basket.getDate()));
        tvTotal.setText("£" + String.format("%.2f", basket.getTotal()));

        itemList = findViewById(R.id.itemList);
        itemList.setLayoutManager(new LinearLayoutManager(this));
        if (!basket.getBasket().isEmpty()) {
            adapter = new RecyclerViewAdapterItem(this, basket.getBasket());
            adapter.setClickListener(this);
            itemList.setAdapter(adapter);
        } else {
            List<String> empty = new ArrayList<>();
            empty.add("Basket is empty");
            adapterEmpty = new RecyclerViewAdapterEmpty(this, empty);
            itemList.setAdapter(adapterEmpty);
        }

    }

    @Override
    public void processFinish(String response) {
            Intent intent = new Intent(this, CampusSelect.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void placeOrder(View v) {
        String order = "{";
        //Campus
        order += "\"campus\":\"" + basket.getCampus() + "\",";
        //Collect Date
        order += "\"collectdate\":\"" + sqlFormat.format(basket.getDate()) + "\",";
        //Location
        order += "\"location\":\"" + basket.getLocation() + "\",";
        //Name
        order += "\"name\":\"" + entryName.getText().toString() + "\",";
        //Order Date
        order += "\"orderdate\":\"" + sqlFormat.format(new Date()) + "\",";
        //Order ID
        order += "\"orderid\":\"" + UUID.randomUUID().toString() + "\",";
        //Total
        order += "\"total\":\"" + basket.getTotal() + "\",";
        //User ID
        order += "\"user\":\"" + userid + "\",";
        //Allergens
        order += "\"allergens\":\"" + basket.getAllergenMatrix() + "\",";
        //Items
        Map<Item, Integer> items = groupItems();
        order += "\"items\":[";
        List<Item> keys = new ArrayList<>(items.keySet());
        List<Integer> values = new ArrayList<>(items.values());
        for (int i = 0; i < items.size(); i++) {
            if (i == items.size() - 1) order += "{\"itemName\":\"" + keys.get(i).getName() + "\",\"category\":\"" + keys.get(i).getCategory() + "\", \"quantity\":" + values.get(i) + "}";
            else order += "{\"itemName\":\"" + keys.get(i).getName() + "\",\"category\":\"" + keys.get(i).getCategory() + "\", \"quantity\":" + values.get(i) + "},";
        }
        order += "]}";
        apiRetrieval.delegate = this;
        apiRetrieval.execute("setorder", order);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<Item, Integer> groupItems() {
        Map<Item, Integer> order = new HashMap<>();

        for (Item i: basket.getBasket()) {
            if (order.containsKey(i)) {
                order.replace(i, order.get(i), order.get(i) + 1);
            } else {
                order.put(i, 1);
            }
        }
        return order;
    }

    @Override
    public void onItemClick(View view, int position, int request) {

    }
}