package inc.zachetka.klakson.Tools;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import inc.zachetka.klakson.AccountDirect.News;
import inc.zachetka.klakson.R;


public class TicketForm extends AppCompatActivity {
    int q = 2, w = 0, i3 = 1, examlength = 0, sizeOfFull = 0, lenght = 0;
    TextView text, result;
    ArrayList<Integer> num;
    ArrayList<String> nS;
    HashMap<Integer, String> URL, rightAn, tt, ans1, ans2, ans3, ans4, ans5, Description;
    Button anB1, anB2, anB3, anB4, anB5;
    AlertDialog.Builder builder;
    News news;
    ArrayList<Integer> WRONG, ALL, tickT, tickN, exsist;
    int all = 0;
    SQLiteDatabase sqLiteDatabase;
    ImageView image;
    Results results;
    String userId, addes = "";
    boolean isR = false, isExam = false, isFull = false;


    public TicketForm() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_ticket_form);
        result = findViewById(R.id.result);
        text = (TextView) findViewById(R.id.quest);
        Intent mIntent = getIntent();
        num = new ArrayList<>();
        userId = mIntent.getStringExtra("userId");
        ALL = new ArrayList<>();
        exsist = new ArrayList<>();
        WRONG = new ArrayList<>();
        news = new News();
        anB1 = (Button) findViewById(R.id.an1);
        anB2 = (Button) findViewById(R.id.an2);
        anB3 = (Button) findViewById(R.id.an3);
        anB4 = (Button) findViewById(R.id.an4);
        anB5 = (Button) findViewById(R.id.an5);
        tickN = new ArrayList<>();
        tickT = new ArrayList<>();
        builder = new AlertDialog.Builder(this);
        anB1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateTicket(0);
            }
        });
        anB2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateTicket(1);
            }
        });
        anB3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateTicket(2);
            }
        });
        anB4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateTicket(3);
            }
        });
        anB5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateTicket(4);
            }
        });

        image = (ImageView) findViewById(R.id.QImage);

        results = new Results();
        tt = new HashMap<Integer, String>();
        ans1 = new HashMap<Integer, String>();
        ans2 = new HashMap<Integer, String>();
        ans3 = new HashMap<Integer, String>();
        ans4 = new HashMap<Integer, String>();
        ans5 = new HashMap<Integer, String>();
        Description = new HashMap<>();
        rightAn = new HashMap<Integer, String>();
        URL = new HashMap<>();
        nS = new ArrayList<>();
//       DbHelper dbHelper = new DbHelper(getActivity().getApplicationContext(), loadVersion());
//       SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        i3 = 1;
        DbHelper dbHelper = new DbHelper(getApplicationContext(), loadVersion());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        if (userId.equals("EXAM_MODE")) {
            isExam = true;
        }
        if (isExam) {
            for (int i = 0; i < mIntent.getIntExtra("length", 0); i++)
                constructExam(mIntent, i);
            createExam();
        } else
            for (int i = 0; i < mIntent.getIntExtra("length", 0); i++)
                createTest(mIntent, i);


        text.setText("" + tt.get(1));
        anB1.setText("" + ans1.get(1));
        anB2.setText("" + ans2.get(1));
        anB3.setText("" + ans3.get(1));
        anB4.setText("" + ans4.get(1));
        anB5.setText("" + ans5.get(1));
        result.setText("1/" + sizeOfFull);
        if (ans4.get(1).equals("null")) {
            anB4.setVisibility(View.INVISIBLE);
        }
        if (ans3.get(1).equals("null")) {
            anB3.setVisibility(View.INVISIBLE);
        }
        if (ans5.get(1).equals("null")) {
            anB5.setVisibility(View.INVISIBLE);
        }
        String s = URL.get(1);

        if (URL.get(1).equals("null")) {
            image.setImageResource(R.drawable.logo);
            //          ViewGroup layout = (ViewGroup) image.getParent();
//            if (null != layout) //for safety only  as you are doing onClick
//                layout.removeView(image);


        } else {

            image.setImageBitmap(loadImageFromStorage(URL.get(1)));
        }

    }

    private void createTest(Intent mIntent, int i) {
        nS.add(mIntent.getStringExtra(String.valueOf(i)));
        num.add(mIntent.getIntExtra("N" + String.valueOf(i), 0));
        int c = 0;
        for (int i2 = 1; i2 <= getProfilesCount(sqLiteDatabase, "T" + num.get(i)); i2++) {
            tt.put(i3, splited("Question", getById(String.valueOf(i2), sqLiteDatabase, "T" + (num.get(i)), "Id", "Question")));

            ans1.put(i3, splited("An1", getById(String.valueOf(i2), sqLiteDatabase, "T" + (num.get(i)), "Id", "An1")));
            ans2.put(i3, splited("An2", getById(String.valueOf(i2), sqLiteDatabase, "T" + (num.get(i)), "Id", "An2")));
            ans3.put(i3, splited("An3", getById(String.valueOf(i2), sqLiteDatabase, "T" + (num.get(i)), "Id", "An3")));
            ans4.put(i3, splited("An4", getById(String.valueOf(i2), sqLiteDatabase, "T" + (num.get(i)), "Id", "An4")));
            ans5.put(i3, splited("An5", getById(String.valueOf(i2), sqLiteDatabase, "T" + (num.get(i)), "Id", "An5")));
            URL.put(i3, splited("ImageUrl", getById(String.valueOf(i2), sqLiteDatabase, "T" + (num.get(i)), "Id", "ImageUrl")));
            rightAn.put(i3, splited("right", getById(String.valueOf(i2), sqLiteDatabase, "T" + (num.get(i)), "Id", "right")));
            Description.put(i3, splited("Description", getById(String.valueOf(i2), sqLiteDatabase, "T" + (num.get(i)), "Id", "Description")));
            lenght++;
            c++;
            i3++;
            all++;
            WRONG.add(0);
            sizeOfFull++;
        }
        ALL.add(c);
        WRONG.add(0);
    }

    private void constructExam(Intent mIntent, int i) {
        num.add(mIntent.getIntExtra("N" + String.valueOf(i), 0));
        for (int i2 = 1; i2 <= getProfilesCount(sqLiteDatabase, "T" + num.get(i)); i2++) {
            tickT.add(num.get(i));
            tickN.add(i2);
        }
        System.out.println(getProfilesCount(sqLiteDatabase, "T" + num.get(i)));
        examlength+=getProfilesCount(sqLiteDatabase, "T" + num.get(i));
    }

    int ti = 0;
    int c = 0;

    private void createExam() {
        int l = 30;
        if (examlength < 30){
            l = examlength;
            if(examlength > 20)
            lenght = 20;
            else lenght = examlength;
        }else{
            lenght = 20;
            isFull = true;
        }
        sizeOfFull = lenght;
        for (int b = 0; b < l; b++) {
            while (exsist.contains(ti)) {
                Random rand = new Random();
                ti = rand.nextInt(l);
            }
            exsist.add(ti);

            tt.put(i3, splited("Question", getById(String.valueOf(tickN.get(ti)), sqLiteDatabase, "T" + tickT.get(ti), "Id", "Question")));
            ans1.put(i3, splited("An1", getById(String.valueOf(tickN.get(ti)), sqLiteDatabase, "T" + tickT.get(ti), "Id", "An1")));
            ans2.put(i3, splited("An2", getById(String.valueOf(tickN.get(ti)), sqLiteDatabase, "T" + tickT.get(ti), "Id", "An2")));
            ans3.put(i3, splited("An3", getById(String.valueOf(tickN.get(ti)), sqLiteDatabase, "T" + tickT.get(ti), "Id", "An3")));
            ans4.put(i3, splited("An4", getById(String.valueOf(tickN.get(ti)), sqLiteDatabase, "T" + tickT.get(ti), "Id", "An4")));
            ans5.put(i3, splited("An5", getById(String.valueOf(tickN.get(ti)), sqLiteDatabase, "T" + tickT.get(ti), "Id", "An5")));
            URL.put(i3, splited("ImageUrl", getById(String.valueOf(tickN.get(ti)), sqLiteDatabase, "T" + tickT.get(ti), "Id", "ImageUrl")));
            rightAn.put(i3, splited("right", getById(String.valueOf(tickN.get(ti)), sqLiteDatabase, "T" + tickT.get(ti), "Id", "right")));
            Description.put(i3, splited("Description", getById(String.valueOf(tickN.get(ti)), sqLiteDatabase, "T" + tickT.get(ti), "Id", "Description")));
            c++;
            i3++;
            all++;
            WRONG.add(0);

        }
        ALL.add(c);
        WRONG.add(0);
    }

    private String splited(String Column, String S) {
        String[] splitStr1 = S.split(Column + " = ");
        String[] finalStr = splitStr1[1].split(";");
        return finalStr[0];
    }

    public String getById(String id, SQLiteDatabase db, String table, String ColID, String Column) {
        String[] column = {Column};
        String where = ColID + " = ?";
        String[] whereArgs = {id};
        Cursor cursor = db.query(table, column, where, whereArgs, null, null, null);
        String obj = null;

        if (cursor.moveToFirst()) {

            do {
                obj = "";
                for (String cn : cursor.getColumnNames()) {
                    obj = obj.concat(cn + " = " + cursor.getString(cursor.getColumnIndex(cn)) + "; ");
                }
                Log.d("logs", obj);

                return obj;


            } while (cursor.moveToNext());
        } else {
            cursor.close();

            return null;
        }

    }

    public void updateTicket(int answerNum) {

        if (q - 1 <= lenght) {


            if (answerNum + 1 == Integer.parseInt(rightAn.get(q - 1))) {
                if (q > lenght) toResults();
                else {
                    if (answerNum == 0) anB1.setBackgroundColor(Color.GREEN);
                    if (answerNum == 1) anB2.setBackgroundColor(Color.GREEN);
                    if (answerNum == 2) anB3.setBackgroundColor(Color.GREEN);
                    if (answerNum == 3) anB4.setBackgroundColor(Color.GREEN);
                    if (answerNum == 4) anB5.setBackgroundColor(Color.GREEN);

                    text.setText("" + tt.get(q));

                    anB1.setText("" + ans1.get(q));
                    anB2.setText("" + ans2.get(q));

                    if (ans4.get(q).equals("")) {
                        anB4.setVisibility(View.INVISIBLE);

                    } else {

                        anB4.setVisibility(View.VISIBLE);

                        anB4.setText("" + ans4.get(q));
                        anB4.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                updateTicket(3);
                            }
                        });
                    }


                    if (ans3.get(q).equals("")) {
                        anB3.setVisibility(View.INVISIBLE);
                    } else {

                        anB3.setVisibility(View.VISIBLE);

                        anB3.setText("" + ans3.get(q));
                        anB3.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                updateTicket(2);
                            }
                        });
                    }

                    if (ans5.get(q).equals("")) {
                        anB5.setVisibility(View.INVISIBLE);

                    } else {

                        anB5.setVisibility(View.VISIBLE);

                        anB5.setText("" + ans5.get(q));
                        anB5.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                updateTicket(4);
                            }
                        });
                    }

                    System.out.println(URL.get(q));
                    if (URL.get(q).equals("null")) {
                        image.setImageResource(R.drawable.logo);

                        //         ViewGroup layout = (ViewGroup) image.getParent();
//                    if (null != layout) //for safety only  as you are doing onClick
//                        layout.removeView(image);

                    } else {
                        if (image != null) {
                            image.setImageBitmap(loadImageFromStorage(URL.get(q)));
                        }
//                    }else {
//                        image = (ImageView) findViewById(R.id.QImage);
//                    }
                    }

                    isR = false;
                    result.setText(q + "/" + sizeOfFull + addes);
                    q++;
                    anB1.setBackgroundColor(128);
                    anB2.setBackgroundColor(128);
                    anB3.setBackgroundColor(128);
                    anB4.setBackgroundColor(128);
                    anB5.setBackgroundColor(128);
                }
            } else {
                if (answerNum == 0) anB1.setBackgroundColor(Color.RED);
                if (answerNum == 1) anB2.setBackgroundColor(Color.RED);
                if (answerNum == 2) anB3.setBackgroundColor(Color.RED);
                if (answerNum == 3) anB4.setBackgroundColor(Color.RED);
                if (answerNum == 4) anB5.setBackgroundColor(Color.RED);
                if(isFull&&isExam&&lenght<30){
                    lenght+=5;
                    addes += " +5";
                    showDial(Description.get(q - 1), addes);
                }
                showDial(Description.get(q - 1), "");
                if (!isR) {
                    int c = 0;
                    int a = ALL.get(c);
                    for (int i = 0; i < q; i++)
                        if (i > a) {
                            c++;
                            while (ALL.get(c) == 0) c++;
                            a += ALL.get(c);
                        }
                    WRONG.set(ALL.get(c), WRONG.get(ALL.get(c)) + 1);
                    isR = true;
                }
            }
        }

    }

    private void toResults() {
        results.setALL(ALL, userId, nS, isExam, lenght);
        results.setWRONG(WRONG);
        this.setContentView(R.layout.fragment_null);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ftr = fragmentManager.beginTransaction();
        ftr.replace(R.id.container1, results, results.getTag());
        ftr.commit();
    }

    private Bitmap loadImageFromStorage(String dir) {

        try {
            File f = new File(dir);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void showDial(String message, String addes) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(TicketForm.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(TicketForm.this);
        }
        builder.setTitle("Неправильный ответ"+ addes)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public long getProfilesCount(SQLiteDatabase db, String tableName) {
        long cnt = DatabaseUtils.queryNumEntries(db, tableName);
        return cnt;
    }

    class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context, int v1) {
            super(context, "ma9Base", null, v1);


        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {


        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

    int loadVersion() {

        String savedText = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("VERSION8", "");

        if (savedText == "")
            return 0;
        else
            return Integer.parseInt(savedText);
    }
}
