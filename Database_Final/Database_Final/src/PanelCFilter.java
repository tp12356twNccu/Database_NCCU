import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import java.util.ArrayList; 

public class PanelCFilter extends JPanel {
    private JButton bt_style,bt_district,bt_open,bt_close;
    private JTextField tf_style = new JTextField(15),tf_district = new JTextField(15),tf_open = new JTextField(15),tf_close = new JTextField(15); 
    private JPanel pNorthbtGroup,pStyle,pDistrict,pOpen,pClose,pContent;
    private JScrollPane sMain;
    
    private ArrayList<BarObject> listSort = new ArrayList<>();

    private JPanel pMain = new JPanel(new BorderLayout(0, 0));
    Connection conn;
    Statement stat;
    private String server = "jdbc:mysql://140.119.19.73:3315/";
    private String database = "111306037"; // change to your own database
    private String url = server + database + "?useSSL=false";
    private String DBUsername = "111306037"; // change to your own user name
    private String DBPassword = "58g95"; // change to your own password
    private PanelCLikedList pc;
    public PanelCFilter(Connection conn,PanelCLikedList pc,String username) throws SQLException{
        this.conn = conn;
        this.stat = conn.createStatement();
        this.pc = pc;
    	cButton(username);
        cComponent();
        cLayOut();
    }

    private void cComponent() {
        pStyle = new JPanel(new FlowLayout());
        pStyle.add(new JLabel("Sort by style: "));
        pStyle.add(tf_style);
        pStyle.add(bt_style);
        pStyle.setPreferredSize(new Dimension(200,50));
        
        pDistrict = new JPanel(new FlowLayout());
        pDistrict.add(new JLabel("Sort by district: "));
        pDistrict.add(tf_district);
        pDistrict.add(bt_district);
        pDistrict.setPreferredSize(new Dimension(200,50));

        pOpen = new JPanel(new FlowLayout());
        pOpen.add(new JLabel("Sort by opentime: "));
        pOpen.add(tf_open);
        pOpen.add(bt_open);
        pOpen.setPreferredSize(new Dimension(200,50));

        pClose = new JPanel(new FlowLayout());
        pClose.add(new JLabel("Sort by closetime: "));
        pClose.add(tf_close);
        pClose.add(bt_close);
        pClose.setPreferredSize(new Dimension(200,50));

        pNorthbtGroup = new JPanel(new GridLayout(2,2));
        pNorthbtGroup.add(pStyle);pNorthbtGroup.add(pDistrict);
        pNorthbtGroup.add(pOpen);pNorthbtGroup.add(pClose);
        pNorthbtGroup.setPreferredSize(new Dimension(800,100));

        pContent = new JPanel();
        pContent.setLayout(new BoxLayout(pContent, BoxLayout.Y_AXIS));
        
        sMain = new JScrollPane(pContent);
        pContent.setPreferredSize(new Dimension(700,listSort.size()*75));
        sMain.setPreferredSize(new Dimension(800,450));
    }

    private void cButton(String username) throws SQLException{
        bt_style = new JButton("Sort");
        bt_style.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String style = tf_style.getText();
                
                
                
					try {
						buildBarList(0,style);
						cList(username);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
                //funtion
                
                
               
            }
        });

        bt_district = new JButton("Sort");
        bt_district.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String district = tf_district.getText();
                
					try {
						buildBarList(1,district);
						cList(username);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
                //funtion
                
            }
        });

        bt_open = new JButton("Sort");
        bt_open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String open = tf_open.getText();
                
					try {
						buildBarList(2,open);
						cList(username);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
                //funtion
               
            }
        });

        bt_close = new JButton("Sort");
        bt_close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String close = tf_close.getText();
                
				try {
					buildBarList(3,close);
					cList(username);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
                //funtion
                
            }
        });
    }

    private void cLayOut() {
        pMain.add(pNorthbtGroup,BorderLayout.NORTH);
        pMain.add(sMain,BorderLayout.CENTER);
        
        add(pMain);
    }

    private void buildBarList(int i,String sort) throws SQLException {
        listSort.clear();
        if(i == 0) {
        	String query1 = "SELECT BarName,Style,District,OpenTime,CloseTime FROM Bar WHERE Bar.Style = '" + sort + "'";
        	ResultSet rs1;
    		
    			rs1 = stat.executeQuery(query1);
    				while(rs1.next()) {
    					String name = rs1.getString("BarName");
            	        String sty = rs1.getString("Style");
            	        String dis = rs1.getString("District");
            	        String opt = rs1.getString("OpenTime");
            	        String clot = rs1.getString("CloseTime");
            	        BarObject barObject = new BarObject(name, sty, dis, opt, clot);
            	        listSort.add(barObject);
                }
    		
        }else if(i == 1) {
        	String query1 = "SELECT BarName,Style,District,OpenTime,CloseTime FROM Bar WHERE Bar.District = '" + sort + "'";
        	ResultSet rs1;
    		
    			rs1 = stat.executeQuery(query1);
    				while(rs1.next()) {
    					String name = rs1.getString("BarName");
            	        String sty = rs1.getString("Style");
            	        String dis = rs1.getString("District");
            	        String opt = rs1.getString("OpenTime");
            	        String clot = rs1.getString("CloseTime");
            	        BarObject barObject = new BarObject(name, sty, dis, opt, clot);
            	        listSort.add(barObject);
                }
    		
        }else if(i == 2) {
        	String query1 = "SELECT BarName,Style,District,OpenTime,CloseTime FROM Bar WHERE Bar.OpenTime = '" + sort + "'";
        	ResultSet rs1 = stat.executeQuery(query1);
    		
    				while(rs1.next()) {
    					String name = rs1.getString("BarName");
            	        String sty = rs1.getString("Style");
            	        String dis = rs1.getString("District");
            	        String opt = rs1.getString("OpenTime");
            	        String clot = rs1.getString("CloseTime");
            	        BarObject barObject = new BarObject(name, sty, dis, opt, clot);
            	        listSort.add(barObject);
                }
    		
        }else if(i == 3) {
        	String query1 = "SELECT BarName,Style,District,OpenTime,CloseTime FROM Bar WHERE Bar.CloseTime = '" + sort + "'";
        	ResultSet rs1;
    		
    			rs1 = stat.executeQuery(query1);
    				while(rs1.next()) {
    					String name = rs1.getString("BarName");
            	        String sty = rs1.getString("Style");
            	        String dis = rs1.getString("District");
            	        String opt = rs1.getString("OpenTime");
            	        String clot = rs1.getString("CloseTime");
            	        BarObject barObject = new BarObject(name, sty, dis, opt, clot);
            	        listSort.add(barObject);
                }
    		
        }
        
       
        //listSort.add(FrameCustomer.barList.get(1));
        //listSort.add(FrameCustomer.barList.get(2));
        //listSort.add(FrameCustomer.barList.get(3));
    }

    private void cList(String username) {
        pContent.removeAll();

        for (int i = 0;i < listSort.size();i++) {
            JPanel pTemp = new JPanel(new BorderLayout());
            pTemp.setPreferredSize(new Dimension(700,100));

            JLabel lblName = new JLabel(listSort.get(i).getName());
            JLabel lblDistrict = new JLabel(listSort.get(i).getDistrict());
            int j = i;
            JButton bt_AddFavor = new JButton("Add to favorite");
            JButton bt_Check = new JButton("Check Reciews");
            bt_AddFavor.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    String barName = listSort.get(j).getName();
                    try (Connection conn = DriverManager.getConnection(url, DBUsername, DBPassword)) {
                        // 構建 SQL 查詢以檢查是否已存在相同的記錄
                        String checkQuery = "SELECT COUNT(*) AS count FROM Likedlist WHERE CustomerName = ? AND BarName = ?";
                        try (PreparedStatement psCheck = conn.prepareStatement(checkQuery)) {
                            psCheck.setString(1, username);
                            psCheck.setString(2, barName);
                            try (ResultSet rsCheck = psCheck.executeQuery()) {
                                if (rsCheck.next()) {
                                    int count = rsCheck.getInt("count");
                                    if (count > 0) {
                                        System.out.println("Record already exists. Not inserting.");
                                        // 如果记录已经存在，则不执行插入操作
                                    } else {
                                        // 如果记录不存在，则执行插入操作
                                        String insertQuery = "INSERT INTO `Likedlist`(`LikedID`, `CustomerName`, `BarName`) VALUES (?, ?, ?)";
                                        try (PreparedStatement psInsert = conn.prepareStatement(insertQuery)) {
                                            // 獲取目前已有的記錄數量，以便計算新的 LikedID
                                            String countQuery = "SELECT COUNT(BarName) AS count FROM Likedlist";
                                            try (Statement stat = conn.createStatement();
                                                 ResultSet rs = stat.executeQuery(countQuery)) {
                                                if (rs.next()) {
                                                    int size = rs.getInt("count");
                                                    psInsert.setInt(1, size + 1);
                                                }
                                            }
                                            psInsert.setString(2, username);
                                            psInsert.setString(3, barName);
                                            psInsert.executeUpdate();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            bt_Check.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                	String query = "SELECT CustomerName, Rating, Comment, Reply FROM Review WHERE BarName = '" + listSort.get(j).getName() + "'";
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
            
            pRight.add(bt_AddFavor);
            pRight.add(bt_Check);

            pLeft.add(lblName);
            pLeft.add(lblDistrict);

            pTemp.add(pLeft, BorderLayout.WEST);
            pTemp.add(pRight, BorderLayout.EAST);

            pContent.add(pTemp);
        }

        pContent.revalidate();
        pContent.repaint();
    }

}