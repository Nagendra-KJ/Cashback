package com.my.mycashback;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class Cashback extends AppCompatActivity {
    Button submitButton,cancelButton;
    EditText name,phoneNumber,code,serialNumber;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashback);
        submitButton = findViewById(R.id.buttonSubmit);
        cancelButton = findViewById(R.id.buttonCancel);
        name=findViewById(R.id.name);
        phoneNumber=findViewById(R.id.phoneNumber);
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
        db=FirebaseFirestore.getInstance();
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
        if(validate()) {
            User user = new User(name.getText().toString(), phoneNumber.getText().toString(), code.getText().toString() + serialNumber.getText().toString());
            db.collection("User").add(user);
            Toast.makeText(this, "Submitting to database", Toast.LENGTH_SHORT).show();
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
}
