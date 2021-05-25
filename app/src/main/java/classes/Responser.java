package edu.education.androiddevelopmentcontest.classes;

public class Responser {

    public String response;
    public String answer;
    public String id;

    public Responser(String response, String answer,String id) {
        this.response = response;
        this.answer = answer;
        this.id = id;
    }

    public String getResponse() {
        return response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
