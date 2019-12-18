package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class REgisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputName,InputPhoneNumber,InputPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        CreateAccountButton=(Button) findViewById(R.id.register_btn);
        InputName=(EditText) findViewById(R.id.register_username_input);
        InputPhoneNumber=(EditText) findViewById(R.id.register_phone_number_input);
        InputPassword=(EditText) findViewById(R.id.register_password_input);

        loadingBar = new ProgressDialog(this);
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateAccount();

            }
        });
    }


    private void CreateAccount()
    {
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please Write Your Name",Toast.LENGTH_SHORT) ;
        }
       else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Please Write Your Phone",Toast.LENGTH_SHORT) ;
        }
       else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please Write Your Pasword",Toast.LENGTH_SHORT) ;
        }
       else
           {
               loadingBar.setTitle("Create Account");
               loadingBar.setMessage("Please wait,while we are checking credentials");
               loadingBar.setCanceledOnTouchOutside(false);
               loadingBar.show();

               ValidatephoneNumber(name, phone,password);

           }
    }

    private void ValidatephoneNumber(final String name, final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("name",name);
                    userdataMap.put("password",password);
                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(REgisterActivity.this, "Congrats Your Account Has Been Created", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent=new Intent(REgisterActivity.this,MainActivity.class);
                                        startActivity(intent);    
                                    }
                                    else{
                                        loadingBar.dismiss();
                                        Toast.makeText(REgisterActivity.this, "Error Please Try Again", Toast.LENGTH_SHORT).show();
                                        
                                    }
                                }
                            });
                    
                }
                else{
                    Toast.makeText(REgisterActivity.this, "This"+ phone +"already Exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(REgisterActivity.this, "Please try using another phone number.", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(REgisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
