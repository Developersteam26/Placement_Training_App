package edu.education.androiddevelopmentcontest.classes;

public class GetReference {

    public String id;
    public String taskid;
    public String title;
    public String videoid;

    public GetReference(String id, String taskid, String title, String videoid) {
        this.id = id;
        this.taskid = taskid;
        this.title = title;
        this.videoid = videoid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
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
