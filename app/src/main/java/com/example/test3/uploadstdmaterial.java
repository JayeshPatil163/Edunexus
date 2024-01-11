package com.example.test3;


import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GestureDetectorCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.tabs.TabLayout;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class uploadstdmaterial extends AppCompatActivity implements GestureDetector.OnGestureListener{

    java.io.File file = null;

    float startScale = 0f;
    float midScale = 1.2f;
    float endScale = 1f;
    int duration = 300;
    ScaleAnimation scaleUpAnimation,scaleDownAnimation;

    private GestureDetectorCompat gestureDetectorCompat;
    int fileexist = 1;
    GoogleSignInAccount acct,stud;
    AppCompatButton bt,bt1,bt2, cancel, sendmeassage;
    ImageView imageView;
    private static final int REQUEST_AUTHORIZATION = 1001;
    private static final int PICK_FILE_REQUEST = 1;
    private Thread signInThread,thread,another;
    String filePath = "default";
    SharedPreferences sharedPreferences1,sharedPreferences,sharedPreferences3,sharedPreferences4,sharedPreferences5;
    public static String sub_name,stud_id = "Nope";
    String flag,folderId;
    int value = 0;
    static String dynamicurl,getFlag;
    public static String sub_Id,s;
    Dialog d1,d2;
    TextView t1, filen, textView1;
    EditText t2,t3,entermessage;
    ViewPager viewPager;
    TabLayout tabLayout;
    TextWatcher textWatcher;
    int ultimateflag = 1;

    public uploadstdmaterial() {
    }

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadstdmaterial);

        sharedPreferences4 = getSharedPreferences("Prefs", Context.MODE_PRIVATE);

        s = sharedPreferences4.getString("myStringKey", "not found");

        d2 = new Dialog(this);
        d2.setContentView(R.layout.uploadlink);
        t2 = d2.findViewById(R.id.linkurl);
        t3 = d2.findViewById(R.id.linktitle);
        bt2 = d2.findViewById(R.id.savelink);
        bt2.setOnClickListener(view -> getUrl());

        linearLayout = findViewById(R.id.fileattached);
        linearLayout.setVisibility(View.GONE);
        entermessage = findViewById(R.id.entermessage);
        //entermessage.set
        filen = findViewById(R.id.filename);
        textView1 = findViewById(R.id.sourcetype);
        imageView = findViewById(R.id.filethumbnail);
        cancel = findViewById(R.id.cancelattach);
        cancel.setVisibility(View.GONE);
        cancel.setOnClickListener(view -> performFadeOutAnimation());
        sendmeassage = findViewById(R.id.send);
        sendmeassage.setOnClickListener(view -> sendMessage());

        applyFrostedGlassEffect(linearLayout);

        bt = findViewById(R.id.upload);
        bt.setOnClickListener(view -> showChooser());

        sendmeassage.setVisibility(View.GONE);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) entermessage.getLayoutParams();
        int newMargin = 180;
        layoutParams.setMarginEnd(newMargin);
        entermessage.setLayoutParams(layoutParams);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (file == null) {
                    if (charSequence.toString().isEmpty()) {
                        sendmeassage.setVisibility(View.GONE);
                        ultimateflag = 1;
                        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) entermessage.getLayoutParams();
                        int newMargin = 180;
                        layoutParams.setMarginEnd(newMargin);
                        entermessage.setLayoutParams(layoutParams);
                    }
                    else if (ultimateflag == 1)
                    {
                        //sendmeassage.setVisibility(View.VISIBLE);
                        performFadeOutAnimationSend();
                        ultimateflag = 0;
                        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) entermessage.getLayoutParams();
                        int newMargin = 335;
                        layoutParams.setMarginEnd(newMargin);
                        entermessage.setLayoutParams(layoutParams);
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(file == null) {
                    if (charSequence.toString().isEmpty()) {
                        sendmeassage.setVisibility(View.GONE);
                        ultimateflag = 1;
                        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) entermessage.getLayoutParams();
                        int newMargin = 180;
                        layoutParams.setMarginEnd(newMargin);
                        entermessage.setLayoutParams(layoutParams);
                    }
                    else if(ultimateflag == 1)
                    {
                        //sendmeassage.setVisibility(View.VISIBLE);
                        performFadeOutAnimationSend();
                        ultimateflag = 0;
                        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) entermessage.getLayoutParams();
                        int newMargin = 335;
                        layoutParams.setMarginEnd(newMargin);
                        entermessage.setLayoutParams(layoutParams);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable == null)
                    sendmeassage.setVisibility(View.GONE);
            }
        };


        entermessage.addTextChangedListener(textWatcher);
        gestureDetectorCompat = new GestureDetectorCompat(this, this);

        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetectorCompat.onTouchEvent(event);
            }
        });


        scaleUpAnimation = new ScaleAnimation(
                startScale, midScale, startScale, midScale,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleUpAnimation.setDuration(duration / 2);

        scaleDownAnimation = new ScaleAnimation(
                midScale, endScale, midScale, endScale,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleDownAnimation.setDuration(duration / 2);
        scaleDownAnimation.setStartOffset(duration / 2);


        bt1 = findViewById(R.id.upldlink);
        bt1.setOnClickListener(view ->updlink());



        acct = GoogleSignIn.getLastSignedInAccount(this);
        sharedPreferences3 = getSharedPreferences("fold_id", Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("sub_name", Context.MODE_PRIVATE);
        sub_name = sharedPreferences.getString("sub", "");
        sub_Id = sharedPreferences.getString("subid", "");
        sharedPreferences1 = getSharedPreferences("flag", Context.MODE_PRIVATE);
        flag = sharedPreferences1.getString("key2" + sub_name,"0");
        if(flag.equals("0")) {

            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putString("key2" + sub_name, "1");
            editor.apply();

            d1 = new Dialog(this);
            d1.setContentView(R.layout.directionofuse);
            t1 = d1.findViewById(R.id.drctouse);
            t1.setText("Hello " + acct.getDisplayName() + "! On this page you can Upload the Study Material related to subject " + sub_name + " and provide links to videos and blogs online.");
            d1.show();
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Drive driveService = getDriveService();
                    File folderMetadata = new File();
                    folderMetadata.setName("Study Material: " + sub_name);
                    folderMetadata.setParents(Collections.singletonList("root"));
                    folderMetadata.setMimeType("application/vnd.google-apps.folder");

                    try {
                        File createdFolder = driveService.files().create(folderMetadata).setFields("id").execute();
                        folderId = createdFolder.getId();

                        SharedPreferences.Editor editor = sharedPreferences3.edit();
                        editor.putString("sub" + sub_name, folderId);
                        editor.apply();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }


        stud = GoogleSignIn.getLastSignedInAccount(this);


        if (s.equals("Student")) {
            stud_id = stud.getId();
            bt.setVisibility(View.GONE);
            bt1.setVisibility(View.GONE);
        }

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Drive driveService = getDriveService();
                File folderMetadata = new File();
                folderMetadata.setName("Study Material: " + sub_name);
                folderMetadata.setParents(Collections.singletonList("root"));
                folderMetadata.setMimeType("application/vnd.google-apps.folder");
                File folder = null;
                try {
                    folder = driveService.files().get(sharedPreferences3.getString("sub" + sub_name, "")).execute();
                } catch (IOException e) {
                    try {
                        if(folder == null) {
                            File createdFolder = driveService.files().create(folderMetadata).setFields("id").execute();
                            folderId = createdFolder.getId();
                            SharedPreferences.Editor editor = sharedPreferences3.edit();
                            editor.putString("sub" + sub_name, folderId);
                            editor.apply();
                        }
                    } catch (IOException ee) {
                        ee.printStackTrace();
                    }
                }
            }
        });
        thread.start();


        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);

        ViewPagerAdapter adapterv = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterv);


        sharedPreferences5 = getSharedPreferences("dynamicurl", Context.MODE_PRIVATE);
        dynamicurl = sharedPreferences5.getString("urldyn", "not found");
        getFlag = sharedPreferences5.getString("flag", "not found");


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(s.equals("Teacher")) {
                    int position = tab.getPosition();
                    switch (position) {
                        case 0:
                            if(!dynamicurl.equals("not found") && value == 0)
                            {
                                if(getFlag.equals("file"))
                                {
                                    bt.setVisibility(View.VISIBLE);
                                    bt1.setVisibility(View.GONE);
                                    java.io.File file = new java.io.File(getFilePath(getApplicationContext(),Uri.parse(dynamicurl)));
                                    Toast.makeText(uploadstdmaterial.this, file.toString(), Toast.LENGTH_SHORT).show();
                                    signInThread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            uploadFile(file);
                                        }
                                    });
                                    signInThread.start();
                                    SharedPreferences.Editor editor = sharedPreferences5.edit();
                                    editor.remove("urldyn");
                                    editor.apply();
                                    dynamicurl = "not found";

                                }
                                else {
                                    linearLayout.setVisibility(View.GONE);
                                    cancel.setVisibility(View.GONE);
                                    file = null;
                                    bt.setVisibility(View.GONE);
                                    bt1.setVisibility(View.VISIBLE);
                                    tab = tabLayout.getTabAt(1);
                                    tabLayout.selectTab(tab);
                                    value = 1;
                                }
                            }
                            else {
                                if(file != null) {
                                    linearLayout.setVisibility(View.VISIBLE);
                                    cancel.setVisibility(View.VISIBLE);
                                    fileexist = 1;
                                }
                                bt.setVisibility(View.VISIBLE);
                                bt1.setVisibility(View.GONE);
                            }
                            break;
                        case 1:
                            if(!dynamicurl.equals("not found"))
                            {
                                if(getFlag.equals("file"))
                                {

                                }
                                else {
                                    t2.setText(dynamicurl);
                                    d2.show();
                                    SharedPreferences.Editor editor = sharedPreferences5.edit();
                                    editor.remove("urldyn");
                                    editor.apply();
                                }
                            }
                            bt.setVisibility(View.GONE);
                            bt1.setVisibility(View.VISIBLE);
                            linearLayout.setVisibility(View.GONE);
                            cancel.setVisibility(View.GONE);
                            if(file != null)
                                fileexist = 0;
                            break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        tabLayout.setupWithViewPager(viewPager);
    }


    /*private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float deltaY = e2.getY() - e1.getY();

            if (Math.abs(deltaY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (deltaY < 0) {
                    // Swipe up detected, perform animation and make the view GONE
                    performFadeOutAnimation();
                    return true;
                }
            }
            return false;
        }
    }
*/


    private void startFloatingAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationY", -1f, 9f);
        animator.setDuration(900);  // Set the duration of the animation (in milliseconds)
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setRepeatMode(ObjectAnimator.REVERSE);  // Reverse the animation
        animator.setRepeatCount(ObjectAnimator.INFINITE);  // Repeat the animation indefinitely
        animator.start();

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(cancel, "translationY", -1f, 9f);
        animator1.setDuration(900);  // Set the duration of the animation (in milliseconds)
        animator1.setInterpolator(new AccelerateDecelerateInterpolator());
        animator1.setRepeatMode(ObjectAnimator.REVERSE);  // Reverse the animation
        animator1.setRepeatCount(ObjectAnimator.INFINITE);  // Repeat the animation indefinitely
        animator1.start();
    }

    public void sendMessage() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String formattedDateTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date(year - 1900, month, day, hour, minute));

        if (file != null && fileexist == 1) {

            String type = "Docs returned";
            try {
                Uri fileUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.test3.fileprovider", file);
                type = getContentResolver().getType(fileUri);
            }
            catch (NullPointerException ne) {

            }
            if(entermessage.getText().toString().isEmpty()) {
                ListOfFiles.addMessage(file.getName(), type, "", formattedDateTime);
            }
            else {
                ListOfFiles.addMessage(file.getName(), type, entermessage.getText().toString(), formattedDateTime);
                entermessage.setText("");
            }
            signInThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //uploadFile(file);
                }
            });
            signInThread.start();

            performFadeOutAnimation();

            sendmeassage.setVisibility(View.GONE);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) entermessage.getLayoutParams();
            int newMargin = 180;
            layoutParams.setMarginEnd(newMargin);
            entermessage.setLayoutParams(layoutParams);
        } else {
            ListOfFiles.addMessage( "" , "", entermessage.getText().toString(), formattedDateTime );
            entermessage.setText("");
            sendmeassage.setVisibility(View.GONE);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) entermessage.getLayoutParams();
            int newMargin = 180;
            layoutParams.setMarginEnd(newMargin);
            entermessage.setLayoutParams(layoutParams);
            sendmeassage.setVisibility(View.GONE);
        }
    }

    public void performFadeOutAnimation()
    {
        Animation fadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        fadeOutAnimation.setDuration(400);  // Set the duration of the fade-out animation
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationY", 0f, -50f);
                animator.setDuration(300);  // Set the duration of the animation (in milliseconds)
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.start();
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(cancel, "translationY", 0f, -50f);
                animator1.setDuration(300);  // Set the duration of the animation (in milliseconds)
                animator1.setInterpolator(new AccelerateDecelerateInterpolator());
                animator1.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linearLayout.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                file = null;
                if(entermessage.getText().toString().isEmpty()) {
                    sendmeassage.setVisibility(View.GONE);
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) entermessage.getLayoutParams();
                    int newMargin = 180;
                    layoutParams.setMarginEnd(newMargin);
                    entermessage.setLayoutParams(layoutParams);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        linearLayout.startAnimation(fadeOutAnimation);
        cancel.startAnimation(fadeOutAnimation);
    }

    public void performFadeOutAnimationSend()
    {
        sendmeassage.startAnimation(scaleUpAnimation);
        scaleUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                sendmeassage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sendmeassage.startAnimation(scaleDownAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animation repeat callback
            }
        });
    }

    public static String getFilePath(Context context, Uri uri) {
        if (getFlag.equals("file")) {
            try {
                String path = null;
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    path = cursor.getString(columnIndex);
                    cursor.close();
                }

                return path;
            }
            catch (SecurityException se)
            {
                se.printStackTrace();
            }
            getFlag = "";
        }
        return "";
    }

    public void getUrl()
    {
        if(!t2.getText().toString().isEmpty()) {
            String url = t2.getText().toString();
            String s = " ";
            if(t3.getText().toString().endsWith(":"))
            {
                s = t3.getText().toString().substring(0,t3.getText().toString().length() - 1);
            }
            else
                s = t3.getText().toString();

            String title = s;
            another = new Thread(new Runnable() {
                @Override
                public void run() {
                    String faviconUrl = null;

                    try {

                        // Assuming you have the website URL stored in a variable called "websiteUrl"
                        Document doc = Jsoup.connect(url).get();
                        Element iconElement = doc.select("link[rel~=(?i)^(shortcut|icon|favicon)]").first();
                        faviconUrl = iconElement.absUrl("href");


                        // Assuming you have the website URL stored in a variable called "websiteUrl"
                        //Document doc = Jsoup.connect(websiteUrl).get();
                        Elements iconElements = doc.select("link[rel~=icon], link[rel~=shortcut icon], link[rel~=apple-touch-icon]");

                        int maxIconSize = 0;

                        for (Element element : iconElements) {
                            String href = element.attr("href");
                            String sizeAttr = element.attr("sizes");

                            // Extract the size from sizes attribute if available
                            int size = 0;
                            if (!sizeAttr.isEmpty()) {
                                String[] sizes = sizeAttr.split("x");
                                if (sizes.length > 0) {
                                    try {
                                        size = Integer.parseInt(sizes[0].trim());
                                    } catch (NumberFormatException e) {
                                        // Handle parsing error if needed
                                    }
                                }
                            }

                            // Check if the size is larger than the previous largest icon
                            if (size > maxIconSize) {
                                maxIconSize = size;
                                faviconUrl = href;
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (RuntimeException re) {
                        re.printStackTrace();
                    }

                    saveLinks(faviconUrl,title, url);
                }
            });
            another.start();
            d2.hide();
            t2.setText("");
            t3.setText("");
        }
        else
        {
            showToast("Please provide link/url...");
        }
    }
    public void saveLinks(String faviconurl, String s, String url)
    {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Links").child(mngtchclass.subId).push();

            reference.child("linktitle").setValue(s);
            reference.child("linkthumnail").setValue(faviconurl);
            reference.child("link").setValue(url);
            dynamicurl = "not found";
    }
    public void updlink()
    {
        d2.show();
    }

    public void showChooser()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    200);
        }
        else {
            showFileChooser();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                    {
                         startActivity(new Intent(this,Request_access.class));
                    }
                    else
                        showFileChooser();
                } else {
                    Toast.makeText(this, "Cannot access storage", Toast.LENGTH_SHORT).show(); // permission denied, handle the situation accordingly
                }
                return;
        }
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(uploadstdmaterial.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public Drive getDriveService()
    {
        acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(this, Collections.singleton(DriveScopes.DRIVE_FILE));
            credential.setSelectedAccount(acct.getAccount());
            Drive googleDriveService = new Drive.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    jsonFactory,
                    credential)
                    .setApplicationName("EduNexus")
                    .build();
            return googleDriveService;
        }
        else
            return null;
    }
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){

            if (requestCode == REQUEST_AUTHORIZATION && resultCode == RESULT_OK) {
                showFileChooser();
            }

            /*if (requestCode == PICK_FILE_REQUEST_11 && resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri fileUri = data.getData();

                    // Access the file using the DocumentFile
                    DocumentFile documentFile = DocumentFile.fromSingleUri(this, fileUri);

                    if (documentFile != null && documentFile.isFile()) {
                        Uri fileContentUri = documentFile.getUri();
                        java.io.File originalFile = new java.io.File(fileContentUri.getPath());
                        signInThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                uploadFile(originalFile);
                            }
                        });
                        signInThread.start();
                    }
                }
            }*/

            /*if (requestCode == REQUEST_CODE_OPEN_DOCUMENT_TREE && resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri treeUri = data.getData();

                    try {
                        getContentResolver().takePersistableUriPermission(treeUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION |
                                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, treeUri); // Set the initial directory

                        startActivityForResult(intent, PICK_FILE_REQUEST);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }*/

            if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    filePath = data.getData().getPath();
                    if (filePath.startsWith("/document/primary:")) {
                        //Toast.makeText(this, "Path contains /document/primary:", Toast.LENGTH_SHORT).show();
                        filePath = Environment.getExternalStorageDirectory() + "/" + filePath.substring("/document/primary:".length());
                    }
                    file = new java.io.File(filePath);

                    linearLayout.setVisibility(View.VISIBLE);
                    cancel.setVisibility(View.VISIBLE);

                    //sendmeassage.setVisibility(View.VISIBLE);
                    ultimateflag = 1;
                    if(ultimateflag == 1) {
                        ultimateflag = 0;
                        performFadeOutAnimationSend();
                    }
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) entermessage.getLayoutParams();
                    int newMargin = 335;
                    layoutParams.setMarginEnd(newMargin);
                    entermessage.setLayoutParams(layoutParams);

                    startFloatingAnimation();

                    filen.setText(file.getName());
                    String type = "Docs returned";

                    try {
                        Uri fileUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.test3.fileprovider", file);
                        type = getContentResolver().getType(fileUri);
                        /*extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
                        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);*/
                    }
                    catch (NullPointerException ne) {

                    }

                    //Toast.makeText(uploadstdmaterial.this, type, Toast.LENGTH_SHORT).show();


                switch (type)
                {
                    case "application/pdf": imageView.setImageResource(R.drawable.pdf);
                        textView1.setText("PDF");
                        break;

                    case "image/jpeg": imageView.setImageResource(R.drawable.jpeg);
                        textView1.setText("JPEG");
                        break;

                    case "image/png": imageView.setImageResource(R.drawable.jpeg);
                        textView1.setText("PNG");
                        break;

                    case "image/gif": imageView.setImageResource(R.drawable.jpeg);
                        textView1.setText("GIF");
                        break;

                    case "text/plain": imageView.setImageResource(R.drawable.txt);
                        textView1.setText("TXT");
                        break;

                    case "application/vnd.openxmlformats-officedocument.presentationml.presentation": imageView.setImageResource(R.drawable.pptx);
                        textView1.setText("GIF");
                        break;

                    case "application/vnd.ms-powerpoint": imageView.setImageResource(R.drawable.pptx);
                        textView1.setText("GIF");
                        break;

                    case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet": imageView.setImageResource(R.drawable.xlsx);
                        textView1.setText("XLSX");
                        break;

                    case "application/vnd.ms-excel": imageView.setImageResource(R.drawable.xlsx);
                        textView1.setText("XLSX");
                        break;

                    default: imageView.setImageResource(R.drawable.common);
                        textView1.setText("DOCS");
                }



            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

        private void showFileChooser () {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");

            try {
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    startActivityForResult(Intent.createChooser(intent1, "Select a file to upload"), REQUEST_CODE_OPEN_DOCUMENT_TREE);
                }
                else {*/
                    showToast("Select Document");
                    startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), PICK_FILE_REQUEST);
               // }
            } catch (android.content.ActivityNotFoundException ex) {
                showToast("please install a file manager");
            }
        }

        LinearLayout linearLayout;

        public void uploadFile (java.io.File originalFile)
        {
            file = null;
            Drive googleDriveService = getDriveService();
            String folder_id = sharedPreferences3.getString("sub" + sub_name, "");
            File fileMetaData = new File();
            fileMetaData.setParents(Collections.singletonList(folder_id));
            java.io.File file = originalFile;
            fileMetaData.setName(file.getName());
            FileContent mediaContent = new FileContent("application/octet-stream", file);
            try {
                File file1 = googleDriveService.files().create(fileMetaData, mediaContent).setFields("id, webContentLink, webViewLink").execute();
                String fileId = file1.getId();
                Permission publicPermission = new Permission()
                        .setType("anyone")
                        .setRole("reader");
                googleDriveService.permissions().create(fileId, publicPermission).execute();
                String publicUrl = file1.getWebViewLink();

                String downloadUrl = file1.getWebContentLink();



                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Drive Links").child(mngtchclass.subId).push();

                reference.child("fileName").setValue(file.getName());
                reference.child("fileLink").setValue(publicUrl);
                reference.child("fileDownloadLink").setValue(downloadUrl);

                showToast("File Uploaded successfully!");
            } catch (UserRecoverableAuthIOException e) {
                showToast("Re-authentication required!");
                Intent authIntent = e.getIntent();
                startActivityForResult(authIntent, REQUEST_AUTHORIZATION);
            } catch (FileNotFoundException fnf) {
                showToast(fnf.toString());
            } catch (Exception ae) {
                ae.printStackTrace();
                showToast("Error uploading file, Please try again...");
            }

            java.io.File destinationFolder = new java.io.File(getApplicationContext().getFilesDir(), "Study material" + sub_Id);
            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();
            }

            java.io.File destinationFile = new java.io.File(destinationFolder, originalFile.getName());
            try {
                InputStream inputStream = new FileInputStream(originalFile);
                OutputStream outputStream = new FileOutputStream(destinationFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
            catch (FileNotFoundException fileNotFoundException)
            {
                showToast("File not found");
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }

    @Override
    public boolean onDown(@NonNull MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float deltaY = e2.getY() - e1.getY();
        if (Math.abs(deltaY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
            if (deltaY < 0) {
                // Swipe up detected, perform animation and make the view GONE

                performFadeOutAnimation();
                return true;
            }
        }
        return false;
    }

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 1000;


    private void applyFrostedGlassEffect(View view) {
        RenderEffect effect = null; // Adjust the radius and the sigma values as needed
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            effect = RenderEffect.createBlurEffect(12.5f, 12.5f, Shader.TileMode.CLAMP);
        }

        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            view.setRenderEffect(effect);
        }
    }

}