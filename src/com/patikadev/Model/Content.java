package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Content {
    private int id;
    private String topic;
    private String exp;
    private int course_id;

    private String ytubeUrl;
    private Course course;

    public Content(int id, String topic, String exp, int course_id, String ytubeUrl) {
        this.id = id;
        this.topic = topic;
        this.exp = exp;
        this.course_id = course_id;
        this.ytubeUrl = ytubeUrl;
        this.course = Course.getFetch(course_id);
    }
    public Content() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getYtubeUrl() {
        return ytubeUrl;
    }

    public void setYtubeUrl(String ytubeUrl) {
        this.ytubeUrl = ytubeUrl;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public static ArrayList<Content> getList() {
        ArrayList<Content> contentList = new ArrayList<>();
        Content obj;
        String query = "SELECT * FROM content";
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                int id = rs.getInt("id");
                String topic = rs.getString("topic");
                String exp = rs.getString("exp");
                int course_id = rs.getInt("course_id");
                String ytubeUrl = rs.getString("ytubeUrl");
                obj = new Content(id,topic,exp,course_id,ytubeUrl);
                contentList.add(obj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return contentList;
    }
    public static Content getFetch(int id) {
        Content obj = null;
        String query = "SELECT * FROM content WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                obj = new Content();
                obj.setId(rs.getInt("id"));
                obj.setTopic(rs.getString("topic"));
                obj.setExp(rs.getString("exp"));
                obj.setCourse_id(rs.getInt("course_id"));
                obj.setYtubeUrl(rs.getString("ytube_url"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return obj;
    }
    public static boolean delete(int id) {
        String query = "DELETE FROM content WHERE id = ? ";
        ArrayList<Content> contentList = Content.getList();
        for(Content obj : contentList) {
            if(obj.getId() == id) {
                Content.delete(obj.getId());
            }
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    public static boolean add(String topic, String exp, int course_id, String ytubeUrl) {
        String query = "INSERT INTO content (topic, exp, course_id, ytube_url) VALUES (?,?,?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,topic);
            pr.setString(2,exp);
            pr.setInt(3,course_id);
            pr.setString(4, ytubeUrl);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }
    public static ArrayList<Content> searchContentList(String query){
        ArrayList<Content> contentList = new ArrayList<>();
        Content obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                int id = rs.getInt("id");
                String topic = rs.getString("topic");
                String exp = rs.getString("exp");
                int course_id = rs.getInt("course_id");
                String ytubeUrl = rs.getString("ytube_url");
                obj = new Content(id, topic, exp, course_id, ytubeUrl);
                contentList.add(obj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return contentList;
    }
}
