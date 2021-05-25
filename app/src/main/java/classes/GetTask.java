package edu.education.androiddevelopmentcontest.classes;

public class GetTask {

    public String id;
    public String username;
    public String progress;
    public String name;
    public String due;
    public String total;
    public String topics;

    public GetTask(String id, String username, String progress, String name, String due, String total,String topics) {
        this.id = id;
        this.username = username;
        this.progress = progress;
        this.name = name;
        this.due = due;
        this.total = total;
        this.topics = topics;
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

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }
}
