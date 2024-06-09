import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import java.util.ArrayList;

public class PanelCRecom extends JPanel{
    private JPanel pContent,pFirst,pSecond,pThird,pFirstSB,pSecondSB,pThirdSB;
    private JScrollPane sMain,sFirst,sSecond,sThird;
    private JLabel lbl1 = new JLabel(),lbl2 = new JLabel(),lbl3 = new JLabel();

    private ArrayList<BarObject> listFirst = new ArrayList<>(),listSecond = new ArrayList<>(),listThird = new ArrayList<>();

    private JPanel pMain = new JPanel(new BorderLayout());
    Connection conn;
    Statement stat;
    private PanelCLikedList pCLiked;
    private String server = "jdbc:mysql://140.119.19.73:3315/";
    private String database = "111306037"; // change to your own database
    private String url = server + database + "?useSSL=false";
    private String DBUsername = "111306037"; // change to your own user name
    private String DBPassword = "58g95"; // change to your own password
    
    public PanelCRecom(Connection conn,String username,PanelCLikedList pCLiked) throws SQLException {
        this.conn = conn;
        stat = conn.createStatement();
        this.pCLiked = pCLiked;
    	buildBarList(conn,username);
        cComponent(username);
        cLayOut();
    }

    private void cComponent(String username) {
        pFirst = new JPanel();
        pFirstSB = new JPanel();
        pFirstSB.setLayout(new BoxLayout(pFirstSB, BoxLayout.Y_AXIS));
        for (int i = 0;i < listFirst.size();i++) {
            JPanel pTemp = new JPanel(new BorderLayout());
            pTemp.setPreferredSize(new Dimension(400,100));
            
            JLabel lblName = new JLabel(listFirst.get(i).getName());
            JLabel lblDistrict = new JLabel(listFirst.get(i).getDistrict());
            
            String time = "OPEN TIME: " + (listFirst.get(i).getOpenTime()) + " ~ " + (listFirst.get(i).getCloseTime());
            JLabel lblTime = new JLabel(time);
            int j = i;
            JButton bt_AddFavor = new JButton("Add to Liked List");
            bt_AddFavor.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent event) {
            	    try (Connection conn = DriverManager.getConnection(url, DBUsername, DBPassword)) {
            	        String qr = "SELECT COUNT(*) AS count FROM Likedlist WHERE CustomerName = ? AND BarName = ?";
            	        PreparedStatement psCheck = conn.prepareStatement(qr);
            	        psCheck.setString(1, username);
            	        psCheck.setString(2, listFirst.get(j).getName());
            	        ResultSet rsCheck = psCheck.executeQuery();
            	        if (rsCheck.next()) {
            	            int count = rsCheck.getInt("count");
            	            if (count > 0) {
            	                System.out.println("Record already exists. Not inserting.");
            	                // 如果记录已经存在，则不执行插入操作
            	            } else {
            	                // 如果记录不存在，则执行插入操作
            	                String query1 = "INSERT INTO `Likedlist`(`LikedID`, `CustomerName`, `BarName`) VALUES (?, ?, ?)";
            	                try (PreparedStatement ps = conn.prepareStatement(query1)) {
            	                    String qrCount = "SELECT Count(BarName) AS count FROM Likedlist";
            	                    try (Statement stat = conn.createStatement();
            	                         ResultSet rs = stat.executeQuery(qrCount)) {
            	                        if (rs.next()) {
            	                            String sz = rs.getString("count");
            	                            int size = Integer.parseInt(sz);
            	                            ps.setInt(1, size + 1);
            	                        }
            	                    }
            	                    ps.setString(2, username);
            	                    ps.setString(3, listFirst.get(j).getName());
            	                    ps.executeUpdate();
            	                }
            	            }
            	        }
            	    } catch (SQLException e) {
            	        e.printStackTrace();
            	    }
            	}

            });

            JButton bt_Check = new JButton("Check reviews");
            bt_Check.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    //funtion
                	String query = "SELECT CustomerName, Rating, Comment, Reply FROM Review WHERE BarName = '" + listFirst.get(j).getName() + "'";
                    try (ResultSet result = stat.executeQuery(query)) {
                        StringBuilder reviews = new StringBuilder();
                        while (result.next()) {
                            String cus = result.getString("CustomerName");
                            int rat = result.getInt("Rating");
                            String com = result.getString("Comment");
                            String rep = result.getString("Reply");
                            
                            // 拼接評論數據到字符串中
                            reviews.append("Customer: ").append(cus).append("\n")
                                   .append("Rating: ").append(rat).append("\n")
                                   .append("Comment: ").append(com).append("\n")
                                   .append("Reply: ").append(rep).append("\n\n");
                        }
                        
                        // 如果有評論數據，顯示對話框
                        if (reviews.length() > 0) {
                            JOptionPane.showMessageDialog(bt_AddFavor, reviews.toString(), "Reviews", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(bt_AddFavor, "No reviews found for this bar.", "Reviews", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            JPanel pRight = new JPanel(new FlowLayout(FlowLayout.RIGHT,20,0));
            JPanel pLeft = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            JPanel pCenter = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            
            pRight.add(bt_AddFavor);
            pRight.add(bt_Check);

            pLeft.add(lblName);
            pLeft.add(lblDistrict);
            
            pCenter.add(lblTime);

            pTemp.add(pLeft, BorderLayout.WEST);
            pTemp.add(pCenter, BorderLayout.CENTER);
            pTemp.add(pRight, BorderLayout.EAST);

            pFirstSB.add(pTemp);
        }
        
        sFirst = new JScrollPane(pFirstSB);
        pFirstSB.setPreferredSize(new Dimension(500,listFirst.size()*75));
        sFirst.setPreferredSize(new Dimension(600,200));
        pFirst.add(sFirst);


        pSecond = new JPanel();
        pSecondSB = new JPanel();
        pSecondSB.setLayout(new BoxLayout(pSecondSB, BoxLayout.Y_AXIS));
        for (int i = 0;i < listSecond.size();i++) {
            JPanel pTemp = new JPanel(new BorderLayout());
            pTemp.setPreferredSize(new Dimension(400,100));
            
            JLabel lblName = new JLabel(listSecond.get(i).getName());
            JLabel lblDistrict = new JLabel(listSecond.get(i).getDistrict());
            
            String time = "OPEN TIME: " + (listSecond.get(i).getOpenTime()) + " ~ " + (listSecond.get(i).getCloseTime());
            JLabel lblTime = new JLabel(time);
            int j = i;
            JButton bt_AddFavor = new JButton("Add to Liked List");
            bt_AddFavor.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent event) {
            	    try (Connection conn = DriverManager.getConnection(url, DBUsername, DBPassword)) {
            	        String queryCheck = "SELECT COUNT(*) AS count FROM Likedlist WHERE CustomerName = ? AND BarName = ?";
            	        PreparedStatement psCheck = conn.prepareStatement(queryCheck);
            	        psCheck.setString(1, username);
            	        psCheck.setString(2, listSecond.get(j).getName());
            	        ResultSet rsCheck = psCheck.executeQuery();
            	        if (rsCheck.next()) {
            	            int count = rsCheck.getInt("count");
            	            if (count > 0) {
            	                System.out.println("Record already exists. Not inserting.");
            	                // 如果记录已经存在，则不执行插入操作
            	            } else {
            	                // 如果记录不存在，则执行插入操作
            	                String qr = "SELECT Count(BarName) AS count FROM Likedlist";
            	                try (Statement stat = conn.createStatement();
            	                     ResultSet rs = stat.executeQuery(qr)) {
            	                    if (rs.next()) {
            	                        String sz = rs.getString("count");
            	                        int size = Integer.parseInt(sz);
            	                        String query2 = "INSERT INTO `Likedlist`(`LikedID`, `CustomerName`, `BarName`) VALUES (?, ?, ?)";
            	                        try (PreparedStatement ps = conn.prepareStatement(query2)) {
            	                            ps.setInt(1, size + 1);
            	                            ps.setString(2, username);
            	                            ps.setString(3, listSecond.get(j).getName());
            	                            ps.executeUpdate();
            	                        }
            	                    }
            	                } catch (SQLException e) {
            	                    e.printStackTrace();
            	                }
            	            }
            	        }
            	    } catch (SQLException e) {
            	        e.printStackTrace();
            	    }
            	}

            });

            JButton bt_Check = new JButton("Check reviews");
            bt_Check.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    //funtion
                	String query = "SELECT CustomerName, Rating, Comment, Reply FROM Review WHERE BarName = '" + listSecond.get(j).getName() + "'";
                    try (ResultSet result = stat.executeQuery(query)) {
                        StringBuilder reviews = new StringBuilder();
                        while (result.next()) {
                            String cus = result.getString("CustomerName");
                            int rat = result.getInt("Rating");
                            String com = result.getString("Comment");
                            String rep = result.getString("Reply");
                            
                            // 拼接評論數據到字符串中
                            reviews.append("Customer: ").append(cus).append("\n")
                                   .append("Rating: ").append(rat).append("\n")
                                   .append("Comment: ").append(com).append("\n")
                                   .append("Reply: ").append(rep).append("\n\n");
                        }
                        
                        // 如果有評論數據，顯示對話框
                        if (reviews.length() > 0) {
                            JOptionPane.showMessageDialog(bt_AddFavor, reviews.toString(), "Reviews", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(bt_AddFavor, "No reviews found for this bar.", "Reviews", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            JPanel pRight = new JPanel(new FlowLayout(FlowLayout.RIGHT,20,0));
            JPanel pLeft = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            JPanel pCenter = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            
            pRight.add(bt_AddFavor);
            pRight.add(bt_Check);

            pLeft.add(lblName);
            pLeft.add(lblDistrict);
            
            pCenter.add(lblTime);

            pTemp.add(pLeft, BorderLayout.WEST);
            pTemp.add(pCenter, BorderLayout.CENTER);
            pTemp.add(pRight, BorderLayout.EAST);

            pSecondSB.add(pTemp);
        }
        
        sSecond = new JScrollPane(pSecondSB);
        pSecondSB.setPreferredSize(new Dimension(500,listSecond.size()*75));
        sSecond.setPreferredSize(new Dimension(600,200));
        pSecond.add(sSecond);


        pThird = new JPanel();
        pThirdSB = new JPanel();
        pThirdSB.setLayout(new BoxLayout(pThirdSB, BoxLayout.Y_AXIS));
        for (int i = 0;i < listThird.size();i++) {
            JPanel pTemp = new JPanel(new BorderLayout());
            
            JLabel lblName = new JLabel(listThird.get(i).getName());
            JLabel lblDistrict = new JLabel(listThird.get(i).getDistrict());
            
            String time = "OPEN TIME: " + (listThird.get(i).getOpenTime()) + " ~ " + (listThird.get(i).getCloseTime());
            JLabel lblTime = new JLabel(time);
            int j = i;
            JButton bt_AddFavor = new JButton("Add to Liked List");
            bt_AddFavor.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent event) {
            	    try (Connection conn = DriverManager.getConnection(url, DBUsername, DBPassword)) {
            	        String qr = "SELECT COUNT(*) AS count FROM Likedlist WHERE CustomerName = ? AND BarName = ?";
            	        PreparedStatement psCheck = conn.prepareStatement(qr);
            	        psCheck.setString(1, username);
            	        psCheck.setString(2, listThird.get(j).getName());
            	        ResultSet rsCheck = psCheck.executeQuery();
            	        if (rsCheck.next()) {
            	            int count = rsCheck.getInt("count");
            	            if (count > 0) {
            	                System.out.println("Record already exists. Not inserting.");
            	                // 如果记录已经存在，则不执行插入操作
            	            } else {
            	                // 如果记录不存在，则执行插入操作
            	                String query3 = "INSERT INTO `Likedlist`(`LikedID`, `CustomerName`, `BarName`) VALUES (?, ?, ?)";
            	                try (PreparedStatement ps = conn.prepareStatement(query3)) {
            	                    String qrCount = "SELECT Count(BarName) AS count FROM Likedlist";
            	                    try (Statement stat = conn.createStatement();
            	                         ResultSet rs = stat.executeQuery(qrCount)) {
            	                        if (rs.next()) {
            	                            String sz = rs.getString("count");
            	                            int size = Integer.parseInt(sz);
            	                            ps.setInt(1, size + 1);
            	                        }
            	                    }
            	                    ps.setString(2, username);
            	                    ps.setString(3, listThird.get(j).getName());
            	                    ps.executeUpdate();
            	                }
            	            }
            	        }
            	    } catch (SQLException e) {
            	        e.printStackTrace();
            	    }
            	}

            });

            JButton bt_Check = new JButton("Check reviews");
            bt_Check.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent event) {
                    // 構建 SQL 查詢以獲取評論數據
                    String query = "SELECT CustomerName, Rating, Comment, Reply FROM Review WHERE BarName = '" + listThird.get(j).getName() + "'";
                    try (ResultSet result = stat.executeQuery(query)) {
                        StringBuilder reviews = new StringBuilder();
                        while (result.next()) {
                            String cus = result.getString("CustomerName");
                            int rat = result.getInt("Rating");
                            String com = result.getString("Comment");
                            String rep = result.getString("Reply");
                            
                            // 拼接評論數據到字符串中
                            reviews.append("Customer: ").append(cus).append("\n")
                                   .append("Rating: ").append(rat).append("\n")
                                   .append("Comment: ").append(com).append("\n")
                                   .append("Reply: ").append(rep).append("\n\n");
                        }
                        
                        // 如果有評論數據，顯示對話框
                        if (reviews.length() > 0) {
                            JOptionPane.showMessageDialog(bt_AddFavor, reviews.toString(), "Reviews", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(bt_AddFavor, "No reviews found for this bar.", "Reviews", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            JPanel pRight = new JPanel(new FlowLayout(FlowLayout.RIGHT,20,0));
            JPanel pLeft = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            JPanel pCenter = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            
            pRight.add(bt_AddFavor);
            pRight.add(bt_Check);

            pLeft.add(lblName);
            pLeft.add(lblDistrict);
            
            pCenter.add(lblTime);

            pTemp.add(pLeft, BorderLayout.WEST);
            pTemp.add(pCenter, BorderLayout.CENTER);
            pTemp.add(pRight, BorderLayout.EAST);

            pThirdSB.add(pTemp);
        }
        
        sThird = new JScrollPane(pThirdSB);
        pThirdSB.setPreferredSize(new Dimension(500,listThird.size()*75));
        sThird.setPreferredSize(new Dimension(600,200));
        pThird.add(sThird);

        pContent = new JPanel();
        pContent.setLayout(new BoxLayout(pContent, BoxLayout.Y_AXIS));
        pContent.add(lbl1);
        pContent.add(pFirst);
        pContent.add(lbl2);
        pContent.add(pSecond);
        pContent.add(lbl3);
        pContent.add(pThird);
        pContent.setPreferredSize(new Dimension(700,800));

        sMain = new JScrollPane(pContent);
        sMain.setPreferredSize(new Dimension(800,500));

    
        lbl1.setFont(new Font("Monospaced", Font.BOLD, 25));
        lbl1.setText("***");
        lbl2.setFont(new Font("Monospaced", Font.BOLD, 25));
        lbl2.setText("**");
        lbl3.setFont(new Font("Monospaced", Font.BOLD, 25));
        lbl3.setText("*");
    
        
    }

    private void cLayOut() {
        pMain.add(sMain);

        add(pMain);
    }

    private void buildBarList(Connection conn, String username) {
        //TODO: build 3 list with FrameCustomer.barList in corresponding method
        //following code is only for testing and avoiding exceptions

        String query1 = "SELECT DISTINCT db.BarName " +
                        "FROM Bar db " + // Add space after "Bar db"
                        "JOIN Customer dc ON db.TA = dc.Occupation " +
                        "AND db.Featured_Activity = dc.Preferred_Activity " +
                        "WHERE dc.Name = ?";

        String query2 = "SELECT DISTINCT db.BarName " +
                        "FROM Bar db " + // Add space after "Bar db"
                        "JOIN Customer dc ON (db.TA = dc.Occupation " +
                        "OR db.Featured_Activity = dc.Preferred_Activity) " +
                        "WHERE dc.Name = ? " +
                        "AND db.BarName NOT IN (" +
                        "  SELECT db2.BarName " + // Change "Name" to "BarName"
                        "  FROM Bar db2 " +
                        "  JOIN Customer dc2 ON db2.TA = dc2.Occupation " +
                        "  AND db2.Featured_Activity = dc2.Preferred_Activity " +
                        "  WHERE dc2.Name = ?)";

        try {
            // Use PreparedStatement to avoid SQL injection and handle parameters correctly
            PreparedStatement stat1 = conn.prepareStatement(query1);
            stat1.setString(1, username);
            ResultSet result1 = stat1.executeQuery();
            ArrayList<BarObject> bar1 = new ArrayList<>();
            while (result1.next()) {
                String barName = result1.getString("BarName"); // Use "BarName" instead of "Name"
                for (BarObject bar : FrameCustomer.barList) {
                    if (bar.getName().equals(barName)) {
                        bar1.add(bar);
                        break;
                    }
                }
            }
             // Close result1 after processing

            if (bar1.isEmpty()) {
                BarObject noBar = new BarObject("null", "null", "null", "null", "null");
                listFirst.add(noBar); // Assuming listFirst is in FrameCustomer
            } else {
                listFirst.addAll(bar1); // Assuming listFirst is in FrameCustomer
            }

            PreparedStatement stat2 = conn.prepareStatement(query2);
            stat2.setString(1, username);
            stat2.setString(2, username); // Set the second parameter
            ResultSet result2 = stat2.executeQuery();
            ArrayList<BarObject> bars2 = new ArrayList<>();
            while (result2.next()) {
                String barName = result2.getString("BarName"); // Use "BarName" instead of "Name"
                for (BarObject bar : FrameCustomer.barList) {
                    if (bar.getName().equals(barName)) {
                        bars2.add(bar);
                        break;
                    }
                }
            }
            // Close result2 after processing

            if (bars2.isEmpty()) {
                BarObject noBar = new BarObject("null", "null", "null", "null", "null");
                listSecond.add(noBar); // Assuming listSecond is in FrameCustomer
            } else {
                listSecond.addAll(bars2); // Assuming listSecond is in FrameCustomer
            }

            // Close statements after all operations
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    	
    	
        //listFirst.add(FrameCustomer.barList.get(0));
        //listFirst.add(FrameCustomer.barList.get(1));
        //listFirst.add(FrameCustomer.barList.get(3));
        //listFirst.add(FrameCustomer.barList.get(5));

        //listSecond.add(FrameCustomer.barList.get(2));
        //listSecond.add(FrameCustomer.barList.get(3));

        //listThird.add(FrameCustomer.barList.get(4));
        //listThird.add(FrameCustomer.barList.get(5));
    }