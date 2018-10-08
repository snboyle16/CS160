package com.example.stephenboyle.irepresent;

import android.icu.text.Replaceable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.stephenboyle.irepresent.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoreInfoActivity extends AppCompatActivity {


    String name;
    String formattedAddress;
    String party;
    String website;
    String phone;
    String imageurl;
    String contactform;


    TextView nameView;
    TextView addressView;
    TextView infoView;
    ImageView repView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        nameView = findViewById(R.id.nameView);
        addressView = findViewById(R.id.locView);
        infoView = findViewById(R.id.infoView);
        repView = findViewById(R.id.repView);

        ArrayList<String> REPRESENTATIVE = getIntent().getExtras().getStringArrayList("rep");


        name = REPRESENTATIVE.get(0);
        formattedAddress = REPRESENTATIVE.get(1);
        party = REPRESENTATIVE.get(2);
        website = REPRESENTATIVE.get(3);
        phone = REPRESENTATIVE.get(4);
        imageurl = REPRESENTATIVE.get(5);
        contactform = REPRESENTATIVE.get(6);


        nameView.setText(name);
        addressView.setText(formattedAddress);
        infoView.setText("Party: " + party + "\n\nWebsite: " +website + "\n\nPhone: " + phone + "\n\nContact Form: "+ contactform);
        System.out.println("very end");
        Picasso.get().load(imageurl).into(repView);



        if (party.equals("Democrat")) {
            infoView.setBackgroundResource(R.drawable.blueopaque);
        } else if (party.equals("Republican")) {
            infoView.setBackgroundResource(R.drawable.redopaque);
        } else {
            infoView.setBackgroundResource(R.drawable.purpleopaque);
        }


    }
}