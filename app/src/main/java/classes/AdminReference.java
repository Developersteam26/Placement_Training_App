package edu.education.androiddevelopmentcontest.classes;

public class AdminReference {
    public String id;
    public String title;
    public String videoid;

    public AdminReference(String id, String title, String videoid) {
        this.id = id;
        this.title = title;
        this.videoid = videoid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }
}
