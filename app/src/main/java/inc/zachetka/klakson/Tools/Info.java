package inc.zachetka.klakson.Tools;

/**
 * Created by Иван on 09.09.2017.
 */

public class Info {
    private String Ofice;
    private String Name;
    private String Lecture;
    private String Theme;
    private String Dates;
    private String Surname;

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getDates() {
        return Dates;
    }

    public void setDates(String dates) {
        Dates = dates;
    }

    private String GroupId;

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String GroupId) {
        this.GroupId = GroupId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    private String Id;

    public Info() {

    }

    public String getTheme() {
        return Theme;
    }

    public void setTheme(String theme) {
        this.Theme = theme;
    }



    public void setLecture(String Lecture) {
        this.Lecture = Lecture;
    }



    public String getLecture() {

        return Lecture;
    }



    public String getOfice() {
        return Ofice;
    }

    public void setOfice(String ofice) {
        this.Ofice = ofice;
    }

    public void setName(String Name){
        this.Name = Name;

    }

    public String getName() {
        return Name;
    }
}
