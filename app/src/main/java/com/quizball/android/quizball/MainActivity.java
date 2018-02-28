package com.quizball.android.quizball;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    String leagues[] = {"UEFA Champions League", "United Kingdom", "Spain", "Germany"};
    private Handler mhandler = new Handler();
    String id = "0";
    String unAnswered = "0";
    int index = 0;
    int clState = 0;
    int ukState = 0;
    int espState = 0;
    int gerState = 0;
    DBHelper helper;
    String deleteID = "";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navView;
    Menu menu;
    int clTries = 0;
    int ukTries = 0;
    int espTries = 0;
    int gerTries = 0;
    int clLevel = 0;
    int ukLevel = 0;
    int espLevel = 0;
    int gerLevel = 0;
    MediaPlayer whistleShort;
    MediaPlayer whistleLong;
    MediaPlayer yes;
    MediaPlayer no;
    ImageView share;
    String image = "screenshot";
    Menu mute;
    int currMuteStatus;
    final String ADMOB_APP_ID = "ca-app-pub-1434090651445655~4094875803";
    final String ADMOB_APP_ID_TEST = "ca-app-pub-3940256099942544~3347511713";
    AdView adView;



    //"ca-app-pub-1434090651445655/5188465626"


    @Override
    protected void onPause() {
        adView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adView!=null){  // Check if Adview is not null in case of fist time load.
            adView.resume();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean info = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("info", true);

        if(info){
            initialDialog();
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("info", false)
                    .commit();
        }

        MobileAds.initialize(this, ADMOB_APP_ID);

        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener()
        {
            public void onAdLoaded(){
                adView.bringToFront();
            }
        });

        helper = new DBHelper(this);
        setTitle("");

        //whistleShort = MediaPlayer.create(this, R.raw.whistle_long);
        whistleLong = MediaPlayer.create(this, R.raw.whistle);
        yes = MediaPlayer.create(this, R.raw.yes);
        no = MediaPlayer.create(this, R.raw.no);
        currMuteStatus = muteState();




        navView = (NavigationView) findViewById(R.id.navView);
        navView.setItemIconTintList(null);
        navView.bringToFront();
        navView.setNavigationItemSelectedListener(this);


        clLevel = checkCLLevel();
        clTries = checkCLTries();

        ukLevel = checkUKLevel();
        ukTries = checkUKTries();

        espLevel = checkESPLevel();
        espTries = checkESPTries();

        gerLevel = checkGERLevel();
        gerTries = checkGERTries();

        menu = navView.getMenu();

        //menu.getItem(0).setTitle("Champions league          " + clTries);

        setMenuCounter(0, clTries);
        setMenuCounter(1, ukTries);
        setMenuCounter(2, espTries);
        setMenuCounter(3, gerTries);


        switch (clLevel){
            case 1:
                menu.getItem(0).setIcon(R.drawable.ic_level_2);
                break;


            case 2:
                menu.getItem(0).setIcon(R.drawable.ic_level_3);
                break;


            case 3:
                menu.getItem(0).setIcon(R.drawable.ic_level_4);
                break;


            case 4:
                menu.getItem(0).setIcon(R.drawable.ic_level_5);
                break;

            case 5:
                menu.getItem(0).setIcon(R.drawable.ic_level_5);
                break;

            case 6:
                menu.getItem(0).setIcon(R.drawable.ic_level_6);
                break;

            case 7:
                menu.getItem(0).setIcon(R.drawable.ic_level_6);
                break;

            case 8:
                menu.getItem(0).setIcon(R.drawable.ic_level_7);
                break;

            case 9:
                menu.getItem(0).setIcon(R.drawable.ic_level_7);
                break;

            case 10:
                menu.getItem(0).setIcon(R.drawable.ic_level_8);
                break;

            case 11:
                menu.getItem(0).setIcon(R.drawable.ic_level_8);
                break;


            case 12:
                menu.getItem(0).setIcon(R.drawable.ic_level_9);
                break;

            case 13:
                menu.getItem(0).setIcon(R.drawable.ic_level_9);
                break;

            case 14:
                menu.getItem(0).setIcon(R.drawable.ic_level_10_dark);
                break;

            case 15:
                menu.getItem(0).setIcon(R.drawable.ic_level_10_dark);
                break;
        }


        switch (ukLevel){
            case 2:
                menu.getItem(1).setIcon(R.drawable.ic_level_2);
                break;

            case 3:
                menu.getItem(1).setIcon(R.drawable.ic_level_2);
                break;

            case 4:
                menu.getItem(1).setIcon(R.drawable.ic_level_3);
                break;

            case 5:
                menu.getItem(1).setIcon(R.drawable.ic_level_3);
                break;

            case 6:
                menu.getItem(1).setIcon(R.drawable.ic_level_4);
                break;

            case 7:
                menu.getItem(1).setIcon(R.drawable.ic_level_4);
                break;

            case 8:
                menu.getItem(1).setIcon(R.drawable.ic_level_5);
                break;

            case 9:
                menu.getItem(1).setIcon(R.drawable.ic_level_5);
                break;

            case 10:
                menu.getItem(1).setIcon(R.drawable.ic_level_5);
                break;

            case 11:
                menu.getItem(1).setIcon(R.drawable.ic_level_6);
                break;

            case 12:
                menu.getItem(1).setIcon(R.drawable.ic_level_6);
                break;

            case 13:
                menu.getItem(1).setIcon(R.drawable.ic_level_6);
                break;

            case 14:
                menu.getItem(1).setIcon(R.drawable.ic_level_7);
                break;

            case 15:
                menu.getItem(1).setIcon(R.drawable.ic_level_7);
                break;

            case 16:
                menu.getItem(1).setIcon(R.drawable.ic_level_7);
                break;

            case 17:
                menu.getItem(1).setIcon(R.drawable.ic_level_8);
                break;

            case 18:
                menu.getItem(1).setIcon(R.drawable.ic_level_8);
                break;

            case 19:
                menu.getItem(1).setIcon(R.drawable.ic_level_8);
                break;

            case 20:
                menu.getItem(1).setIcon(R.drawable.ic_level_9);
                break;

            case 21:
                menu.getItem(1).setIcon(R.drawable.ic_level_9);
                break;

            case 22:
                menu.getItem(1).setIcon(R.drawable.ic_level_9);
                break;

            case 23:
                menu.getItem(1).setIcon(R.drawable.ic_level_9);
                break;

            case 24:
                menu.getItem(1).setIcon(R.drawable.ic_level_9);
                break;

            case 25:
                menu.getItem(1).setIcon(R.drawable.ic_level_9);
                break;

            case 26:
                menu.getItem(1).setIcon(R.drawable.ic_level_10_dark);
                break;

            case 27:
                menu.getItem(1).setIcon(R.drawable.ic_level_10_dark);
                break;

            case 28:
                menu.getItem(1).setIcon(R.drawable.ic_level_10_dark);
                break;

            case 29:
                menu.getItem(1).setIcon(R.drawable.ic_level_10_dark);
                break;

            case 30:
                menu.getItem(1).setIcon(R.drawable.ic_level_10_dark);
                break;
        }

        switch (espLevel){
            case 2:
                menu.getItem(2).setIcon(R.drawable.ic_level_2);
                break;

            case 3:
                menu.getItem(2).setIcon(R.drawable.ic_level_2);
                break;

            case 4:
                menu.getItem(2).setIcon(R.drawable.ic_level_3);
                break;

            case 5:
                menu.getItem(2).setIcon(R.drawable.ic_level_3);
                break;

            case 6:
                menu.getItem(2).setIcon(R.drawable.ic_level_4);
                break;

            case 7:
                menu.getItem(2).setIcon(R.drawable.ic_level_4);
                break;

            case 8:
                menu.getItem(2).setIcon(R.drawable.ic_level_5);
                break;

            case 9:
                menu.getItem(2).setIcon(R.drawable.ic_level_5);
                break;

            case 10:
                menu.getItem(2).setIcon(R.drawable.ic_level_5);
                break;

            case 11:
                menu.getItem(2).setIcon(R.drawable.ic_level_6);
                break;

            case 12:
                menu.getItem(2).setIcon(R.drawable.ic_level_6);
                break;

            case 13:
                menu.getItem(2).setIcon(R.drawable.ic_level_6);
                break;

            case 14:
                menu.getItem(2).setIcon(R.drawable.ic_level_7);
                break;

            case 15:
                menu.getItem(2).setIcon(R.drawable.ic_level_7);
                break;

            case 16:
                menu.getItem(2).setIcon(R.drawable.ic_level_7);
                break;

            case 17:
                menu.getItem(2).setIcon(R.drawable.ic_level_8);
                break;

            case 18:
                menu.getItem(2).setIcon(R.drawable.ic_level_8);
                break;

            case 19:
                menu.getItem(2).setIcon(R.drawable.ic_level_8);
                break;

            case 20:
                menu.getItem(2).setIcon(R.drawable.ic_level_9);
                break;

            case 21:
                menu.getItem(2).setIcon(R.drawable.ic_level_9);
                break;

            case 22:
                menu.getItem(2).setIcon(R.drawable.ic_level_9);
                break;

            case 23:
                menu.getItem(2).setIcon(R.drawable.ic_level_9);
                break;

            case 24:
                menu.getItem(2).setIcon(R.drawable.ic_level_9);
                break;

            case 25:
                menu.getItem(2).setIcon(R.drawable.ic_level_9);
                break;

            case 26:
                menu.getItem(2).setIcon(R.drawable.ic_level_10_dark);
                break;

            case 27:
                menu.getItem(2).setIcon(R.drawable.ic_level_10_dark);
                break;

            case 28:
                menu.getItem(2).setIcon(R.drawable.ic_level_10_dark);
                break;

            case 29:
                menu.getItem(2).setIcon(R.drawable.ic_level_10_dark);
                break;

            case 30:
                menu.getItem(2).setIcon(R.drawable.ic_level_10_dark);
                break;
        }

        switch (gerLevel){
            case 2:
                menu.getItem(3).setIcon(R.drawable.ic_level_2);
                break;

            case 3:
                menu.getItem(3).setIcon(R.drawable.ic_level_2);
                break;

            case 4:
                menu.getItem(3).setIcon(R.drawable.ic_level_3);
                break;

            case 5:
                menu.getItem(3).setIcon(R.drawable.ic_level_3);
                break;

            case 6:
                menu.getItem(3).setIcon(R.drawable.ic_level_4);
                break;

            case 7:
                menu.getItem(3).setIcon(R.drawable.ic_level_4);
                break;

            case 8:
                menu.getItem(3).setIcon(R.drawable.ic_level_5);
                break;

            case 9:
                menu.getItem(3).setIcon(R.drawable.ic_level_5);
                break;

            case 10:
                menu.getItem(3).setIcon(R.drawable.ic_level_5);
                break;

            case 11:
                menu.getItem(3).setIcon(R.drawable.ic_level_6);
                break;

            case 12:
                menu.getItem(3).setIcon(R.drawable.ic_level_6);
                break;

            case 13:
                menu.getItem(3).setIcon(R.drawable.ic_level_6);
                break;

            case 14:
                menu.getItem(3).setIcon(R.drawable.ic_level_7);
                break;

            case 15:
                menu.getItem(3).setIcon(R.drawable.ic_level_7);
                break;

            case 16:
                menu.getItem(3).setIcon(R.drawable.ic_level_7);
                break;

            case 17:
                menu.getItem(3).setIcon(R.drawable.ic_level_8);
                break;

            case 18:
                menu.getItem(3).setIcon(R.drawable.ic_level_8);
                break;

            case 19:
                menu.getItem(3).setIcon(R.drawable.ic_level_8);
                break;

            case 20:
                menu.getItem(3).setIcon(R.drawable.ic_level_9);
                break;

            case 21:
                menu.getItem(3).setIcon(R.drawable.ic_level_9);
                break;

            case 22:
                menu.getItem(3).setIcon(R.drawable.ic_level_9);
                break;

            case 23:
                menu.getItem(3).setIcon(R.drawable.ic_level_9);
                break;

            case 24:
                menu.getItem(3).setIcon(R.drawable.ic_level_9);
                break;

            case 25:
                menu.getItem(3).setIcon(R.drawable.ic_level_9);
                break;

            case 26:
                menu.getItem(3).setIcon(R.drawable.ic_level_10_dark);
                break;

            case 27:
                menu.getItem(3).setIcon(R.drawable.ic_level_10_dark);
                break;

            case 28:
                menu.getItem(3).setIcon(R.drawable.ic_level_10_dark);
                break;

            case 29:
                menu.getItem(3).setIcon(R.drawable.ic_level_10_dark);
                break;

            case 30:
                menu.getItem(3).setIcon(R.drawable.ic_level_10_dark);
                break;
        }



        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //navView.setItemIconTintList(null);
        boolean firstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstRun", true);

        if(firstRun){
            SQLiteDatabase muteDB = helper.getWritableDatabase();
            helper.muteAudio(0,muteDB);
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("firstRun", false)
                    .commit();
        }
        /*getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_quizball_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);*/



        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        //setSupportActionBar(toolbar);

        //toolbar.setLogo(R.drawable.ic_quizball_logo);
        clState = clState();
        ukState = ukState();
        espState = espState();
        gerState = gerState();
        //centerTitle();
        CircleMenu circleMenu = (CircleMenu)findViewById(R.id.circle_menu);
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"),R.drawable.ic_football,R.drawable.ic_football)
                .addSubMenu(Color.parseColor("#258CFF"), R.drawable.ic_uefa)
                .addSubMenu(Color.parseColor("#F44336"), R.drawable.ic_united_kingdom)
                .addSubMenu(Color.parseColor("#FFEB3B"), R.drawable.ic_spain_flag2)
                .addSubMenu(Color.parseColor("#000000"), R.drawable.ic_germany_flag)


                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(final int index) {
                        Log.d("MENUSELECTED", " " + index);
                        currMuteStatus = muteState();
                        if(currMuteStatus == 0) {
                            whistleLong.start();
                        }
                        if(clState < 15 && index == 0) {
                                mhandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                       // switch (index) {
                                          //  case 0:

                                            Intent intent = new Intent(MainActivity.this, ChampionsLeague.class);
                                            intent.putExtra("muteState", currMuteStatus);
                                            startActivity(intent);
                                            finish();
                                            //overridePendingTransition(0,0);

                                    }
                                }, 1500);//2 second delay

                    }

                       else if(ukState < 30 && index == 1) {
                            mhandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(MainActivity.this, UnitedKingdom.class);
                                    intent.putExtra("muteState", currMuteStatus);
                                    startActivity(intent);
                                    finish();

                                }

                            }, 1500);
                        }

                        else if(espState < 30 && index == 2) {
                                    mhandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(MainActivity.this, Spain.class);
                                            intent.putExtra("muteState", currMuteStatus);
                                            startActivity(intent);
                                            finish();                                        }
                                    }, 1500);

                                }


                        else if(gerState < 30 && index == 3) {
                                        mhandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(MainActivity.this, Germany.class);
                                                intent.putExtra("muteState", currMuteStatus);
                                                startActivity(intent);
                                                finish();                                             }
                                        }, 1500);

                                    }


                    else if(clState >= 15 && index == 0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("All questions answered");
                            builder.setMessage("You have already answered all the Champions League questions, would you like to give it another attempt? " +
                                    "Be aware that this will add to your Champions League attempts");

                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    currMuteStatus = muteState();
                                    if(currMuteStatus == 0) {
                                        yes.start();
                                    }
                                    SQLiteDatabase db = helper.getWritableDatabase();

                                    helper.ChangeCLTries(1);
                                    Integer deletedRows = helper.delete("CL", db);
                                    clState = 0;

                                    Intent intent = new Intent(MainActivity.this, ChampionsLeague.class);
                                    //intent.putExtra("muteState", currMuteStatus);
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                    //overridePendingTransition(0,0);
                                    finish();

                                    }


                            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    currMuteStatus = muteState();
                    if(currMuteStatus == 0) {
                        no.start();
                    }
                    // You don't have to do anything here if you just want it dismissed when clicked
                }
            });
                            builder.show();
                        }


                        else if(ukState >= 30 && index == 1){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("All questions answered");
                            builder.setMessage("You have already answered all the United Kingdom questions, would you like to give it another attempt? " +
                                    "Be aware that this will add to your United Kingdom attempts");

                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    currMuteStatus = muteState();
                                    if(currMuteStatus == 0) {
                                        yes.start();
                                    }
                                    SQLiteDatabase db = helper.getWritableDatabase();

                                    helper.ChangeUKTries(1);
                                    Integer deletedRows = helper.delete("UK", db);
                                    ukState = 0;

                                    Intent intent = new Intent(MainActivity.this, UnitedKingdom.class);
                                    //intent.putExtra("muteState", currMuteStatus);
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                    //overridePendingTransition(0,0);
                                    finish();

                                }


                            });

                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    currMuteStatus = muteState();
                                    if(currMuteStatus == 0) {
                                        no.start();
                                    }
                                    // You don't have to do anything here if you just want it dismissed when clicked
                                }
                            });
                            builder.show();
                        }

                        else if(espState >= 30 && index == 2){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("All questions answered");
                            builder.setMessage("You have already answered all the Spain related questions, would you like to give it another attempt? " +
                                    "Be aware that this will add to your Spain attempts");

                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    currMuteStatus = muteState();
                                    if(currMuteStatus == 0) {
                                        yes.start();
                                    }
                                    SQLiteDatabase db = helper.getWritableDatabase();

                                    helper.ChangeESPTries(1);
                                    Integer deletedRows = helper.delete("ESP", db);
                                    espState = 0;

                                    Intent intent = new Intent(MainActivity.this, Spain.class);
                                    //intent.putExtra("muteState", currMuteStatus);
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                    //overridePendingTransition(0,0);
                                    finish();

                                }


                            });

                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    currMuteStatus = muteState();
                                    if(currMuteStatus == 0) {
                                        no.start();
                                    }
                                    // You don't have to do anything here if you just want it dismissed when clicked
                                }
                            });
                            builder.show();
                        }


                        else if(gerState >= 30 && index == 3){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("All questions answered");
                            builder.setMessage("You have already answered all the Germany related questions, would you like to give it another attempt? " +
                                    "Be aware that this will add to your Germany attempts");

                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    currMuteStatus = muteState();
                                    if(currMuteStatus == 0) {
                                        yes.start();
                                    }
                                    SQLiteDatabase db = helper.getWritableDatabase();

                                    helper.ChangeGERTries(1);
                                    Integer deletedRows = helper.delete("GER", db);
                                    gerState = 0;

                                    Intent intent = new Intent(MainActivity.this, Germany.class);
                                    //intent.putExtra("muteState", currMuteStatus);
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                    //overridePendingTransition(0,0);
                                    finish();

                                }


                            });

                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    currMuteStatus = muteState();
                                    if(currMuteStatus == 0) {
                                        no.start();
                                    }
                                    // You don't have to do anything here if you just want it dismissed when clicked
                                }
                            });
                            builder.show();
                        }


                    }
                });


    }

    private void initialDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("About question's");
        builder.setMessage("All question's asked in this app are as of the end of the 2016/2017 season");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                    yes.start();
            }


        });

        /*builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // You don't have to do anything here if you just want it dismissed when clicked
            }
        });*/
        builder.show();

    }


    private int espState() {
        DBHelper helper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getESPUnanswered(db);

        while (cursor.moveToNext() && index <= 30) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            unAnswered = cursor.getString(cursor.getColumnIndex(helper.COL_IF_ANSWERED));
            index = Integer.parseInt(id);

            Log.d("UNANSWERED", "current position: " + id);

        }
        return index;
    }

    private int gerState() {
        DBHelper helper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getGERUnanswered(db);

        while (cursor.moveToNext() && index <= 30) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            unAnswered = cursor.getString(cursor.getColumnIndex(helper.COL_IF_ANSWERED));
            index = Integer.parseInt(id);

            Log.d("UNANSWEREDGER", "current position: " + id);

        }
        return index;
    }


    private void setMenuCounter(int itemId, int count){
        TextView view = (TextView) navView.getMenu().getItem(itemId).getActionView();
        view.setText(count > -1 ? String.valueOf(count) : null);
    }

    private int checkCLTries() {
        DBHelper helper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getCLTries(db);
        String tries;
        int triesCount = 0;

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            tries = cursor.getString(cursor.getColumnIndex(helper.COL_CL_TRIES));
            index = Integer.parseInt(id);


            //Log.d("UNANSWERED", "current position: " + id);

        }
        return index;
    }



    private int checkCLLevel() {
        DBHelper helper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getCLLevel(db);
        String level;
        int ansCorr = 0;

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            level = cursor.getString(cursor.getColumnIndex(helper.COL_CHAMPS_LEVEL));
            index = Integer.parseInt(level);

            if(index == 1){
                ansCorr++;
            }

            //Log.d("UNANSWERED", "current position: " + id);

        }
        return ansCorr;
    }


    private int checkUKTries(){
        DBHelper helper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getUKTries(db);
        String tries;
        int triesCount = 0;

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            tries = cursor.getString(cursor.getColumnIndex(helper.COL_UNITED_TRIES));
            index = Integer.parseInt(id);

        }
        return index;
    }

    private int checkUKLevel() {
        DBHelper helper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getUKLevel(db);
        String level;
        int ansCorr = 0;

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            level = cursor.getString(cursor.getColumnIndex(helper.COL_UNITED_LEVEL));
            index = Integer.parseInt(level);

            if(index == 1){
                ansCorr++;
            }

            //Log.d("UNANSWERED", "current position: " + id);

        }
        return ansCorr;
    }

    private int checkESPLevel() {
        DBHelper helper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getESPLevel(db);
        String level;
        int ansCorr = 0;

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            level = cursor.getString(cursor.getColumnIndex(helper.COL_ESP_LEVEL));
            index = Integer.parseInt(level);

            if(index == 1){
                ansCorr++;
            }

            //Log.d("UNANSWERED", "current position: " + id);

        }
        return ansCorr;
    }

    private int checkESPTries(){
        DBHelper helper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getESPTries(db);
        String tries;
        int triesCount = 0;

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            tries = cursor.getString(cursor.getColumnIndex(helper.COL_ESP_TRIES));
            index = Integer.parseInt(id);

        }
        return index;
    }


    private int checkGERLevel() {
        DBHelper helper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getGERLevel(db);
        String level;
        int ansCorr = 0;

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            level = cursor.getString(cursor.getColumnIndex(helper.COL_GER_LEVEL));
            index = Integer.parseInt(level);

            if(index == 1){
                ansCorr++;
            }

            //Log.d("UNANSWERED", "current position: " + id);

        }
        return ansCorr;
    }

    private int checkGERTries(){
        DBHelper helper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getGERTries(db);
        String tries;
        int triesCount = 0;

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            tries = cursor.getString(cursor.getColumnIndex(helper.COL_GER_TRIES));
            index = Integer.parseInt(id);

        }
        return index;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mute, menu);

        mute = menu;

        currMuteStatus = muteState();
        if(currMuteStatus == 0){
            menu.getItem(0).setIcon(R.drawable.ic_unmute);
        }

        else if(currMuteStatus == 1){
            menu.getItem(0).setIcon(R.drawable.ic_mute);
        }
        return true;
    }

    private void centerTitle() {
        ArrayList<View> textViews = new ArrayList<>();

        getWindow().getDecorView().findViewsWithText(textViews, getTitle(), View.FIND_VIEWS_WITH_TEXT);

        if(textViews.size() > 0) {
            AppCompatTextView appCompatTextView = null;
            if(textViews.size() == 1) {
                appCompatTextView = (AppCompatTextView) textViews.get(0);
            } else {
                for(View v : textViews) {
                    if(v.getParent() instanceof Toolbar) {
                        appCompatTextView = (AppCompatTextView) v;
                        break;
                    }
                }
            }

            if(appCompatTextView != null) {
                ViewGroup.LayoutParams params = appCompatTextView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                appCompatTextView.setLayoutParams(params);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    appCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        else if(item.getItemId() == R.id.mute_state){
            SQLiteDatabase muteDB = helper.getWritableDatabase();

            currMuteStatus = muteState();
            if(currMuteStatus == 0){
                mute.getItem(0).setIcon(R.drawable.ic_mute);
                helper.muteAudio(1,muteDB);
            }

            else if(currMuteStatus == 1){
                mute.getItem(0).setIcon(R.drawable.ic_unmute);
                helper.muteAudio(0,muteDB);
                yes.start();
            }
        }
        return super.onOptionsItemSelected(item);
    }



    private int muteState() {
        DBHelper helper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.muteState(db);
        String muteState;
        int muteIndex = 0;

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            muteState = cursor.getString(cursor.getColumnIndex(helper.COL_MUTE));
            muteIndex = Integer.parseInt(muteState);

            Log.d("UNANSWERED", "current position: " + id);

        }
        return muteIndex;
    }

    private int clState() {
        DBHelper helper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getCLUnanswered(db);

        while (cursor.moveToNext() && index <= 15) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            unAnswered = cursor.getString(cursor.getColumnIndex(helper.COL_IF_ANSWERED));
            index = Integer.parseInt(id);

            Log.d("UNANSWERED", "current position: " + id);

        }
        return index;
    }


    private int ukState() {
        DBHelper helper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getUKUnanswered(db);

        while (cursor.moveToNext() && index <= 30) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            unAnswered = cursor.getString(cursor.getColumnIndex(helper.COL_IF_ANSWERED));
            index = Integer.parseInt(id);

            Log.d("UNANSWERED", "current position: " + id);

        }
        return index;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.share){
            currMuteStatus = muteState();
            if(currMuteStatus == 0) {
                yes.start();
            }
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = "Check out this new game \"QuizBall\" on Google Play and see how well you know the world's greatest sport" + " " +
                    "https://play.google.com/store/apps/details?id=com.quizball.android.quizball";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"QuizBall");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));


            /*Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.quizball.android.quizball"));
            startActivity(intent);*/
        }

        return false;
    }



    /*public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void store(Bitmap bm, String fileName){
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        shareImage(dir);
    }

    private void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            //Toast.makeText(context, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }*/
}
