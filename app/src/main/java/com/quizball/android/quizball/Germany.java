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

public class Germany extends AppCompatActivity {
    DBHelper helper = new DBHelper(Germany.this);
    BubblePicker bubblePicker;
    String[] answers = {"","","","",""};
    String id = "0";
    String unAnswered = "0";
    int index = 0;
    ItemObject gerQuestions[] = new ItemObject[30];
    int correctAns = 0;
    int currQuest = 0;
    TextView quest;
    int state = 0;
    int level = 0;
    int tries = 1;
    String wrongAnsPos = "";
    int quesState = 0;
    private Handler mhandler = new Handler();
    int[] images = {R.drawable.bayern, R.drawable.bundes, R.drawable.bayern, R.drawable.bundes, R.drawable.bundes, R.drawable.wolfsburg, R.drawable.bundes, R.drawable.tasmania, R.drawable.bundes, R.drawable.bayern, R.drawable.werder, R.drawable.leipzig, R.drawable.bundes, R.drawable.bundes, R.drawable.matthias, R.drawable.oliver, R.drawable.bernard, R.drawable.bundes, R.drawable.bundes, R.drawable.hans, R.drawable.kevin, R.drawable.bundes, R.drawable.boateng, R.drawable.mario, R.drawable.kevin, R.drawable.bundes, R.drawable.schalke, R.drawable.pizzaro, R.drawable.moritz, R.drawable.bundes};
    int ball = R.drawable.ic_football;
    MediaPlayer correct;
    MediaPlayer wrong;
    MediaPlayer applause;
    MediaPlayer next;
    int muteStatus = 0;
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
        setContentView(R.layout.activity_germany);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        //Intent intent = getIntent();
        muteStatus = muteState();        //muteStatus = Integer.parseInt(muted);

        setTitle("");

        initialRun();

        check = (ImageView) findViewById(R.id.ger_check);
        cross = (ImageView) findViewById(R.id.ger_cross);

        applause = MediaPlayer.create(this, R.raw.applause);
        correct = MediaPlayer.create(this, R.raw.wet);
        wrong = MediaPlayer.create(this, R.raw.nope);
        next = MediaPlayer.create(this, R.raw.yes);

        //Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_level_1);

        state = state();
        quest = (TextView) findViewById(R.id.GERText);
        gerQuestions[0] = new ItemObject("How many titles has Bayern Munich won in the Bundesliga?","19","22","24","26","29",3,0,0);
        gerQuestions[1] = new ItemObject("What is the highest number of games left after becoming Bundesliga champions?","1","2","5","7","10",3,0,0);
        gerQuestions[2] = new ItemObject("When did Bayern Munich win their first Bundesliga title?","1967/68","1968/69","1969/70","1970/71","1971/72",1,0,0);
        gerQuestions[3] = new ItemObject("What is the lowest number of seasons before getting relegated by a Bundesliga champion?","1 season","3 seasons","5 seasons","6 season","8 seasons",0,0,0);
        gerQuestions[4] = new ItemObject("What is the highest number of points recorded in a single Bundesliga season?","83","86","91","95","98",2,0,0);
        gerQuestions[5] = new ItemObject("What team in the Bundesliga in a single season has the highest number of points at home?","Bayern Leverkusen","VFL Wolfsburg","Borussia Dortmund","Hoffenheim","Hamburger SV",1,0,0);
        gerQuestions[6] = new ItemObject("What is the biggest margin of points between champions and runner-up in the Bundesliga?","12","15","18","21","25",4,0,0);
        gerQuestions[7] = new ItemObject("What team has the lowest recorded number of points in a single Bundesliga season?","Fortuna Dusseldorf","Tasmania Berlin","Nurnberg","Duisburg","Dynamo Dresden",1,0,0);
        gerQuestions[8] = new ItemObject("What is the lowest number of wins in a single Bundeliga season at home?","0","2","4","5","6",0,0,0);
        gerQuestions[9] = new ItemObject("In what year did Bayern Munich score a record 101 goals in the Bundesliga?","1969/70","1970/71","1971/72","1972/73","1973/74",2,0,0);
        gerQuestions[10] = new ItemObject("What team has the lowest number of conceded goals at home in a single Bundesliga season?","Bayern Munich","Borussia Dortmund","Borussia Monchengladbach","Hertha Berlin","Werder Bremen",4,0,0);
        gerQuestions[11] = new ItemObject("What team has the highest number of consecutive wins in a single debut season in the Bundesliga?","Bayern Munich","Augsburg","RB Leipzig","FC Koln","Mainz 05",2,0,0);
        gerQuestions[12] = new ItemObject("What is the highest number of Bundesliga championships won by a player?","3","5","8","10","15",2,0,0);
        gerQuestions[13] = new ItemObject("What is the highest number of Bundesliga championships won by a manager?","3","5","8","10","15",2,0,0);
        gerQuestions[14] = new ItemObject("Which manager won the Bundesliga at a record youngest age of 34 years?","Matthias Sammer","Jupp Heynckes","Udo Lattek","Mehmet Scholl","Michael Okpuno",0,0,0);
        gerQuestions[15] = new ItemObject("Which player has the highest number of wins in the Bundesliga’s history?","Bastian Schweinsteiger","Oliver Khan","Bernard Dietz","Gerd Muller","Thomas Muller",1,0,0);
        gerQuestions[16] = new ItemObject("Which player has the lowest number of wins in the Bundesliga’s history?","Bernard Dietz","Gerd Muller","Dieter Muller","Timo Konietzka","Milos Jojic",0,0,0);
        gerQuestions[17] = new ItemObject("What is the youngest age for a player who scored in the Bundesliga?","14","17","20","23","26",1,0,0);
        gerQuestions[18] = new ItemObject("What is the oldest age for a player who scored in the Bundesliga?","29","32","34","37","40",4,0,0);
        gerQuestions[19] = new ItemObject("Which keeper in the Bundesliga’s history has scored the most goals?","Jens Lehmann","Frank Rost","Hans-Jorg Butt","Marwin Hitz","Manuel Neuer",2,0,0);
        gerQuestions[20] = new ItemObject("Which player has the most assists in a single Bundesliga season?","Henrikh Mkhitaryan","Mario Gotze","Kevin de bruyne","Thomas Muller","Manfred Kaltz",2,0,0);
        gerQuestions[21] = new ItemObject("What is the highest number of consecutive scored goals by penalties?","9","11","13","15","17",4,0,0);
        gerQuestions[22] = new ItemObject("Who is the player with the highest number of consecutive games unbeaten in the Bundesliga?","Manuel Neuer","Thomas Muller","Frank Ribery","Jerome Boateng","Philip Lahm",3,0,0);
        gerQuestions[23] = new ItemObject("Which player was sold for the highest transfer fee between 2 teams in the Bundesliga?","Mat Hummels","Mario Gotze","Manuel Neuer","Kevin De Bruyne","Leroy Sane",1,0,0);
        gerQuestions[24] = new ItemObject("Which player was sold for the highest transfer by a Bundesliga team to a club outside of the Bundesliga?","Granit Xhaka","Julian Draxler","Henrikh Mkhitaryan","Leroy Sane","Kevin De Bruyne",4,0,0);
        gerQuestions[25] = new ItemObject("What is the highest number of red cards in a single Bundesliga season?","87","92","95","98","105",3,0,0);
        gerQuestions[26] = new ItemObject("What team has the most runner up medals in the DFB-Pokal?","Bayern Munich","1. FC Koln","Borussia Dortmund","Schalke 04","Werder Bremen",3,0,0);
        gerQuestions[27] = new ItemObject("Who is the 5th top goal scorer in Bundesliga history?","Gerd Muller","Klaus Fischer","Jupp Heynckes","Manfred Burgsmuller","Claudio Pizarro",4,0,0);
        gerQuestions[28] = new ItemObject("Who scored a goal from the farthest distance in the Bundesliga?","Andre Schurrle","Julian Draxler","Mario Gotze","Henrikh Mkhitaryan","Moritz Stoppelkamp",4,0,0);
        gerQuestions[29] = new ItemObject("What is the highest number of scored goals by a goal keeper from open play?","1","3","5","7","9",0,0,0);



        if(state < gerQuestions.length) {
            getUnanswered();
        }

        MobileAds.initialize(this, ADMOB_APP_ID);

        adView = (AdView) findViewById(R.id.adViewGer);
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
        Log.d("LEVELCHECK", "first GER try 1");
        boolean runFirstGER = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("runFirstGER", true);

        if(runFirstGER){
            Log.d("LEVELCHECK", "first GER try 2");
            helper.ChangeGERTries(1);
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("runFirstGER", false)
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
            Intent intent = new Intent(Germany.this, MainActivity.class);
            startActivity(intent);
            //overridePendingTransition(0,0);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void askQuestions(int quesNum) {
        currQuest = quesNum;
        quesState = getGERWrongAns(currQuest);
        tries = getTries();

        if(quesState == 0 && tries > 1){
            cross.setVisibility(View.VISIBLE);
        }

        else if(quesState == 1 && tries > 1){
            check.setVisibility(View.VISIBLE);
        }
        bubblePicker = (BubblePicker) findViewById(R.id.GERpicker);
        ArrayList<PickerItem> listItems = new ArrayList<>();
        quest.setText(gerQuestions[quesNum].getQuestions());
        answers[0] = gerQuestions[quesNum].get_firstAns();
        answers[1] = gerQuestions[quesNum].get_secondAns();
        answers[2] = gerQuestions[quesNum].get_thirdAns();
        answers[3] = gerQuestions[quesNum].get_fourthAns();
        answers[4] = gerQuestions[quesNum].get_fifthAns();
        correctAns = gerQuestions[quesNum].get_answer();

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
                selected++;
                //Toast.makeText(ChampionsLeague.this, "" + pickerItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();

                if(pickerItem.getTitle().equals(answers[correctAns])){//
                    if(muteStatus == 0 && selected == 0) {
                        correct.start();
                    }
                    //Toast.makeText(ChampionsLeague.this, "correct Answer", Toast.LENGTH_SHORT).show();
                    if(selected == 0) {
                        helper.GERInformation(1, 1);

                        tries = getTries();
                        quesState = getGERWrongAns(currQuest);
                        Log.d("CHECKQUESTSTATE", "questState: " + quesState);
                    }
                    if(tries == 1 && selected == 0){
                        Log.d("LEVELCHECK", "enters 1");
                        helper.changeGERLevel(1);//for changing level in menu
                    }

                    else if(tries > 1 && quesState == 0 && selected == 0){
                        //helper.CLInformation(1,1);
                        Log.d("LEVELCHECK", "enters 2");
                        helper.updateGERLevel(1, wrongAnsPos);
                    }
                }

                else {
                    if (selected == 0) {
                        if (muteStatus == 0) {
                            wrong.start();
                        }
                        //Toast.makeText(ChampionsLeague.this, "wrong Answer", Toast.LENGTH_SHORT).show();
                        helper.GERInformation(1, 0);
                        tries = getTries();
                        quesState = getGERWrongAns(currQuest);

                        if (tries == 1) {
                            helper.changeGERLevel(0);
                        }

                    /*else if(tries > 1 && quesState == 0){

                    }*/
                    }
                }

                if((currQuest + 1) < gerQuestions.length && selected == 0) {
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Germany.this, GERRefresh.class);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(Germany.this);
                            //builder.setTitle("Congratulations");
                            //builder.setMessage("All questions answered");

                            View view = LayoutInflater.from(Germany.this).inflate(R.layout.alert_dialog, null);

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
                                    Intent intent = new Intent(Germany.this, MainActivity.class);
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
        DBHelper helper = new DBHelper(Germany.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getGERTries(db);

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            unAnswered = cursor.getString(cursor.getColumnIndex(helper.COL_GER_TRIES));
            tries = Integer.parseInt(id);

            //Log.d("UNANSWERED", "current position: " + id);
            Log.d("LEVELCHECK", "current try: " + id);
        }

        return tries;

    }

    private int state() {
        DBHelper helper = new DBHelper(Germany.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getGERUnanswered(db);

        while (cursor.moveToNext() && index <= gerQuestions.length) {
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
        Intent intent = new Intent(Germany.this, MainActivity.class);
        startActivity(intent);
        //overridePendingTransition(0,0);
        finish();
    }

    private int checkLevel() {
        DBHelper helper = new DBHelper(Germany.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getGERLevel(db);
        String level;
        int ansCorr = 0;

        while (cursor.moveToNext() && index <= gerQuestions.length) {
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



    private void getUnanswered() {
        DBHelper helper = new DBHelper(Germany.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getGERUnanswered(db);

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            unAnswered = cursor.getString(cursor.getColumnIndex(helper.COL_IF_ANSWERED));
            index = Integer.parseInt(id);

            Log.d("UNANSWERED", "current position: " + id);


        }
        if(index < gerQuestions.length) {
            Log.d("INDEXED", "index is " + index);
            askQuestions(index);
        }

    }

    //getCLWrongAns(SQLiteDatabase db, String id)

    private int getGERWrongAns(int state) {
        DBHelper helper = new DBHelper(Germany.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getGERLevel(db);
        String ansState;
        int questState = 0;

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            ansState = cursor.getString(cursor.getColumnIndex(helper.COL_GER_LEVEL));
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
        DBHelper helper = new DBHelper(Germany.this);
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

