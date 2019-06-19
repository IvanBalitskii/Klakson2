package inc.zachetka.klakson.Tools;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import inc.zachetka.klakson.AccountDirect.News;

/**
 * Created by Иван on 08.01.2018.
 */

public class InfoItem {
    private String title;
    private String post;


    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public InfoItem(String title, String post) {
        this.title = title;
        this.post = post;

    }


}