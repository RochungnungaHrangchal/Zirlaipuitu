package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class VideoTest extends AppCompatActivity implements View.OnClickListener{
    private Button btnSelect,btnUpload;
    private static final int ZIRTIRTU_VIDEO_REQUEST = 234;
    private Uri filePath;
    private String MALIANA="CCC";
    private String path="https://firebasestorage.googleapis.com/v0/b/zirtirtu-6b320.appspot.com/o/IncidentReport%20%3A%3Dmp4?alt=media&token=198f44f1-cb8c-43cb-ad46-985404ea943a";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videotest);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btnSelect= findViewById(R.id.btnSelect);
       // btnUpload = findViewById(R.id.btnUpload);
       // btnUpload.setOnClickListener(this);
        btnSelect.setOnClickListener(this);
        VideoView videoView =(VideoView)findViewById(R.id.videoView);

        //Creating MediaController
        MediaController mediaController= new MediaController(this);
        mediaController.setMediaPlayer(videoView);
       // mediaController.setAnchorView(videoView);

        //specify the location of media file
        Uri uri=Uri.parse("https://firebasestorage.googleapis.com/v0/b/zirtirtu-6b320.appspot.com/o/Mathematics%2FPolygon%20%3Amp4?alt=media&token=b7c2e7da-da19-4eb4-b447-2cb56ef765be");

        //Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(mediaController);

        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

    }

    @Override
    public void onClick(View v) {

   if(v==btnSelect)
   {
       Intent intent = new Intent();
       intent.setType("video/*");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(Intent.createChooser(intent, "Select Video"), ZIRTIRTU_VIDEO_REQUEST);
  // Video Upload keuh..
       Toast.makeText(getApplicationContext(),"Uploaded Clicked",Toast.LENGTH_LONG).show();

   }




   if(v==btnUpload)
   {
      Toast.makeText(getApplicationContext(),"Uploaded Clicked",Toast.LENGTH_LONG).show();
       if (filePath != null) {
           //displaying a progress dialog while upload is going on
           final ProgressDialog progressDialog = new ProgressDialog(this);
           progressDialog.setTitle("Uploading");
           progressDialog.show();

           final StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("Mathematics").child("Polygon :"  + GetFileExtension(filePath));
           riversRef.putFile(filePath)
                   .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           //if the upload is successfull
                           //hiding the progress dialog
                           progressDialog.dismiss();


                           Toast.makeText(getApplicationContext(), " File Uploaded Successfully !", Toast.LENGTH_LONG).show();

                       }
                   })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception exception) {
                           //if the upload is not successfull
                           //hiding the progress dialog
                           progressDialog.dismiss();

                           //and displaying error message
                           Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                       }
                   })
                   .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                           //calculating progress percentage
                           double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                           //displaying percentage in progress dialog
                           progressDialog.setMessage("Almost Complete .....   ");
                       }
                   });
       }
       //if there is not any file
       else {
           //you can display an error toast
       }


     }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ZIRTIRTU_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
            if (filePath != null) {
                //displaying a progress dialog while upload is going on
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                final StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("Mathematics").child("Polygon :"  + GetFileExtension(filePath));
                riversRef.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //if the upload is successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();

                                Toast.makeText(getApplicationContext(), " File Uploaded Successfully !", Toast.LENGTH_LONG).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                //if the upload is not successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();

                                //and displaying error message
                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                //calculating progress percentage
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                //displaying percentage in progress dialog
                                progressDialog.setMessage("Almost Complete .....   " + progress + "  %");
                            }
                        });
            }
            //if there is not any file
            else {
                //you can display an error toast
            }


        }

    }



    public String GetFileExtension(Uri uri)
    {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

}
