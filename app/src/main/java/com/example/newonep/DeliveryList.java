package com.example.ceylonparadise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ceylonparadise.Models.DeliveryItems;
import com.example.ceylonparadise.Models.OrderDetails;
import com.example.ceylonparadise.Models.OrderedItems;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DeliveryList extends AppCompatActivity {

    Button button;
    ListView listView;
    List<DeliveryItems> user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_list);

        listView = (ListView) findViewById(R.id.listviewdel);
        button = (Button) findViewById(R.id.gotodelItms);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryList.this, SelectedDeliveryList.class);
                startActivity(intent);
            }
        });

        user = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("OrderDetails");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()) {

                    DeliveryItems deliveryItems = studentDatasnap.getValue(DeliveryItems.class);
                    user.add(deliveryItems);
                }

                MyAdapter adapter = new MyAdapter(DeliveryList.this, R.layout.custom_delivery_items, (ArrayList<DeliveryItems>) user);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder {

        ImageView imageView;
        TextView COL1;
        TextView COL2;
        Button button;
    }

    class MyAdapter extends ArrayAdapter<DeliveryItems> {
        LayoutInflater inflater;
        Context myContext;
        List<DeliveryItems> user;


        public MyAdapter(Context context, int resource, ArrayList<DeliveryItems> objects) {
            super(context, resource, objects);
            myContext = context;
            user = objects;
            inflater = LayoutInflater.from(context);
            int y;
            String barcode;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final OrderList.ViewHolder holder;
            if (view == null) {
                holder = new OrderList.ViewHolder();
                view = inflater.inflate(R.layout.custom_delivery_items, null);

                holder.COL1 = (TextView) view.findViewById(R.id.food_name);
                holder.COL2 = (TextView) view.findViewById(R.id.food_price);
                holder.imageView = (ImageView) view.findViewById(R.id.food_image);
                holder.button = (Button) view.findViewById(R.id.delivernow);


                view.setTag(holder);
            } else {

                holder = (OrderList.ViewHolder) view.getTag();
            }

            holder.COL1.setText(user.get(position).getName());
            holder.COL2.setText(user.get(position).getPrice());
            Picasso.get().load(user.get(position).getImage()).into(holder.imageView);
            System.out.println(holder);

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view = inflater.inflate(R.layout.custom_deliver_item_user, null);
                    dialogBuilder.setView(view);

                    final TextView textView1 = (TextView) view.findViewById(R.id.food_id);
                    final TextView textView2 = (TextView) view.findViewById(R.id.food_namee);
                    final TextView textView3 = (TextView) view.findViewById(R.id.food_pricee);
                    final ImageView imageView1 = (ImageView) view.findViewById(R.id.food_imagee);
                    final EditText editText1 = (EditText) view.findViewById(R.id.fooditemcuname);
                    final EditText editText2 = (EditText) view.findViewById(R.id.fooditemcunic);
                    final EditText editText3 = (EditText) view.findViewById(R.id.fooditemcucontat);
                    final EditText editText4 = (EditText) view.findViewById(R.id.fooditemcuaddress);
                    final EditText editText5 = (EditText) view.findViewById(R.id.fooditemcuqty);
                    final Button buttonAdd = (Button) view.findViewById(R.id.fooditemuordernow);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final String idd = user.get(position).getId();
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("OrderDetails").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = (String) snapshot.child("id").getValue();
                            String name = (String) snapshot.child("name").getValue();
                            String price = (String) snapshot.child("price").getValue();
                            String image = (String) snapshot.child("image").getValue();

                            textView1.setText(id);
                            textView2.setText(name);
                            textView3.setText(price);
                            Picasso.get().load(image).into(imageView1);

                            buttonAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DeliveryItems");

                                    final String username = editText1.getText().toString();
                                    final String nic = editText2.getText().toString();
                                    final String contact = editText3.getText().toString();
                                    final String address = editText4.getText().toString();
                                    final String qty = editText5.getText().toString();

                                    String image = snapshot.child("image").getValue().toString();

                                    if (username.isEmpty()) {
                                        editText1.setError("Name is required");
                                    }else if (nic.isEmpty()) {
                                        editText2.setError("NIC is required");
                                    }else if (contact.isEmpty()) {
                                        editText3.setError("Contact Number is required");
                                    }else if (address.isEmpty()) {
                                        editText4.setError("Address is required");
                                    }
                                    else if (qty.isEmpty()) {
                                        editText5.setError("Quantity is required");
                                    }else {

                                        Integer qtyval = Integer.valueOf(editText5.getText().toString());
                                        String id = textView1.getText().toString();
                                        String name = textView2.getText().toString();
                                        Integer price = Integer.valueOf(textView3.getText().toString());

                                        Integer tax = (price*2) / 100 ;
                                        Integer total = price+tax;
                                        String finalval = String.valueOf(total * qtyval);

                                        OrderedItems orderedItems = new OrderedItems(id,name,finalval,qty,image,username,nic,contact,address);
                                        reference.child(id).setValue(orderedItems);

                                        Toast.makeText(DeliveryList.this, "Successfully added", Toast.LENGTH_SHORT).show();

                                        alertDialog.dismiss();
                                    }

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });

                }

            });

            return view;

        }
    }
}