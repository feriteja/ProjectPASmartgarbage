package com.example.feriteja.projectpasmartgarbage;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class DeltaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {

    FirebaseAuth firebaseAuth;

    private SearchView mSearchView;
    private ArrayList<Category> catList;
    private ArrayList<Boolean> hampirpenuh;
    private ArrayList<Boolean> sudahpenuh;


    ExpandableListView expandableListView;
    CustomExpandableListAdapter exAdpt;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference bagianRef = database.getReference();
    DatabaseReference userLog = database.getReference("UserLog");

    Spinner mspiner;

    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_delta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        text=(TextView)findViewById(R.id.testsa);



        initData();


        hampirpenuh=new ArrayList<Boolean>();
        sudahpenuh=new ArrayList<Boolean>();

        int des=0;
        while (des<=400){
            hampirpenuh.add(false);
            sudahpenuh.add(true);

            des++;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {

            finish();
            startActivity(new Intent(this, MainActivity.class));
        }


        // Intent intent = getIntent();

        //int namanyaa = intent.getIntExtra("this",5);


        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        mSearchView = (SearchView) findViewById(R.id.searchView1);


        exAdpt = new CustomExpandableListAdapter(catList, this);
        expandableListView.setAdapter(exAdpt);

        expandableListView.setTextFilterEnabled(true);
        setupSearchView();
        exAdpt.notifyDataSetChanged();


        mspiner = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(DeltaActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.shorting));
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mspiner.setAdapter(mAdapter);
        mspiner.setOnItemSelectedListener(this);




    }






    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        String msParent = parent.getItemAtPosition(pos).toString();
        if (msParent.equals("A~Z")) {
            sort("asc");
        }
        if (msParent.equals("Z~A")) {
            sort("dsc");
        }
        if (msParent.equals("1~9")) {
            sort2("kapup");
        }
        if (msParent.equals("9~1")) {
            sort2("kapdown");
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    @Override
    public void onBackPressed() {


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            new AlertDialog.Builder(this).setIcon(R.drawable.exit_button).setTitle("Exit")
                    .setMessage("Are you sure?")
                    .setNegativeButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            ngambil();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }).setPositiveButton("no", null).show();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==R.id.refresh){

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



            reload();



        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.show_all) {
            startActivity(new Intent(DeltaActivity.this, MapsActivity.class));
        } else if (id == R.id.sign_out) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
            userLog.setValue("belum");


        } else if (id == R.id.nav_help) {

            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {

            about_app();
        }
        else if(id==R.id.nav_setting){
            setting_notif();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void shownotification(String a, String b, int id) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentTitle(a)
                        .setContentText(b);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(id, builder.build());
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(this, id, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(id, builder.build());


    }


    public void setAlarm(Long x, String isi, int id) {


        PendingIntent pendingIntent;


        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);

        intent.putExtra("isi", isi);
        intent.putExtra("id", id);

        pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);


        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + x,
                120000, pendingIntent);
        /*
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        x, pendingIntent);
        */


    }


    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");


    }


    private void initData() {
        catList = new ArrayList<Category>();


        SharedPreferences jumlah = getSharedPreferences("kaka", MODE_PRIVATE);
        int namanyaajumlah = jumlah.getInt("nilai", 16);
        SharedPreferences prefsss = getSharedPreferences("datanya", MODE_PRIVATE);


        int de = 0;
        int da = 1;
        while (de < namanyaajumlah && da < 200) {


            catList.add(new Category(da));


            de++;

            da++;


        }


////////////////////////////BISA//////////////////////////////////////////////


        int des = 0;
        int das = 1;
        while (des <= 200 && das <= 200) {

            String namanyaaa = prefsss.getString("nilaiTempat" + das, "waiting");
            DatabaseReference tempatRef = database.getReference("tempats" + das);

            final int des0 = des;
            final int das0 = das;


            if (namanyaaa.equals("kosong")) {


            } else {

                tempatRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            String nama = dataSnapshot.child("namaTempat").getValue(String.class);
                            String kapasitasString = dataSnapshot.child("kapasitas").getValue(String.class);
                            int kapasitas = Integer.valueOf(kapasitasString);


                            try {
                                catList.set(des0, new Category(kapasitas, das0, nama));
                                exAdpt.notifyDataSetChanged();

                            } catch (Exception e) {

                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                des++;
            }


            das++;
        }
///////////////////////////////BISA?////////////////////////////////////////////////////

    }


    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)) {
            expandableListView.clearTextFilter();
            reload();


        } else {
            expandableListView.setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    public void ngambil() {

        int urutan = 1;
        while (urutan <= 200) {
            final DatabaseReference bagianRef = database.getReference("tempats" + urutan);


            final int urutanakhir0 = urutan - 1;

            bagianRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("latitude") && dataSnapshot.hasChild("longitude")) {

                        String latitudeString = dataSnapshot.child("latitude").getValue(String.class);
                        String longitudeString = dataSnapshot.child("longitude").getValue(String.class);
                        String kapasitasString = dataSnapshot.child("kapasitas").getValue(String.class);
                        String title = dataSnapshot.child("namaTempat").getValue(String.class);
                        String snipet = dataSnapshot.child("status").getValue(String.class);

                        int kapasitas = Integer.parseInt(kapasitasString);


                        SharedPreferences prefs = getSharedPreferences("letaknya", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("latitude" + urutanakhir0, latitudeString);
                        editor.putString("longitude" + urutanakhir0, longitudeString);
                        editor.putString("title" + urutanakhir0, title);
                        editor.putString("snipet" + urutanakhir0, snipet);
                        editor.putInt("kapasitas" + urutanakhir0, kapasitas);
                        editor.commit();


                    } else {

                        SharedPreferences prefs = getSharedPreferences("letaknya", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("title" + urutanakhir0, "mencoba");

                        editor.commit();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            urutan++;
        }


        int urutan2 = 1;
        while (urutan2 <= 200) {
            final DatabaseReference bagianRef = database.getReference("tempats" + urutan2);

            final int urutanakhir = urutan2;

            final int urutanakhir0 = urutan2 - 1;
            bagianRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        String namatempat = dataSnapshot.child("namaTempat").getValue(String.class);
                        String kapasitasString = dataSnapshot.child("kapasitas").getValue(String.class);
                        int kapasitas = Integer.parseInt(kapasitasString);

                        SharedPreferences prefs = getSharedPreferences("datanya", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("nilaiTempat" + urutanakhir, namatempat);
                        editor.putInt("nilaiKapasitas" + urutanakhir, kapasitas);
                        editor.commit();
                        editor.apply();

                    } else {
                        SharedPreferences prefs = getSharedPreferences("datanya", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("nilaiTempat" + urutanakhir, "kosong");
                        editor.commit();
                        editor.apply();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            urutan2++;
        }
    }

    public void sort2(final String order) {

        Comparator<Category> comp = new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {

                int first = o1.getKapasitas();
                int second = o2.getKapasitas();

                if (order.equals("kapup"))
                    return (first < second ? -1 : (first == second ? 0 : 1));

                else
                    return (first > second ? -1 : (first == second ? 0 : 1));


            }
        };

        Collections.sort(catList, comp);
        exAdpt.notifyDataSetChanged();

    }


    public void sort(final String order) {

        Comparator<Category> comp = new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {

                String first = String.valueOf(o1.getNamaTempat() + "");
                String second = String.valueOf(o2.getNamaTempat() + "");

                if (order.equals("asc"))
                    return (first.compareTo(second));

                else
                    return (second.compareTo(first));


            }
        };

        Collections.sort(catList, comp);
        exAdpt.notifyDataSetChanged();

    }



    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }



    @Override
    protected void onStart() {
        super.onStart();






        int urutan=1;
        while (urutan<=200) {
            final DatabaseReference bagianRef = database.getReference("tempats" + urutan);

            final int urutanakhir = urutan;
            final int urutanakhir0 = urutan-1;


            bagianRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                            final String nama = dataSnapshot.child("namaTempat").getValue(String.class);
                            String kapasitasString = dataSnapshot.child("kapasitas").getValue(String.class);
                            int kapasitas = Integer.parseInt(kapasitasString);

                            Boolean penentuhampir = hampirpenuh.get(urutanakhir0);
                            Boolean penentupenuh = sudahpenuh.get(urutanakhir0);

                        SharedPreferences prefspengatur = getSharedPreferences("pengatur", MODE_PRIVATE);
                        int hampir = prefspengatur.getInt("hampir", 50);
                        int penuh = prefspengatur.getInt("penuh",76);

                            if (kapasitas >= hampir && kapasitas <= penuh-1) {
                                if (penentuhampir) {
                                    shownotification(nama, "Hampir penuh", urutanakhir0);
                                    hampirpenuh.set(urutanakhir0, false);
                                }
                            } else {
                                hampirpenuh.set(urutanakhir0, true);

                            }
                            if (kapasitas >= penuh && kapasitas <= 100) {
                                if (!penentupenuh) {
                                    shownotification(nama, "Sudah penuh", urutanakhir0);
                                    sudahpenuh.set(urutanakhir0, true);
                                }
                            } else {
                                sudahpenuh.set(urutanakhir0, false);

                            }


                            if (dataSnapshot.hasChild("batas") && dataSnapshot.hasChild("namaTempat")) {

                                final String namatempat = dataSnapshot.child("namaTempat").getValue(String.class);

                                Calendar cel = Calendar.getInstance();
                                Date klk = cel.getTime();
                                Date Batasnya = dataSnapshot.child("batas").getValue(Date.class);
                                long sekarrangggg = klk.getTime();


                                long batasAtas = Batasnya.getTime();


                                final long kurang = batasAtas - sekarrangggg;


                                if (kurang >= 2000) {
                                    setAlarm(kurang, namatempat, urutanakhir);
                                }


                            }
                        }
                    }



                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            urutan++;
        }


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



    }

    public void about_app() {
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this).setIcon(R.drawable.about).setTitle("About");
        final View dialogView = li.inflate(R.layout.dialog_view, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("About");



        AlertDialog b = dialogBuilder.create();
        b.show();


    }



    public void setting_notif() {
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this).setIcon(R.drawable.icon_setting).setTitle("Setting");
        final View dialogView = li.inflate(R.layout.setting_layout, null);
        dialogBuilder.setView(dialogView);


        final EditText edthampir = (EditText) dialogView.findViewById(R.id.editTexthampir);
        final EditText edtpenuh = (EditText) dialogView.findViewById(R.id.editTextpenuh);

      //  edthampir.setText(hampir);
        //edtpenuh.setText(penuh);

        final Button kirimpenuh =(Button)dialogView.findViewById(R.id.pengaturpenuh);

        dialogBuilder.setTitle("pengaturan pemberitahuan tingkat ketinggian");


           kirimpenuh.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   Toast.makeText(DeltaActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();

                       int penuhbox = Integer.parseInt(edtpenuh.getText().toString());
                       int hampirbox = Integer.parseInt(edthampir.getText().toString());


                       SharedPreferences prefs = getSharedPreferences("pengatur", MODE_PRIVATE);
                       SharedPreferences.Editor editor = prefs.edit();
                       editor.putInt("hampir", hampirbox);
                       editor.putInt("penuh", penuhbox);


                       editor.apply();
                       editor.commit();
                   }




           });







        AlertDialog b = dialogBuilder.create();
        b.show();


    }



}