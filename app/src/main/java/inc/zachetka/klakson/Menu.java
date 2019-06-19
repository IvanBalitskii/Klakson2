package inc.zachetka.klakson;


import android.content.ClipData;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.database.FirebaseDatabase;

public class Menu extends AppCompatActivity {


    Tickets tickets;
    Information info;
    ClipData.Item button;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    goTickets();
                    return true;
                case R.id.navigation_dashboard:
                     goInfo();
                    return true;
                case R.id.navigation_notifications:
                  goLogin();

//                   LoginActivity loginActivity = new LoginActivity();
//                    android.app.FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.loginLayot, loginActivity, loginActivity.getTag()).commit();
                    return false;
            }

            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        tickets = new Tickets();
        info = new Information();

        android.app.FragmentTransaction ftr = getFragmentManager().beginTransaction();

        ftr.replace(R.id.container, tickets, tickets.getTag());
        ftr.commit();
    }
    public void goTickets(){
        android.app.FragmentTransaction ftr = getFragmentManager().beginTransaction();

        ftr.replace(R.id.container, tickets, tickets.getTag());
        ftr.commit();
    }

    public void goInfo(){
        android.app.FragmentTransaction ftr = getFragmentManager().beginTransaction();

        ftr.replace(R.id.container, info, info.getTag());
        ftr.commit();
    }
    public void goLogin(){
        android.app.FragmentTransaction ftr = getFragmentManager().beginTransaction();
        ftr.detach(info);
        ftr.detach(tickets);
        ftr.commit();
        Intent intent = new Intent(Menu.this, LoginActivity.class );
        startActivity(intent);


    }




}
