import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import java.util.ArrayList;

public class FrameCustomer extends JFrame {
    private JButton bt_Recom, bt_Filter, bt_Review, bt_LikedList, bt_PlaceHolder, bt_LogOut;
    private ArrayList<JButton> btList = new ArrayList<>();
    private JPanel pWestbtGroup;

    private int control = 0;
    private JPanel pMain = new JPanel();

    public static ArrayList<BarObject> barList = new ArrayList<>();
    private boolean build = true;

    Connection conn;
    Statement stat;
    String server = "jdbc:mysql://140.119.19.73:3315/";
    String database = "111306037"; // 改为你的数据库名
    String url = server + database + "?useSSL=false";
    String dbUsername = "111306037"; // 改为你的用户名
    String password = "58g95"; // 改为你的密码
    PanelCLikedList pc;

    public FrameCustomer(Connection conn, String userName) throws SQLException {
        this.conn = DriverManager.getConnection(url, dbUsername, password);
        this.conn = conn;
        stat = conn.createStatement();
        buildBarList();
        cButton(userName);
        cComponent();
        this.pc = new PanelCLikedList(userName, conn); // 确保传入的用户名是正确的
        
        setTitle("Customer");
        setSize(1000, 618);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().add(pWestbtGroup, BorderLayout.WEST);
        getContentPane().add(pMain, BorderLayout.CENTER);

        cLayOut(userName);
        setVisible(true);
    }

    private void cComponent() {
        pWestbtGroup = new JPanel();
        pWestbtGroup.setLayout(new GridLayout(6, 1));
        for (JButton bt : btList) {
            pWestbtGroup.add(bt);
        }
    }

    private void cButton(String username) {
        bt_Recom = new JButton("Recommendate");
        bt_Recom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                control = 0;
                try {
                    cLayOut(username);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        bt_Filter = new JButton("Filter");
        bt_Filter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                control = 1;
                try {
                    cLayOut(username);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        bt_Review = new JButton("Review & Rate");
        bt_Review.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                control = 2;
                try {
                    cLayOut(username);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        bt_LikedList = new JButton("Liked List");
        bt_LikedList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                control = 3;
                try {
                    cLayOut(username);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        bt_PlaceHolder = new JButton();

        bt_LogOut = new JButton("Log out");
        bt_LogOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                int reply = JOptionPane.showConfirmDialog(pMain, "Log out?", "Logging out", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    try {
                        new FrameLogin(conn);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    setVisible(false);
                } else {
                    return;
                }
            }
        });

        btList.add(bt_Recom);
        btList.add(bt_Filter);
        btList.add(bt_Review);
        btList.add(bt_LikedList);
        btList.add(bt_PlaceHolder);
        btList.add(bt_LogOut);
    }

    private void cLayOut(String username) throws SQLException {
        pMain.removeAll();
        btResetAll();

        switch (control) {
            case 0:
                if (conn.isClosed()) {
                    conn = DriverManager.getConnection(url, dbUsername, password);
                }
                JPanel panelCRecom = new PanelCRecom(conn, username, pc);
                pMain.add(panelCRecom, BorderLayout.CENTER);
                bt_Recom.setEnabled(false);
                break;
            case 1:
                if (conn.isClosed()) {
                    conn = DriverManager.getConnection(url, dbUsername, password);
                }
                JPanel panelCFilter = new PanelCFilter(conn, pc, username);
                pMain.add(panelCFilter, BorderLayout.CENTER);
                bt_Filter.setEnabled(false);
                break;
            case 2:
                if (conn.isClosed()) {
                    conn = DriverManager.getConnection(url, dbUsername, password);
                }
                JPanel panelCReview = new PanelCReview(conn, username);
                pMain.add(panelCReview, BorderLayout.CENTER);
                bt_Review.setEnabled(false);
                break;
            case 3:
                if (conn.isClosed()) {
                    conn = DriverManager.getConnection(url, dbUsername, password);
                }
                // 使用已存在的PanelCLikedList对象
                pMain.add(pc, BorderLayout.CENTER);
                bt_LikedList.setEnabled(false);
                break;
        }

        pMain.revalidate();
        pMain.repaint();
    }

    private void btResetAll() {
        for (JButton bt : btList) {
            bt.setEnabled(true);
        }
        bt_LogOut.setBackground(new Color(250, 210, 210));
        bt_PlaceHolder.setEnabled(false);
    }

    private void buildBarList() throws SQLException {
        if (build) {
            String sql = "SELECT BarName, Style, District, OpenTime, CloseTime FROM Bar";
            try (ResultSet rs = stat.executeQuery(sql)) {
                while (rs.next()) {
                    String name = rs.getString("BarName");
                    String style = rs.getString("Style");
                    String district = rs.getString("District");
                    String openTime = rs.getString("OpenTime");
                    String closeTime = rs.getString("CloseTime");

                    BarObject barObject = new BarObject(name, style, district, openTime, closeTime);
                    FrameCustomer.barList.add(barObject);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            build = false;
        }
    }
}
