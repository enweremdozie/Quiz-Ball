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

public class ChampionsLeague extends AppCompatActivity {
    DBHelper helper = new DBHelper(ChampionsLeague.this);
    BubblePicker bubblePicker;
    String[] answers = {"","","","",""};
    String id = "0";
    String unAnswered = "0";
    int index = 0;
    ItemObject clQuestions[] = new ItemObject[15];
    int correctAns = 0;
    int currQuest = 0;
    TextView quest;
    int state = 0;
    int level = 0;
    int tries = 1;
    String wrongAnsPos = "";
    int quesState = 0;
    Menu item;
    boolean foundUnAns = false;
    private Handler mhandler = new Handler();
    int[] images = {R.drawable.real_madrid, R.drawable.netherlands, R.drawable.madrid, R.drawable.benzema, R.drawable.iker_casillas, R.drawable.bafetimbi, R.drawable.luiz_adriano, R.drawable.ancelotti, R.drawable.bob_paisley, R.drawable.ernst_happel, R.drawable.ancelotti, R.drawable.italy, R.drawable.champions_league, R.drawable.real_madrid, R.drawable.ic_arsenal};
    int ball = R.drawable.ic_football;
    MediaPlayer correct;
    MediaPlayer wrong;
    MediaPlayer applause;
    MediaPlayer next;
    int selected = -1;
    int muteStatus = 0;
    ImageView check;
    ImageView cross;
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
        setContentView(R.layout.activity_champions_league);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Intent intent = getIntent();
        muteStatus = muteState();        //muteStatus = Integer.parseInt(muted);

        setTitle("");

        initialRun();

        check = (ImageView) findViewById(R.id.cl_check);
        cross = (ImageView) findViewById(R.id.cl_cross);

        applause = MediaPlayer.create(this, R.raw.applause);
        correct = MediaPlayer.create(this, R.raw.wet);
        wrong = MediaPlayer.create(this, R.raw.nope);
        next = MediaPlayer.create(this, R.raw.yes);

        //Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_level_1);

        state = state();
        quest = (TextView) findViewById(R.id.CLText);
        clQuestions[0] = new ItemObject("Which team has won the most Champions league trophies?","AC Milan","Barcelona","Real Madrid","Bayern Munich","Liverpool",2,0,0);
        clQuestions[1] = new ItemObject("Which nation has the 5th most Champions League trophies?","Spain","Italy","England","Germany","Netherlands",4,0,0);
        clQuestions[2] = new ItemObject("Which city has the most Champions League trophies?","Barcelona","Munich","Milan","Madrid","Liverpool",3,0,0);
        clQuestions[3] = new ItemObject("Who is the Champions League 5th all time goal scorer?","Christiano Ronaldo","Lionel Messi","Raul","Ruud van Nistelrooy","Karim Benzema",4,0,0);
        clQuestions[4] = new ItemObject("Which footballer has made the most appearances in the history of the Champions League?","Xavi","Raul","Ryan Giggs","Iker Casillas","Christiano Ronaldo",3,0,0);
        clQuestions[5] = new ItemObject("Which footballer scored the quickest Champions league hat-trick?","Christiano Ronaldo","Bafetimbi Gomis","Lionel Messi","Luiz Adriano","Mario Gomez",1,0,0);
        clQuestions[6] = new ItemObject("Which footballer was the first to score back to back hat-tricks in the Champions League?","Christiano Ronaldo","Bafetimbi Gomis","Lionel Messi","Luiz Adriano","Mario Gomez",3,0,0);
        clQuestions[7] = new ItemObject("Which manager was the first to get to the Champions League final 4 times?","Pep Guardiola","Jose Mourinho","Carlo Ancelotti","Bob Paisley","Arsene Wenger",2,0,0);
        clQuestions[8] = new ItemObject("Which Manager has won the Champions league three times with the same club?","Pep Guardiola","Jose Mourinho","Carlo Ancelotti","Bob Paisley","Tony Pulis",3,0,0);
        clQuestions[9] = new ItemObject("Which manager was the first manager to win the Champions League with two different teams?","Ernst Happel","Jose Mourinho","Pep Guardiola","Bob Paisley","Jupp Heynckes",0,0,0);
        clQuestions[10] = new ItemObject("Which manager has won the Champions League the most?","Jose Mourinho","Pep Guardiola","Alex Ferguson","Carlo Ancelotti","Jose Villalonga",3,0,0);
        clQuestions[11] = new ItemObject("Based on nationality what country has had the most managers win the Champions League?","Italy","Spain","England","Germany","Netherlands",0,0,0);
        clQuestions[12] = new ItemObject("How many clubs have reached the final of the Champions league without ever winning it?","10","13","17","19","20",2,0,0);
        clQuestions[13] = new ItemObject("What team has the all time most points in the Champions league history?","Real Madrid","Bayern Munich","Barcelona","Manchester United","AC Milan",0,0,0);
        clQuestions[14] = new ItemObject("How Many times has Arsenal won the Champions League?","1 time","2 times","3 times","5 times","NEVER",4,0,0);




        if(state < clQuestions.length) {
            getUnanswered();
        }

        MobileAds.initialize(this, ADMOB_APP_ID);

        adView = (AdView) findViewById(R.id.adViewCL);
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
        Log.d("LEVELCHECK", "first CL try 1");
        boolean runFirst = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("runFirst", true);

        if(runFirst){
            Log.d("LEVELCHECK", "first CL try 2");
            helper.ChangeCLTries(1);
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("runFirst", false)
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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(ChampionsLeague.this, MainActivity.class);
            startActivity(intent);
            //overridePendingTransition(0,0);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void askQuestions(int quesNum) {
        currQuest = quesNum;
        quesState = getCLWrongAns(currQuest);
        tries = getTries();

        if(quesState == 0 && tries > 1){
            cross.setVisibility(View.VISIBLE);
        }

        else if(quesState == 1 && tries > 1){
            check.setVisibility(View.VISIBLE);
        }

        bubblePicker = (BubblePicker) findViewById(R.id.CLpicker);
        ArrayList<PickerItem> listItems = new ArrayList<>();
        quest.setText(clQuestions[quesNum].getQuestions());
        answers[0] = clQuestions[quesNum].get_firstAns();
        answers[1] = clQuestions[quesNum].get_secondAns();
        answers[2] = clQuestions[quesNum].get_thirdAns();
        answers[3] = clQuestions[quesNum].get_fourthAns();
        answers[4] = clQuestions[quesNum].get_fifthAns();
        correctAns = clQuestions[quesNum].get_answer();
        //if (selected == 1) {

        for (int i = 0; i < answers.length; i++) {
            PickerItem item = new PickerItem(answers[i], colors[i], Color.WHITE, getDrawable(ball));
            if (i == correctAns) {
                item = new PickerItem(answers[i], colors[i], Color.WHITE, getDrawable(images[quesNum]));
            }
            listItems.add(item);
        }

        bubblePicker.setItems(listItems);
        bubblePicker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem pickerItem) {
                //Toast.makeText(ChampionsLeague.this, "" + pickerItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
                selected++;
                if (pickerItem.getTitle().equals(answers[correctAns])) {
                    if (muteStatus == 0 && selected == 0) {
                        correct.start();
                    }
                    //Toast.makeText(ChampionsLeague.this, "correct Answer", Toast.LENGTH_SHORT).show();
                    if (selected == 0) {
                        helper.CLInformation(1, 1);
                    }

                    Log.d("CHECKQUESTSTATE", "questState: " + quesState);

                    if (tries == 1 && selected == 0) {
                        Log.d("LEVELCHECK", "enters 1");

                        helper.changeCLLevel(1);//for changing level in menu
                    }

                    if (tries > 1 && quesState == 0 && selected == 0) {
                        //helper.CLInformation(1,1);
                        Log.d("LEVELCHECK", "enters 2");
                        helper.updateCLLevel(1, wrongAnsPos);
                    }
                }

                else {

                    if (selected == 0) {
                        if (muteStatus == 0) {
                            wrong.start();
                        }
                        //Toast.makeText(ChampionsLeague.this, "wrong Answer", Toast.LENGTH_SHORT).show();
                        helper.CLInformation(1, 0);
                        tries = getTries();
                        quesState = getCLWrongAns(currQuest);

                        if (tries == 1) {
                            helper.changeCLLevel(0);
                        }

                    /*else if(tries > 1 && quesState == 0){

                    }*/
                    }
                }

                if ((currQuest + 1) < clQuestions.length && selected == 0) {
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ChampionsLeague.this, CLRefresh.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(ChampionsLeague.this);
                            //builder.setTitle("Congratulations");
                            //builder.setMessage("All questions answered");

                            View view = LayoutInflater.from(ChampionsLeague.this).inflate(R.layout.alert_dialog, null);

                            TextView title = (TextView) view.findViewById(R.id.dialog_title);

                            ImageView imageView = (ImageView) view.findViewById(R.id.dialog_image);

                            title.setText("Congratulations");

                            if (level <= 5) {
                                imageView.setImageResource(R.drawable.bronze_trophy);
                            } else if (level > 5 && level <= 13) {
                                imageView.setImageResource(R.drawable.silver_trophy);
                            } else if (level > 13) {
                                imageView.setImageResource(R.drawable.gold_trophy);
                            }


                            builder.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (muteStatus == 0) {
                                        next.start();
                                    }
                                    applause.stop();
                                    Intent intent = new Intent(ChampionsLeague.this, MainActivity.class);
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
                //Toast.makeText(ChampionsLeague.this, "testing", Toast.LENGTH_SHORT).show();

            }

            //}
        });

   // }
         /*else if(selected > 1){
            //Toast.makeText(ChampionsLeague.this, "testing", Toast.LENGTH_SHORT).show();


        }*/

    }

    private int getTries() {
        int tries = 1;
        DBHelper helper = new DBHelper(ChampionsLeague.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getCLTries(db);

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            unAnswered = cursor.getString(cursor.getColumnIndex(helper.COL_CL_TRIES));
            tries = Integer.parseInt(id);

            //Log.d("UNANSWERED", "current position: " + id);
            Log.d("LEVELCHECK", "current try: " + id);
        }

        return tries;

    }

    private int state() {
        DBHelper helper = new DBHelper(ChampionsLeague.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getCLUnanswered(db);

        while (cursor.moveToNext() && index <= clQuestions.length) {
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
        Intent intent = new Intent(ChampionsLeague.this, MainActivity.class);
        startActivity(intent);
        //overridePendingTransition(0,0);
        finish();
    }

    private int checkLevel() {
        DBHelper helper = new DBHelper(ChampionsLeague.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getCLLevel(db);
        String level;
        int ansCorr = 0;

        while (cursor.moveToNext() && index <= clQuestions.length) {
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



    private void getUnanswered() {
        DBHelper helper = new DBHelper(ChampionsLeague.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getCLUnanswered(db);

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            unAnswered = cursor.getString(cursor.getColumnIndex(helper.COL_IF_ANSWERED));
            index = Integer.parseInt(id);

            Log.d("UNANSWERED", "current position: " + id);


        }
        if(index < clQuestions.length) {
            Log.d("INDEXED", "index is " + index);
            askQuestions(index);
        }

    }

    //getCLWrongAns(SQLiteDatabase db, String id)

    private int getCLWrongAns(int state) {
        DBHelper helper = new DBHelper(ChampionsLeague.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getCLLevel(db);
        String ansState;
        int questState = 0;

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            ansState = cursor.getString(cursor.getColumnIndex(helper.COL_CHAMPS_LEVEL));
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
        DBHelper helper = new DBHelper(ChampionsLeague.this);
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
