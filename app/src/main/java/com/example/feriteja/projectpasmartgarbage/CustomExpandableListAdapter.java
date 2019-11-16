package com.example.feriteja.projectpasmartgarbage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter implements Filterable {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    public ArrayList<Category> orig;

    private ArrayList<Category> catList;


    private Context context;


    private Calendar calendar1 = Calendar.getInstance();


    private DateFormat formatDateTime = DateFormat.getDateTimeInstance();


    public CustomExpandableListAdapter(ArrayList<Category> catList, Context context) {


        this.catList = catList;
        this.context = context;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return catList.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return catList.get(groupPosition).hashCode();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }

        final TextView itemName = (TextView) convertView.findViewById(R.id.progressText);
        final TextView statusText=(TextView)convertView.findViewById(R.id.statusText);
        final Button button= (Button)convertView.findViewById(R.id.button);
        final Button jamButton=(Button)convertView.findViewById(R.id.jamButton);
        final Button tglButton=(Button)convertView.findViewById(R.id.tglButton);
        final Button mConfirm=(Button)convertView.findViewById(R.id.confButton);
        final ProgressBar progressBar=(ProgressBar)convertView.findViewById(R.id.progressBar);



        final int a=catList.get(groupPosition).getId();

        tglButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,AlertDialog.THEME_HOLO_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                calendar1.set(Calendar.YEAR,year);
                                calendar1.set(Calendar.MONTH,monthOfYear);
                                calendar1.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                updateTextLabel(a);

                            }
                        }, calendar1.get(Calendar.YEAR),
                        calendar1.get(Calendar.MONTH),
                        calendar1.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });


        jamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,AlertDialog.THEME_HOLO_LIGHT,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                calendar1.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar1.set(Calendar.MINUTE,minute);
                                updateTextLabel(a);

                            }
                        }, calendar1.get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });








      final  DatabaseReference tempatRef = database.getReference("tempats" + a);





        tempatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("namaTempat")){
                   final String nama= dataSnapshot.child("namaTempat").getValue(String.class);
                    mConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Calendar calconfirm = Calendar.getInstance();
                            calconfirm.add(Calendar.DAY_OF_MONTH, 3);
                            Date calconfirmDate = calconfirm.getTime();
                            tempatRef.child("batas").setValue(calconfirmDate);
                            shownotifwaktu(calconfirmDate,nama);


                        }

                    });
                }

               if (dataSnapshot.exists()) {
                   String kapasitasString = dataSnapshot.child("kapasitas").getValue(String.class);
                   itemName.setText(kapasitasString + "%");
                   int kapasitas = Integer.parseInt(kapasitasString);
                   progressBar.setProgress(kapasitas);

                   if (dataSnapshot.hasChild("batas")) {

                       final Date Batasnya = dataSnapshot.child("batas").getValue(Date.class);
                       statusText.setText(formatDateTime.format(Batasnya.getTime()));

                   }


                   if (dataSnapshot.hasChild("latitude") && dataSnapshot.hasChild("longitude")) {
                       String latitudeString = dataSnapshot.child("latitude").getValue(String.class);
                       String longitudeString = dataSnapshot.child("longitude").getValue(String.class);

                       final Double latitude = Double.parseDouble(latitudeString);
                       final Double longitude = Double.parseDouble(longitudeString);


                       button.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               Intent intent = new Intent(context, MapsActivity.class);
                               intent.putExtra("latitude", latitude);
                               intent.putExtra("longitude", longitude);
                               intent.putExtra("zoom", 16);

                               context.startActivity(intent);


                           }
                       });
                   }
               }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        return convertView;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = 1;
        System.out.println("Child for group ["+groupPosition+"] is ["+size+"]");
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return catList.get(groupPosition);
    }


    @Override
    public int getGroupCount() {

        int soze=catList.size();

        return soze;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return catList.get(groupPosition).hashCode();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        final TextView groupName = (TextView) convertView.findViewById(R.id.listTitlenya);
        groupName.setTypeface(null, Typeface.BOLD);


        groupName.setText(catList.get(groupPosition).getNamaTempat());

        final int a=catList.get(groupPosition).getId();



        final ImageView imagestatus = (ImageView)convertView.findViewById(R.id.imageView2);
        final ProgressBar progressBaratas=(ProgressBar)convertView.findViewById(R.id.progressBar2);


       // progressBaratas.setProgress(Integer.valueOf(catList.get(groupPosition).getKapasitas()));
        DatabaseReference tempatRef = database.getReference("tempats" + a);

        tempatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    final   String nama=dataSnapshot.child("namaTempat").getValue(String.class);
                    String kapasitasString = dataSnapshot.child("kapasitas").getValue(String.class);
                    int kapasitas = Integer.parseInt(kapasitasString);
                    groupName.setText(nama);


                    progressBaratas.setProgress(kapasitas);

                    if(dataSnapshot.hasChild("batas")){
                    try {


                        String latitudeString = dataSnapshot.child("latitude").getValue(String.class);
                        String longitudeString = dataSnapshot.child("longitude").getValue(String.class);

                        final Double latitude = Double.parseDouble(latitudeString);
                        final Double longitude = Double.parseDouble(longitudeString);

                            Calendar cel = Calendar.getInstance();
                            Date sekarang = cel.getTime();
                            Date Batasnya = dataSnapshot.child("batas").getValue(Date.class);

                        SharedPreferences prefspengatur = context.getSharedPreferences("pengatur", MODE_PRIVATE);
                        int hampir = prefspengatur.getInt("hampir", 50);
                        int penuh = prefspengatur.getInt("penuh",76);




                            if (kapasitas >=0  && kapasitas <= hampir-1) {
                                imagestatus.setImageResource(R.drawable.circle_green);
                                if (sekarang.after(Batasnya)) {
                                    imagestatus.setImageResource(R.drawable.brown_circle);
                                    imagestatus.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                           // showChangeLangDialog(a);
                                            showChangenamaDialog(latitude,longitude,nama,a);
                                        }
                                    });

                                }

                            } else if (kapasitas >= hampir && kapasitas <= penuh-1) {
                                imagestatus.setImageResource(R.drawable.circle_yellow);
                                if (sekarang.after(Batasnya)) {
                                    imagestatus.setImageResource(R.drawable.brown_circle);
                                    imagestatus.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            //showChangeLangDialog(a);
                                            showChangenamaDialog(latitude,longitude,nama,a);
                                        }
                                    });

                                }
                            } else if (kapasitas >= penuh && kapasitas <= 100) {
                                imagestatus.setImageResource(R.drawable.circle_red);
                                if (sekarang.after(Batasnya)) {
                                    imagestatus.setImageResource(R.drawable.brown_circle);
                                    imagestatus.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            //showChangeLangDialog(a);
                                            showChangenamaDialog(latitude,longitude,nama,a);
                                        }
                                    });

                                }
                            }



                    }catch (Exception e){

                    }


                }}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return convertView;

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Category> results = new ArrayList<Category>();
                if (orig == null)
                    orig = catList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Category g : orig) {
                            if (g.getNamaTempat().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          Filter.FilterResults results) {
                catList = (ArrayList<Category>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    private void updateTextLabel(int nombernya) {


        DatabaseReference bagianRef = database.getReference("tempats"+nombernya);

        Date batas1 = calendar1.getTime();
        bagianRef.child("batas").setValue(batas1);

    }


    public void showChangeLangDialog(final int urutan) {

        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final View dialogView = li.inflate(R.layout.dialog_view, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("Masukan nama baru");
        dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = edt.getText().toString().trim();
                DatabaseReference tempatRef = database.getReference("tempats" + urutan);
                tempatRef.child("namaTempat").setValue(value);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();


    }

    public void showChangenamaDialog(final Double latitude, final Double longitude, final String nama, final int urutan) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Change dumpster's name ?");
        builder1.setCancelable(true);


        builder1.setPositiveButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(context, Gantinama.class);
                        intent.putExtra("latitudegantinama", latitude);
                        intent.putExtra("longitudegantinama", longitude);
                        intent.putExtra("nama", nama);
                        intent.putExtra("zoom", 16);
                        intent.putExtra("urutan",urutan);

                        context.startActivity(intent);
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();


    }


    public void shownotifwaktu(Date waktu, String nama){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Batas waktu pengangkutan "+ nama +" berikutnya: \n"+ String.valueOf(waktu));
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "CONFIRM",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}