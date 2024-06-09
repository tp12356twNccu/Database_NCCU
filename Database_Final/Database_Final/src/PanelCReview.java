import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

public class PanelCReview extends JPanel {
    private JScrollPane sMain;
    private JPanel pContent;
    private JPanel pMain = new JPanel(new BorderLayout());
    private Connection conn;
    private Statement stat;

    public PanelCReview(Connection conn, String username) throws SQLException {
        this.conn = conn;
        this.stat = conn.createStatement();
        cComponent(username);
        cLayOut();
    }

    private void cComponent(String username) {
        pContent = new JPanel();
        pContent.setLayout(new BoxLayout(pContent, BoxLayout.Y_AXIS));

        for (int i = 0; i < FrameCustomer.barList.size(); i++) {
            JPanel pTemp = new JPanel(new BorderLayout());
            pTemp.setPreferredSize(new Dimension(700, 100));

            JLabel lblName = new JLabel(FrameCustomer.barList.get(i).getName());
            JLabel lblDistrict = new JLabel(FrameCustomer.barList.get(i).getDistrict());
            JLabel lblReview = new JLabel();

            JTextField ta_Review = new JTextField(20);

            JButton bt_Review = new JButton("Add review");

            JRadioButton rb_1 = new JRadioButton("1");
            JRadioButton rb_2 = new JRadioButton("2");
            JRadioButton rb_3 = new JRadioButton("3");
            JRadioButton rb_4 = new JRadioButton("4");
            JRadioButton rb_5 = new JRadioButton("5");
            ButtonGroup rbGroup = new ButtonGroup();
            rbGroup.add(rb_1); rbGroup.add(rb_2); rbGroup.add(rb_3); rbGroup.add(rb_4); rbGroup.add(rb_5);

            JButton bt_Rate = new JButton("Rate");
            bt_Rate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    int r = 0;

                    if (rb_1.isSelected()) {
                        r = 1;
                    } else if (rb_2.isSelected()) {
                        r = 2;
                    } else if (rb_3.isSelected()) {
                        r = 3;
                    } else if (rb_4.isSelected()) {
                        r = 4;
                    } else if (rb_5.isSelected()) {
                        r = 5;
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select your rate first!", "Select before Rating!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String queryCheck = "SELECT * FROM Review WHERE CustomerName = ? AND BarName = ?";
                    try (PreparedStatement pstmtCheck = conn.prepareStatement(queryCheck)) {
                        pstmtCheck.setString(1, username);
                        pstmtCheck.setString(2, lblName.getText());
                        ResultSet rs = pstmtCheck.executeQuery();

                        if (rs.next()) {
                            String queryUpdate = "UPDATE Review SET Rating = ? WHERE CustomerName = ? AND BarName = ?";
                            try (PreparedStatement pstmtUpdate = conn.prepareStatement(queryUpdate)) {
                                pstmtUpdate.setInt(1, r);
                                pstmtUpdate.setString(2, username);
                                pstmtUpdate.setString(3, lblName.getText());
                                pstmtUpdate.executeUpdate();
                            }
                        } else {
                            String queryInsert = "INSERT INTO Review (CustomerName, BarName, Rating) VALUES (?, ?, ?)";
                            try (PreparedStatement pstmtInsert = conn.prepareStatement(queryInsert)) {
                                pstmtInsert.setString(1, username);
                                pstmtInsert.setString(2, lblName.getText());
                                pstmtInsert.setInt(3, r);
                                pstmtInsert.executeUpdate();
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            bt_Review.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    String review = ta_Review.getText();
                    lblReview.setText(review);

                    String queryCheck = "SELECT * FROM Review WHERE CustomerName = ? AND BarName = ?";
                    try (PreparedStatement pstmtCheck = conn.prepareStatement(queryCheck)) {
                        pstmtCheck.setString(1, username);
                        pstmtCheck.setString(2, lblName.getText());
                        ResultSet rs = pstmtCheck.executeQuery();

                        if (rs.next()) {
                            String queryUpdate = "UPDATE Review SET Comment = ? WHERE CustomerName = ? AND BarName = ?";
                            try (PreparedStatement pstmtUpdate = conn.prepareStatement(queryUpdate)) {
                                pstmtUpdate.setString(1, review);
                                pstmtUpdate.setString(2, username);
                                pstmtUpdate.setString(3, lblName.getText());
                                pstmtUpdate.executeUpdate();
                            }
                        } else {
                            String queryInsert = "INSERT INTO Review (CustomerName, BarName, Comment) VALUES (?, ?, ?)";
                            try (PreparedStatement pstmtInsert = conn.prepareStatement(queryInsert)) {
                                pstmtInsert.setString(1, username);
                                pstmtInsert.setString(2, lblName.getText());
                                pstmtInsert.setString(3, review);
                                pstmtInsert.executeUpdate();
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            JPanel pRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
            JPanel pLeft = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            JPanel pCenter = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            JPanel pBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

            pRight.add(ta_Review);
            pRight.add(bt_Review);

            pLeft.add(lblName);
            pLeft.add(lblDistrict);

            pCenter.add(new JLabel("Your review: "));
            pCenter.add(lblReview);

            pBottom.add(new JLabel("Rating: "));
            pBottom.add(rb_1);
            pBottom.add(rb_2);
            pBottom.add(rb_3);
            pBottom.add(rb_4);
            pBottom.add(rb_5);
            pBottom.add(bt_Rate);

            pTemp.add(pLeft, BorderLayout.WEST);
            pTemp.add(pCenter, BorderLayout.CENTER);
            pTemp.add(pRight, BorderLayout.EAST);
            pTemp.add(pBottom, BorderLayout.SOUTH);

            pContent.add(pTemp);
        }

        sMain = new JScrollPane(pContent);
        pContent.setPreferredSize(new Dimension(700, FrameCustomer.barList.size() * 75));
        sMain.setPreferredSize(new Dimension(800, 500));
    }

    private void cLayOut() {
        pMain.add(sMain, BorderLayout.CENTER);
        add(pMain);
    }
}
