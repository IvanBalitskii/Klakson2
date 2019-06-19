package inc.zachetka.klakson.LoadTicket;

/**
 * Created by Иван on 25.10.2017.
 */

public class TicketsInfo {
    private String Question;
    private String Answer1;
    private String Answer2;
    private String Answer3;
    private String Answer4;
    private int right;
    private String URL;

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getRight() {

        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public String getAnswer4() {

        return Answer4;
    }

    public void setAnswer4(String answer4) {
        Answer4 = answer4;
    }

    public String getAnswer3() {

        return Answer3;
    }

    public void setAnswer3(String answer3) {
        Answer3 = answer3;
    }

    public String getAnswer2() {

        return Answer2;
    }

    public void setAnswer2(String answer2) {
        Answer2 = answer2;
    }

    public String getAnswer1() {

        return Answer1;
    }

    public void setAnswer1(String answer1) {
        Answer1 = answer1;
    }

    public String getQuestion() {

        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }


}
