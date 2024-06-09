import java.awt.MenuItem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Bar {
        String server = "jdbc:mysql://140.119.19.73:3315/";
    	String database = "111306037"; // change to your own database
    	String url = server + database + "?useSSL=false";
    	String DBUsername = "111306037"; // change to your own user name
    	String DBPassword = "58g95"; // change to your own password

        //private String query;
        private Statement stat;
        private String barName;
        private static Bar instance;

        private Bar() {};

        public static Bar getInstance() {
            if (instance == null) {
                instance = new Bar();
            }
            return instance;
        }
        
        //Class FrameLogin
        public void Login(String username, String password) throws WrongDataError {
    		try (Connection conn = DriverManager.getConnection(url, DBUsername, DBPassword)) {
    			setBarName(username);
    			stat = conn.createStatement();

    			String query = "SELECT * FROM User WHERE Name = '" + username + "' AND Password = '" + password + "'";
                
    			ResultSet rs = stat.executeQuery(query);
    			if (!rs.next()) {
    				throw new WrongDataError("Wrong username or password");
    			}else {
    				new FrameBar(conn);
    			}

    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
        
        //Class FrameBar
        public void Logout() throws WrongDataError {
        	try(Connection conn = DriverManager.getConnection(url, DBUsername, DBPassword)) {
        		stat = conn.createStatement();
        		new FrameLogin(conn);
        	}catch (SQLException e) {
        		e.printStackTrace();
        	}
        }
        
        //PanelBManage-------------------------
        public void setBarName(String Username) {
        	barName = Username;
        }
        
        public String getBarName() {
        	return barName;
        }
        
        public void startOfManage(ArrayList<String> infoTitles, ArrayList<String> infoDetails) throws WrongDataError {
        	try(Connection conn = DriverManager.getConnection(url, DBUsername, DBPassword)) {
        		stat = conn.createStatement();
        		
        		for(int i = 0; i < infoTitles.size(); i++) {
        			String query = "SELECT " + infoTitles.get(i) + " FROM Bar WHERE BarName = '" + getBarName() + "'";
        			ResultSet rs = stat.executeQuery(query);
        			rs.next();
        			infoDetails.add(rs.getString(infoTitles.get(i)));
        		}
        	}catch (SQLException e) {
        		e.printStackTrace();
        	}
        }
        
        public void ManageUpdateDB(ArrayList<String> infoTitles, ArrayList<String> infoDetails) throws WrongDataError {
        	try(Connection conn = DriverManager.getConnection(url, DBUsername, DBPassword)) {
        		stat = conn.createStatement();
        		for(int i = 0; i < infoTitles.size(); i++) {
        			String query = "UPDATE Bar SET " + infoTitles.get(i) + " = '" + infoDetails.get(i) + "' WHERE BarName = '" + getBarName() + "'";
        			stat.execute(query);
        		}
        	}catch (SQLException e) {
        		e.printStackTrace();
        	}
        }
        
        //PanelBAnalysis----------------------
        public String showResultSet(String sort) throws WrongDataError{
        	try(Connection conn = DriverManager.getConnection(url, DBUsername, DBPassword)) {
        		stat = conn.createStatement();
        		String query = "";
        		switch(sort) {
        			case "Sales figures":
        				query = "SELECT DISTINCT a.Item, SUM(a.total_Price) AS 'Sales figures', SUM(a.Amount) AS 'Sales volume' "
        						+ "FROM cus_Order a JOIN Menu b "
        						+ "ON a.BarName = b.BarName "
        						+ "AND a.Item = b.Item "
        						+ "WHERE a.BarName = '" + getBarName() + "' "
        						+ "GROUP BY a.Item "
        						+ "ORDER BY 'Sales figures' DESC";
        				break;
        				
        			case "Sales Volume":
        				query = "SELECT DISTINCT a.Item, SUM(a.total_Price) AS 'Sales figures', SUM(a.Amount) AS 'Sales volume' "
        						+ "FROM cus_Order a JOIN Menu b "
        						+ "ON a.BarName = b.BarName "
        						+ "AND a.Item = b.Item "
        						+ "WHERE a.BarName = '" + getBarName() + "' "
        						+ "GROUP BY a.Item "
        						+ "ORDER BY 'Sales Volume' DESC";
        				break;
        				
        			case "Net Income":
        				query = "SELECT DISTINCT a.Item, SUM(a.total_Price) AS 'Sales figures', SUM(a.Amount) AS 'Sales volume', (b.Price - b.Cost)*SUM(a.Amount) AS 'Net Income' "
        						+ "FROM cus_Order a JOIN Menu b "
        						+ "ON a.BarName = b.BarName "
        						+ "AND a.Item = b.Item "
        						+ "WHERE a.BarName = '" + getBarName() + "' "
        						+ "GROUP BY a.Item "
        						+ "ORDER BY 'Sales figures' DESC";
        				break;
        			
        		}
        		ResultSet rs = stat.executeQuery(query);
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				String output = "";
				for (int i = 1; i <= columnCount; i++) {
					output += String.format("%-12s", metaData.getColumnLabel(i));
				}
				output += "\n";
				while (rs.next()) {
					for (int i = 1; i <= columnCount; i++) {
						output += String.format("%-12s", rs.getString(i));
					}
					output += "\n";
				}
				return output; 
				
        	}catch (SQLException e) {
        		e.printStackTrace();
        	}
        	return "";
        }
        
        //PanelBManageMenu--------------
        public void buildListMenu(ArrayList<MenuItemObject> listmenu) throws WrongDataError{
        	try(Connection conn = DriverManager.getConnection(url, DBUsername, DBPassword)) {
        		stat = conn.createStatement();
        		String query = "SELECT Menu_ID, Item, Price, Cost FROM Menu WHERE BarName = '" + getBarName() + "'";
        		ResultSet rs = stat.executeQuery(query);
        		while(rs.next()) {
        			listmenu.add(new MenuItemObject(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
        		}
        	}catch (SQLException e) {
        		e.printStackTrace();
        	}
        }
        
        public void UpdateMenuDB(int menuID, String updateColumn, String updateData) throws WrongDataError{
        	try(Connection conn = DriverManager.getConnection(url, DBUsername, DBPassword)) {
        		stat = conn.createStatement();
        		String query = "UPDATE Menu SET " + updateColumn + " = '" + updateData + "' WHERE Menu_ID = '" + menuID + "'";
        		stat.execute(query);
        	}catch (SQLException e) {
        		e.printStackTrace();
        	}
        }
        
        //PanelBView
        public void buildReview(ArrayList<String> viewCustomer, ArrayList<Integer> viewRates, ArrayList<String> viewComment, ArrayList<String> viewReply) throws WrongDataError{
        	try(Connection conn = DriverManager.getConnection(url, DBUsername, DBPassword)) {
        		stat = conn.createStatement();
        		String query = "SELECT CustomerName, Rating, Comment, Reply FROM Review WHERE BarName = '" + getBarName() + "'";
        		ResultSet rs = stat.executeQuery(query);
        		while(rs.next()) {
        			viewCustomer.add(rs.getString(1));
        			viewRates.add(rs.getInt(2));
        			viewComment.add(rs.getString(3));  
        			viewReply.add(rs.getString(4));
        		}
        	}catch (SQLException e) {
        		e.printStackTrace();
        	}
        }
        
        public void UpdateReply(ArrayList<String> viewCustomer, ArrayList<String> viewReply) throws WrongDataError{
        	try(Connection conn = DriverManager.getConnection(url, DBUsername, DBPassword)) {
        		stat = conn.createStatement();
        		for(int i = 0; i < viewCustomer.size(); i++) {
        			String query = "UPDATE Review SET Reply = '" + viewReply.get(i) + "' WHERE CustomerName = '" + viewCustomer.get(i) + "' AND BarName = '" + getBarName() + "'";
        			stat.execute(query);
        		}
        	}catch (SQLException e) {
        		e.printStackTrace();
        	}
        }
    }

    class WrongDataError extends Exception {
    	public WrongDataError(String Error) {
    		super(Error);
    	}
    }