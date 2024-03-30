package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Helper.Item;
import com.patikadev.Model.Content;
import com.patikadev.Model.Course;
import com.patikadev.Model.Educator;
import com.patikadev.Model.Quiz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class EducatorGUI extends JFrame{
    private JPanel wrapper;
    private JTabbedPane pnl_content_form;
    private JPanel pnl_edu_course_list;
    private JTable tbl_edu_course_list;
    private JScrollPane scrl_edu_course;
    private JPanel pnl_edu_content_list;
    private JScrollPane scrl_edu_content_list;
    private JTable tbl_edu_content_list;
    private JScrollPane scrl_quiz;
    private JTable tbl_edu_quiz_list;
    private JButton btn_logout;
    private JTextField fld_content_exp;
    private JTextField fld_ytube_url;
    private JTextField fld_content_id;
    private JButton ekleButton;
    private JLabel btn_add;
    private JTextField fld_quiz_question;
    private JButton btn_quiz_del;
    private JComboBox cmb_quiz_content;
    private JButton btn_quiz_que_add;
    private JLabel lbl_edu_welcome;
    private JComboBox cmb_content_course;
    private JButton btn_sh_quiz;
    private JTextField fld_content_topic;
    private JButton btn_content_sh;
    private JTextField fld_sh_content_topic;
    private JTextField fld_sh_course_name;
    private JLabel fld_del;
    private JTextField fld_quiz_id_del;
    private JTextField fld_sh_quiz_contTopic;
    private final Educator educator;
    private DefaultTableModel mdl_edu_course_list;
    private Object[] row_edu_course_list;
    private DefaultTableModel mdl_edu_content_list;
    private Object[] row_edu_content_list;
    private DefaultTableModel mdl_edu_quiz_list;
    private Object[] row_edu_quiz_list;
    private JPopupMenu contentMenu;
    ArrayList<Integer> courseID = new ArrayList<>();
    ArrayList<String> courseName = new ArrayList<>();
    ArrayList<Integer> contentID = new ArrayList<>();

    public EducatorGUI(Educator educator) {
        this.educator = educator;
        add(wrapper);
        setSize(1000, 500);
        int x = Helper.screenCenterPoint("x", getSize());
        int y = Helper.screenCenterPoint("y", getSize());
        setLocation(x, y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        lbl_edu_welcome.setText("Hoşgeldiniz " + educator.getName());

        mdl_edu_course_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 || column == 1 || column == 2 || column == 3 ||column == 4)
                    return false;
                return super.isCellEditable(row, column);
            }
        };

        Object[] col_edu_course_list = {"ID", "Dersin Adı", "Programlama Dili", "Patika", "Eğitmen"};
        mdl_edu_course_list.setColumnIdentifiers(col_edu_course_list);
        row_edu_course_list = new Object[col_edu_course_list.length];
        loadEduCourseModel();
        tbl_edu_course_list.setModel(mdl_edu_course_list);
        tbl_edu_course_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_edu_course_list.getTableHeader().setReorderingAllowed(false);


        contentMenu = new JPopupMenu();
        JMenuItem deleteMenu = new JMenuItem("Sil");
        JMenuItem arrangeMenu = new JMenuItem("Düzenle");
        contentMenu.add(deleteMenu);
        contentMenu.add(arrangeMenu);

        deleteMenu.addActionListener(e -> {
            int selected_id = Integer.parseInt(tbl_edu_content_list.getValueAt(tbl_edu_content_list.getSelectedRow(),0).toString());
            DeleteGUI deleteGUI = new DeleteGUI(Content.getFetch(selected_id));
        });

        mdl_edu_content_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 || column == 1 || column == 2 || column == 3 ||column == 4)
                    return true;
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_edu_content_list = {"ID", "İçerik Başlığı", "Açıklama", "Ders Adı", "YouTube Linki"};
        mdl_edu_content_list.setColumnIdentifiers(col_edu_content_list);
        row_edu_content_list = new Object[col_edu_content_list.length];
        loadEduContentModel();
        tbl_edu_content_list.setModel(mdl_edu_content_list);

        tbl_edu_content_list.setComponentPopupMenu(contentMenu);
        tbl_edu_content_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_edu_content_list.getTableHeader().setReorderingAllowed(false);
        tbl_edu_content_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_edu_content_list.rowAtPoint(point);
                tbl_edu_content_list.setRowSelectionInterval(selected_row,selected_row);
            }
        });
        loadCourseCombo();

        mdl_edu_quiz_list = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 || column == 1 || column == 2 || column == 3 ||column == 4)
                    return false;
                return super.isCellEditable(row, column);
            }
        };

        Object[] col_edu_quiz_list = {"ID", "Quiz Sorusu", "İçerik Başlığı", "İçerik ID"};
        mdl_edu_quiz_list.setColumnIdentifiers(col_edu_quiz_list);
        row_edu_quiz_list = new Object[col_edu_quiz_list.length];
        loadEduQuizModel();
        tbl_edu_quiz_list.setModel(mdl_edu_quiz_list);
        tbl_edu_quiz_list.getColumnModel().getColumn(0).setMaxWidth(50);
        tbl_edu_quiz_list.getColumnModel().getColumn(3).setMaxWidth(100);
        tbl_edu_quiz_list.getTableHeader().setReorderingAllowed(false);
        loadContentCombo();

        btn_logout.addActionListener(e -> {
            dispose();
            LoginGUI login = new LoginGUI();
        });

        tbl_edu_quiz_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String select_quiz_id = tbl_edu_quiz_list.getValueAt(tbl_edu_quiz_list.getSelectedRow(),0).toString();
                fld_quiz_id_del.setText(select_quiz_id);
            }
            catch (Exception exception){

            }
        });
        ekleButton.addActionListener(e -> {
            Item courseItem = (Item) cmb_content_course.getSelectedItem();

            if (Helper.isFieldEmpty(fld_content_topic) || Helper.isFieldEmpty(fld_content_exp) || Helper.isFieldEmpty(fld_ytube_url)){
                Helper.showMessage("fill");
            }
            else{
                if (Content.add(fld_content_topic.getText(), fld_content_exp.getText(), courseItem.getKey(), fld_ytube_url.getText())){
                    Helper.showMessage("done");
                    loadEduContentModel();
                    fld_content_topic.setText(null);
                    fld_content_exp.setText(null);
                    fld_ytube_url.setText(null);
                    loadContentCombo();
                }
                else {
                    Helper.showMessage("error");
                }
            }
        });
        btn_content_sh.addActionListener(e -> {
            String topic = fld_sh_content_topic.getText();

            if (Helper.isFieldEmpty(fld_sh_content_topic) && Helper.isFieldEmpty(fld_sh_course_name)){
                loadEduContentModel();
            }
            else{
                String query = Helper.searchQuery(topic);
                ArrayList<Content> searchingContent = Content.searchContentList(query);
                loadEduContentModel(searchingContent);
            }
        });
        btn_quiz_que_add.addActionListener(e -> {
            Item contentItem = (Item) cmb_quiz_content.getSelectedItem();
            if (Helper.isFieldEmpty(fld_quiz_question)){
                Helper.showMessage("fill");
            }
            else{
                if (Quiz.add(fld_quiz_question.getText(), contentItem.getValue(), contentItem.getKey())){
                    Helper.showMessage("done");
                    loadEduQuizModel();
                    fld_quiz_question.setText(null);
                }
                else {
                    Helper.showMessage("error");
                }
            }
        });
        btn_quiz_del.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_quiz_id_del)){
                Helper.showMessage("fill");
            }
            else {
                if (Helper.confirm("sure")){
                    int quiz_id = Integer.parseInt(fld_quiz_id_del.getText());
                    if (Quiz.delete(quiz_id)){
                        Helper.showMessage("done");
                        loadEduQuizModel();
                        fld_quiz_id_del.setText(null);
                    }
                    else {
                        Helper.showMessage("error");
                    }
                }
            }
        });
        btn_sh_quiz.addActionListener(e -> {
            String content_topic = fld_sh_quiz_contTopic.getText();
            String query = Helper.searchQuizQuery(content_topic);
            ArrayList <Quiz> searchingQuiz = Quiz.searchQuizList(query);

            loadEduQuizModel(searchingQuiz);
        });
    }

    private void loadContentCombo() {
        cmb_quiz_content.removeAllItems();
        for (Content obj : Content.getList()){
            if (courseID.contains(obj.getCourse_id())){
                cmb_quiz_content.addItem(new Item(obj.getId(), obj.getTopic()));
            }
        }
    }
    private void loadEduQuizModel(ArrayList<Quiz> list) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_edu_quiz_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for (Quiz obj : list){
            if (contentID.contains(obj.getContent_id())){
                i = 0;
                row_edu_quiz_list[i++] = obj.getId();
                row_edu_quiz_list[i++] = obj.getQuestion();
                row_edu_quiz_list[i++] = obj.getContent_topic();
                row_edu_quiz_list[i++] = obj.getContent_id();
                mdl_edu_quiz_list.addRow(row_edu_quiz_list);
            }
        }
    }


    private void loadEduQuizModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_edu_quiz_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for (Quiz obj : Quiz.getList()){
            if (contentID.contains(obj.getContent_id())){
                i = 0;
                row_edu_quiz_list[i++] = obj.getId();
                row_edu_quiz_list[i++] = obj.getQuestion();
                row_edu_quiz_list[i++] = obj.getContent_topic();
                row_edu_quiz_list[i++] = obj.getContent_id();
                mdl_edu_quiz_list.addRow(row_edu_quiz_list);
            }
        }
    }

    private void loadCourseCombo() {
        cmb_content_course.removeAllItems();
        for (Course obj : Course.getList()){
            if (obj.getUser_id() == educator.getId()){
                cmb_content_course.addItem(new Item(obj.getId(), obj.getName()));
            }
        }
    }

    private void loadEduContentModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_edu_content_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for (Content obj : Content.getList()){
            if (courseID.contains(obj.getCourse_id())){
                i=0;
                row_edu_content_list[i++] = obj.getId();
                contentID.add(obj.getId());
                row_edu_content_list[i++] = obj.getTopic();
                row_edu_content_list[i++] = obj.getExp();
                row_edu_content_list[i++] = obj.getCourse().getName();
                row_edu_content_list[i++] = obj.getYtubeUrl();
                mdl_edu_content_list.addRow(row_edu_content_list);
            }
        }
    }

    private void loadEduCourseModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_edu_course_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for (Course obj : Course.getListByUser(educator.getId())){
            i = 0;
            row_edu_course_list[i++] = obj.getId();
            courseID.add(obj.getId());
            row_edu_course_list[i++] = obj.getName();
            courseName.add(obj.getName());
            row_edu_course_list[i++] = obj.getLang();
            row_edu_course_list[i++] = obj.getPatika().getName();
            row_edu_course_list[i++] = obj.getEducator().getName();

            mdl_edu_course_list.addRow(row_edu_course_list);

        }
    }
    private void loadEduContentModel(ArrayList<Content> list) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_edu_content_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for (Content obj : list){
            if (Helper.isFieldEmpty(fld_sh_course_name)){
                i=0;
                row_edu_content_list[i++] = obj.getId();
                row_edu_content_list[i++] = obj.getTopic();
                row_edu_content_list[i++] = obj.getExp();
                row_edu_content_list[i++] = obj.getCourse().getName();
                row_edu_content_list[i++] = obj.getYtubeUrl();
                mdl_edu_content_list.addRow(row_edu_content_list);
            }
            else{
                for (String name : courseName){
                    if (name.contains(fld_sh_course_name.getText()) && name.equals(obj.getCourse().getName())){
                        i=0;
                        row_edu_content_list[i++] = obj.getId();
                        row_edu_content_list[i++] = obj.getTopic();
                        row_edu_content_list[i++] = obj.getExp();
                        row_edu_content_list[i++] = obj.getCourse().getName();
                        row_edu_content_list[i++] = obj.getYtubeUrl();
                        mdl_edu_content_list.addRow(row_edu_content_list);
                    }
                }
            }

        }
    }

    public static void main(String[] args) {
        Educator edu = new Educator();
        Helper.setLayout();
        EducatorGUI educatorGUI = new EducatorGUI(edu);
    }
}
