package com.to.hacks;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Regstraion extends AppCompatActivity {

    AppCompatTextView alreadylogin;
    TextInputEditText rname,remail,rpassword,rconfirmpsw;
    AppCompatButton register;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    DatabaseReference reff;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regstraion);

        progressDialog = new ProgressDialog(this);

        alreadylogin = findViewById(R.id.alredyregister);
        rname=findViewById(R.id.rname);
        remail=findViewById(R.id.remail);
        rpassword=findViewById(R.id.rpassword);
        rconfirmpsw=findViewById(R.id.rconfirmPassword);
        register=findViewById(R.id.rregister);

        //it will go to register page
        alreadylogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Regstraion.this,Login.class);
                startActivity(i);
            }
        });

        //when click Register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = rname.getText().toString();
                String email = remail.getText().toString();
                String psw = rpassword.getText().toString();
                String psw2 = rconfirmpsw.getText().toString();

                if(name.isEmpty())
                {
                    rname.setError("please Enter Your Name");
                }
                else if(email.isEmpty())
                {
                    remail.setError("please Enter Your email");
                }
                else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    remail.setError("invalid Email address");
                }
                else if(!psw.equals(psw2))
                {
                    Toast.makeText(Regstraion.this, "Password Not Match", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog.setMessage("Registering....");
                    progressDialog.show();
                    validate();
                }

            }
        });
    }

    public void validate()
    {
        String email = remail.getText().toString();
        String encrypted = "";
        try {
            encrypted = AESUtils.encrypt(email);
            Log.d("TEST", "encrypted:" + encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }

        reff= FirebaseDatabase.getInstance().getReference().child("User").child(encrypted);
        reff.addValueEventListener(new ValueEventListener() {
            @SuppressLint({"SetTextI18n", "LongLogTag"})
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                int x= (int) snapshot.getChildrenCount();
                if(x>0)
                {
                    // String givemoney = Objects.requireNonNull(snapshot.child("give").getValue()).toString();
                    progressDialog.dismiss();
                    remail.setError("Already Registered");


                }
                else
                {
                    updateuserdata();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //it will update user data to Firebase
    public void updateuserdata()
    {
        String name = rname.getText().toString();
        String email = remail.getText().toString();
        String psw = rpassword.getText().toString();

        String encrypted = "";
        String ename = "";
        String eemail= "";
        String epsw = "";
        try {
            encrypted = AESUtils.encrypt(email);
            ename = AESUtils.encrypt(name);
            eemail = AESUtils.encrypt(email);
            epsw = AESUtils.encrypt(psw);
        } catch (Exception e) {
            e.printStackTrace();
        }

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("User");

        RegisterHelperClass registerHelperClass = new RegisterHelperClass(ename,eemail,epsw);
        reference.child(encrypted).setValue(registerHelperClass);
        progressDialog.dismiss();

        SharedPreferences.Editor editor = getSharedPreferences("DATA", MODE_PRIVATE).edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("evalue", encrypted);
        editor.putString("R", "1");
        editor.apply();


        Intent i = new Intent(Regstraion.this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();

    }


}