package com.my.mycashback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class Cashback extends AppCompatActivity {
    Button submitButton,cancelButton;
    EditText name,phoneNumber,code,serialNumber;
    FirebaseFirestore db;
    ImageView profilePicture;
    int REQUEST_IMAGE_GALLERY = 1;
    int REQUEST_IMAGE_CAPTURE = 2;
    Uri profilePhotoUri;
    RadioGroup cashbackOption;
    RadioButton selectedOption;
    User user;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashback);
        submitButton = findViewById(R.id.buttonSubmit);
        cancelButton = findViewById(R.id.buttonCancel);
        profilePicture = findViewById(R.id.profilePicture);
        name=findViewById(R.id.name);
        phoneNumber=findViewById(R.id.phoneNumber);
        storageReference = FirebaseStorage.getInstance().getReference();
        code=findViewById(R.id.code);
        serialNumber=findViewById(R.id.serialNumber);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });
        db=FirebaseFirestore.getInstance();
        cashbackOption = findViewById(R.id.cashbackOption);
        int option=cashbackOption.getCheckedRadioButtonId();
        selectedOption = findViewById(option);

    }

    void cancel()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to cancel?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Cashback.this,MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    void submit()
    {
        if(validate())
        {
            user = new User(name.getText().toString(), phoneNumber.getText().toString(), Integer.parseInt(code.getText().toString() + serialNumber.getText().toString()),selectedOption.getText().toString());
            user.setUserId(UUID.randomUUID().toString());
            Toast.makeText(this, "Submitting to database", Toast.LENGTH_SHORT).show();
            db.collection("Users").document(user.getUserId()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Intent intent = new Intent(Cashback.this, ScratchCard.class);
                    intent.putExtra("USER_ID",user.getUserId());
                    startActivity(intent);
                }
            });
            if(profilePhotoUri!=null)
            {
                StorageReference dpRef = storageReference.child("Images/"+user.getUserId());
                dpRef.putFile(profilePhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Cashback.this, "DP uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }
    boolean validate()
    {
        boolean valid=true;
        if(name.getText().toString().isEmpty())
        {
            name.setError("Please enter a valid name");
            valid=false;
        }
        else {
            name.setError(null);
        }
        if(code.getText().toString().isEmpty())
        {
            code.setError("Please enter a valid code");
            valid=false;
        }
        else
            code.setError(null);
        if(serialNumber.getText().toString().isEmpty())
        {
            serialNumber.setError("Please enter a valid serial number");
            valid=false;
        }
        else
            serialNumber.setError(null);
        if(phoneNumber.getText().toString().length()!=10 || phoneNumber.getText().toString().isEmpty() )
        {
            phoneNumber.setError("Please enter a valid phone number");
            valid=false;
        }
        else
            phoneNumber.setError(null);
        return valid;
    }

    void selectPhoto()
    {
        String[] options = {"Gallery","Camera","Use Default Picture","Cancel"};
        AlertDialog.Builder dpBuilder = new AlertDialog.Builder(this);
        dpBuilder.setTitle("Add Image");
        dpBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which)
                {
                    case 0:
                        takePhotoFromGallery();
                        break;
                    case 1:
                        takePhotoFromCamera();
                        break;
                    case 2:
                        profilePicture.setImageResource(R.mipmap.default_dp);
                        break;
                    case 3:
                        dialog.dismiss();
                        break;
                }
            }
        });
        dpBuilder.show();
    }

    private void takePhotoFromGallery()
    {
        Intent takePictureFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takePictureFromGallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(takePictureFromGallery,REQUEST_IMAGE_GALLERY);
    }

    private void takePhotoFromCamera()
    {
        Intent takePictureFromCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureFromCamera.resolveActivity(getPackageManager())!=null)
        {
            File photoFile;
            photoFile = createImageFile();
            profilePhotoUri = FileProvider.getUriForFile(this,getApplicationContext().getPackageName()+".fileprovider",photoFile);
            takePictureFromCamera.putExtra(MediaStore.EXTRA_OUTPUT, profilePhotoUri);
            startActivityForResult(takePictureFromCamera,REQUEST_IMAGE_CAPTURE);
        }
    }
    private File createImageFile()
    {
        long timeStamp = new Date().getTime();
        String imageFileName = "JPEG_"+timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(storageDir,imageFileName+".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK)
        {
            profilePhotoUri = data.getData();
            assert profilePhotoUri != null;
        }
        profilePicture.setImageURI(profilePhotoUri);
    }

}
