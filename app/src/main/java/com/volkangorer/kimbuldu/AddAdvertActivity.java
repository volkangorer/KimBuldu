package com.volkangorer.kimbuldu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.volkangorer.kimbuldu.databinding.ActivityAddAdvertBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class AddAdvertActivity extends AppCompatActivity {
    ActivityAddAdvertBinding binding;

    FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth auth;
    FirebaseUser user;

    private StorageReference storageReference;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLaunher;
    Uri imageData;
    Uri icon ;
    Spinner spinnerCategory;
    ArrayAdapter<CharSequence> adapter3;
    String scategory;
    int black;
    TextView textView3;
    EditText question1;
    EditText question2;
    EditText question3;
    String question;

    String question11;
    String question22;
    String question33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityAddAdvertBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        registerLauncher();
        firebaseStorage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = firebaseStorage.getReference();
        user = auth.getCurrentUser();

        icon = null;

        spinnerCategory = binding.spinnerCategory;
        textView3 = binding.textView3;
        question = "off";

        black = 0xFF6C0000;

        question1 = binding.question1;
        question2 = binding.question2;
        question3 = binding.question3;


        getProfil();

        adapter3 = ArrayAdapter.createFromResource(this,R.array.category, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter3);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                scategory = adapterView.getItemAtPosition(i).toString();
                textView3 = (TextView) adapterView.getChildAt(0);
                textView3.setTextSize(20);
                textView3.setTextColor(black);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void getProfil(){
        firebaseFirestore.collection("Users").document(user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> map = documentSnapshot.getData();
                String name = (String) map.get("name");
                binding.nameLabel.setText("Hoşgeldin "+name);
            }
        });
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox:
                if (checked){
                    question = "on";
                    question1.setVisibility(View.VISIBLE);
                    question2.setVisibility(View.VISIBLE);
                    question3.setVisibility(View.VISIBLE);
                }
                // Put some meat on the sandwich
            else{
                    question = "off";
                    question1.setVisibility(View.GONE);
                    question2.setVisibility(View.GONE);
                    question3.setVisibility(View.GONE);
            }
                // Remove the meat
                break;

            // TODO: Veggie sandwich
        }
    }

    public void publishOnClicked(View view){
        String product = binding.product.getText().toString();

        if (product.equals("")){
            Toast.makeText(AddAdvertActivity.this,"Eşya Adı Giriniz",Toast.LENGTH_LONG).show();
        }else {
            if (question.equals("on")){
                question11 = binding.question1.getText().toString();
                question22 = binding.question2.getText().toString();
                question33 = binding.question3.getText().toString();

                if (question11.equals("")||question22.equals("")||question33.equals("")){
                    Toast.makeText(AddAdvertActivity.this,"Soru alanlarını doldurun",Toast.LENGTH_LONG).show();
                }else {
                    publish(1);
                }
            }else {
                publish(0);
            }
        }


    }


    public void publish(int value){



        if(imageData != null){
            System.out.println("we are here2");
            UUID uuid = UUID.randomUUID();
            String imageName = "images/" + uuid +".jpg";

            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    StorageReference newReference = firebaseStorage.getReference(imageName);

                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            int id = new Random().nextInt(999999);
                            String dowloadUrl = uri.toString();

                            String product = binding.product.getText().toString();
                            FirebaseUser user = auth.getCurrentUser();
                            String email = user.getEmail();

                            String explanation = binding.explanation.getText().toString();

                            HashMap<String , Object> postData = new HashMap<>();


                            postData.put("usermail",email);
                            postData.put("dowloadurl",dowloadUrl);
                            postData.put("category",scategory);
                            postData.put("product",product);
                            postData.put("id",id);
                            postData.put("status","onaylanmadı");
                            postData.put("explanation",explanation);

                            if (value==1){
                                postData.put("verify","etkin");
                                postData.put("q1",question11);
                                postData.put("q2",question22);
                                postData.put("q3",question33);
                            }else {
                                postData.put("verify","etkin değil");
                            }

                            String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(Calendar.getInstance().getTime());
                            //String output = myLocalDate.toString()
                            //FieldValue.serverTimestamp()

                            postData.put("date",timeStamp);

                            firebaseFirestore.collection("Adverts").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Intent intent = new Intent(AddAdvertActivity.this,HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddAdvertActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull  Exception e) {
                    Toast.makeText(AddAdvertActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }else {
            if (icon == null){
                System.out.println("we are here");
                Toast.makeText(AddAdvertActivity.this,"Lütfen Görsel Seçiniz",Toast.LENGTH_LONG).show();

            }else {
                imageData = icon;
                publish(value);

            }



        }
    }

    public void selectImageClicked(View view){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Galeri için izin gerekli",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permissionLaunher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }else{
                permissionLaunher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);
        }

    }

    private void registerLauncher(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if(intentFromResult != null){
                        imageData = intentFromResult.getData();
                        binding.imageView2.setImageURI(imageData);


                    }
                }
            }
        });

        permissionLaunher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                }else{
                    Toast.makeText(AddAdvertActivity.this,"İzin gerekli",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void homeOnClicked(View view){
        Intent intent = new Intent(AddAdvertActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    public void addOnClicked(View view){
        Intent intent = new Intent(AddAdvertActivity.this,AddAdvertActivity.class);
        startActivity(intent);
    }

    public void logOutOnClicked(View view){
        auth.signOut();

        Intent intentToMain = new Intent(AddAdvertActivity.this,MainActivity.class);
        startActivity(intentToMain);
        finish();
    }

    public void profileOnClicked(View view){
        Intent intent = new Intent(AddAdvertActivity.this,ProfileActivity.class);
        startActivity(intent);
    }

    public void removeOnClicked(View view){
        int image = getResources().getIdentifier("com.volkangorer.kimbuldu:drawable/image", null, null);
        binding.imageView2.setImageDrawable(getResources().getDrawable(image,null));
        icon = null;
        imageData = null;

    }

    public void icon1OnClicked(View view){
        icon = Uri.parse("android.resource://com.volkangorer.kimbuldu/drawable/vehicle");
        binding.imageView2.setImageURI(icon);
    }

    public void icon2OnClicked(View view){
        icon = Uri.parse("android.resource://com.volkangorer.kimbuldu/drawable/devices");
        binding.imageView2.setImageURI(icon);
    }

    public void icon3OnClicked(View view){
        icon = Uri.parse("android.resource://com.volkangorer.kimbuldu/drawable/book");
        binding.imageView2.setImageURI(icon);
    }

    public void icon4OnClicked(View view){
        icon = Uri.parse("android.resource://com.volkangorer.kimbuldu/drawable/money");
        binding.imageView2.setImageURI(icon);
    }
    public void icon5OnClicked(View view){
        icon = Uri.parse("android.resource://com.volkangorer.kimbuldu/drawable/necklace");
        binding.imageView2.setImageURI(icon);
    }
    public void icon6OnClicked(View view){
        icon = Uri.parse("android.resource://com.volkangorer.kimbuldu/drawable/sport");
        binding.imageView2.setImageURI(icon);
    }
    public void icon7OnClicked(View view){
        icon = Uri.parse("android.resource://com.volkangorer.kimbuldu/drawable/idcard");
        binding.imageView2.setImageURI(icon);
    }
    public void icon8OnClicked(View view){
        icon = Uri.parse("android.resource://com.volkangorer.kimbuldu/drawable/card");
        binding.imageView2.setImageURI(icon);
    }
    public void icon9OnClicked(View view){
        icon = Uri.parse("android.resource://com.volkangorer.kimbuldu/drawable/other");
        binding.imageView2.setImageURI(icon);
    }


}