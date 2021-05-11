package com.to.hacks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button logout;
    TextView name,email;
    DatabaseReference reff;
    String evalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout=findViewById(R.id.logout);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        SharedPreferences prefs = getSharedPreferences("DATA", MODE_PRIVATE);
        String sname = prefs.getString("name", "");
        String semail = prefs.getString("email", "");
        evalue = prefs.getString("evalue", "");

        name.setText("name: "+sname);
        email.setText("email: "+semail);

        if (sname.isEmpty())
        {
            getdata();
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences("DATA", MODE_PRIVATE).edit();
                editor.putString("R", "0");
                editor.apply();
                Intent i = new Intent(MainActivity.this,Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

            }
        });
    }

    public void getdata()
    {

        reff= FirebaseDatabase.getInstance().getReference().child("User").child(evalue);
        reff.addValueEventListener(new ValueEventListener() {
            @SuppressLint({"SetTextI18n", "LongLogTag"})
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                int x= (int) snapshot.getChildrenCount();
                if(x>0)
                {
                    String email2 = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    String password2 = Objects.requireNonNull(snapshot.child("name").getValue()).toString();

                    String email1 = "";
                    String name1 = "";
                    try {
                        email1 = AESUtils.decrypt(email2);
                        name1 = AESUtils.decrypt(password2);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SharedPreferences.Editor editor = getSharedPreferences("DATA", MODE_PRIVATE).edit();
                    editor.putString("name", name1);
                    editor.putString("email", email1);
                    editor.apply();
                    name.setText("name: "+name1);
                    email.setText("email: "+email1);

                }
                else
                {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}