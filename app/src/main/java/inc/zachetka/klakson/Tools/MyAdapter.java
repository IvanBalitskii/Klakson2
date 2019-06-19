package inc.zachetka.klakson.Tools;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import inc.zachetka.klakson.AccountDirect.News;
import inc.zachetka.klakson.R;

/**
 * Created by Иван on 05.09.2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public ArrayList<ModelItem> items = new ArrayList<>();




    public MyAdapter() {
    }

    public void addAll(List<ModelItem> items) {
        int pos = getItemCount();
        this.items.addAll(items);
        notifyItemRangeInserted(pos, this.items.size());

    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, date, theme;
        SQLiteDatabase sqLiteDatabase;
        News n;
        ArrayList<String> num;
        ArrayList<Integer> idN;
        Context ctx;
        String userId;
        Button button;
        View view;
        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.lect);
            date = (TextView) v.findViewById(R.id.date);
            theme = (TextView) v.findViewById(R.id.theme);
            button = (Button) v.findViewById(R.id.goTick);
            view = v;
        }

        public void bind(final ModelItem modelItem) {
            ctx = modelItem.getContext();
            title.setText(modelItem.getLect());
            date.setText(modelItem.getDate());
            theme.setText(modelItem.getTheme());
            sqLiteDatabase = modelItem.getSqLiteDatabase();
            num = modelItem.getNum();
            idN = modelItem.getIdN();
            n = modelItem.getN();
            userId = modelItem.getUserId();
            button.setVisibility(View.VISIBLE);
            if (userId == "null") {
              button.setVisibility(View.INVISIBLE);
            } else {
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent intent = new Intent(ctx, TicketForm.class);
                        intent.putExtra("length", num.size());
                        for (int i = 0; i < num.size(); i++) {
                            intent.putExtra(String.valueOf(i), num.get(i));
                            intent.putExtra( "N" + String.valueOf(i), idN.get(i));
                        }
                        intent.putExtra("userId", userId);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(intent);


                    }
                });
            }
        }
    }
}
