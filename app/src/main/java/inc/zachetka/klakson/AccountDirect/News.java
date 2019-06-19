package inc.zachetka.klakson.AccountDirect;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import inc.zachetka.klakson.LoadTicket.Loader;
import inc.zachetka.klakson.R;
import inc.zachetka.klakson.Tools.Info;
import inc.zachetka.klakson.Tools.Lectures;
import inc.zachetka.klakson.Tools.ModelItem;
import inc.zachetka.klakson.Tools.MyAdapter;
import inc.zachetka.klakson.Tools.Themes;
import inc.zachetka.klakson.Tools.TicketForm;


public class News extends Fragment {

    private RecyclerView mRecyclerView;
    public MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Lectures> lectures;
    ArrayList<Themes> themesAr;
    ArrayList<ModelItem> itemsList;
    String userId, Date;
    Info uInfo;
    ArrayList<String> themes;
    ArrayList<ArrayList<Integer>> numTickets;
    Calendar nowDate, startDate, lectDate;
    final String LOG_TAG = "myLogs11";
    SQLiteDatabase sqLiteDatabase;
    Button goToExam;

    public void set(Info uInfo, String uID){
        this.uInfo = uInfo;
        this.userId = uID;
    }
    public News() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        themes = new ArrayList<>();
        themesAr = new ArrayList<>();
        numTickets = new ArrayList<ArrayList<Integer>>();
        nowDate = Calendar.getInstance();
        startDate = Calendar.getInstance();
        lectDate = Calendar.getInstance();

        lectures = new ArrayList<Lectures>();
        DbHelper dbHelper = new DbHelper(getActivity().getApplicationContext(), loadVersion());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                showData(dataSnapshot, FirebaseDatabase.getInstance().getReference());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void showData(DataSnapshot dataSnapshot, DatabaseReference ref) {
        itemsList = new ArrayList<>();
        DataSnapshot dsU = dataSnapshot;
        DataSnapshot dsG = dataSnapshot;
        for (DataSnapshot postSnapshot : dataSnapshot.child("Students").getChildren()) {
            uInfo.setId(dataSnapshot.child("Students/" + postSnapshot.getKey()).getValue(Info.class).getId());
            dsU = postSnapshot;
            if (uInfo.getId().equals(userId)) break;
        }

        uInfo.setName(dataSnapshot.child("Students/" + dsU.getKey()).getValue(Info.class).getName());
        uInfo.setSurname(dataSnapshot.child("Students/" + dsU.getKey()).getValue(Info.class).getSurname());

        int k = 0;
        for (DataSnapshot postSnapshot : dataSnapshot.child("Themes").getChildren()) {
            Themes t = new Themes();
            t.setID(postSnapshot.getKey());
            t.setNum(k);
            themesAr.add(t);
            k++;
        }
        int count = 0;

        uInfo.setDates(dataSnapshot.child("Students/" + dsU.getKey()).getValue(Info.class).getDates());
        uInfo.setGroupId(dataSnapshot.child("Students/" + dsU.getKey()).getValue(Info.class).getGroupId());

        String str = uInfo.getDates();
        String[] splitStr = str.split("-");
        startDate.set(Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]) - 1, Integer.parseInt(splitStr[2]));

        for (DataSnapshot postSnapshot : dataSnapshot.child("Groups/" + uInfo.getGroupId() + "/Lects").getChildren()) {

            Date = (String) postSnapshot.child("Date").getValue();
            for (DataSnapshot ps : postSnapshot.child("Themes").getChildren())
                themes.add((String) ps.getValue());
            Lectures l = new Lectures();
            String[] splitStr1 = Date.split("-");
            l.setDateD(Integer.parseInt(splitStr1[0]), Integer.parseInt(splitStr1[1]) - 1, Integer.parseInt(splitStr1[2]) - 1);
            if ((startDate.before(l.getDateD()) || startDate.equals(l.getDateD())) && l.getDateD().before(nowDate) || l.getDateD().equals(nowDate))
                count++;

            l.setThemes(themes);
            lectures.add(l);

            themes = new ArrayList<>();
            Date = "";
        }
        Collections.sort(lectures, new Comparator<Lectures>() {
            @Override
            public int compare(Lectures lectures, Lectures t1) {
                return lectures.getDateD().getTime().compareTo(t1.getDateD().getTime());
            }
        });


        for (int s = lectures.size() - 1; s >= 0; s--) {
            lectDate = lectures.get(s).getDateD();
            if ((startDate.before(lectDate) || startDate.equals(lectDate)) && lectDate.before(nowDate) || lectDate.equals(nowDate)) {
                String theme = "";
                String uK = "null", n3 = "null";
                ArrayList<Integer> idN = new ArrayList<>();
                for (int i = 0; i < lectures.get(s).getThemes().size(); i++) {
                    int id = 0;
                    for (int i2 = 0; i2 < themesAr.size(); i2++) {
                        if (themesAr.get(i2).getID().equals(lectures.get(s).getThemes().get(i))) {
                            id = themesAr.get(i2).getNum();
                            Log.d(LOG_TAG, "---Table Themes---");
                            System.out.println(getById(lectures.get(s).getThemes().get(i), sqLiteDatabase, "Themes", "Id", "Name"));
                            Log.d(LOG_TAG, "--- ---");
                            String n = getById(lectures.get(s).getThemes().get(i), sqLiteDatabase, "Themes", "Id", "Theme");
                            String[] s1 = n.split("Theme =");
                            String[] s2 = s1[1].split(";");
                            theme = theme + s2[0] + "\n";
                            idN.add(id);
                            n3 = getById(String.valueOf(1), sqLiteDatabase, "T" + id, "Id", "Question");
                            uK = dsU.getKey();
                            break;
                        }
                    }

                }
                SimpleDateFormat format = new SimpleDateFormat("d MMMM");
                if (n3 == null) uK = "null";
                else
                    numTickets.add(idN);
                itemsList.add(new ModelItem(getActivity().getApplicationContext(), "Лекция " + count, format.format(lectures.get(s).getDateD().getTime()), theme,
                        this, lectures.get(s).getThemes(), sqLiteDatabase, uK, getActivity(), idN));

                count--;

            }
        }


        mAdapter = new MyAdapter();
        mAdapter.addAll(itemsList);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void createExamView() {
        if (numTickets.size() > 1) {
            Intent intent = new Intent(getActivity().getApplicationContext(), TicketForm.class);
            intent.putExtra("length", numTickets.size());
            for (int i = 0; i < numTickets.size(); i++) {
                for (int i2 = 0; i2 < numTickets.get(i).size(); i2++) {
                    System.out.println(numTickets.get(i).get(i2));
                    intent.putExtra("N" + String.valueOf(i), numTickets.get(i).get(i2));
                }
            }
            intent.putExtra("userId", "EXAM_MODE");

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().getApplicationContext().startActivity(intent);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Недостаточно лекций для экзамена", Toast.LENGTH_SHORT).show();

        }
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
                Log.d(LOG_TAG, obj);

                return obj;


            } while (cursor.moveToNext());
        } else {
            cursor.close();

            return null;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);


        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        goToExam = view.findViewById(R.id.exam);
        goToExam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             createExamView();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    int loadVersion() {

        String savedText = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("VERSION8", "");

        if (savedText == "")
            return 0;
        else
            return Integer.parseInt(savedText);
    }

    class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context, int v) {
            super(context, "ma9Base", null, v);


        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {


        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

    }
}
