package inc.zachetka.klakson;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import inc.zachetka.klakson.AccountDirect.News;
import inc.zachetka.klakson.Tools.ConstInfo;
import inc.zachetka.klakson.Tools.Info;
import inc.zachetka.klakson.Tools.InfoAdapter;
import inc.zachetka.klakson.Tools.InfoItem;
import inc.zachetka.klakson.Tools.Lectures;
import inc.zachetka.klakson.Tools.ModelItem;
import inc.zachetka.klakson.Tools.MyAdapter;
import inc.zachetka.klakson.Tools.Themes;


public class Information extends Fragment {
    private RecyclerView mRecyclerView;
    public InfoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<InfoItem> itemsList;
    ConstInfo info;
    String grid;
    public Information() {
        // Required empty public constructor
    }

    public void setGrid(String grid) {
        this.grid = grid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Information");
         info = new ConstInfo();
        Query lastQuery = database.orderByKey().limitToLast(15);
        lastQuery.addValueEventListener(new ValueEventListener() {
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

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            info.setTitle((dataSnapshot.child(postSnapshot.getKey()).child("Title")).getValue().toString());
            info.setPost((dataSnapshot.child(postSnapshot.getKey()).child("Post")).getValue().toString());
            info.setGroupId((String) (dataSnapshot.child(postSnapshot.getKey()).child("GroupId")).getValue());
            if (info.getGroupId() == null) info.setGroupId("n");

            if ((info.getGroupId().equals(grid) || info.getGroupId().equals("n"))) {
                itemsList.add(new InfoItem(info.getTitle(), info.getPost()));
            }
        }
        Collections.reverse(itemsList);
        mAdapter = new InfoAdapter();
        mAdapter.addAll(itemsList);
        mRecyclerView.setAdapter(mAdapter);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_information, container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.info_recycle);


        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        return view;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
