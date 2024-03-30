package com.patikadev.View;

import com.patikadev.Helper.*;
import com.patikadev.Model.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OperatorGUI extends JFrame {
    private JPanel wrapper;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JPanel pnl_user_list;
    private JScrollPane scrl_user_list;
    private JTable tbl_user_list;
    private JPanel pnl_user_form;
    private JTextField fld_user_name;
    private JTextField fld_user_uname;
    private JTextField fld_user_pass;
    private JComboBox cmb_user_type;
    private JButton btn_user_add;
    private JTextField fld_user_id;
    private JButton btn_user_delete;
    private JTextField fld_sh_user_name;
    private JTextField fld_sh_user_uname;
    private JComboBox cmb_sh_user_type;
    private JButton btn_sh_user;
    private JPanel pnl_patika_list;
    private JTabbedPane tab_operator;
    private JScrollPane scrl_patika_list;
    private JTable tbl_patika_list;
    private JPanel pnl_patika_add;
    private JTextField fld_patika_name;
    private JButton btn_patika_add;
    private JPanel pnl_course_form;
    private JTable tbl_course_list;
    private JPanel pnl_course_add;
    private JTextField fld_course_name;
    private JTextField fld_course_lang;
    private JComboBox cmb_course_patika;
    private JComboBox cmb_course_user;
    private JButton btn_course_add;
    private JLabel fld_ders_adı;
    private JTable tbl_content_list;
    private JTextField fld_content_id;
    private JComboBox cmb_content_course;
    private JTable tbl_quiz_list;
    private JTextField fld_quiz_id_del;
    private JComboBox cmb_quiz_content;
    private JTextField fld_course_id;
    private JButton btn_course_delete;
    private JButton btn_content_add;
    private JTextField fld_content_topic;
    private JTextField fld_content_explanation;
    private JTextField fld_ytube_url;
    private JButton btn_content_delete;
    private JButton btn_quiz_add;
    private JTextField fld_quiz_question;
    private JButton btn_quiz_delete;
    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;
    private final Operator operator;
    private Object[] row_patika_list;
    private DefaultTableModel mdl_patika_list;
    private DefaultTableModel mdl_content_list;
    private Object[] row_content_list;
    private JPopupMenu patikaMenu;
    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;
    private DefaultTableModel mdl_quiz_list;
    private Object[] row_quiz_list;

    public OperatorGUI(Operator operator) {

        this.operator = operator;
        add(wrapper);
        setSize(1000, 500);
        int x = Helper.screenCenterPoint("x", getSize());
        int y = Helper.screenCenterPoint("y", getSize());
        setLocation(x, y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);


        lbl_welcome.setText("Hoş Geldin! " + operator.getName());

        //ModelUserList
        mdl_user_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0)
                    return false;
                return super.isCellEditable(row, column);
            }
        };

        Object[] col_user_list = {"ID", "Ad Soyad", "Kullanıcı Adı", "Şifre", "Üyelik Tipi"};
        mdl_user_list.setColumnIdentifiers(col_user_list);
        row_user_list = new Object[col_user_list.length];
        loadUserModel();

        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);

        tbl_user_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String select_user_id = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString();
                fld_user_id.setText(select_user_id);
            } catch (Exception exception) {

            }
        });

        tbl_user_list.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int user_id = Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString());
                String user_name = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 1).toString();
                String user_uname = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 2).toString();
                String user_pass = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 3).toString();
                String user_type = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 4).toString();

                if (User.update(user_id, user_name, user_uname, user_pass, user_type)) {
                    Helper.showMessage("done");
                }
                loadUserModel();
                loadEducatorCombo();
                loadCourseModel();
            }
        });
        //PatikaList
        patikaMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Güncelle");
        JMenuItem deleteMenu = new JMenuItem("Sil");
        patikaMenu.add(updateMenu);
        patikaMenu.add(deleteMenu);

        updateMenu.addActionListener(e -> {
            int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
            UpdatePatikaGUI updateGUI = new UpdatePatikaGUI(Patika.getFetch(select_id));
            updateGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadCourseModel();
                }
            });
        });
        deleteMenu.addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
                if (Patika.delete(select_id)) {
                    Helper.showMessage("done");
                    for (Course cou : Course.getList()) {
                        if (cou.getPatika_id() == select_id) {   // Course.delete(cou.getId());
                            for (Content con : Content.getList()) {
                                if (con.getCourse_id() == cou.getId()) {  //Content.delete(con.getId());
                                    for (Quiz q : Quiz.getList()) {
                                        if (q.getContent_id() == con.getId()) { //Quiz.delete(q.getId());
                                            for (AnswerQuiz ans : AnswerQuiz.getList()) {
                                                if (ans.getQuestion_id() == q.getId()) { //AnswerQuiz.delete(ans.getId());
                                                    AnswerQuiz.delete(ans.getId());

                                                }
                                            }
                                            Quiz.delete(q.getId());
                                        }
                                    }
                                    Content.delete(con.getId());
                                }
                            }
                            Course.delete(cou.getId());
                        }
                    }
                    Helper.showMessage("done");
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadCourseModel();
                } else {
                    Helper.showMessage("error");
                }
            }
        });

        mdl_patika_list = new DefaultTableModel();
        Object[] col_patika_list = {"ID", "Patika Adı"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];
        loadPatikaModel();

        tbl_patika_list.setModel(mdl_patika_list);
        tbl_patika_list.setComponentPopupMenu(patikaMenu);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false);
        tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(75);

        tbl_patika_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_patika_list.rowAtPoint(point);
                tbl_patika_list.setRowSelectionInterval(selected_row, selected_row);
            }
        });

        //Course List
        mdl_course_list = new DefaultTableModel();
        Object[] col_courseList = {"ID", "Ders Adı", "Programlama Dili", "Patika", "Eğitmen"};
        mdl_course_list.setColumnIdentifiers(col_courseList);
        row_course_list = new Object[col_courseList.length];
        loadCourseModel();
        tbl_course_list.setModel(mdl_course_list);
        tbl_course_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);
        loadPatikaCombo();
        loadEducatorCombo();

        mdl_content_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0)
                    return false;
                return super.isCellEditable(row, column);
            }
        };

        Object[] col_edu_content_list = {"ID", "İçerik Başlığı", "Açıklama", "Ders Adı", "YouTube Linki"};
        mdl_content_list.setColumnIdentifiers(col_edu_content_list);
        row_content_list = new Object[col_edu_content_list.length];
        loadContentModel();
        tbl_content_list.setModel(mdl_content_list);
        tbl_content_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_content_list.getTableHeader().setReorderingAllowed(false);
        loadCourseCombo();


        tbl_content_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String select_content_id = tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(), 0).toString();
                fld_content_id.setText(select_content_id);
            } catch (Exception exception) {

            }
        });
        mdl_quiz_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0)
                    return false;
                return super.isCellEditable(row, column);
            }
        };

        Object[] col_edu_quiz_list = {"ID", "Quiz Sorusu", "İçerik Başlığı", "İçerik ID"};
        mdl_quiz_list.setColumnIdentifiers(col_edu_quiz_list);
        row_quiz_list = new Object[col_edu_quiz_list.length];
        loadQuizModel();
        tbl_quiz_list.setModel(mdl_quiz_list);
        tbl_quiz_list.getColumnModel().getColumn(0).setMaxWidth(50);
        //tbl_edu_quiz_list.getColumnModel().getColumn(2).setMaxWidth(300);
        tbl_quiz_list.getColumnModel().getColumn(3).setMaxWidth(100);
        tbl_quiz_list.getTableHeader().setReorderingAllowed(false);
        loadContentCombo();

        tbl_quiz_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String select_quiz_id = tbl_quiz_list.getValueAt(tbl_quiz_list.getSelectedRow(), 0).toString();
                fld_quiz_id_del.setText(select_quiz_id);
            } catch (Exception exception) {

            }
        });

        tbl_quiz_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String select_content_id = tbl_quiz_list.getValueAt(tbl_quiz_list.getSelectedRow(), 0).toString();
                fld_content_id.setText(select_content_id);
            } catch (Exception exception) {

            }
        });

        btn_user_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_name) || Helper.isFieldEmpty(fld_user_uname) || Helper.isFieldEmpty(fld_user_pass)) {
                Helper.showMessage("fill");
            } else {
                String name = fld_user_name.getText();
                String uname = fld_user_uname.getText();
                String pass = fld_user_pass.getText();
                String type = cmb_user_type.getSelectedItem().toString();
                if (User.add(name, uname, pass, type)) {
                    Helper.showMessage("done");
                    loadUserModel();
                    loadEducatorCombo();
                    fld_user_name.setText(null);
                    fld_user_uname.setText(null);
                    fld_user_pass.setText(null);
                }
            }
        });
        btn_user_delete.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_id)) {
                Helper.showMessage("fill");
            } else {
                if (Helper.confirm("sure")) {
                    int user_id = Integer.parseInt(fld_user_id.getText());
                    if (User.delete(user_id)) {
                        Helper.showMessage("done");
                        loadUserModel();
                        loadEducatorCombo();
                        loadCourseModel();
                        fld_user_id.setText(null);
                    } else {
                        Helper.showMessage("error");
                    }
                }
            }
        });
        btn_sh_user.addActionListener(e -> {
            String name = fld_sh_user_name.getText();
            String uname = fld_sh_user_uname.getText();
            String type = cmb_sh_user_type.getSelectedItem().toString();
            String query = User.searchQuery(name, uname, type);
            ArrayList<User> searchingUser = User.searchUserList(query);
            loadUserModel(searchingUser);
        });
        btn_logout.addActionListener(e -> {
            dispose();
            LoginGUI login = new LoginGUI();
        });
        btn_patika_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_patika_name)) {
                Helper.showMessage("fill");
            } else {
                if (Patika.add(fld_patika_name.getText())) {
                    Helper.showMessage("done");
                    loadPatikaModel();
                    loadPatikaCombo();
                    fld_patika_name.setText(null);
                } else {
                    Helper.showMessage("error");
                }
            }
        });
        btn_course_add.addActionListener(e -> {
            Item patikaItem = (Item) cmb_course_patika.getSelectedItem();
            Item userItem = (Item) cmb_course_user.getSelectedItem();
            if (Helper.isFieldEmpty(fld_course_name) || Helper.isFieldEmpty(fld_course_lang)) {
                Helper.showMessage("fill");
            } else {
                if (Course.add(userItem.getKey(), patikaItem.getKey(), fld_course_name.getText(), fld_course_lang.getText())) {
                    Helper.showMessage("done");
                    loadCourseModel();
                    fld_course_lang.setText(null);
                    fld_course_name.setText(null);
                } else {
                    Helper.showMessage("error");
                }
            }
        });
        btn_course_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.isFieldEmpty(fld_course_id)) {
                    Helper.showMessage("fill");
                } else {
                    Helper.showMessage("delete");
                    if (Helper.confirm("sure")) {
                        int course_id = Integer.parseInt(fld_course_id.getText());
                        if (Course.delete(course_id)) {
                            Helper.showMessage("done");

                            for (Content c : Content.getList()) {
                                if (c.getCourse_id() == course_id) {
                                    for (Quiz q : Quiz.getList()) {
                                        if (q.getContent_id() == c.getId()) {
                                            Quiz.delete(q.getId());
                                            Content.delete(c.getId());
                                        }
                                    }
                                }
                            }

                            loadCourseModel();
                            fld_course_id.setText(null);
                        } else {
                            Helper.showMessage("error");
                        }
                    }
                }
            }
        });
        btn_content_add.addActionListener(e -> {
            Item courseItem = (Item) cmb_content_course.getSelectedItem();

            if (Helper.isFieldEmpty(fld_content_topic) || Helper.isFieldEmpty(fld_content_explanation) || Helper.isFieldEmpty(fld_ytube_url)){
                Helper.showMessage("fill");
            }
            else{
                if (Content.add(fld_content_topic.getText(), fld_content_explanation.getText(), courseItem.getKey(), fld_ytube_url.getText())){
                    Helper.showMessage("done");
                    loadContentModel();
                    fld_content_topic.setText(null);
                    fld_content_explanation.setText(null);
                    fld_ytube_url.setText(null);
                    loadContentCombo();
                }
                else {
                    Helper.showMessage("error");
                }
            }
        });
        btn_content_delete.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_content_id)){
                Helper.showMessage("fill");
            }
            else {
                Helper.showMessage("delete");
                if (Helper.confirm("sure")){
                    int content_id = Integer.parseInt(fld_content_id.getText());
                    if (Content.delete(content_id)){
                        Helper.showMessage("done");

                        for (Quiz q : Quiz.getList()){
                            if (q.getContent_id() == content_id){
                                Quiz.delete(q.getId());
                            }
                        }
                        loadContentModel();
                        fld_content_id.setText(null);
                        loadContentCombo();
                        loadQuizModel();
                    }
                    else{
                        Helper.showMessage("error");
                    }
                }
            }
        });
        btn_quiz_add.addActionListener(e -> {
            Item contentItem = (Item) cmb_quiz_content.getSelectedItem();
            if (Helper.isFieldEmpty(fld_quiz_question)){
                Helper.showMessage("fill");
            }
            else{
                if (Quiz.add(fld_quiz_question.getText(), contentItem.getValue(), contentItem.getKey())){
                    Helper.showMessage("done");
                    loadQuizModel();
                    fld_quiz_question.setText(null);
                }
                else {
                    Helper.showMessage("error");
                }
            }
        });
        btn_quiz_delete.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_quiz_id_del)){
                Helper.showMessage("fill");
            }
            else {
                if (Helper.confirm("sure")){
                    int quiz_id = Integer.parseInt(fld_quiz_id_del.getText());
                    if (Quiz.delete(quiz_id)){
                        Helper.showMessage("done");
                        loadQuizModel();
                        fld_quiz_id_del.setText(null);
                    }
                    else {
                        Helper.showMessage("error");
                    }
                }
            }
        });
    }

    private void loadContentCombo() {
        cmb_quiz_content.removeAllItems();
        for (Content obj : Content.getList()){
            cmb_quiz_content.addItem(new Item(obj.getId(), obj.getTopic()));
        }
    }

    private void loadQuizModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_quiz_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for (Quiz obj : Quiz.getList()){
            i = 0;
            row_quiz_list[i++] = obj.getId();
            row_quiz_list[i++] = obj.getQuestion();
            row_quiz_list[i++] = obj.getContent_topic();
            row_quiz_list[i++] = obj.getContent_id();
            mdl_quiz_list.addRow(row_quiz_list);

        }
    }

    private void loadCourseCombo() {
        cmb_content_course.removeAllItems();
        for (Course obj : Course.getList()){
            cmb_content_course.addItem(new Item(obj.getId(), obj.getName()));
        }
    }

    private void loadContentModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_content_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for (Content obj : Content.getList()){
            i=0;
            row_content_list[i++] = obj.getId();
            row_content_list[i++] = obj.getTopic();
            row_content_list[i++] = obj.getExp();
            row_content_list[i++] = obj.getCourse().getName();
            row_content_list[i++] = obj.getYtubeUrl();
            mdl_content_list.addRow(row_content_list);

        }
    }

    public void loadCourseModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);

        for(Course obj : Course.getList()) {
            int i = 0;
            row_course_list[i++] = obj.getId();
            row_course_list[i++] = obj.getName();
            row_course_list[i++] = obj.getLang();
            row_course_list[i++] = obj.getPatika().getName();
            row_course_list[i++] = obj.getEducator().getName();
            mdl_course_list.addRow(row_course_list);
        }
    }

    //PatikaModeList
    public void loadPatikaModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);

        for(Patika obj : Patika.getList()) {
            int i = 0;
            row_patika_list[i++] = obj.getId();
            row_patika_list[i++] = obj.getName();
            mdl_patika_list.addRow(row_patika_list);
        }
    }

    public void loadUserModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for(User obj : User.getList()) {
            int i = 0;
            row_user_list[i++]= obj.getId();
            row_user_list[i++]= obj.getName();
            row_user_list[i++]= obj.getUname();
            row_user_list[i++]= obj.getPass();
            row_user_list[i++]= obj.getType();
            mdl_user_list.addRow(row_user_list);
        }
    }
    public void loadUserModel(ArrayList<User> list) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for(User obj : list) {
            int i = 0;
            row_user_list[i++]= obj.getId();
            row_user_list[i++]= obj.getName();
            row_user_list[i++]= obj.getUname();
            row_user_list[i++]= obj.getPass();
            row_user_list[i++]= obj.getType();
            mdl_user_list.addRow(row_user_list);
        }
    }
    public void loadPatikaCombo() {
        cmb_course_patika.removeAllItems();
        for(Patika obj : Patika.getList()) {
            cmb_course_patika.addItem(new Item(obj.getId(),obj.getName()));
        }
    }
    public void loadEducatorCombo() {
        cmb_course_user.removeAllItems();
        for(User obj : User.getList()) {
            if(obj.getType().equals("educator")) {
                cmb_course_user.addItem(new Item(obj.getId(),obj.getName()));
            }
        }
    }
    public static void main(String[] args) {
        Helper.setLayout();
        Operator op = new Operator();
        op.setId(1);
        op.setName("Burçak Yıldırım");
        op.setUname("Burcak");
        op.setPass("1234");
        op.setType("operator");
        OperatorGUI operatorGUI = new OperatorGUI(op);
    }
}
