package inc.zachetka.klakson.Tools;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import inc.zachetka.klakson.R;

import static android.content.ContentValues.TAG;


public class Results extends Fragment {
    ImageView image;
    TextView tRes, nRes;
    Button goN;
    String uId;
    ArrayList<String> num;
    ArrayList<Integer> WRONG, ALL;
    DatabaseReference myRef, bn, un;
    int size = 0;
     boolean isExam = false, isSended = false;
    public void setWRONG(ArrayList<Integer> WRONG) {
        this.WRONG = WRONG;
    }

    public void setALL(ArrayList<Integer> ALL, String userId, ArrayList<String> num, boolean isExam, int size) {
        this.uId = userId;
        this.ALL = ALL;
        this.num = num;
        this.isExam = isExam;
        this.size = size;
    }

    public Results() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();


        bn = database.getRef().child("ResultsNow").child("Marks");
        un = database.getRef().child("ResultsNow").child("Users");


        image = view.findViewById(R.id.ImageResult);
        tRes = view.findViewById(R.id.TextResult);
        nRes = view.findViewById(R.id.NumResult);
        goN = view.findViewById(R.id.goNews);
        goN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        int wrong = 0, maxMistakes = 1, all = 0;
        if(isExam){
            maxMistakes = 2;
        }

        for (int i = 0; i < WRONG.size(); i++)
            wrong += WRONG.get(i);
        for (int i = 0; i < ALL.size(); i++)
            all += ALL.get(i);
        for (int i = 0; i < ALL.size(); i++) {
            if (ALL.get(i) != 0) {
                Person person = new Person();

                if(!isExam) {
                    //Adding values
                    person.setId(uId);
                    person.setMark(ALL.get(i) - WRONG.get(ALL.get(i)) + "/" + ALL.get(i));
                    person.setLect(String.valueOf(num.get(i)));
                    myRef = database.getRef().child("Students").child(uId).child("Marks").child(num.get(i));
                }

                if (WRONG.get(ALL.get(i)) > maxMistakes) {
                    if(!isExam) myRef.setValue(ALL.get(i) - WRONG.get(ALL.get(i)) + "/" + ALL.get(i));
                    nRes.setText(size - wrong + " Правильных из " + size);
                    if(!isExam)person.setStat("Провален");

                } else {

                    if(!isExam) myRef.setValue("З");
                    if(!isExam)person.setStat("Зачет");
                }
                if(!isExam){DatabaseReference newRef = database.getRef().child("ResultsNow").push();
             newRef.setValue(person);
                }
            }
        }
        if (wrong > maxMistakes) {
            image.setImageResource(R.drawable.imred);
            tRes.setText("Зачет не сдан.");
            if(isExam)tRes.setText("Экзамен не сдан.");
            tRes.setTextColor(Color.RED);
        }else{
            image.setImageResource(R.drawable.imgreen);
            tRes.setText("Зачет сдан!");
            if(isExam)tRes.setText("Экзамен сдан!");
            tRes.setTextColor(Color.GREEN);
            nRes.setText(size - wrong + " Правильных из " + size);
        }


        return view;
    }

}
