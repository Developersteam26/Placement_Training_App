package edu.education.androiddevelopmentcontest.classes;

public class AdminTasks {

    public String id;
    public String name;
    public String due;
    public String total;
    public String topic;

    public AdminTasks(String id, String name, String due, String total, String topic) {
        this.id = id;
        this.name = name;
        this.due = due;
        this.total = total;
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
