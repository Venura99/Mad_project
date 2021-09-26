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

import com.example.ceylonparadise.Models.ReservesRooms;
import com.example.ceylonparadise.Models.RoomDetails;
import com.example.ceylonparadise.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RoomsList extends AppCompatActivity {

    Button button;
    ListView listView;
    List<RoomDetails> user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms_list);

        listView = (ListView) findViewById(R.id.listview);
        button = (Button) findViewById(R.id.gotochannelling);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomsList.this, ManageRooms.class);
                startActivity(intent);
            }
        });

        user = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("RoomDetails");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()) {

                    RoomDetails roomDetails = studentDatasnap.getValue(RoomDetails.class);
                    user.add(roomDetails);
                }

                MyAdapter adapter = new MyAdapter(RoomsList.this, R.layout.custom_rooms, (ArrayList<RoomDetails>) user);
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

    class MyAdapter extends ArrayAdapter<RoomDetails> {
        LayoutInflater inflater;
        Context myContext;
        List<RoomDetails> user;


        public MyAdapter(Context context, int resource, ArrayList<RoomDetails> objects) {
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
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.custom_rooms, null);

                holder.COL1 = (TextView) view.findViewById(R.id.roomName);
                holder.COL2 = (TextView) view.findViewById(R.id.romPrice);
                holder.imageView = (ImageView) view.findViewById(R.id.roomImage);
                holder.button = (Button) view.findViewById(R.id.reserve);


                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.COL1.setText(user.get(position).getName());
            holder.COL2.setText(user.get(position).getPrice());
            Picasso.get().load(user.get(position).getImage()).into(holder.imageView);
            System.out.println(holder);


            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view = inflater.inflate(R.layout.custom_rooms_details, null);
                    dialogBuilder.setView(view);

                    final TextView textView1 = (TextView) view.findViewById(R.id.cd_name);
                    final TextView textView2 = (TextView) view.findViewById(R.id.cd_price);
                    final TextView textView3 = (TextView) view.findViewById(R.id.cd_description);
                    final ImageView imageView1 = (ImageView) view.findViewById(R.id.cd_image);
                    final EditText editText1 = (EditText) view.findViewById(R.id.cuname);
                    final EditText editText2 = (EditText) view.findViewById(R.id.cunic);
                    final EditText editText3 = (EditText) view.findViewById(R.id.cucontat);
                    final EditText editText4 = (EditText) view.findViewById(R.id.cuaddress);
                    final Button buttonAdd = (Button) view.findViewById(R.id.ureservenow);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final String idd = user.get(position).getId();
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RoomDetails").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = (String) snapshot.child("id").getValue();
                            String name = (String) snapshot.child("name").getValue();
                            String price = (String) snapshot.child("price").getValue();
                            String description = (String) snapshot.child("description").getValue();
                            String image = (String) snapshot.child("image").getValue();

                            textView1.setText(name);
                            textView2.setText(price);
                            textView3.setText(description);
                            Picasso.get().load(image).into(imageView1);

                            buttonAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ReservedRooms");

                                    final String username = editText1.getText().toString();
                                    final String nic = editText2.getText().toString();
                                    final String contact = editText3.getText().toString();
                                    final String address = editText4.getText().toString();

                                    String image = snapshot.child("image").getValue().toString();

                                    if (username.isEmpty()) {
                                        editText1.setError("Name is required");
                                    }else if (nic.isEmpty()) {
                                        editText2.setError("NIC is required");
                                    }else if (contact.isEmpty()) {
                                        editText3.setError("Contact Number is required");
                                    }else if (address.isEmpty()) {
                                        editText4.setError("Address is required");
                                    }else {

                                        String name = textView1.getText().toString();
                                        Integer price = Integer.valueOf(textView2.getText().toString());
                                        String description = textView3.getText().toString();

                                        Integer tax = (price*2) / 100 ;
                                        String total = String.valueOf(price+tax);


                                        ReservesRooms reservesRooms = new ReservesRooms(id, name,total, description, image, username, nic, contact, address);
                                        reference.child(id).setValue(reservesRooms);

                                        Toast.makeText(RoomsList.this, "Successfully added", Toast.LENGTH_SHORT).show();

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