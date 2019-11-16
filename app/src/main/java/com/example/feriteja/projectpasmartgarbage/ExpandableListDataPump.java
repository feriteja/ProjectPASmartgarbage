package com.example.feriteja.projectpasmartgarbage;

import android.app.Activity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class ExpandableListDataPump extends Activity  {




    public static LinkedHashMap<String, List<String>> getData(int jumlah) {

      final  LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bagianRef = database.getReference();
        Query queryRef=bagianRef.orderByChild("namaTempat");





        int x = 1;
            while (x <= jumlah) {


                List<String> TEMPATS1 = new ArrayList<>();

                TEMPATS1.add("ini satu");


                expandableListDetail.put("TEMPATS" + x, TEMPATS1);


                x++;

            }


            return expandableListDetail;
        }
    }