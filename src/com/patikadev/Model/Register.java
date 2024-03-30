package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Register {
    private int id;
    private int course_id;
    private int student_id;

    public Register(int id, int course_id, int student_id) {
        this.id = id;
        this.course_id = course_id;
        this.student_id = student_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }
    public static ArrayList<Register> getList(){
        ArrayList<Register> regcourseList = new ArrayList<>();
        String query = "SELECT * FROM register";
        Register obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                int id = rs.getInt("id");
                int course_id = rs.getInt("course_id");
                int student_id = rs.getInt("student_id");
                obj = new Register(id, course_id, student_id);
                regcourseList.add(obj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return regcourseList;
    }
    public static boolean add(int course_id, int student_id) {
        String query = "INSERT INTO register (course_id, student_id) VALUES (?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, course_id);
            pr.setInt(2, student_id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }
}
