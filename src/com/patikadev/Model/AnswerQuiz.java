package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AnswerQuiz {
    private int id;
    private String question;
    private String answer;
    private int question_id;
    private int content_id;
    private Quiz quiz;

    public AnswerQuiz(int id, String question, String answer, int question_id, int content_id) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.question_id = question_id;
        this.content_id = content_id;
        this.quiz = Quiz.getFetch(question_id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public static ArrayList<AnswerQuiz> getList() {
        ArrayList<AnswerQuiz> ansquizList = new ArrayList<>();
        AnswerQuiz obj;
        String query = "SELECT * FROM answer_quiz";
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                String question = rs.getString("question");
                String answer = rs.getString("answer");
                int question_id = rs.getInt("question_id");
                int content_id = rs.getInt("content_id");
                obj = new AnswerQuiz(id, question, answer, question_id, content_id);
                ansquizList.add(obj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ansquizList;
    }
    public static boolean add(String question,String answer,int question_id,int content_id) {
        String query = "INSERT INTO answer_quiz (question, answer,question_id, content_id) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,question);
            pr.setString(2,answer);
            pr.setInt(3,question_id);
            pr.setInt(4,content_id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    public static boolean delete(int id) {
        String query ="DELETE FROM answer_quiz WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
}
