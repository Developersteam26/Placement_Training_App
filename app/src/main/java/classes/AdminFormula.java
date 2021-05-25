package edu.education.androiddevelopmentcontest.classes;

public class AdminFormula {
    public String id;
    public String title;
    public String formula;

    public AdminFormula(String id, String title, String formula) {
        this.id = id;
        this.title = title;
        this.formula = formula;
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

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
