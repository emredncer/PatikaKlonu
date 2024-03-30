package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Quiz {
    private int id;
    private String question;
    private String content_topic;
    private int content_id;

    private Content content;

    public Quiz(int id, String question, String content_topic, int content_id) {
        this.id = id;
        this.question = question;
        this.content_topic = content_topic;
        this.content_id = content_id;
        this.content = Content.getFetch(content_id);
    }

    public Quiz() {
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

    public String getContent_topic() {
        return content_topic;
    }

    public void setContent_topic(String content_topic) {
        this.content_topic = content_topic;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
    public static ArrayList<Quiz> getList() {
        ArrayList<Quiz> quizList = new ArrayList<>();
        Quiz obj;
        String query = "SELECT * FROM quiz";
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                int id = rs.getInt("id");
                String question = rs.getString("question");
                String content_topic = rs.getString("content_topic");
                int content_id = rs.getInt("content_id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return quizList;
    }
    public static Quiz getFetch(int id) {
        Quiz obj = null;
        String query = "SELECT * FROM quiz WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                obj = new Quiz();
                obj.setId(rs.getInt("id"));
                obj.setQuestion(rs.getString("question"));
                obj.setContent_topic(rs.getString("content_topic"));
                obj.setContent_id(rs.getInt("content_id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return obj;
    }
    public static boolean add(String question, String content_topic, int content_id) {
        String query = "INSERT INTO quiz (question, content_topic, content_id) VALUES (?, ?, ?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, question);
            pr.setString(2, content_topic);
            pr.setInt(3, content_id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    public static boolean delete(int id) {
        String query = "DELETE FROM quiz WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    public static ArrayList<Quiz> searchQuizList(String query) {
        ArrayList<Quiz> quizList = new ArrayList<>();
        Quiz obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                obj = new Quiz();
                obj.setId(rs.getInt("id"));
                obj.setQuestion(rs.getString("question"));
                obj.setContent_topic(rs.getString("content_topic"));
                obj.setContent_id(rs.getInt("content_id"));
                quizList.add(obj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return quizList;
    }
}
