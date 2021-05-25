package edu.education.androiddevelopmentcontest.classes;

public class GetSolution {

    public String id;
    public String question;
    public String answer;
    public String response;

    public GetSolution(String id, String question, String answer, String response) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.response = response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
