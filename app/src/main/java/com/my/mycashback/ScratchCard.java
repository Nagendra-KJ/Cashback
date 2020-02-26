package com.my.mycashback;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.ScratchTextView;

import java.util.Random;

public class ScratchCard extends AppCompatActivity {

    int number;
    ScratchTextView scratchCard;
    TextView cashbackValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_card);
        number =  new Random().nextInt(11)+1;
        scratchCard = findViewById(R.id.scratchCard);
        cashbackValue = findViewById(R.id.scratchText);
        cashbackValue.setText(String.valueOf(number));
    }
}
