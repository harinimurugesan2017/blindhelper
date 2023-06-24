package com.example.software2.ocrhy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity8 extends AppCompatActivity {
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    TextView textView4, textView5;
    float x1, x2, y1, y2;
    FusedLocationProviderClient fusedLocationProviderClient;
    private TextToSpeech texttospeech;
    RelativeLayout RelativeLayout;
    TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
        textView4 = findViewById(R.id.text_view4);
        textView5 = findViewById(R.id.text_view5);
        RelativeLayout = findViewById(R.id.location);
        texttospeech = new TextToSpeech(MainActivity8.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    texttospeech.setLanguage(Locale.getDefault());
                    texttospeech.setSpeechRate(1f);
                    texttospeech.speak("swipe left to get current location and swipe right to return in main menu", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity8.this);
//        RelativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ActivityCompat.checkSelfPermission(MainActivity8.this,
//                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    getLocation();
//                } else {
//                    ActivityCompat.requestPermissions(MainActivity8.this,
//                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//                }
//            }
//        });
//    }
    }

    public boolean onTouchEvent(MotionEvent touchEvent) {

        switch (touchEvent.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if (x1 > x2) {
                    Handler handler=new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            texttospeech.speak("you are in main menu. just swipe right and say what you want", TextToSpeech.QUEUE_FLUSH, null);

                        }
                    },1000);
                    Intent intent = new Intent(MainActivity8.this, MainActivity.class);
                    startActivity(intent);
                    break;
                }
            if (x1 < x2) {
                getLocation();
                break;
            }
        }
        return false;
    }
    public void onPause() {
        if (texttospeech != null) {
            texttospeech.stop();
        }
        super.onPause();
    }


        public void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity8.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity8.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(MainActivity8.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                return;
        } else {
            ActivityCompat.requestPermissions(MainActivity8.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {

            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(MainActivity8.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        textView4.setText(Html.fromHtml(
                                "<font colour='#6200EE'><b>Locality :</b><br></font >" + addresses.get(0).getLocality()
                        ));
                        textView5.setText(Html.fromHtml(
                                "<font colour='#6200EE'><b>Address :</b><br></font>" + addresses.get(0).getAddressLine(0).replaceAll("62/1/8" + "411046, india", "")
                        ));
                        texttospeech = new TextToSpeech(MainActivity8.this, new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status != TextToSpeech.ERROR) {
                                    texttospeech.setLanguage(Locale.UK);
                                    texttospeech.setSpeechRate(1f);
                                    texttospeech.speak("Your nearest locality is ," + addresses.get(0).getLocality() + ",And current address is " + addresses.get(0).getAddressLine(0).replaceAll("62/1/8" + "411046", ""), TextToSpeech.QUEUE_FLUSH, null);
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            texttospeech.speak("swipe left to listen again or swipe right to return back in main menu", TextToSpeech.QUEUE_ADD,null);
                                        }
                                    });
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }


        });
    }

    public boolean onKeyDown(int keyCode, @Nullable KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            texttospeech.speak("You are in main menu. just swipe right and say what you want", TextToSpeech.QUEUE_FLUSH, null);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    texttospeech.speak("you are in main menu. just swipe right and say what you want", TextToSpeech.QUEUE_FLUSH, null);

                }
            }, 1000);

        }
        return true;
    }



}

