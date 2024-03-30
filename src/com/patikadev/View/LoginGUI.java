package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Educator;
import com.patikadev.Model.Operator;
import com.patikadev.Model.Student;
import com.patikadev.Model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame{
    private JPanel wrapper;
    private JPanel wtop;
    private JPanel wbottom;
    private JTextField fld_user_uname;
    private JPasswordField fld_user_pass;
    private JButton btn_login;
    private JButton btn_sign_up;

    public LoginGUI() {
        add(wrapper);
        setSize(400,400);
        int x = Helper.screenCenterPoint("x", getSize());
        int y =Helper.screenCenterPoint("y", getSize());
        setLocation(x,y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        btn_login.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_user_uname) || Helper.isFieldEmpty(fld_user_pass)) {
                Helper.showMessage("fill");
            } else {
                User u = User.getFetch(fld_user_uname.getText(), fld_user_pass.getText());
                if(u == null) {
                    Helper.showMessage("Kullanıcı bulunamadı.");
                    fld_user_uname.setText(null);
                    fld_user_pass.setText(null);
                } else {
                    switch (u.getType()) {
                        case "operator":
                            OperatorGUI opGUI = new OperatorGUI((Operator) u);
                            break;
                        case "educator":
                            EducatorGUI educatorGUI = new EducatorGUI((Educator) u);
                            break;
                        case "student":
                            StudentGUI stuGUI = new StudentGUI((Student) u);
                            break;
                    }
                    dispose();
                }
            }
        });
        btn_sign_up.addActionListener(e -> {
            SignUpGUI sign = new SignUpGUI();
            sign.setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        Helper.setLayout();
        LoginGUI login = new LoginGUI();
    }
}
