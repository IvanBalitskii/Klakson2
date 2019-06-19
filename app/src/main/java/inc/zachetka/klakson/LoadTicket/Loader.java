package inc.zachetka.klakson.LoadTicket;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import inc.zachetka.klakson.AccountDirect.Account;
import inc.zachetka.klakson.AccountDirect.News;
import inc.zachetka.klakson.LoginActivity;
import inc.zachetka.klakson.R;


/**
 * Created by Иван on 25.10.2017.
 */

public class Loader extends AppCompatActivity {
    public Loader() {
    }

    final String LOG_TAG = "myLogs";

    int DATABASE_VERSION;
    long v2;
    ThemesInfo themesInfo;
    TicketsInfo ticketsInfo;
    String[] ID, Name, Theme;
    HashMap<String, String[]> An1, An2, An3, An4, An5, Description, Quest, URL, Right;
    public Context cc;
    News news;
    SharedPreferences sPref;
    private StorageReference mStorageRef;
    public String IMG_NAME, PATH;
    boolean isLoad = false;
    int i4 = 0, i5 = 0;
    public DbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loader);

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        themesInfo = new ThemesInfo();
        ticketsInfo = new TicketsInfo();
        cc = this;
        news = new News();
        An1 = new HashMap<>();
        An2 = new HashMap<>();
        An3 = new HashMap<>();
        An4 = new HashMap<>();
        An5 = new HashMap<>();
        Description = new HashMap<>();
        Quest = new HashMap<>();
        URL = new HashMap<>();
        Right = new HashMap<>();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        DATABASE_VERSION = loadVersion();

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                v2 = (long) dataSnapshot.child("DBversion").child("Version").getValue();
                Log.d("----LOADED-VERSION---", String.valueOf(v2));
                if (v2 > DATABASE_VERSION) {
                    showData(dataSnapshot, database);
                    DATABASE_VERSION = (int) v2;
                    dbHelper = new DbHelper(getApplicationContext());
                    SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();


                } else {
                    Toast.makeText(Loader.this, "У вас последняя версия", Toast.LENGTH_SHORT).show();

                    goAccount();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    void goAccount() {
        Intent intent = new Intent(Loader.this, Account.class);
        startActivity(intent);
        finish();

    }

    void logCursor(Cursor cursor) {
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : cursor.getColumnNames()) {
                        str = str.concat(cn + " = " + cursor.getString(cursor.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(LOG_TAG, str);
                } while (cursor.moveToNext());
            }
        } else Log.d(LOG_TAG, "Cursor is null");
    }

    private void showData(DataSnapshot dataSnapshot, DatabaseReference ref) {
        DataSnapshot dsU = dataSnapshot;
        System.out.println(dataSnapshot.child("Themes").getChildrenCount() + 1);
        for (int i = 0; i < dataSnapshot.child("Themes").getChildrenCount() + 1; i++) {
            ID = new String[i];
            Name = new String[i];
            Theme = new String[i];

        }
        int i = 0;
        for (DataSnapshot postSnapshot : dataSnapshot.child("Themes").getChildren()) {
            ID[i] = postSnapshot.getKey();
            Name[i] = (String) dataSnapshot.child("Themes/" + postSnapshot.getKey() + "/Name").getValue();

            Theme[i] = (String) dataSnapshot.child("Themes/" + postSnapshot.getKey() + "/Theme").getValue();

            System.out.println(ID[i]);
            i++;

            for (int a = 0; a < dataSnapshot.child("Themes/" + postSnapshot.getKey() + "/Tickets").getChildrenCount() + 1; a++) {

                An1.put(postSnapshot.getKey(), new String[a]);
                An2.put(postSnapshot.getKey(), new String[a]);
                An3.put(postSnapshot.getKey(), new String[a]);
                An4.put(postSnapshot.getKey(), new String[a]);
                An5.put(postSnapshot.getKey(), new String[a]);
                Quest.put(postSnapshot.getKey(), new String[a]);
                Right.put(postSnapshot.getKey(), new String[a]);
                Description.put(postSnapshot.getKey(), new String[a]);
                URL.put(postSnapshot.getKey(), new String[a]);
            }
            int i2 = 0;

            for (DataSnapshot s2 : dataSnapshot.child("Themes").child(postSnapshot.getKey()).child("Tickets").getChildren()) {
                String id = (String) dataSnapshot.child("Themes/" + postSnapshot.getKey() + "/Tickets/" + s2.getKey() + "/Id").getValue();


                String[] ar = An1.get(postSnapshot.getKey());
                ar[i2] = (String) dataSnapshot.child("Tickets/" + id + "/An1").getValue();
                An1.put(postSnapshot.getKey(), ar);

                ar = An2.get(postSnapshot.getKey());
                ar[i2] = (String) dataSnapshot.child("Tickets").child(id).child("An2").getValue();
                An2.put(postSnapshot.getKey(), ar);

                ar = An3.get(postSnapshot.getKey());
                ar[i2] = (String) dataSnapshot.child("Tickets").child(id).child("An3").getValue();
                An3.put(postSnapshot.getKey(), ar);

                ar = An4.get(postSnapshot.getKey());
                ar[i2] = (String) dataSnapshot.child("Tickets").child(id).child("An4").getValue();
                An4.put(postSnapshot.getKey(), ar);

                ar = An5.get(postSnapshot.getKey());
                ar[i2] = (String) dataSnapshot.child("Tickets").child(id).child("An5").getValue();
                An5.put(postSnapshot.getKey(), ar);

                ar = Quest.get(postSnapshot.getKey());
                ar[i2] = (String) dataSnapshot.child("Tickets").child(id).child("Question").getValue();
                Quest.put(postSnapshot.getKey(), ar);

                ar = URL.get(postSnapshot.getKey());
                ar[i2] = id;
                URL.put(postSnapshot.getKey(), ar);

                ar = Right.get(postSnapshot.getKey());
                ar[i2] = (String) dataSnapshot.child("Tickets").child(id).child("true").getValue();
                Right.put(postSnapshot.getKey(), ar);

                ar = Description.get(postSnapshot.getKey());
                ar[i2] = (String) dataSnapshot.child("Tickets").child(id).child("Description").getValue();
                Description.put(postSnapshot.getKey(), ar);

                i2++;

            }

        }

    }

    class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, "ma9Base", null, DATABASE_VERSION);


        }

        @Override
        public void onCreate(final SQLiteDatabase sqLiteDatabase) {

            ContentValues contentValues = new ContentValues();

            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Themes("
                    + "Id text, "
                    + "Name text, "
                    + "Theme text, "
                    + "Tickets int "
                    + ");");
            System.out.println("created");
            for (int i2 = 0; i2 < ID.length; i2++) {
                contentValues.clear();
                contentValues.put("Id", ID[i2]);
                contentValues.put("Name", Name[i2]);
                contentValues.put("Theme", Theme[i2]);
                contentValues.put("Tickets", i2);
                sqLiteDatabase.insert("Themes", null, contentValues);

                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + "T" + i2 + "("
                        + "Id integer primary key, "
                        + "An1 text, "
                        + "An2 text, "
                        + "An3 text, "
                        + "An4 text, "
                        + "An5 text, "
                        + "Question text, "
                        + "ImageUrl text, "
                        + "right int,"
                        + "Description text "
                        + ");");
                if (An1.get(ID[i2]) != null)
                    for (int i = 0; i < An1.get(ID[i2]).length; i++) {
                        StorageReference riversRef = mStorageRef.child(URL.get(ID[i2])[i]);
                        IMG_NAME = URL.get(ID[i2])[i];
                        final long ONE_MEGABYTE = 1024 * 1024;
                        i4++;

                        isLoad = true;
                        final int finalI = i2;
                        final int finalI1 = i;
                        if(riversRef!=null)isLoad = false;
                        riversRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(
                                new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        doInBackground(bytes);
                                        ContentValues contentValues = new ContentValues();


                                        contentValues.clear();
                                        Log.d(LOG_TAG, An1.get(ID[finalI])[finalI1]);

                                        contentValues.put("An1", An1.get(ID[finalI])[finalI1]);
                                        contentValues.put("An2", An2.get(ID[finalI])[finalI1]);
                                        contentValues.put("An3", An3.get(ID[finalI])[finalI1]);
                                        contentValues.put("An4", An4.get(ID[finalI])[finalI1]);
                                        contentValues.put("An5", An5.get(ID[finalI])[finalI1]);
                                        contentValues.put("Question", Quest.get(ID[finalI])[finalI1]);
                                        contentValues.put("ImageURL", PATH);
                                        contentValues.put("right", Right.get(ID[finalI])[finalI1]);
                                        contentValues.put("Description", Description.get(ID[finalI])[finalI1]);
                                        System.out.println(An1.get(ID[finalI])[finalI1]);
                                        sqLiteDatabase.insert("T" + finalI, null, contentValues);




                                        Cursor cursor;

                                        Log.d(LOG_TAG, "---Table Tickets---" + "T" + finalI);
                                        cursor = sqLiteDatabase.query("T" + finalI, null, null, null, null, null, null);
                                        logCursor(cursor);
                                        cursor.close();
                                        Log.d(LOG_TAG, "--- ---");
                                        Log.d(LOG_TAG, "---Table Themes---");
                                        cursor = sqLiteDatabase.query("Themes", null, null, null, null, null, null);
                                        logCursor(cursor);
                                        cursor.close();
                                        Log.d(LOG_TAG, "--- ---");
                                        isLoad = true;
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.d(LOG_TAG, exception.getMessage());
                                ContentValues contentValues = new ContentValues();


                                contentValues.clear();
                                Log.d(LOG_TAG, An1.get(ID[finalI])[finalI1]);

                                contentValues.put("An1", An1.get(ID[finalI])[finalI1]);
                                contentValues.put("An2", An2.get(ID[finalI])[finalI1]);
                                contentValues.put("An3", An3.get(ID[finalI])[finalI1]);
                                contentValues.put("An4", An4.get(ID[finalI])[finalI1]);
                                contentValues.put("An5", An5.get(ID[finalI])[finalI1]);
                                contentValues.put("Question", Quest.get(ID[finalI])[finalI1]);
                                contentValues.put("ImageURL", "null");
                                contentValues.put("right", Right.get(ID[finalI])[finalI1]);
                                contentValues.put("Description", Description.get(ID[finalI])[finalI1]);
                                System.out.println(An1.get(ID[finalI])[finalI1]);
                                sqLiteDatabase.insert("T" + finalI, null, contentValues);




                                Cursor cursor;

                                Log.d(LOG_TAG, "---Table Tickets---" + "T" + finalI);
                                cursor = sqLiteDatabase.query("T" + finalI, null, null, null, null, null, null);
                                logCursor(cursor);
                                cursor.close();
                                Log.d(LOG_TAG, "--- ---");
                                Log.d(LOG_TAG, "---Table Themes---");
                                cursor = sqLiteDatabase.query("Themes", null, null, null, null, null, null);
                                logCursor(cursor);
                                cursor.close();
                                Log.d(LOG_TAG, "--- ---");
                            }
                        }).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                            @Override
                            public void onComplete(@NonNull Task<byte[]> task) {
                                i5++;
                                if(i5 == i4) {
                                    sqLiteDatabase.close();
                                    dbHelper.close();
                                    goAccount();

                                }
                            }
                        });

                    }


            }
            Toast.makeText(Loader.this, "Обновление успешно", Toast.LENGTH_SHORT).show();

            saveVersion(DATABASE_VERSION);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE Themes;");
            for (int i2 = 0; i2 < ID.length; i2++)
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "T" + i2 + ";");
            onCreate(sqLiteDatabase);

            saveVersion(i1);
        }
    }

    void saveVersion(int DATABASE_VERSION) {
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("VERSION8", String.valueOf(DATABASE_VERSION)).apply();

    }

    int loadVersion() {

        String savedText = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("VERSION8", "");

        if (savedText == "")
            return 1;
        else
            return Integer.parseInt(savedText);
    }

    
    private void doInBackground(byte[]... jpeg) {
        File photo = null;
        try {
            photo = File.createTempFile(IMG_NAME, "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (photo.exists()) {
            photo.delete();
        }

        try {
            PATH = photo.getPath();
            FileOutputStream fos = new FileOutputStream(photo.getPath());

            fos.write(jpeg[0]);
            fos.close();
        } catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }

    }


}

