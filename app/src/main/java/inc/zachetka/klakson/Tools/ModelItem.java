package inc.zachetka.klakson.Tools;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ArrayAdapter;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import inc.zachetka.klakson.AccountDirect.News;

/**
 * Created by Иван on 07.09.2017.
 */

public class ModelItem {

    private String lect;
    private String date;
    private String theme;
    Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;
    News n;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<String> num;
Context context;
  ArrayList<Integer> idN;



    public Context getContext() {
        return context;
    }

    public ArrayList<String> getNum() {
        return num;
    }

    public ArrayList<Integer> getIdN() {
        return idN;
    }

    public ModelItem(Context ctx, String Lect, String Date, String Theme, News n, ArrayList<String> number, SQLiteDatabase sqLiteDatabase, String userId, Activity activity, ArrayList<Integer> idN) {
        this.lect = Lect;
        this.date = Date;
        this.theme = Theme;
        this.n = n;
        this.num = number;
        this.sqLiteDatabase = sqLiteDatabase;
        this.context = ctx;
        this.userId = userId;
        this.activity = activity;
        this.idN = idN;
    }

    public News getN() {
        return n;
    }

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    public String getLect() {
        return lect;
    }

    public String getTheme() {
        return theme;
    }

    public String getDate() {
        return date;
    }
}

