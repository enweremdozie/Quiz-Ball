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

public class Spain extends AppCompatActivity {
    DBHelper helper = new DBHelper(Spain.this);
    BubblePicker bubblePicker;
    String[] answers = {"","","","",""};
    String id = "0";
    String unAnswered = "0";
    int index = 0;
    ItemObject espQuestions[] = new ItemObject[30];
    int correctAns = 0;
    int currQuest = 0;
    TextView quest;
    int state = 0;
    int level = 0;
    int tries = 1;
    String wrongAnsPos = "";
    int quesState = 0;
    private Handler mhandler = new Handler();
    int[] images = {R.drawable.real_madrid, R.drawable.bilbao, R.drawable.liga, R.drawable.real_madrid, R.drawable.barca, R.drawable.liga, R.drawable.bilbao, R.drawable.espanyol, R.drawable.palmas, R.drawable.bilbao, R.drawable.andoni, R.drawable.liga, R.drawable.messi, R.drawable.raul, R.drawable.laszlo, R.drawable.liga, R.drawable.ronaldo, R.drawable.bebeto, R.drawable.suarez, R.drawable.alves, R.drawable.claudio, R.drawable.liga, R.drawable.liga, R.drawable.raul, R.drawable.telmo, R.drawable.bilbao, R.drawable.liga, R.drawable.telmo, R.drawable.messi, R.drawable.telmo};
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
        setContentView(R.layout.activity_spain);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Intent intent = getIntent();
        muteStatus = muteState();        //muteStatus = Integer.parseInt(muted);


        setTitle("");

        initialRun();

        check = (ImageView) findViewById(R.id.esp_check);
        cross = (ImageView) findViewById(R.id.esp_cross);

        applause = MediaPlayer.create(this, R.raw.applause);
        correct = MediaPlayer.create(this, R.raw.wet);
        wrong = MediaPlayer.create(this, R.raw.nope);
        next = MediaPlayer.create(this, R.raw.yes);

        //Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_level_1);

        state = state();
        quest = (TextView) findViewById(R.id.ESPText);
        espQuestions[0] = new ItemObject("What team has the most consecutive league titles in La Liga?","Athletico Madrid","Barcelona","Real Madrid","Athletic Bilbao","Sevilla",2,0,0);
        espQuestions[1] = new ItemObject("What team has the most appearances in La Liga?","Athletic Bilbao","Athletico Madrid","Valencia","Sevilla","Athletic Club",0,0,0);
        espQuestions[2] = new ItemObject("What is the most wins gotten by a La Liga team in 1 season?","25","32","39","41","49",1,0,0);
        espQuestions[3] = new ItemObject("What team in La Liga has the most home wins in a single season?","Barcelona","Real Madrid","Athletico Madrid","Girona","Real Betis",1,0,0);
        espQuestions[4] = new ItemObject("In what years did Barcelona get a record 39 consecutive home wins?","1958-1960","1959-1961","1960-1962","1963-1965","2010-2012",0,0,0);
        espQuestions[5] = new ItemObject("How many teams in La Liga history have the fewest wins in a single season?","2","4","7","9","12",1,0,0);
        espQuestions[6] = new ItemObject("What team has the most draws in La Liga history?","Levante","Athletic Club","Real Betis","Espanyol","Athletic Bilbao",4,0,0);
        espQuestions[7] = new ItemObject("What team has the most losses in La Liga history?","Levante","Athletic Club","Real Betis","Espanyol","Athletic Bilbao",3,0,0);
        espQuestions[8] = new ItemObject("What team has the most consecutive losses in a single La Liga season?","Real Betis","Las Palmas","Levante","Athletic Bilbao","Espanyol",1,0,0);
        espQuestions[9] = new ItemObject("What team has the fewest losses in a single La Liga season?","Athletic Bilbao","Barcelona","Athletico Madrid","Athletic Club","Sevilla",0,0,0);
        espQuestions[10] = new ItemObject("Which player has made the most La Liga appearances?","Andoni Zubizarreta","Raul","Donato Gama da Silva","Xavi","Iker Casillas",0,0,0);
        espQuestions[11] = new ItemObject("What is the worst goal difference recorded by a team in a single La Liga season?","-75","-101","-89","-93","-57",3,0,0);
        espQuestions[12] = new ItemObject("Which player has the most goals in La Liga history?","Raul","Lionel Messi","Christiano Ronaldo","Telmo Zarra","Semai Abulu",1,0,0);
        espQuestions[13] = new ItemObject("Which player has scored against the most opponents in La Liga history?","Raul","Christiano Ronaldo","Hugo Sanchez","Cesar Rodriguez","Telmo Zarra",0,0,0);
        espQuestions[14] = new ItemObject("Which footballer scored the most goals in a single La Liga game?","Laszlo Kubala","Raul","Christiano Ronaldo","Uzo Ikem","Lionel Messi",0,0,0);
        espQuestions[15] = new ItemObject("What is the most consecutive league appearances scored in by a player in La Liga?","15","21","26","29","34",1,0,0);
        espQuestions[16] = new ItemObject("Which footballer in La Liga history has scored the most hat-tricks?","Raul","Lionel Messi","Ike Anyanwu","Christiano Ronaldo","Mariano Martin",3,0,0);
        espQuestions[17] = new ItemObject("Which footballer in La Liga history scored the fastest hattrick ever?","Mariano Martin","Bebeto","Raul","Christiano Ronaldo","Lionel Messi",1,0,0);
        espQuestions[18] = new ItemObject("Which footballer in La Liga history scored a super hat-trick(4 goals) in 2 consecutive matches?","Kola Dimeji","Lionel Messi","Luis Suarez","Christiano Ronaldo","Raul",2,0,0);
        espQuestions[19] = new ItemObject("Which goalkeeper in La Liga history has saved the most penalties?","Kameni","Diego Alves","Diego Lopez","Ter Stegen","Andoni Zubuzarreta",1,0,0);
        espQuestions[20] = new ItemObject("Which goalkeeper has the best unbeaten start in La Liga history?","Claudio Bravo","Thibaut Courtois","Victor Valdes","Iker Casillas","Fransisco Liano",0,0,0);
        espQuestions[21] = new ItemObject("What is the biggest recorded win in La Liga history?","Athletic Club 0-9 Real Madrid","Real Madrid 12-3 Levante","Athletic Bilbao 12-1 Barcelona","Real Sociedad 2-13 Athletico Madrid","Athletic Club 11-1 Athletico Madrid",2,0,0);
        espQuestions[22] = new ItemObject("What is the most goals scored by a team in a single La Liga season?","100","110","117","121","124",3,0,0);
        espQuestions[23] = new ItemObject("Who is the 5th highest goal scorer in La Liga history?","Raul","Alfredo Stefano","Hugo Sanchez","Cesar Rodriguez","Leslie Enwerem",0,0,0);
        espQuestions[24] = new ItemObject("Who is the Spain’s highest goal scorer in La Liga history?","Raul","Cesar Rodriguez","Mobi","Telmo Zarra","Pahino",3,0,0);
        espQuestions[25] = new ItemObject("What team has won the most consecutive matches in Copa del Rey history?","Barcelona","Athletic Bilbao","Athletic Club","Celta Vigo","Athletico Madrid",1,0,0);
        espQuestions[26] = new ItemObject("What is the biggest winning scoreline in Copa del Rey history?","CD Don Benito 0-13 Celta Vigo","Real Murcia 14-0 Cieza Promesas","Celta Vigo 22-0 Don Benito","Athletic Bilbao 15-1 Levante","Cordoba 15-1 Grenada",2,0,0);
        espQuestions[27] = new ItemObject("Who is the top scorer in Copa del Rey history?","Josep Samitier","Quini","Ferenc Puskas","Telmo Zarra","Laszlo Kubala",3,0,0);
        espQuestions[28] = new ItemObject("Which footballer has scored the most braces in La Liga history?","Lionel Messi","Christiano Ronaldo","Telmo Zarra","Aritz Aduriz","Samuel Eto",0,0,0);
        espQuestions[29] = new ItemObject("Which footballer has scored the most consecutive finals goals in Copa del Rey?","Telmo Zarra","Lionel Messi","Raul","Ronaldo Nazario","Christiano Ronaldo",0,0,0);



        if(state < espQuestions.length) {
            getUnanswered();
        }

        MobileAds.initialize(this, ADMOB_APP_ID);

        adView = (AdView) findViewById(R.id.adViewEsp);
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
        Log.d("LEVELCHECK", "first ESP try 1");
        boolean runFirstESP = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("runFirstESP", true);

        if(runFirstESP){
            Log.d("LEVELCHECK", "first ESP try 2");
            helper.ChangeESPTries(1);
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("runFirstESP", false)
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
            Intent intent = new Intent(Spain.this, MainActivity.class);
            startActivity(intent);
            //overridePendingTransition(0,0);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void askQuestions(int quesNum) {
        currQuest = quesNum;

        quesState = getESPWrongAns(currQuest);
        tries = getTries();

        if(quesState == 0 && tries > 1){
            cross.setVisibility(View.VISIBLE);
        }

        else if(quesState == 1 && tries > 1){
            check.setVisibility(View.VISIBLE);
        }

        bubblePicker = (BubblePicker) findViewById(R.id.ESPpicker);
        ArrayList<PickerItem> listItems = new ArrayList<>();
        quest.setText(espQuestions[quesNum].getQuestions());
        answers[0] = espQuestions[quesNum].get_firstAns();
        answers[1] = espQuestions[quesNum].get_secondAns();
        answers[2] = espQuestions[quesNum].get_thirdAns();
        answers[3] = espQuestions[quesNum].get_fourthAns();
        answers[4] = espQuestions[quesNum].get_fifthAns();
        correctAns = espQuestions[quesNum].get_answer();

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
                        helper.ESPInformation(1, 1);

                        tries = getTries();
                        quesState = getESPWrongAns(currQuest);
                        Log.d("CHECKQUESTSTATE", "questState: " + quesState);
                    }

                    if(tries == 1 && selected == 0){
                        Log.d("LEVELCHECK", "enters 1");
                        helper.changeESPLevel(1);//for changing level in menu
                    }

                    else if(tries > 1 && quesState == 0 && selected == 0){
                        //helper.CLInformation(1,1);
                        Log.d("LEVELCHECK", "enters 2");
                        helper.updateESPLevel(1, wrongAnsPos);
                    }
                }

                else {
                    if (selected == 0) {

                        if (muteStatus == 0) {
                            wrong.start();
                        }
                        //Toast.makeText(ChampionsLeague.this, "wrong Answer", Toast.LENGTH_SHORT).show();
                        helper.ESPInformation(1, 0);
                        tries = getTries();
                        quesState = getESPWrongAns(currQuest);

                        if (tries == 1) {
                            helper.changeESPLevel(0);
                        }

                    /*else if(tries > 1 && quesState == 0){

                    }*/
                    }
                }

                if((currQuest + 1) < espQuestions.length && selected == 0) {
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Spain.this, ESPRefresh.class);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(Spain.this);
                            //builder.setTitle("Congratulations");
                            //builder.setMessage("All questions answered");

                            View view = LayoutInflater.from(Spain.this).inflate(R.layout.alert_dialog, null);

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
                                    Intent intent = new Intent(Spain.this, MainActivity.class);
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
        DBHelper helper = new DBHelper(Spain.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getESPTries(db);

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            unAnswered = cursor.getString(cursor.getColumnIndex(helper.COL_ESP_TRIES));
            tries = Integer.parseInt(id);

            //Log.d("UNANSWERED", "current position: " + id);
            Log.d("LEVELCHECK", "current try: " + id);
        }

        return tries;

    }

    private int state() {
        DBHelper helper = new DBHelper(Spain.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getESPUnanswered(db);

        while (cursor.moveToNext() && index <= espQuestions.length) {
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
        Intent intent = new Intent(Spain.this, MainActivity.class);
        startActivity(intent);
        //overridePendingTransition(0,0);
        finish();
    }

    private int checkLevel() {
        DBHelper helper = new DBHelper(Spain.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getESPLevel(db);
        String level;
        int ansCorr = 0;

        while (cursor.moveToNext() && index <= espQuestions.length) {
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



    private void getUnanswered() {
        DBHelper helper = new DBHelper(Spain.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getESPUnanswered(db);

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            unAnswered = cursor.getString(cursor.getColumnIndex(helper.COL_IF_ANSWERED));
            index = Integer.parseInt(id);

            Log.d("UNANSWERED", "current position: " + id);


        }
        if(index < espQuestions.length) {
            Log.d("INDEXED", "index is " + index);
            askQuestions(index);
        }

    }

    //getCLWrongAns(SQLiteDatabase db, String id)

    private int getESPWrongAns(int state) {
        DBHelper helper = new DBHelper(Spain.this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = helper.getESPLevel(db);
        String ansState;
        int questState = 0;

        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(helper.COL_ID));
            ansState = cursor.getString(cursor.getColumnIndex(helper.COL_ESP_LEVEL));
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
        DBHelper helper = new DBHelper(Spain.this);
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
