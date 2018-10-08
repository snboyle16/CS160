package com.example.stephenboyle.irepresent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stephenboyle.irepresent.MoreInfoActivity;
import com.example.stephenboyle.irepresent.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class FetchActivity extends AppCompatActivity {

        ImageButton info1;
        ImageButton info2;
        ImageButton info3;
        int ZIPCODE;
        RequestQueue requestQueue;
        String URLBASE = "https://api.geocod.io/v1.3/geocode/?api_key=55feb5f2c9fbb550bc57aacf5f1ebfbecc0767a&fields=cd&postal_code=";

        String formattedAddress;
        String IMAGEURL =  "http://bioguide.congress.gov/bioguide/photo/";
        TextView nameTextView;
        TextView numReps;
        TextView[] congs = new TextView[3];
        ImageView[] images = new ImageView[3];

        HashMap<Integer, ArrayList<String>> hashMap = new HashMap<>();



        String[] ZIPCODES = new String[]{"11357","07753","37066","15010","48178","30008","19064","02155","55379","29445","16601","46614","40601","30736","10952","37055","32725","28205","14850","06457","46410","44266","21701","43560","33904","30736","20743","22041","60621","13126","33414","20136","59701","06776","19061","54901","60025","38053","27834","07003","19460","33510","75126","29710","45140","28540","30274","60462","98290","28540","53154","03051","06095","98908","33414","33414","23703","11729","08330","32533","17011","59715",
                "48331","20707","04401","44281","44512","36330","08520","21701","27603","43081","17011","49503","34997","01906","11102","11102","23860","55337","08060","57401","08401","48021","21921","29456","78501","08865","60466","33442","56601","13021","27526","48178","32904","27870","54935","50010","37601","21075","46112","24502","08054","02130","94070","06484","56601","54401","20748","76901","11795","33756","28792","08742","43512","11741","21228","20707","27028","34711","85365","03054","77016","03054","32955","30281","11801","85021"};






        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                System.out.println(ZIPCODES.length);
                setContentView(R.layout.activity_fetch);
                info1 = findViewById(R.id.info1);
                info2 = findViewById(R.id.info2);
                info3 = findViewById(R.id.info3);
                System.out.println("bitch");

                numReps = findViewById(R.id.numReps);

                if (getIntent().hasExtra("zipcode")) {
                        URLBASE = "https://api.geocod.io/v1.3/geocode/?api_key=55feb5f2c9fbb550bc57aacf5f1ebfbecc0767a&fields=cd&postal_code=" + getIntent().getExtras().getInt("zipcode");
                } else if (getIntent().hasExtra("lat")) {
                        URLBASE = "https://api.geocod.io/v1.3/reverse?q=" + getIntent().getExtras().getDouble("lat") + "," + getIntent().getExtras().getDouble("lon") + "&fields=cd&api_key=55feb5f2c9fbb550bc57aacf5f1ebfbecc0767a";
                } else {
                        URLBASE = "https://api.geocod.io/v1.3/geocode/?api_key=55feb5f2c9fbb550bc57aacf5f1ebfbecc0767a&fields=cd&postal_code=" + ZIPCODES[new Random().nextInt(129)];
                }
                info1.setBackgroundDrawable(Drawable.createFromPath("@drawable/infopaque"));
                info2.setBackgroundDrawable(Drawable.createFromPath("@drawable/infopaque"));
                info3.setBackgroundDrawable(Drawable.createFromPath("@drawable/infopaque"));

                requestQueue = Volley.newRequestQueue(this);

                nameTextView = findViewById(R.id.nametextView);
                jsonParse();
                congs[0] = findViewById(R.id.cong0);
                congs[1] = findViewById(R.id.cong1);
                congs[2] = findViewById(R.id.cong2);
                images[0] = findViewById(R.id.image0);
                images[1] = findViewById(R.id.image1);
                images[2] = findViewById(R.id.image2);
                nameTextView.setTextColor(Color.BLACK);


        }

        public void clickedInfo(View view) {
                Bundle index = new Bundle();

                System.out.println(R.id.info1);
                System.out.println(R.id.info2);
                System.out.println(R.id.info3);
                if (view.getId() == R.id.info1) {
                        index.putStringArrayList("rep", hashMap.get(0));

                } else if (view.getId() == R.id.info2) {
                        index.putStringArrayList("rep", hashMap.get(1));
                } else {
                        index.putStringArrayList("rep", hashMap.get(2));
                }
                System.out.println("big bitch");
                Intent intent = new Intent (this, MoreInfoActivity.class);
                intent.putExtras(index);
                startActivity(intent);
        }




        public void jsonParse() {
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URLBASE, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                                try {

                                        JSONArray jsonArray = response.getJSONArray("results");
                                        JSONObject address = jsonArray.getJSONObject(0).getJSONObject("address_components");
                                        formattedAddress = address.getString("city") + ", " + address.getString("state") + " " + address.getString("zip");
                                        nameTextView.setText(formattedAddress);
                                        int numCongressionalDistricts = jsonArray.getJSONObject(0).getJSONObject("fields").getJSONArray("congressional_districts").length();
                                        numReps.setText("Above is 1 of " + Integer.toString(numCongressionalDistricts) + " Representative(s) in this district");
                                        JSONArray currentLegislature = jsonArray.getJSONObject(0).getJSONObject("fields").getJSONArray("congressional_districts").getJSONObject(0).getJSONArray("current_legislators");
                                        int repCounter = 0;
                                        for (int i = 0; i < currentLegislature.length(); i++) {
                                                IMAGEURL = "http://bioguide.congress.gov/bioguide/photo/";
                                                JSONObject temprep = currentLegislature.getJSONObject(i);

                                                String type = temprep.getString("type");
                                                String name = temprep.getJSONObject("bio").getString("first_name") + " " + temprep.getJSONObject("bio").getString("last_name");
                                                String realName = type + name;
                                                String party = temprep.getJSONObject("bio").getString("party");
                                                String website = temprep.getJSONObject("contact").getString("url");
                                                String phone = temprep.getJSONObject("contact").getString("phone");
                                                String imageid = temprep.getJSONObject("references").getString("bioguide_id");
                                                String contactForm = temprep.getJSONObject("contact").getString("contact_form");
                                                if (contactForm == null) {
                                                        contactForm = "N/A";
                                                }

                                                IMAGEURL = IMAGEURL + imageid.charAt(0) + "/" + imageid + ".jpg";
                                                System.out.println(IMAGEURL);
                                                Picasso.get().load(IMAGEURL).into(images[i]);

//
                                                ArrayList<String> rep = new ArrayList<>();

                                                if (type.equals("representative") && repCounter == 0) {
                                                        String newText = "Representative " + name + "\nPARTY: " + party + "\nWEBSITE: " + Html.fromHtml(website) + "\nPHONE: " + phone + "\nCONTACT FORM: " +contactForm;
                                                        congs[i].setText(newText);
                                                        repCounter += 1;
                                                        rep.add("Representative " + name);
                                                        if (party.equals("Democrat")) {
                                                                congs[i].setBackgroundResource(R.drawable.blueopaque);
                                                        } else if (party.equals("Republican")) {
                                                                congs[i].setBackgroundResource(R.drawable.redopaque);
                                                        } else {
                                                                congs[i].setBackgroundResource(R.drawable.purpleopaque);
                                                        }
                                                }
                                                if (type.equals("senator")) {
                                                        String newText = "Senator " + name + "\nPARTY: " + party + "\nWEBSITE: " + Html.fromHtml(website) + "\nPHONE: " + phone + "\nCONTACT FORM: " +contactForm;
                                                        congs[i].setText(newText);
                                                        rep.add("Senator " + name);
                                                        if (party.equals("Democrat")) {
                                                                congs[i].setBackgroundResource(R.drawable.blueopaque);
                                                        } else if (party.equals("Republican")) {
                                                                congs[i].setBackgroundResource(R.drawable.redopaque);
                                                        } else {
                                                                congs[i].setBackgroundResource(R.drawable.purpleopaque);
                                                        }
                                                }
                                                rep.add(formattedAddress);
                                                rep.add(party);
                                                rep.add(website);
                                                rep.add(phone);
                                                rep.add(IMAGEURL);
                                                rep.add(contactForm);
                                                hashMap.put(i, rep);

                                        }
                                } catch (JSONException e) {
                                        nameTextView.setTextColor(Color.RED);
                                        info1.setImageDrawable(null);
                                        info2.setImageDrawable(null);
                                        info3.setImageDrawable(null);
                                        nameTextView.setText("Invalid Zipcode! Please try again.");
                                        e.printStackTrace();
                                }
                        }
                }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                        }
                });
                requestQueue.add(request);
        }
}