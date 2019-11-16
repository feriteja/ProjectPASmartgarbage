package com.example.feriteja.projectpasmartgarbage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Sples_Screen extends Activity {


    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference bagianRef = database.getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sples__screen);



        bagianRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i(dataSnapshot.getKey(), dataSnapshot.getChildrenCount() + "Count");


                final int count =(int) dataSnapshot.getChildrenCount();

                SharedPreferences prefs = getSharedPreferences("kaka", MODE_PRIVATE);
                SharedPreferences.Editor editor=prefs.edit();
                editor.putInt("nilai",count);
                editor.commit();
                editor.apply();


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        Thread mythread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);

                   /*
                    bagianRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Log.i(dataSnapshot.getKey(), dataSnapshot.getChildrenCount() + "Count");


                            final int count =(int) dataSnapshot.getChildrenCount();




                                    Intent intent=new Intent(Sples_Screen.this,CoreActivity.class);
                                    //  String jumlahnyastring = String.valueOf(count);
                                    intent.putExtra("this",count);
                                        finish();

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();





                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    */

                    Intent intent = new Intent(getApplicationContext(),DeltaActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        mythread.start();
    }
}
