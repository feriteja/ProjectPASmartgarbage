

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mButtonLogin;
    TextView mforgot;
    FrameLayout activity_main;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference bagianRef = database.getReference();

    private ProgressDialog mProgressDialog;

    private FirebaseAuth firebaseAuth;
    DatabaseReference userLog = database.getReference("UserLog");
    ArrayList<Boolean> login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        firebaseAuth=FirebaseAuth.getInstance();



        login=new ArrayList<Boolean>();
        login.add(false);



        if(firebaseAuth.getCurrentUser()!=null){
            //profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(),DeltaActivity.class));

        }
        activity_main = (FrameLayout) findViewById(R.id.activity_main);
        mProgressDialog=new ProgressDialog(this);

        mEditTextEmail=(EditText)findViewById(R.id.editTextEmail);

        mEditTextPassword=(EditText)findViewById(R.id.editTextPassword);

        mButtonLogin=(Button)findViewById(R.id.buttonLogin);

        mforgot=(TextView)findViewById(R.id.forgot_password);

        mButtonLogin.setOnClickListener(this);
        mforgot.setOnClickListener(this);



    }

    private void signin(){
      final  String Email = mEditTextEmail.getText().toString().trim();
      final  String Password = mEditTextPassword.getText().toString().trim();

        mProgressDialog.setMessage("Sedang Login" );
        mProgressDialog.show();


        if(TextUtils.isEmpty(Email)){
            Toast.makeText(this,"Mohon Masukan Email",Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
            return;
        }

        if(TextUtils.isEmpty(Password)) {
            Toast.makeText(this, "Mohon Masukan Password", Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();

            return;
        }


        mProgressDialog.setMessage("Please wait" );
        mProgressDialog.show();

        userLog.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value= dataSnapshot.getValue(String.class);
                String mail= "feriteja@gmail.com";
                if(mail.equals(value)){
                    login.set(0,false);
                }
                else if(!mail.equals(value)){
                    login.set(0,true);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


            firebaseAuth.signInWithEmailAndPassword(Email,Password)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(login.get(0)) {
                                if (task.isSuccessful()) {
                                    //Start the profile Activity
                                    pindah();
                                    Toast.makeText(getBaseContext(), "Wellcome", Toast.LENGTH_SHORT).show();


                                    finish();
                                    startActivity(new Intent(getApplicationContext(), DeltaActivity.class));
                                    userLog.setValue(Email);

                                } else if (!task.isSuccessful()) {


                                    Snackbar.make(activity_main, "Incorrect Email or Password", Snackbar.LENGTH_LONG).show();

                                    mProgressDialog.dismiss();


                                }

                            }
                            else{
                                Toast.makeText(getBaseContext(), "already login", Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();


                            }

                        }

                    });
        }




    @Override
    public void onClick(View view) {
        if(view == mButtonLogin){
            signin();

        }
        if(view == mforgot){
            startActivity(new Intent(this, ForgotPassword.class));
        }
    }



    public void pindah(){
        int urutan=1;
        while (urutan<=200) {
            final DatabaseReference bagianRef = database.getReference("tempats" + urutan);

            final int urutanakhir=urutan;

            final int urutanakhir0=urutan-1;
            bagianRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists())
                    {


                        String namatempat=dataSnapshot.child("namaTempat").getValue(String.class);
                        String kapasitasString= dataSnapshot.child("kapasitas").getValue(String.class);
                        int kapasitas=Integer.parseInt(kapasitasString);
                        if(kapasitas ==50 ||kapasitas ==51 || kapasitas== 58 ||kapasitas== 59 ||kapasitas== 65 ||kapasitas== 66 ||kapasitas== 74||kapasitas== 75){
                            shownotification(namatempat,"Tempat hampir penuh",urutanakhir0);

                        }

                        if(kapasitas== 81 || kapasitas==82|| kapasitas==85 || kapasitas==86|| kapasitas==89||(kapasitas>=93&&kapasitas <=99) ){
                            shownotification(namatempat,"Tempat sampah penuh",urutanakhir0);
                        }


                        SharedPreferences prefs = getSharedPreferences("datanya", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("nilaiTempat"+urutanakhir, namatempat);
                        editor.putInt("nilaiKapasitas"+urutanakhir, kapasitas);

                        editor.commit();
                        editor.apply();

                    }
                    else{
                        SharedPreferences prefs = getSharedPreferences("datanya", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("nilaiTempat"+urutanakhir, "kosong");
                        editor.commit();
                        editor.apply();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            urutan++;
        }
    }

    public void shownotification (String a, String b,int id) {

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
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(id, builder.build());


    }


}
