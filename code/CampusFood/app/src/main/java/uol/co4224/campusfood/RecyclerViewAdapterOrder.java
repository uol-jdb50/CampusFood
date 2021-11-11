package uol.co4224.campusfood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapterOrder extends RecyclerView.Adapter<RecyclerViewAdapterOrder.ViewHolder> implements ApiResponse {
    private List<Order> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private SimpleDateFormat displayFormat = new SimpleDateFormat("E, MMMM dd yyyy");
    private ApiRetrieval apiRetrieval;
    private Context mContext;

    // data is passed into the constructor
    RecyclerViewAdapterOrder(Context context, List<Order> data) {
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order object = mData.get(position);
        holder.myTextView.setText(displayFormat.format(object.getCollectDate()) + " | £" + String.format("%.2f", object.getPrice()));
        holder.row2.setText("Collection Name: " + object.getCollectName());
        holder.row3.setText("Collect From: " + object.getLocation() + ", " + object.getCampus());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void processFinish(String response) {
        if (response.equals("\nOrder successfully deleted")) {
            Toast.makeText(mContext, "Order deleted successfully.", Toast.LENGTH_LONG);
        } else {
            boolean verify = false;
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray arr = obj.getJSONArray("Table1");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject check = arr.getJSONObject(i);
                    verify = check.getBoolean("CheckedIn");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (verify == false) {
                Toast.makeText(mContext, "Verification failed. Please try again.", Toast.LENGTH_LONG);
            } else if (verify == true) {
                System.out.println("Checked in successfully");
                Toast.makeText(mContext, "Checked in successfully.", Toast.LENGTH_LONG);
            }
        }
    }

    public void checkIn(int position) {
        Order order = mData.get(position);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        if (fmt.format(order.getCollectDate()).equals(fmt.format(new Date()))) {
            final String[] verify = {""};
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            final EditText input = new EditText(mContext);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    postCheckIn(order, input.getText().toString());
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }

    public void postCheckIn(Order order, String verify) {
        apiRetrieval = new ApiRetrieval();
        apiRetrieval.delegate = this;
        apiRetrieval.execute("checkin", order.getId(), order.getLocation(), order.getCampus(), verify);
    }

    public void cancelOrder(int position) {
        Order order = mData.get(position);
        apiRetrieval = new ApiRetrieval();
        apiRetrieval.delegate = this;
        apiRetrieval.execute("cancel", order.getId());
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;
        TextView row2;
        TextView row3;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvName);
            row2 = itemView.findViewById(R.id.tvDescription);
            row3 = itemView.findViewById(R.id.tvAllergens);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickListener != null) {
                        int position = getAdapterPosition();
                        PopupMenu popup = new PopupMenu(row3.getContext(), itemView);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if (item.getTitle().equals("Check In")) {
                                    checkIn(position);
                                } else if (item.getTitle().equals("Cancel Order")) {
                                    cancelOrder(position);
                                }
                                return true;
                            }
                        });
                        popup.inflate(R.menu.popup_menu);
                        popup.setGravity(Gravity.RIGHT);
                        popup.show();
                    }
                }
            });
        }
    }

    // convenience method for getting data at click position
    Order getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
