package uol.co4224.campusfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class OrdersActivity extends AppCompatActivity implements ApiResponse, RecyclerViewAdapterOrder.ItemClickListener {

    RecyclerView recyclerView;
    RecyclerViewAdapterOrder adapter;
    RecyclerViewAdapterEmpty adapterEmpty;
    ApiRetrieval apiRetrieval = new ApiRetrieval();
    String userid;
    List<Order> orders = new ArrayList<>();
    SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        userid = getIntent().getStringExtra("USER");
        recyclerView = (RecyclerView) findViewById(R.id.lstOrders);
        getSupportActionBar().setTitle("CampusFood | Your Orders");
        apiRetrieval.delegate = this;
        apiRetrieval.execute("getorders", userid, sqlFormat.format(new Date()));
    }

    @Override
    public void processFinish(String response) {

        try {
            JSONObject obj = new JSONObject(response);
            JSONArray arr = obj.getJSONArray("Table1");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject order = arr.getJSONObject(i);
                orders.add(new Order(order.getString("OrderID"), order.getString("CampusName"), order.getString("Name"), (Date) new SimpleDateFormat("yyyy-MM-dd").parse(order.getString("CollectDate")), order.getDouble("TotalPrice"), order.getString("CollectName")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(orders, new OrderComparator());
        if (!orders.isEmpty()) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new RecyclerViewAdapterOrder(this, orders);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        } else {
            List<String> empty = new ArrayList<>();
            empty.add("No Orders Found");
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapterEmpty = new RecyclerViewAdapterEmpty(this, empty);
            recyclerView.setAdapter(adapterEmpty);
        }

    }

    @Override
    public void onItemClick(View view, int position) {

    }
}