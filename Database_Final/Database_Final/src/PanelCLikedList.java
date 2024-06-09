import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import java.util.ArrayList;

public class PanelCLikedList extends JPanel{
    private JPanel pContent;
    private JScrollPane sMain;

    private ArrayList<BarObject> listLiked = new ArrayList<>();
    private String server = "jdbc:mysql://140.119.19.73:3315/";
    private String database = "111306037"; // change to your own database
    private String url = server + database + "?useSSL=false";
    private String DBUsername = "111306037"; // change to your own user name
    private String DBPassword = "58g95"; // change to your own password

    private JPanel pMain = new JPanel(new BorderLayout());
    Connection conn;
    Statement stat;
    public PanelCLikedList(String username,Connection conn)throws SQLException {
        this.conn = conn;
        stat = conn.createStatement();
        buildBarList(username);
        cComponent();
        cLayOut();
    }

    private void cComponent() {
        pContent = new JPanel();
        pContent.setLayout(new BoxLayout(pContent, BoxLayout.Y_AXIS));

        updateContent(); // Initial population of the content

        sMain = new JScrollPane(pContent);
        pContent.setPreferredSize(new Dimension(700, listLiked.size() * 75));
        sMain.setPreferredSize(new Dimension(800, 500));
    }

    private void updateContent() {
        pContent.removeAll(); // Clear existing content

        for (int i = 0; i < listLiked.size(); i++) {
            JPanel pTemp = new JPanel(new BorderLayout());
            pTemp.setPreferredSize(new Dimension(400, 100));

            JLabel lblName = new JLabel(listLiked.get(i).getName());
            JLabel lblDistrict = new JLabel(listLiked.get(i).getDistrict());

            JButton bt_Remove = new JButton("Remove from Liked List");
            int index = i;
            bt_Remove.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    int selectedIndex = listLiked.indexOf(listLiked.get(index));
                    if (selectedIndex != -1) {
                        
                        String query = "DELETE FROM Likedlist WHERE BarName = '" + listLiked.get(selectedIndex).getName() + "'";
                        listLiked.remove(selectedIndex);
                        try {
                            Connection con = DriverManager.getConnection(url, DBUsername, DBPassword);
                            Statement stat = con.createStatement();
                            stat.execute(query);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        // Update the content
                        updateContent();
                        pContent.revalidate();
                        pContent.repaint();
                    }
                }
            });


            JPanel pRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
            JPanel pLeft = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));

            pRight.add(bt_Remove);

            pLeft.add(lblName);
            pLeft.add(lblDistrict);

            pTemp.add(pLeft, BorderLayout.WEST);
            pTemp.add(pRight, BorderLayout.EAST);

            pContent.add(pTemp);
        }

        // Update the preferred size based on the new number of items
        pContent.setPreferredSize(new Dimension(700, listLiked.size() * 75));
    }


    private void cLayOut() {
        pMain.add(sMain);

        add(pMain);
    }
    
    

    
    private void buildBarList(String username) throws SQLException {
        String qr = "SELECT Count(BarName) AS count From Bar";
        ResultSet rs = stat.executeQuery(qr);
        int size = 0;
        if (rs.next()) {
            size = rs.getInt("count");
        }
        for(int i = 1; i <= size; i++) {
            String query = "SELECT BarName FROM Likedlist WHERE LikedID = " + i + " AND CustomerName = '" + username + "'";
            ResultSet result = stat.executeQuery(query);
            
            if (result.next()) {
                String barname = result.getString("BarName");
                String query2 = "SELECT BarName,Style,District,OpenTime,CloseTime FROM Bar WHERE BarName = '" + barname + "'";
                ResultSet result2 = stat.executeQuery(query2);
                if (result2.next()) {
                    String sty = result2.getString("Style");
                    String dis = result2.getString("District");
                    String Op = result2.getString("OpenTime");
                    String Clo = result2.getString("CloseTime");
                    BarObject bar = new BarObject(barname,sty,dis,Op,Clo);
                    listLiked.add(bar);  
                }
            }
        }
    }


    	//listLiked.add(FrameCustomer.barList.get(1));
        //listLiked.add(FrameCustomer.barList.get(2));
        //listLiked.add(FrameCustomer.barList.get(3));
    
    
    
}