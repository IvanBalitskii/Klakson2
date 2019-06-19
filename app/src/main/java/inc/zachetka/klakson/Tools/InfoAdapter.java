package inc.zachetka.klakson.Tools;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import inc.zachetka.klakson.AccountDirect.News;
import inc.zachetka.klakson.R;

/**
 * Created by Иван on 08.01.2018.
 */

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    public ArrayList<InfoItem> items = new ArrayList<>();




    public InfoAdapter() {
    }

    public void addAll(List<InfoItem> items) {
        int pos = getItemCount();
        this.items.addAll(items);
        notifyItemRangeInserted(pos, this.items.size());

    }

    @Override
    public InfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itempost, parent, false);

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
        private TextView title, post;

        View view;
        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            post = (TextView) v.findViewById(R.id.postText);
            view = v;
        }

        public void bind(final InfoItem modelItem) {

            title.setText(modelItem.getTitle());
            post.setText(modelItem.getPost());
        }
    }
}
