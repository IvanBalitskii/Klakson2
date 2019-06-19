package inc.zachetka.klakson.Tools;

/**
 * Created by Иван on 08.01.2018.
 */

public class ConstInfo {
    private String Post, Title, GroupId;

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPost() {

        return Post;
    }

    public void setPost(String post) {
        Post = post;
    }

    public ConstInfo() {

    }

    public ConstInfo(String Post, String Title, String GroupId) {
        this.Post = Post;
        this.Title = Title;
        this.GroupId = GroupId;
    }
}
