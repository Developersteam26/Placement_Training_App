package edu.education.androiddevelopmentcontest.classes;

public class ActualTasks {
    public String taskid;
    public String title;
    public String duration;
    public String type;

    public ActualTasks(String taskid, String title, String duration, String type) {
        this.taskid = taskid;
        this.title = title;
        this.duration = duration;
        this.type = type;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
