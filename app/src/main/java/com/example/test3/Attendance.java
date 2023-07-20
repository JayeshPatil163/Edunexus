package com.example.test3;

import static androidx.fragment.app.FragmentManager.TAG;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Attendance extends Fragment {

    private TextView qrCodeTV;
    private ImageView qrCodeIV;
    private TextInputEditText dataEdt;
    String currentDate, currentDateTime;
    ArrayList<String> stud_id;
    ArrayList<String> stud_name;
    ArrayList<String> stud_enr;

    ArrayList<String> studentLs;
    ArrayList<String> studName;
    ArrayList<String> studEnr;
    Button generateQRBtn;
    Button generate_attendace;
    Button doneAttendance;
    private Dialog loadingDialog;

    public Attendance() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_attendance, container, false);
        qrCodeTV = v.findViewById(R.id.idTVGenarateQR);
        qrCodeIV = v.findViewById(R.id.idIVQRCode);
        dataEdt = v.findViewById(R.id.idEdtData);
        generateQRBtn = v.findViewById(R.id.idBtnGenerateQR);
        generate_attendace = v.findViewById(R.id.attendance);
        doneAttendance = v.findViewById(R.id.Done);

        loadingDialog = new Dialog(requireContext());
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);

        generate_attendace.setEnabled(false);
        doneAttendance.setEnabled(false);
        doneAttendance.setBackground(getResources().getDrawable(R.drawable.button_red_back));
        generate_attendace.setBackground(getResources().getDrawable(R.drawable.button_red_back));

        doneAttendance.setOnClickListener(v1 -> {
            doneAttendance.setEnabled(false);
            doneAttendance.setBackground(getResources().getDrawable(R.drawable.button_red_back));
            generate_attendace.setEnabled(true);
            generate_attendace.setBackground(getResources().getDrawable(R.drawable.button_back));
            Toast.makeText(requireContext(),"Now you can generate attendance",Toast.LENGTH_SHORT).show();
        });

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        currentDate = dateFormat.format(new Date());
        String currentTime = timeFormat.format(new Date());
        currentDateTime = currentDate + " " + currentTime;

        dataEdt.setText(String.format("%s_%s", currentDateTime, mngtchclass.sub_name));

        generateQRBtn.setOnClickListener(view -> generateQR());
        generate_attendace.setOnClickListener(view -> generateAttendace());

        processStudentData();
        // Inflate the layout for this fragment
        return v;
    }

    private Handler mHandler = new Handler();

    private Runnable mProcessStudentDataRunnable = new Runnable() {
        @Override
        public void run() {
            getStudentId();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    writeInFile();
                }
            }, 1000); // delay for 1000 milliseconds
        }
    };

    private void processStudentData() {
        new Thread(mProcessStudentDataRunnable).start();
    }


    private void showToast(final String message) {
        if (isAdded()) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void writeInFile(){

        //showToast("updating file");
        if(isAdded()) {

            int i = 0;
            // require a empty public constructor
            File file = new File(requireContext().getExternalFilesDir(null), mngtchclass.sub_name + ".xlsx");

            if (file.exists()) {
                //showToast("file exist");
                FileInputStream inputStream = null;
                loadingDialog.show();
                try {
                    inputStream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                Workbook workbook = null;
                try {
                    workbook = new HSSFWorkbook(inputStream);
                } catch (IOException e) {
                    //showToast(e.getMessage());
                }
                assert workbook != null;
                Sheet sheet = workbook.getSheet("My Sheet");

                int rowCount = stud_enr.size();
                Row row;

                for (int j = 0; j < rowCount; j++) {
                    row = sheet.getRow(j + 1);
                    if (row == null) {
                        row = sheet.createRow(j + 1);
                    }

                    Cell enrNoValueCell = row.createCell(0);
                    enrNoValueCell.setCellValue(stud_enr.get(j));

                    Cell nameValueCell = row.createCell(1);
                    nameValueCell.setCellValue(stud_name.get(j));
                }

                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(file);
                    workbook.write(outputStream);
                    outputStream.close();
                    //showToast("file updated");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireContext());
                FirebaseStorage storage = FirebaseStorage.getInstance();
                assert acct != null;
                StorageReference reference = storage.getReference("attendance_files/").child(acct.getId() + "/" + mngtchclass.sub_name + ".xlsx");

                Uri fileUri = Uri.fromFile(file);
                StorageMetadata metadata = new StorageMetadata.Builder()
                        .setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") // or "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        .build();

                UploadTask uploadTask = reference.putFile(fileUri, metadata);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        showToast("File uploaded successfully.....");
                        loadingDialog.dismiss();
                        // File upload successful
                        // Retrieve the download URL of the uploaded file
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUrl) {
                                String downloadUrlStr = downloadUrl.toString();
                                // Do something with the download URL (e.g., save it to a database)
                                // ...
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to retrieve the download URL
                                // Handle the error
                                showToast("Error occurred");
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // File upload failed
                        // Handle the error
                    }
                });

            } else {
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireContext());
                assert acct != null;
                StorageReference r = FirebaseStorage.getInstance().getReference("attendance_files/").child(acct.getId() + "/" + mngtchclass.sub_name + ".xlsx");

                r.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // File exists
                        // Perform your desired actions here
                        if(!isAdded()){
                            return;
                        }

                        File f = new File(requireContext().getExternalFilesDir(null), mngtchclass.sub_name + ".xlsx");
                        try {

                            // Open output stream to the local file
                            FileOutputStream outputStream = new FileOutputStream(f);
                            outputStream.write(bytes);
                            outputStream.close();

                            // Close the streams
                            //inputStream.close();
                            outputStream.close();

                            writeInFile();
                            // File has been copied successfully
                            // Perform your desired actions here

                        } catch (IOException e) {
                            e.printStackTrace();
                            // Handle the exception appropriately
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if(!isAdded()){
                            return;
                        }

                        if (exception instanceof StorageException && ((StorageException) exception).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                            // File does not exist
                            // Perform your desired actions here

                            Workbook workbook = new HSSFWorkbook();
                            // Create a new sheet
                            Sheet sheet = workbook.createSheet("My Sheet");

                            // Create the header row with column names
                            Row headerRow = sheet.createRow(0);
                            Cell enrNoCell = headerRow.createCell(0);
                            enrNoCell.setCellValue("Enrollment No.");

                            Cell nameCell = headerRow.createCell(1);
                            nameCell.setCellValue("Name");

                            File f = new File(requireContext().getExternalFilesDir(null), mngtchclass.sub_name + ".xlsx");
                            FileOutputStream outputStream = null;
                            try {
                                outputStream = new FileOutputStream(file);
                                workbook.write(outputStream);
                                outputStream.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            writeInFile();

                        } else {
                            // Error occurred while checking file existence
                            // Handle the error appropriately
                            showToast("Error occurred while checking file existence");
                        }
                    }
                });
            }
        }
        else{
            showToast("Something wrong happened");
        }
    }



    public void getStudentId(){
        stud_id = new ArrayList<>();
        stud_name  = new ArrayList<>();
        stud_enr = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("SubjectConnectsStudent");
        DatabaseReference r = FirebaseDatabase.getInstance().getReference("Users");

        //showToast("hello bhai..........");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //showToast("hello bhai" + 10);
                // This method is called once with the initial value and again whenever data at this location is updated.
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    //showToast("" + mngtchclass.subId);
                    //showToast(childSnapshot.child("subject_id").getValue().toString() + " - " + mngtchclass.subId);

                    if(childSnapshot.child("subject_id").getValue().toString().equals(mngtchclass.subId)){
                        stud_id.add(childSnapshot.child("student_id").getValue().toString());
                    }
                }

                //showToast(stud_id.size()+"");

                r.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int i = 0;
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            UserInfo u = childSnapshot.getValue(UserInfo.class);
                            if(stud_id.contains(u.getId())){
                                //showToast("" + i++);
                                stud_name.add(u.getName());
                                stud_enr.add(u.getEnr_no());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                //Toast.makeText(requireContext(),"Failed to read value.",Toast.LENGTH_SHORT).show();
            }
        });

        //Toast.makeText(requireContext(),stud_id.size()+"",Toast.LENGTH_SHORT).show();

    }

    public void generateQR(){
        String data = Objects.requireNonNull(dataEdt.getText()).toString();
        if(data.isEmpty())
        {
            Toast.makeText(requireContext(), "Please enter some data to generate QR Code..", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingDialog.show();
            WindowManager manager = (WindowManager) requireActivity().getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int dimen = Math.min(width, height);
            dimen = dimen * 3/4;

            //Generating QR Code
            QRCodeWriter writer = new QRCodeWriter();

            //passing Contents to encode in QR Code
            BitMatrix bit;
            try {
                bit = writer.encode(data, BarcodeFormat.QR_CODE,dimen,dimen);
            } catch (WriterException e) {
                throw new RuntimeException(e);
            }

            //convert bit matrix into image
            int w = bit.getWidth();
            int h = bit.getHeight();
            int[] pixels = new int[w*h];
            for(int y = 0 ; y<h ; y++)
            {
                int offset = y*w;
                for(int x = 0; x<w ; x++)
                {
                    pixels[offset + x] = bit.get(x,y) ? Color.BLACK : Color.WHITE;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels,0,w,0,0,w,h);
            //displaying QR code on screen
            qrCodeIV.setImageBitmap(bitmap);
            qrCodeTV.setVisibility(View.GONE);
            doneAttendance.setEnabled(true);
            doneAttendance.setBackground(getResources().getDrawable(R.drawable.button_back));
            generateQRBtn.setEnabled(false);
            generateQRBtn.setBackground(getResources().getDrawable(R.drawable.button_red_back));
            loadingDialog.dismiss();
        }
    }




    public void getData(){
        studentLs = new ArrayList<>();
        studName = new ArrayList<>();
        studEnr = new ArrayList<>();
        //showToast("Function started for generating attendance");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Attendance");
        DatabaseReference r = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.child(mngtchclass.sub_name)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            studentLs.add(String.valueOf(childSnapshot.getValue()));
                            //showToast(String.valueOf(childSnapshot.getValue()));
                        }

                        //showToast(studentLs.get(0));
                        //ArrayList<String> id = new ArrayList<>();
                        r.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    UserInfo u = childSnapshot.getValue(UserInfo.class);
                                    //showToast(u.getId());
                                    //id.add(u.getId());

                                    for(int i = 0; i < studentLs.size(); i++){
                                        //String id = "{"+u.getId()+"="+u.getId()+"}";
                                        //showToast(id + "  -  " + studentLs.get(i) +"is "+ u.getId().equals(studentLs.get(i)));
                                        if(studentLs.get(i).contains(u.getId())){
                                            studName.add(u.getName());
                                            studEnr.add(u.getEnr_no());
                                            //showToast(u.getName() + " - " + u.getEnr_no());
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    public void markAttendance(){
        //showToast("Started writing in file");
        File file = new File(requireContext().getExternalFilesDir(null), mngtchclass.sub_name+".xlsx");
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Workbook workbook = null;
        try {
            workbook = new HSSFWorkbook(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadingDialog.show();

        Sheet sheet = workbook.getSheet("My Sheet");

        int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum() + 1;
        //showToast(rowCount + "");

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());

        // Get the last row number and last column number
        int lastColNum = 0;
        HSSFRow firstRow = (HSSFRow) sheet.getRow(0); // Get the first row of the sheet
        if (firstRow != null) {
            lastColNum = firstRow.getLastCellNum() - 1; // Subtract 1 to get the index of the last column
        }

        //showToast("Last column of the first row: " + lastColNum);

        /*int lastRowNum = sheet.getLastRowNum();

        for (int i = 0; i <= lastRowNum; i++) {
            HSSFRow row = (HSSFRow) sheet.getRow(i);
            if (row != null) {
                int colNum = row.getLastCellNum();
                if (colNum > lastColNum) {
                    lastColNum = colNum;
                }
            }
        }*/


        String s = dataEdt.getText().toString();
        String[] ar = s.split("_");
        String a = ar[0];
        //showToast("hmmm " + a);
//A0D2E8
        //String dayRow = "Day " + currentDay;
        // Iterate over each row
        showToast("" + rowCount);
        for (int i = 1; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            Row r = sheet.getRow(0);
            Cell c = r.createCell(lastColNum + 1);
            c.setCellValue(a);

            // Get the cell values for the current row
            String enrNo = row.getCell(0).getStringCellValue();
            String name = row.getCell(1).getStringCellValue();

            //showToast(enrNo);
            //showToast(name);

            // Check if the student is present and mark attendance
            Cell cell = row.createCell(lastColNum + 1);
            if(studEnr.contains(enrNo) && studName.contains(name)){
                cell.setCellValue("P");
            }else {
                cell.setCellValue("A");
            }
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.close();
            showToast("Attendance Marked");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireContext());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        assert acct != null;
        StorageReference reference = storage.getReference("attendance_files/").child(acct.getId()+"/" + mngtchclass.sub_name+".xlsx");

        Uri fileUri = Uri.fromFile(file);
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") // or "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                .build();

        UploadTask uploadTask = reference.putFile(fileUri, metadata);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                showToast("File uploaded successfully");
                loadingDialog.dismiss();
                // File upload successful
                // Retrieve the download URL of the uploaded file
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                        String downloadUrlStr = downloadUrl.toString();
                        // Do something with the download URL (e.g., save it to a database)
                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to retrieve the download URL
                        // Handle the error
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // File upload failed
                // Handle the error
                loadingDialog.dismiss();
            }
        });
    }


    public void generateAttendace(){
        generateQRBtn.setEnabled(true);
        generate_attendace.setEnabled(false);
        generateQRBtn.setBackground(getResources().getDrawable(R.drawable.button_back));
        generate_attendace.setBackground(getResources().getDrawable(R.drawable.button_red_back));
        takingAttendance();
    }


    private Handler handler = new Handler();
    private Runnable processStudentDataRunnable = new Runnable() {
        @Override
        public void run() {
            getData();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    markAttendance();
                }
            }, 1000); // delay for 1000 milliseconds
        }
    };

    private void takingAttendance() {
        new Thread(processStudentDataRunnable).start();
    }
}