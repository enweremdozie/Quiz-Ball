package com.quizball.android.quizball;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UnitedKingdom extends AppCompatActivity {
    DBHelper helper = new DBHelper(UnitedKingdom.this);
    BubblePicker bubblePicker;
    String[] answers = {"","","","",""};
    String id = "0";
    String unAnswered = "0";
    int index = 0;
    ItemObject ukQuestions[] = new ItemObject[30];
    int correctAns = 0;
    int currQuest = 0;
    TextView quest;
    int state = 0;
    int level = 0;
    int tries = 1;
    String wrongAnsPos = "";
    int quesState = 0;
    private Handler mhandler = new Handler();
    int[] images = {R.drawable.ic_chelsea, R.drawable.trevor_francis, R.drawable.andrei_kanchelskis, R.drawable.ic_manchester_united, R.drawable.ic_manchester_united, R.drawable.ic_arsenal, R.drawable.liverpool, R.drawable.prem_carling, R.drawable.bale, R.drawable.lukaku, R.drawable.pogba, R.drawable.prem, R.drawable.sir_alex, R.drawable.sir_alex, R.drawable.mancini, R.drawable.giggs, R.drawable.shearer, R.drawable.lampard, R.drawable.henry, R.drawable.fulham, R.drawable.ic_chelsea, R.drawable.ic_chelsea, R.drawable.derby, R.drawable.ic_manchester_united, R.drawable.prem, R.drawable.ic_chelsea, R.drawable.prem, R.drawable.ic_arsenal, R.drawable.sunderland, R.drawable.prem};
    int ball = R.drawable.ic_football;
    MediaPlayer correct;
    MediaPlayer wrong;
    MediaPlayer applause;
    MediaPlayer next;
    int muteStatus = 0;
    String muted = "";
    ImageView check;
    ImageView cross;
    int selected = -1;
    final String ADMOB_APP_ID = "ca-app-pub-1434090651445655~4094875803";
    final String ADMOB_APP_ID_TEST = "ca-app-pub-3940256099942544~3347511713";
    AdView adView;

    int[] colors = {Color.parseColor("#E040FB"),
            Color.parseColor("#42A5F5"),
            Color.parseColor("#D32F2F"),
            Color.parseColor("#388E3C"),
            Color.parseColor("#000000")};

    @Override
    protected void onResume() {
        super.onResume();
        adView.pause();
        bubblePicker.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(adView!=null){  // Check if Adview is not null in case of fist time load.
            adView.resume();
        }
        bubblePicker.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_united_kingdom);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        muteStatus = muteState();
        //muteStatus = Integer.parseInt(muted);
        setTitle("");

        initialRun();

        check = (ImageView) findViewById(R.id.uk_check);
        cross = (ImageView) findViewById(R.id.uk_cross);

        applause = MediaPlayer.create(this, R.raw.applause);
        correct = MediaPlayer.create(this, R.raw.wet);
        wrong = MediaPlayer.create(this, R.raw.nope);
        next = MediaPlayer.create(this, R.raw.yes);

        //Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_level_1);

        state = state();
        quest = (TextView) findViewById(R.id.UKText);
        ukQuestions[0] = new ItemObject("What team won the Premier league in the 2016/2017 season?","Chelsea","Arsenal","Manchester City","Manchester United","Leicester city",0,0,0);
        ukQuestions[1] = new ItemObject("Who was Britain's first 1 Million Pound player?","Dozie Enwerem","Trevor Francis","Bobby Charlton","Billy Wright","Kenny Sansom",1,0,0);
        ukQuestions[2] = new ItemObject("Who is the only player to score in a Manchester, Merseyside and Glasgow derby?","Andrei Kanchelskis","Tom Finney","Roger Hunt","Pegguy Arphexad","Daniel Carvalho",0,0,0);
        ukQuestions[3] = new ItemObject("What team has won the most premier league titles?","Arsenal","Chelsea","Manchester united","Aston villa","Manchester City",2,0,0);
        ukQuestions[4] = new ItemObject("In what season did Manchester United win their first Premier league trophy?","1991/92","1992/93","1993/94","1994/95","1995/96",1,0,0);
        ukQuestions[5] = new ItemObject("In what season did Arsenal win their first premier league trophy?","1995/96","1996/97","1997/98","2001/02","2003/04",2,0,0);
        ukQuestions[6] = new ItemObject("In what season did Liverpool win the premier league?","1992/93","1994/95","1997/98","1999/2000","NEVER",4,0,0);
        ukQuestions[7] = new ItemObject("In what years was Carling the title sponsor of the premier league?","1990-93","1993-2001","2001-2004","2004-2007","2008-2016",1,0,0);
        ukQuestions[8] = new ItemObject("Who is the most expensive player sold by a premier league team to a team outside of England?","Christiano Ronaldo","Paul Pogba","Gareth Bale","Luis Suarez","Angel Di Maria",2,0,0);
        ukQuestions[9] = new ItemObject("Who is the most expensive player sold between 2 premier league teams?","Romelu Lukaku","Raheem Sterling","Paul Pogba","Alvaro Morata","Fernando Torres",0,0,0);
        ukQuestions[10] = new ItemObject("Who is the most expensive player bought by a premier league team from outside of England?","Alvaro Morata","Paul Pogba","Kevin de Bruyne","Angel Di Maria","Gareth Bale",1,0,0);
        ukQuestions[11] = new ItemObject("As of the 2016/17 season how many stadiums had the premier league been played in?","41","45","49","53","55",4,0,0);
        ukQuestions[12] = new ItemObject("In how many seasons was Alex Ferguson a manager in the premier league?","20","21","22","23","24",1,0,0);
        ukQuestions[13] = new ItemObject("How many premier League titles does Alex Ferguson have?","10","11","12","13","14",3,0,0);
        ukQuestions[14] = new ItemObject("Which of these managers won the premier league in the 2011/12 season?","Manuel Pellegrini","Carlo Ancelotti","Arsene Wenger","Roberto Mancini","Jose Mourinho",3,0,0);
        ukQuestions[15] = new ItemObject("Which footballer has the most premier league appearances?","Frank Lampard","Gareth Barry","Ryan Giggs","Emile Heskey","Eltee Taiwo",2,0,0);
        ukQuestions[16] = new ItemObject("Which British player was the first to sell for more than £3 million?","Kelvin Agara","Ian Wright","Alan Shearer","Paul Ince","Martin Keown",2,0,0);
        ukQuestions[17] = new ItemObject("How many goals did Frank Lampard score in the premier league?","185","175","179","177","181",3,0,0);
        ukQuestions[18] = new ItemObject("Who is the 5th highest goal scorer in the premier leagues history?","Andrew Cole","Frank Lampard","Jermain Defoe","Thierry Henry","Robbie Fowler",3,0,0);
        ukQuestions[19] = new ItemObject("Which of these teams is not a part of Greater London?","Chelsea","West Ham United","Fulham","Crystal Palace","Tottenham Hotspur",2,0,0);
        ukQuestions[20] = new ItemObject("What team won the premier league in the 2005/06 season?","Manchester united","Arsenal","Chelsea","Manchester City","Liverpool",2,0,0);
        ukQuestions[21] = new ItemObject("In what season did Chelsea finish with 95 points?","2004/05","2010/11","2002/03","2001/02","2007/08",0,0,0);
        ukQuestions[22] = new ItemObject("What team finished with the fewest points in premier league history?","Aston Villa","Sunderland","Fulham","Derby County","Watford",3,0,0);
        ukQuestions[23] = new ItemObject("What team got the most points in a season without winning the league?","Chelsea","Manchester City","Manchester United","Arsenal","Liverpool",2,0,0);
        ukQuestions[24] = new ItemObject("What is the fewest points a team got in a premier league season while still winning the title?","69","71","72","75","79",3,0,0);
        ukQuestions[25] = new ItemObject("What team has the most wins in a single premier league season?","Chelsea (30 wins)","Manchester United (31 wins)","Leicester City (29 wins)","Manchester City (31 wins)","Arsenal(33 wins)",0,0,0);
        ukQuestions[26] = new ItemObject("How many teams have the joint record of 1 away win in a premier league season?","0","1","3","5","6",4,0,0);
        ukQuestions[27] = new ItemObject("In what season did Arsenal go unbeaten in the premier league?","2001/02","2002/03","2003/04","2005/06","2006/07",2,0,0);
        ukQuestions[28] = new ItemObject("What team has the most consecutive losses over more than one premier league season?","Sunderland","Watford","Blackpool","Burnley","Fulham",0,0,0);
        ukQuestions[29] = new ItemObject("What premier league game had the highest attendance in premier league history?","Manchester United vs Arsenal","Arsenal vs Chelsea","Arsenal vs Newcastle","Manchester United vs Blackburn Rovers","Tottenham vs Liverpool",3,0,0);



        if(state < ukQuestions.length) {
            getUnanswered();
        }

        MobileAds.initialize(this, ADMOB_APP_ID);

        adView = (AdView) findViewById(R.id.adViewUK);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener()
        {
            public void onAdLoaded(){
                adView.bringToFront();
            }
        });
    }

    private void initialRun() {
        //sharedpreference for setting questions and answers only once ever
        Log.d("LEVELCHECK", "first UK try 1");
        boolean runFirstUK = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("runFirstUK", true);

        if(runFirstUK){
            Log.d("LEVELCHECK", "first UK try 2");
            helper.ChangeUKTries(1);
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("runFirstUK", false)
                    .commit();
        }
    }

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem settingsItem = menu.findItem(R.id.level);
        settingsItem.setIcon(R.drawable.ic_level_1);
        return super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.level, menu);

        level = checkLevel();

        switch (level){
            case 2:
                menu.getItem(0).setIcon(R.drawable.ic_level_2);
                break;

            case 3:
                menu.getItem(0).setIcon(R.drawable.ic_level_2);
                break;

            case 4:
                menu.getItem(0).setIcon(R.drawable.ic_level_3);
                break;

            case 5:
                menu.getItem(0).setIcon(R.drawable.ic_level_3);
                break;

            case 6:
                menu.getItem(0).setIcon(R.drawable.ic_level_4);
                break;

            case 7:
                menu.getItem(0).setIcon(R.drawable.ic_level_4);
                break;

            case 8:
                menu.getItem(0).setIcon(R.drawable.ic_level_5);
                break;

            case 9:
                menu.getItem(0).setIcon(R.drawable.ic_level_5);
                break;

            case 10:
                menu.getItem(0).setIcon(R.drawable.ic_level_5);
                break;

            case 11:
                menu.getItem(0).setIcon(R.drawable.ic_level_6);
                break;

            case 12:
                menu.getItem(0).setIcon(R.drawable.ic_level_6);
                break;

            case 13:
                menu.getItem(0).setIcon(R.drawable.ic_level_6);
                break;

            case 14:
                menu.getItem(0).setIcon(R.drawable.ic_level_7);
                break;

            case 15:
                menu.getItem(0).setIcon(R.drawable.ic_level_7);
                break;

            case 16:
                menu.getItem(0).setIcon(R.drawable.ic_level_7);
                break;

            case 17:
                menu.getItem(0).setIcon(R.drawable.ic_level_8);
                break;

            case 18:
                menu.getItem(0).setIcon(R.drawable.ic_level_8);
                break;

            case 19:
                menu.getItem(0).setIcon(R.drawable.ic_level_8);
                break;

            case 20:
                menu.getItem(0).setIcon(R.drawable.ic_level_9);
                break;

            case 21:
                menu.getItem(0).setIcon(R.drawable.ic_level_9);
                break;

            case 22:
                menu.getItem(0).setIcon(R.drawable.ic_level_9);
                break;

            case 23:
                menu.getItem(0).setIcon(R.drawable.ic_level_9);
                break;

            case 24:
                menu.getItem(0).setIcon(R.drawable.ic_level_9);
                break;

            case 25:
                menu.getItem(0).setIcon(R.drawable.ic_level_9);
                break;

            case 26:
                menu.getItem(0).setIcon(R.drawable.ic_level_10_dark);
                break;

            case 27:
                menu.getItem(0).setIcon(R.drawable.ic_level_10_dark);
                break;

            case 28:
                menu.getItem(0).setIcon(R.drawable.ic_level_10_dark);
                break;

            case 29:
                menu.getItem(0).setIcon(R.drawable.ic_level_10_dark);
                break;

            case 30:
                menu.getItem(0).setIcon(R.drawable.ic_level_10_dark);
                break;
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(UnitedKingdom.this, MainActivity.class);
            startActivity(intent);
            //overridePendingTransition(0,0);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void askQuestions(int quesNum) {
        currQuest = quesNum;

        quesState = getUKWrongAns(currQuest);
        tries = getTries();

        if(quesState == 0 && tries > 1){
            cross.setVisibility(View.VISIBLE);
        }

        else if(quesState == 1 && tries > 1){
            check.setVisibility(View.VISIBLE);
        }

        bubblePicker = (BubblePicker) findViewById(R.id.UKpicker);
        ArrayList<PickerItem> listItems = new ArrayList<>();
        quest.setText(ukQuestions[quesNum].getQuestions());
        answers[0] = ukQuestions[quesNum].get_firstAns();
        answers[1] = ukQuestions[quesNum].get_secondAns();
        answers[2] = ukQuestions[quesNum].get_thirdAns();
        answers[3] = ukQuestions[quesNum].get_fourthAns();
        answers[4] = ukQuestions[quesNum].get_fifthAns();
        correctAns = ukQuestions[quesNum].get_answer();

        for(int i = 0; i < answers.length; i++){
            PickerItem item = new PickerItem(answers[i],colors[i],Color.WHITE,getDrawable(ball));
            if(i == correctAns){
                item = new PickerItem(answers[i],colors[i],Color.WHITE,getDrawable(images[quesNum]));
            }
            listItems.add(item);
        }

        bubblePicker.setItems(listItems);
        bubblePicker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem pickerItem) {
                //Toast.makeText(ChampionsLeague.this, "" + pickerItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
                selected++;
                if(pickerItem.getTitle().equals(answers[correctAns])){//
                    if(muteStatus == 0 && selected == 0) {
                        correct.start();
                    }
                    //Toast.makeText(ChampionsLeague.this, "correct Answer", Toast.LENGTH_SHORT).show();
                    if (selected == 0) {
                        helper.UKInformation(1, 1);

                        tries = getTries();
                        quesState = getUKWrongAns(currQuest);
                    }

                    Log.d("CHECKQUESTSTATE", "questState: " + quesState);

                    if(tries == 1 && selected == 0){
                        Log.d("LEVELCHECK", "enters 1");
                        helper.changeUKLevel(1);//for changing level in menu
                    }

                    else if(tries > 1 && quesState == 0 && selected == 0){
                        //helper.CLInformation(1,1);
                        Log.d("LEVELCHECK", "enters 2");
                        helper.updateUKLevel(1, wrongAnsPos);
                    }
                }

                else {
                        if(selected == 0){

                    if (muteStatus == 0) {
                        wrong.start();
                    }
                    //Toast.makeText(ChampionsLeague.this, "wrong Answer", Toast.LENGTH_SHORT).show();
                    helper.UKInformation(1, 0);
                    tries = getTries();
                    quesState = getUKWrongAns(currQuest);

                    if (tries == 1) {
                        helper.changeUKLevel(0);
                    }

                    /*else if(tries > 1 && quesState == 0){

                    }*/
                    }
                }

                if((currQuest + 1) < ukQuestions.length && selected == 0) {
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(UnitedKingdom.this, UKRefresh.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            finish();
                        }
                    }, 1000);//1 second delay
                }

                else {
                    if(selected == 0){
                    level = checkLevel();
                    mhandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(UnitedKingdom.this);
                            //builder.setTitle("Congratulations");
                            //builder.setMessage("All questions answered");

                            View view = LayoutInflater.from(UnitedKingdom.this).inflate(R.layout.alert_dialog, null);

                            TextView title = (TextView) view.findViewById(R.id.dialog_title);

                            ImageView imageView = (ImageView) view.findViewById(R.id.dialog_image);

                            title.setText("Congratulations");

                            if (level <= 10) {
                                imageView.setImageResource(R.drawable.bronze_trophy);
                            } else if (level > 10 && level <= 25) {
                                imageView.setImageResource(R.drawable.silver_trophy);
                            } else if (level > 25) {
                                imageView.setImageResource(R.drawable.gold_trophy);
                            }


                            builder.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (muteStatus == 0) {
                                        next.start();
                                    }
                                    applause.stop();
                                    Intent intent = new Intent(UnitedKingdom.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

            /*builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    // You don't have to do anything here if you just want it dismissed when clicked
                }

            });*/
                            if (muteStatus == 0) {
                                applause.start();
                                }
                            builder.setCancelable(false);
                            builder.setView(view);
                            builder.show();
                            }
                        }, 1000);//1 second delay

                    }

                }

            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem pickerItem) {
                //Toast.makeText(ChampionsLeague.this, ""+pickerItem.getTitle() + " deselected", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private int getTries() {
        int tries = 1;
        DBHelper helper = new DBHelper(UnitedKingdom.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getUKTries(db);

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            unAnswered = cursor.getString(cursor.getColumnIndex(helper.COL_UNITED_TRIES));
            tries = Integer.parseInt(id);

            //Log.d("UNANSWERED", "current position: " + id);
            Log.d("LEVELCHECK", "current try: " + id);
        }

        return tries;

    }

    private int state() {
        DBHelper helper = new DBHelper(UnitedKingdom.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getUKUnanswered(db);

        while (cursor.moveToNext() && index <= ukQuestions.length) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            unAnswered = cursor.getString(cursor.getColumnIndex(helper.COL_IF_ANSWERED));
            index = Integer.parseInt(id);

            Log.d("UNANSWERED", "current position: " + id);

        }
        return index;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UnitedKingdom.this, MainActivity.class);
        startActivity(intent);
        //overridePendingTransition(0,0);
        finish();
    }

    private int checkLevel() {
        DBHelper helper = new DBHelper(UnitedKingdom.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getUKLevel(db);
        String level;
        int ansCorr = 0;

        while (cursor.moveToNext() && index <= ukQuestions.length) {
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



    private void getUnanswered() {
        DBHelper helper = new DBHelper(UnitedKingdom.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getUKUnanswered(db);

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            unAnswered = cursor.getString(cursor.getColumnIndex(helper.COL_IF_ANSWERED));
            index = Integer.parseInt(id);

            Log.d("UNANSWERED", "current position: " + id);


        }
        if(index < ukQuestions.length) {
            Log.d("INDEXED", "index is " + index);
            askQuestions(index);
        }

    }

    //getCLWrongAns(SQLiteDatabase db, String id)

    private int getUKWrongAns(int state) {
        DBHelper helper = new DBHelper(UnitedKingdom.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getUKLevel(db);
        String ansState;
        int questState = 0;

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            ansState = cursor.getString(cursor.getColumnIndex(helper.COL_UNITED_LEVEL));
            index = Integer.parseInt(id);

            if(index == (state + 1)){
                Log.d("CHECKIFANS", "ansState: " + ansState);
                wrongAnsPos = id;
                questState = Integer.parseInt(ansState);
                Log.d("CHECKIFANS", "enters id: " + id);
                Log.d("CHECKIFANS", "enters quesNum: " + (state + 1));

            }
            //Log.d("UNANSWERED", "current position: " + id);
        }
        return questState;
    }

    private int muteState() {
        DBHelper helper = new DBHelper(UnitedKingdom.this);
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

}