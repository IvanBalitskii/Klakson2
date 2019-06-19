package inc.zachetka.klakson.AccountDirect;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import inc.zachetka.klakson.LoginActivity;
import inc.zachetka.klakson.R;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);




        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentFirebaseUser == null) {
            Intent intent = new Intent(Start.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(Start.this, Account.class);
            startActivity(intent);
            finish();
        }
    }

}
