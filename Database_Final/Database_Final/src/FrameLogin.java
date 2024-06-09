import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import javax.swing.*;


public class FrameLogin extends JFrame {
    private JTextField username_input = new JTextField(20);
    private JPasswordField password_input = new JPasswordField(20);
    private JPanel pUser,pPass,pInput,pButton,pNorthrbt;
    private JButton bt_SignUp,bt_LogIn,bt_CheckUserName,bt_CheckPassWord;
    private JCheckBox cb_pVisible;
    private JRadioButton rbt_Customer,rbt_Bar;
    private ButtonGroup rbtGroup;
    Statement stat;    
    
    public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				String server = "jdbc:mysql://140.119.19.73:3315/";
				String database = "111306037"; // change to your own database
				String url = server + database + "?useSSL=false";
				String username = "111306037"; // change to your own user name
				String password = "58g95"; // change to your own password　　　　
				
				try {
					Connection conn = DriverManager.getConnection(url, username, password);
					System.out.println("DB Connected");
					
					FrameLogin frame = new FrameLogin(conn);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
			
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
    
    public FrameLogin(Connection conn)throws SQLException {  
    	
        setTitle("Login");
        setSize(400, 247);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        stat = conn.createStatement();
        cLayOut();
    }  
    
    private void cComponent() {
        pUser = new JPanel(new FlowLayout(FlowLayout.CENTER,3,0));
        pUser.add(new JLabel("Username: "));
        pUser.add(username_input);
        pUser.add(bt_CheckUserName);
        bt_CheckUserName.setPreferredSize(new Dimension(80,20));

        pPass = new JPanel(new FlowLayout(FlowLayout.CENTER,4,0));
        pPass.add(new JLabel("Password: "));
        pPass.add(password_input);
        pPass.add(bt_CheckPassWord);
        bt_CheckPassWord.setPreferredSize(new Dimension(80,20));
        
        pButton = new JPanel(new GridLayout(1,2,10,6));
        pButton.add(bt_SignUp);
        pButton.add(bt_LogIn);
        
        pButton.setPreferredSize(new Dimension(200,20));
        
        rbtGroup = new ButtonGroup();
        rbtGroup.add(rbt_Customer);
        rbtGroup.add(rbt_Bar);
        pNorthrbt = new JPanel();
        pNorthrbt.add(new JLabel("Select identity:"));
        pNorthrbt.add(rbt_Customer);
        pNorthrbt.add(rbt_Bar);
        
        pInput = new JPanel();
        pInput.add(pUser);
        pInput.add(pPass);
        pInput.add(pButton);
        pInput.add(cb_pVisible);
        
    }
    
    private void cButton() {
        bt_SignUp = new JButton("Sign up");
        bt_SignUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String username = username_input.getText();
                String password = new String(password_input.getPassword());
                
                if (rbt_Customer.isSelected()) {
                    //TODO: function_Customer
                	try{
                		String query = "INSERT INTO User (Name, Password, Type) "
                		+ "VALUES('" + username + "', '" + password + "', 'Customer')";
                		stat.execute(query);
                	}catch(SQLException e) {
                		e.printStackTrace();
                	}
                } else {
                    //TODO: function_Bar
                	try{
                		String query = "INSERT INTO User (Name, Password, Type) "
                		+ "VALUES('" + username + "', '" + password + "', 'Bar')";
                		stat.execute(query);
                	}catch(SQLException e) {
                		e.printStackTrace();
                	}
                }
            }
        });
        
        bt_LogIn = new JButton("Log in");
        bt_LogIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String username = username_input.getText();
                String password = new String(password_input.getPassword());
                
                if (rbt_Customer.isSelected()) {
                    // Customer part.
                		try {
							Customer.getInstance().login(username, password);
						} catch (Customer.WrongDataError e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                } else {
                    try {
                        Bar.getInstance().Login(username, password);
                    } catch (WrongDataError e) {
                        JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        

        bt_CheckUserName = new JButton("Check");
        bt_CheckUserName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String username = username_input.getText();
                
              //function
                try {
                	String query = "SELECT Name FROM User WHERE Name = '" + username + "'";
                	ResultSet rs = stat.executeQuery(query);
                	if(rs.next()) {
                		JOptionPane.showMessageDialog(null, "此名稱已有人使用", "無法使用", 0);
                	}else {
                		JOptionPane.showMessageDialog(null, "此名稱可以使用", "可以使用", 1);
                	}
                }catch(SQLException e) {
                	e.printStackTrace();
                }
            }
        });

        bt_CheckPassWord = new JButton("Check");
        bt_CheckPassWord.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String password = new String(password_input.getPassword());

                //function
                try {
                	String query = "SELECT Password FROM User WHERE Password = '" + password + "'";
                	ResultSet rs = stat.executeQuery(query);
                	if(rs.next()) {
                		JOptionPane.showMessageDialog(null, "此密碼已有人使用", "無法使用", 0);
                	}else {
                		JOptionPane.showMessageDialog(null, "此密碼可以使用", "可以使用", 1);
                	}
                }catch(SQLException e) {
                	e.printStackTrace();
                }
            }
        });

        rbt_Customer = new JRadioButton("Customer");
        rbt_Customer.setSelected(true);
        rbt_Bar = new JRadioButton("Bar");
    }

    private void cCheckBox() {
        cb_pVisible = new JCheckBox("Show password");
        password_input.setEchoChar('*');

        cb_pVisible.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (cb_pVisible.isSelected()) {
                    password_input.setEchoChar((char)0);
                } else {
                    password_input.setEchoChar('*');
                }
            }
        });
    }

    private void cLayOut() {
        cButton();
        cCheckBox();
        cComponent();
        
        setLayout(new BorderLayout(30,20));
        getContentPane().add(pNorthrbt,BorderLayout.NORTH);
        getContentPane().add(pInput,BorderLayout.CENTER);

        setVisible(true);
    }
}