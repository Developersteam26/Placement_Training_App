package edu.education.androiddevelopmentcontest.classes;

public class WeeklyTask {

    public String id;
    public String username;
    public String duration;
    public String progress;
    public String taskid;
    public String type;
    public String title;

    public WeeklyTask(String id, String username, String duration, String progress, String taskid, String type, String title) {
        this.id = id;
        this.username = username;
        this.duration = duration;
        this.progress = progress;
        this.taskid = taskid;
        this.type = type;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
