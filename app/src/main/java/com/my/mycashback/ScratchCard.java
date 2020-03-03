package com.my.mycashback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class ScratchCard extends AppCompatActivity {

    int number;
    in.myinnos.androidscratchcard.ScratchCard scratchCard;
    TextView cashbackValue;
    AlertDialog dialog;
    String userId;
    FirebaseFirestore db;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_card);
        userId = getIntent().getStringExtra("USER_ID");
        db=FirebaseFirestore.getInstance();
        number =  new Random().nextInt(11)+1;
        scratchCard = findViewById(R.id.scratchCard);
        cashbackValue = findViewById(R.id.scratchText);
        cashbackValue.setText(String.valueOf(number));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cashback");
        builder.setMessage("You will receive a cashback of rupees "+number);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                db.collection("Users").document(userId).update("cashbackAmount",number);
                Toast.makeText(ScratchCard.this, "Transaction is initiated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ScratchCard.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        dialog = builder.create();
        scratchCard.setOnScratchListener(new in.myinnos.androidscratchcard.ScratchCard.OnScratchListener() {
            @Override
            public void onScratch(in.myinnos.androidscratchcard.ScratchCard scratchCard, float visiblePercent) {
                if(visiblePercent>0.4) {
                    scratchCard.setVisibility(View.GONE);
                    dialog.show();
                }
            }
        });

    }
}
