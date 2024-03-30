package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Content;

import javax.swing.*;

public class DeleteGUI extends JFrame{
    private JPanel wrapper;
    private JTextField fld_content_topic;
    private JButton btn_delete;
    private Content content;

    public DeleteGUI(Content content) {
        this.content = content;
        add(wrapper);
        setSize(300,150);
        int x = Helper.screenCenterPoint("x", getSize());
        int y =Helper.screenCenterPoint("y", getSize());
        setLocation(x,y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);


        fld_content_topic.setColumns(content.getId());
        btn_delete.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_content_topic)) {
                Helper.showMessage("fill");
            } else {
                if(Content.delete(content.getId())); {
                    Helper.showMessage("done");
                }
                dispose();
            }
        });
    }
}
