import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) throws SQLException {
    	String server = "jdbc:mysql://140.119.19.73:3315/";
        String database = "111306037"; // 改为你的数据库名
        String url = server + database + "?useSSL=false";
        String username = "111306037"; // 改为你的用户名
        String password = "58g95"; // 改为你的密码

        
        EventQueue.invokeLater(() -> {
            try {
                Connection conn = DriverManager.getConnection(url, username, password);
                System.out.println("User Connected");
                FrameLogin frame1 = new FrameLogin(conn);
                frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame1.setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    
    
}