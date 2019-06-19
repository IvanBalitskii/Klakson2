package inc.zachetka.klakson.LoadTicket;

/**
 * Created by Иван on 25.10.2017.
 */

public class ThemesInfo {
    private String Name;
    private String Theme;
    private String[] Tickets;

    public ThemesInfo() {

    }

    public String[] getTickets() {
        return Tickets;
    }

    public void setTickets(String[] tickets) {
        Tickets = tickets;
    }

    public String getTheme() {
        return Theme;
    }

    public void setTheme(String theme) {
        Theme = theme;
    }

    public String getName() {

        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
}
