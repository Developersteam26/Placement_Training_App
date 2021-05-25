package edu.education.androiddevelopmentcontest.classes;

public class GetFormula {

    public String id;
    public String taskid;
    public String title;
    public String formula;

    public GetFormula(String id, String taskid, String title, String formula) {
        this.id = id;
        this.taskid = taskid;
        this.title = title;
        this.formula = formula;
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

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
