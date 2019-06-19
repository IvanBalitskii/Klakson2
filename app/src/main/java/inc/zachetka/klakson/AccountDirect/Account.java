package inc.zachetka.klakson.AccountDirect;


import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;


import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;


import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import inc.zachetka.klakson.Information;
import inc.zachetka.klakson.LoadTicket.Loader;
import inc.zachetka.klakson.LoginActivity;
import inc.zachetka.klakson.Practise.Practise;
import inc.zachetka.klakson.R;
import inc.zachetka.klakson.Tools.ConstInfo;
import inc.zachetka.klakson.Tools.Info;
import inc.zachetka.klakson.Tools.InfoItem;
import inc.zachetka.klakson.Tools.Lectures;
import inc.zachetka.klakson.Tools.ModelItem;
import inc.zachetka.klakson.Tools.MyAdapter;
import inc.zachetka.klakson.Tools.Themes;


public class Account extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    News news;
    Lections lects;
    Information info;
    Loader loader;
    TextView uName, grText;
    Practise practise;
    Info uInfo;
    String userId;
    ConstInfo info1;
    boolean sended = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account2);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        uInfo = new Info();
        info1 = new ConstInfo();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userId = currentFirebaseUser.getUid();

        practise = new Practise();
        news = new News();
        lects = new Lections();
        info = new Information();


        int v = loadVersion();
        if (v == 0) {
            loader = new Loader();
            Log.d("DW", "TO LOAD STARTED");
            load();
        } else {


            android.app.FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ftr = fragmentManager.beginTransaction();
            ftr.replace(R.id.container, news);
            ftr.commit();
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    showData(dataSnapshot, FirebaseDatabase.getInstance().getReference());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Query lastQuery = database.child("Information").orderByKey().limitToLast(1);
            lastQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (sended)
                        updateInfo(dataSnapshot, FirebaseDatabase.getInstance().getReference());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Handle possible errors.
                }
            });


            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View header = navigationView.getHeaderView(0);

            uName = header.findViewById(R.id.userName);
            grText = header.findViewById(R.id.GroupText);

        }

    }

    private void showData(DataSnapshot dataSnapshot, DatabaseReference ref) {
        DataSnapshot dsU = dataSnapshot;
        for (DataSnapshot postSnapshot : dataSnapshot.child("Students").getChildren()) {
            uInfo.setId(dataSnapshot.child("Students/" + postSnapshot.getKey()).getValue(Info.class).getId());
            dsU = postSnapshot;
            if (uInfo.getId().equals(userId)) break;
        }
        uInfo.setGroupId(dataSnapshot.child("Students/" + dsU.getKey()).getValue(Info.class).getGroupId());
        uInfo.setName(dataSnapshot.child("Students/" + dsU.getKey()).getValue(Info.class).getName());
        uInfo.setSurname(dataSnapshot.child("Students/" + dsU.getKey()).getValue(Info.class).getSurname());
        uName.setText(uInfo.getName() + " " + uInfo.getSurname());
        grText.setText((String) dataSnapshot.child("Groups/" + uInfo.getGroupId() + "/Name").getValue());
        news.set(uInfo, userId);
        info.setGrid(uInfo.getGroupId());
        sended = true;

    }

    private void updateInfo(DataSnapshot dataSnapshot, DatabaseReference ref) {
        DataSnapshot dsU = dataSnapshot;
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            System.out.println(postSnapshot.getKey());
            info1.setTitle((dataSnapshot.child(postSnapshot.getKey()).child("Title")).getValue().toString());
            info1.setPost((dataSnapshot.child(postSnapshot.getKey()).child("Post")).getValue().toString());
            info1.setGroupId((String) (dataSnapshot.child(postSnapshot.getKey()).child("GroupId")).getValue());
            if (info1.getGroupId() == null) info1.setGroupId("n");
            if ((uInfo.getGroupId().equals(info1.getGroupId()) || info1.getGroupId().equals("n")) && sended) {

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.icon))
                        .setContentTitle(info1.getTitle())
                        .setContentText(info1.getPost())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0, notificationBuilder.build());


            }
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            load();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.news) {
            android.app.FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ftr = fragmentManager.beginTransaction();
            ftr.replace(R.id.container, news, news.getTag());
            ftr.commit();
        } else if (id == R.id.lections) {
            android.app.FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ftr = fragmentManager.beginTransaction();
            ftr.replace(R.id.container, practise, practise.getTag());
            ftr.commit();
        } else if (id == R.id.info) {
            android.app.FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ftr = fragmentManager.beginTransaction();
            ftr.replace(R.id.container, info, info.getTag());
            ftr.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void load() {
        Intent intent = new Intent(Account.this, Loader.class);
        startActivity(intent);
        finish();
    }

    int loadVersion() {

        String savedText = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("VERSION8", "");

        if (savedText == "")
            return 0;
        else
            return Integer.parseInt(savedText);
    }


}
