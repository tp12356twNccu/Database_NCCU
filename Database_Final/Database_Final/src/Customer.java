import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {
    private String server = "jdbc:mysql://140.119.19.73:3315/";
    private String database = "111306037"; // change to your own database
    private String url = server + database + "?useSSL=false";
    private String DBUsername = "111306037"; // change to your own user name
    private String DBPassword = "58g95"; // change to your own password

    private static Customer instance;
    
    private Customer() {}

    public static Customer getInstance() {
        if (instance == null) {
            instance = new Customer();
        }
        return instance;
    }

    public void login(String username, String password) throws WrongDataError {
        try (Connection conn = DriverManager.getConnection(url, DBUsername, DBPassword)) {
            
            String query1 = "SELECT Name FROM User WHERE Name = ?";
            String query2 = "SELECT Name FROM User WHERE Password = ? AND Name = ?";
            String query3 = "SELECT Name FROM Customer WHERE Name = ?";

            try (PreparedStatement stat1 = conn.prepareStatement(query1)) {
                stat1.setString(1, username);
                try (ResultSet result1 = stat1.executeQuery()) {
                    if (result1.next()) {
                        try (PreparedStatement stat2 = conn.prepareStatement(query2)) {
                            stat2.setString(1, password);
                            stat2.setString(2, username);
                            try (ResultSet result2 = stat2.executeQuery()) {
                                if (result2.next()) {
                                    try (PreparedStatement stat3 = conn.prepareStatement(query3)) {
                                        stat3.setString(1, username);
                                        try (ResultSet result3 = stat3.executeQuery()) {
                                            if (result3.next()) {
                                                new FrameCustomer(conn, username);
                                            } else {
                                                throw new WrongDataError("CustomerName does not exist");
                                            }
                                        }
                                    }
                                } else {
                                    throw new WrongDataError("UserName does not exist or Password is wrong");
                                }
                            }
                        }
                    } else {
                        throw new WrongDataError("UserName does not exist");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public class WrongDataError extends Exception {
        public WrongDataError(String error) {
            super(error);
        }
    }
}
