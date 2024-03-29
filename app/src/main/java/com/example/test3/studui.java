package com.example.test3;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



public class studui extends AppCompatActivity {

    AppCompatButton ap,ap2;
    Toolbar t;
    //final int[] j = {0};
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView imageView;
    Dialog d;
    EditText e;
    int i = 0;
    LinearLayout l;
    FirebaseDatabase database;
    DatabaseReference ref;
    SharedPreferences sharedPreferences1;
    ArrayList<String> ls;
    String flag = "0";


    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studui);
        t = findViewById(R.id.toolbar);
        setSupportActionBar(t);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .requestProfile()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        Uri photoUrl = signInAccount.getPhotoUrl();

        // Get the FCM token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Retrieve the FCM token
                        token = task.getResult();
                        String stud_id = getIntent().getStringExtra("id");
                        isTokenStoredForStudent(stud_id);
                    }
                });


        imageView = findViewById(R.id.imgpro);
        Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.baseline_person_24)
                .error(R.drawable.baseline_person_24)
                .circleCrop()
                .into(imageView);


        sharedPreferences1 = getSharedPreferences("photouristud", Context.MODE_PRIVATE);
        flag = sharedPreferences1.getString("key2" + signInAccount.getId(),"0");
        if(flag.equals("0")) {

            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putString("key2" + signInAccount.getId(), "1");
            editor.apply();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("photouris");
            String key = reference.push().getKey();

            if(photoUrl != null) {
                reference.child(key).child("uri").setValue(photoUrl.toString());
                reference.child(key).child("id").setValue(signInAccount.getId());
            }
            else
            {
                Resources resources = getResources();
                int drawableId = R.drawable.profile_def; // Replace with the actual drawable resource ID
                Uri drawableUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        resources.getResourcePackageName(drawableId) + '/' +
                        resources.getResourceTypeName(drawableId) + '/' +
                        resources.getResourceEntryName(drawableId));

                reference.child(key).child("uri").setValue(drawableUri.toString());
                reference.child(key).child("id").setValue(signInAccount.getId());
            }
        }

        imageView.setOnClickListener(view -> show_profile());

        l = findViewById(R.id.cc);

        d = new Dialog(this);
        d.setContentView(R.layout.joinclass);
        e = d.findViewById(R.id.clscode);
        ap2 = d.findViewById(R.id.join);
        ap2.setOnClickListener(view -> join());

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Subject");

        ap = findViewById(R.id.joinclass);
        ap.setOnClickListener(view -> joinClass());
        ls = new ArrayList();
        /*notificationWork();*/
        loadSubjects();
    }

    /*private void notificationWork() {
        Toast.makeText(this, "hello all", Toast.LENGTH_SHORT).show();
        ArrayList<String> sub = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SubjectConnectsStudent");
        DatabaseReference r = FirebaseDatabase.getInstance().getReference("Scheduling");
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    assert acct != null;
                    // Toast.makeText(studui.this, acct.getId() + " - "+ childSnapshot.child("student_id").getValue().toString(), Toast.LENGTH_SHORT).show();
                    if(Objects.equals(acct.getId(), Objects.requireNonNull(childSnapshot.child("student_id").getValue()).toString())){
                        sub.add(childSnapshot.child("subject_id").getValue().toString());
                        //Toast.makeText(studui.this, "hiii"+childSnapshot.child("subject_id").getValue().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                for (String subjectId : sub) {
                    DatabaseReference subjectSchedulingRef = r.child(subjectId);
                    subjectSchedulingRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Loop through each push ID under the subject ID and retrieve scheduling data for each push ID
                            for (DataSnapshot pushIdSnapshot : dataSnapshot.getChildren()) {
                                String pushId = pushIdSnapshot.getKey();
                                String date = pushIdSnapshot.child("Date").getValue(String.class);
                                String time = pushIdSnapshot.child("Time").getValue(String.class);
                                String repetition = pushIdSnapshot.child("Repetition").getValue(String.class);
                                String description = pushIdSnapshot.child("description").getValue(String.class);

                                // Toast.makeText(studui.this, time, Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur while retrieving data from Firebase Realtime Database
                // ...
            }
        });
    }*/

    private void show_profile() {
        Intent i = new Intent(studui.this, Profile_page.class);
        startActivity(i);
    }

    public void loadSubjects(){
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Subject");
        DatabaseReference r = FirebaseDatabase.getInstance().getReference("SubjectConnectsStudent");
        Query query = r.orderByChild("student_id").equalTo(getIntent().getStringExtra("id"));

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ls.clear();
                l.removeAllViews();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String subId = childSnapshot.child("subject_id").getValue(String.class);
                    ls.add(subId);
                    //Log.w("Firebase", "" + subId);
                    // Do something with the sub ID
                }
                for(int i = 0; i < ls.size(); i++){
                    int finalI = i;
                    dr.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                if(Objects.equals(childSnapshot.child("subject_id").getValue(String.class), ls.get(finalI))){
                                    String name = childSnapshot.child("name").getValue(String.class);
                                    String desc = childSnapshot.child("description").getValue(String.class);
                                    String lec_hour = childSnapshot.child("estimated_lec").getValue(String.class);
                                    String teacher = childSnapshot.child("teacher_id").getValue(String.class);
                                    addClass(name,desc,ls.get(finalI),lec_hour,teacher);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }

    public void checkSubject(String sub_code){
        ref.orderByChild("subject_id").equalTo(sub_code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    connectWithSubject(sub_code);
                }else{
                    Toast.makeText(studui.this, "Enter Correct Code", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });
    }

    public void connectWithSubject(String subject_id){
        String stud_id = getIntent().getStringExtra("id");
        Map<String, Object> data = new HashMap<>();
        data.put("student_id", stud_id);
        data.put("subject_id", subject_id);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("SubjectConnectsStudent").push().setValue(data);

        Toast.makeText(studui.this, "Subject Added", Toast.LENGTH_SHORT).show();
        e.setText("");

        loadSubjects();
    }

    public void join(){
        String s = e.getText().toString().trim();
        checkSubject(s);
    }

    public void addClass(String name, String description, String sub_id, String lec_hour, String t){
        TextView ed = new TextView(studui.this);
        ed.setText(String.format("%s\n%s\n%s",name,description,lec_hour + " Lecture hours"));
        ed.setBackgroundResource(R.drawable.fortui);
        ed.setTextSize(20);
        ed.setPadding(40, 25, 40, 150);
        ed.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ed.getLayoutParams();
        int leftMargin = 20;
        int topMargin = 20;
        int rightMargin = 20;
        int bottomMargin = 0;
        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        ed.setLayoutParams(layoutParams);
        ed.setOnClickListener(view -> mngPage(name, sub_id, description, lec_hour,t));
        l.addView(ed);
        d.hide();
    }

    public void joinClass()
    {
        d.show();
    }

    public void mngPage(String name, String sub_id, String desc, String lec_hour, String t){
        Intent i = new Intent(studui.this, mngtchclass.class);
        i.putExtra("sub_id", sub_id);
        i.putExtra("name",name);
        i.putExtra("dsc",desc);
        i.putExtra("lec",lec_hour);
        i.putExtra("tid",t);
        startActivity(i);
    }

    private void storeTokenInDatabase(Map<String, Object> data) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("FCM Token").push().setValue(data);
        Toast.makeText(this, "Token Stored", Toast.LENGTH_SHORT).show();
    }

    private boolean isTokenStoredForStudent(String studId) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("FCM Token");

        databaseRef.orderByChild("Token").equalTo(token).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Token is already stored for the student
                    Log.d(TAG, "Token is already stored for the student");
                    // TODO: Handle the case where the token exists
                    Toast.makeText(studui.this, "already Exists" + token, Toast.LENGTH_SHORT).show();
                    // You can return true from here or perform any necessary actions
                } else {
                    // Token is not stored for the student
                    Log.d(TAG, "Token is not stored for the student");
                    // TODO: Handle the case where the token does not exist
                    Toast.makeText(studui.this, "Not Exists", Toast.LENGTH_SHORT).show();
                    Map<String, Object> data = new HashMap<>();
                    data.put("student_id", studId);
                    data.put("Token", token);
                    // Store the token in your backend or database
                    storeTokenInDatabase(data);
                    Toast.makeText(studui.this, "DONE", Toast.LENGTH_SHORT).show();
                    // You can return false from here or perform any necessary actions
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error occurred while querying the database
                Log.e(TAG, "Error querying FCM Token: " + databaseError.getMessage());
            }
        });

        // Default return value
        return false;
    }



}