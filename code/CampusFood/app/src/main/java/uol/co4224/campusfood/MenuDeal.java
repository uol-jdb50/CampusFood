package uol.co4224.campusfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.mvel2.MVEL;

import java.util.ArrayList;
import java.util.List;

public class MenuDeal extends AppCompatActivity implements RecyclerViewAdapterString.ItemClickListener, RecyclerViewAdapterItem.ItemClickListener, ApiResponse {

    RecyclerView groups;
    RecyclerView items;
    Button submit;
    Basket basket;

    ApiRetrieval apiRetrieval;
    RecyclerViewAdapterString groupAdapter;
    RecyclerViewAdapterItem itemAdapter;

    Deal deal;
    String currentGroup;
    List<String> groupList = new ArrayList<>();
    List<Item> currentItemGroup = new ArrayList<>();
    List<Item> selected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_deal);
        getSupportActionBar().setTitle("CampusFood | Deal Configuration");
        basket = (Basket) getIntent().getSerializableExtra("BASKET");
        deal = (Deal) getIntent().getSerializableExtra("DEAL");
        submit = (Button) findViewById(R.id.btnSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(v);
            }
        });

        groups = (RecyclerView) findViewById(R.id.lstGroup);
        groups.setLayoutManager(new LinearLayoutManager(this));

        List<String> allItems = deal.getAllItemId();
        String stringList = allItems.toString();
        stringList = stringList.replace("[",  "[{\"id\":\"")
                .replace("]","\"}]")
                .replace(",", "")
                .replace(" ", "\"},{\"id\":\"");

        items = (RecyclerView) findViewById(R.id.lstItem);
        items.setLayoutManager(new LinearLayoutManager(this));

        apiRetrieval = new ApiRetrieval();
        apiRetrieval.delegate = this;
        apiRetrieval.execute("manyitems", stringList);
    }

    @Override
    public void onItemClick(View view, int position, int request) {
        if (request == 1) {//Item
            if (selected.contains(currentItemGroup.get(position))) {
                selected.remove(currentItemGroup.get(position));
            } else {
                selected.add(currentItemGroup.get(position));
                selected.get(selected.size() - 1).setCategory(currentGroup);
            }
            updateSubmitButton();
        } else if (request == 2) {//String (Group)
            currentGroup = "Group " + (position + 1);
            List<Item> groupItems = deal.getItemGroup(position + 1);
            currentItemGroup = groupItems;
            itemAdapter = new RecyclerViewAdapterItem(this, groupItems);
            itemAdapter.setClickListener(this);
            items.setAdapter(itemAdapter);
        }
    }

    @Override
    public void processFinish(String response) {
        try {
            JSONArray items = new JSONArray(response);
            for (int i = 0; i < items.length(); i++) {
                deal.itemDirectory.add(new Item((String) items.getJSONObject(i).get("ItemID"), (String) items.getJSONObject(i).get("Name"), (String) items.getJSONObject(i).get("Description"), "", 0, (int) items.getJSONObject(i).get("AllergenMatrix"), (int) items.getJSONObject(i).get("ListOrder")));
                deal.setLastItemToDealPart((String) items.getJSONObject(i).get("ItemID"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int count = 1;
        for (DealPart p: deal.getDeal()) {
            if (p.type == DealPart.PartType.ITEMGROUP_OPEN) {
                groupList.add("Group " + count);
                count++;
            }
        }
        groupAdapter = new RecyclerViewAdapterString(this, groupList);
        groupAdapter.setClickListener(this);
        groups.setAdapter(groupAdapter);
    }

    public void updateSubmitButton() {
        submit.setText("Submit (" + selected.size() + " Items Selected)");
    }

    public void validate(View v) {
        String validationInfo = "";
        int[] groups = new int[groupList.size()];
        for (Item i: selected) {
            String category = i.getCategory();
            while (true) {
                if (category.charAt(0) == ' ') {
                    groups[Integer.parseInt(category.substring(1)) - 1] += 1;
                    break;
                }
                category = category.substring(1);
            }
        }
        for (int i = 0; i < groups.length; i++) {
            if (groups[i] > 1) {
                validationInfo += "Group " + (i + 1) + " must have one item selected.\n";
            }
        }
        String shortenedDeal = deal.shortenDeal();
        boolean valid = checkDeal(shortenedDeal, groups);
        if (!valid) validationInfo += "Items selected do not fit deal structure.";
        if (validationInfo.equals("")) {
            //Deal valid, add to basket
            for (Item i: selected) {
                basket.addDealItem(i);
            }
            basket.addDealPrice(deal.getPrice());
            Intent intent = new Intent();
            intent.putExtra("BASKET", basket);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            //Deal invalid, display message
            new AlertDialog.Builder(this)
                    .setTitle("Deal Invalid")
                    .setMessage(validationInfo)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
    }
    public boolean checkDeal(String shortenedDeal, int[] groups) {
        String booleanCheck = "";
        while (true) {
            if (shortenedDeal.equals("")) break;
            if (shortenedDeal.charAt(0) == '[') {booleanCheck += "(";}
            else if (shortenedDeal.charAt(0) == ']') {booleanCheck += ")";}
            else if (shortenedDeal.charAt(0) == '{') {booleanCheck += (groups[getGroupValue(shortenedDeal.substring(1)) - 1] == 1);}
            else if (shortenedDeal.charAt(0) == '*') {booleanCheck += " && ";}
            else if (shortenedDeal.charAt(0) == '/') {booleanCheck += " != ";}
            shortenedDeal = shortenedDeal.substring(1);
        }

        return (boolean) MVEL.eval(booleanCheck);
    }
    public int getGroupValue(String s) {
        String num = "";
        while (true) {
            if (s.charAt(0) == '}') return Integer.parseInt(num);
            num += s.charAt(0);
            s = s.substring(1);
        }
    }
}