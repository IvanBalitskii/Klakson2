package inc.zachetka.klakson.Tools;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Иван on 07.10.2017.
 */

public class Lectures {
    ArrayList<String> Themes;
    Calendar dateD;

    public Lectures() {
       dateD = Calendar.getInstance();
    }

    public Calendar getDateD() {
        return dateD;
    }

    public void setDateD(int days, int month, int years) {
        dateD.set(days, month, years);
    }

    public ArrayList<String> getThemes() {
        return Themes;
    }

    public void setThemes(ArrayList<String> themes) {
        Themes = themes;
    }


}
