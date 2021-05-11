package com.to.hacks;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {

    AppCompatTextView create_account;

    TextInputEditText lemail,lpassword;

    AppCompatButton login;
    DatabaseReference reff;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        create_account = findViewById(R.id.create);
        lemail=findViewById(R.id.lemail);
        lpassword=findViewById(R.id.lpassword);
        login=findViewById(R.id.lLogin);



        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this,Regstraion.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = lemail.getText().toString();
                String psw = lpassword.getText().toString();

                if(email.isEmpty())
                {
                    lemail.setError("please Enter Your email");
                }
                else if(psw.isEmpty())
                {
                    lpassword.setError("please Enter Your password");
                }
                else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    lemail.setError("invalid Email address");
                }
                else
                {
                    progressDialog.setMessage("Pleas Wait....");
                    progressDialog.show();
                    validate();
                }


            }
        });
    }

    public void validate()
    {
        String email = lemail.getText().toString();
        String psw = lpassword.getText().toString();

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
                    String email2 = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    String password2 = Objects.requireNonNull(snapshot.child("password").getValue()).toString();
                    String verify=email2+password2;
                    if(verify.equals(email+psw))
                    {
                        Intent i = new Intent(Login.this,MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                    else
                    {
                        lpassword.setError("Invalid password");
                        progressDialog.dismiss();
                    }


                }
                else
                {
                    lemail.setError("This Mail Not Registered");
                    progressDialog.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}