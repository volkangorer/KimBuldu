package com.volkangorer.kimbuldu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.volkangorer.kimbuldu.databinding.ActivityRegisterBinding;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore =  FirebaseFirestore.getInstance();
    }

    public  void signupClicked(View view){

        String email = binding.eMail.getText().toString();
        String password = binding.pass1.getText().toString();
        String password2 = binding.pass2.getText().toString();
        String name = binding.name.getText().toString();
        String phoneNumber = binding.phoneNumber.getText().toString();

        Map<String,Object> map = new HashMap<>();

        map.put("user",email);
        map.put("name",name);
        map.put("phoneNumber",phoneNumber);

        if (password.equals(password2)){
            if(email.equals("") || password.equals("")){
                Toast.makeText(this,"E-mail ve parola giriniz",Toast.LENGTH_LONG).show();
            }else{
                auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        firebaseFirestore.collection("Users").document(email).set(map);
                        Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        }else {
            Toast.makeText(this,"Parolalar eşleşmiyor",Toast.LENGTH_LONG).show();
        }
    }


}