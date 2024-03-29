package com.example.test3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class teacherlogin extends AppCompatActivity {
    public FirebaseDatabase database;
    public static String path;
    public DatabaseReference databaseReference;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView bt,t2;
    AppCompatButton ap;
    EditText e1,e2;
    SharedPreferences sharedPreferences;
    String s1,s2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherlogin);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        //t1 = findViewById(R.id.name);
        t2 = findViewById(R.id.email);
        ap = findViewById(R.id.bt1);

        ap.setOnClickListener(view -> {
            teacherui();
        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null)
        {
            String s1 = acct.getEmail();
            t2.setText(s1);
        }

        bt = findViewById(R.id.bt);
        bt.setPaintFlags(bt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        bt.setOnClickListener(view -> signOut());
    }

    void signOut()
    {
        gsc.signOut().addOnCompleteListener(task -> {
            finish();
            startActivity(new Intent(teacherlogin.this,page2.class));
        });
    }

    void teacherui()
    {
        e1 = findViewById(R.id.pno);
        e2 = findViewById(R.id.name);

        s1 = e1.getText().toString().trim();
        s2 = e2.getText().toString().trim();
        String s3 = t2.getText().toString().trim();

        if(!s1.isEmpty() && !s2.isEmpty() && isValidMobileNumber(s1)) {
            Intent intent = getIntent();
            String id = intent.getStringExtra("id").toString();
            UserInfo u = new UserInfo(id,s2,s1,s3,"",id +"_1");

            String[] key = {"id", "name", "phone", "email", "enr_no", "identity"};
            String s = u.getIdentity();
            String[] value = {u.getId(), u.getName(), u.getPhone(), u.getEmail(), u.getEnr_no(), s};

            for(int i = 0; i < 6; i++){
                databaseReference.child("Users").child(id).child(key[i]).setValue(value[i]);
            }

            Intent i = new Intent(teacherlogin.this, teachui.class);
            i.putExtra("id",u.getId());
            startActivity(i);
            finish();

        } else if (!isValidMobileNumber(s1)) {
            e1.setError("Please enter a valid phone number");
            Toast.makeText(teacherlogin.this, "Please, enter all the details.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(teacherlogin.this, "Please, enter all the details.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValidMobileNumber(String mobileNumber) {
        String mobilePattern = "^[1-9]\\d{9}$"; // Define the pattern for a valid 10-digit mobile number
        return mobileNumber.matches(mobilePattern); // Check if the input matches the pattern
    }
}