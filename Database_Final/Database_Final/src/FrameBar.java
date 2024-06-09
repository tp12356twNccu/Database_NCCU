import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import java.util.ArrayList;

public class FrameBar extends JFrame {
    private JButton bt_Manage, bt_ManageMenu, bt_Analytics, bt_View, bt_PlaceHolder, bt_LogOut;
    private ArrayList<JButton> btList = new ArrayList<>();
    private JPanel pWestbtGroup;

    private int control = 0;
    private JPanel pMain = new JPanel(new BorderLayout());  // Changed layout to BorderLayout for pMain
    Statement stat;
    
    public FrameBar(Connection conn) throws SQLException{
        cButton();
        cComponent();

        setTitle("Customer");
        setSize(1000, 618);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().add(pWestbtGroup, BorderLayout.WEST);
        getContentPane().add(pMain, BorderLayout.CENTER);

        cLayOut();
        setVisible(true);
        stat = conn.createStatement();
    }

    private void cComponent() {
        pWestbtGroup = new JPanel();
        pWestbtGroup.setLayout(new GridLayout(6, 1));
        for (JButton bt : btList) {
            pWestbtGroup.add(bt);
        }
    }

    private void cButton() {
        bt_Manage = new JButton("Manage Info");
        bt_Manage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                control = 0;
                cLayOut();
            }
        });
        
        bt_ManageMenu = new JButton("Manage Menu");
        bt_ManageMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                control = 1;
                cLayOut();
            }
        });

        bt_Analytics = new JButton("Analytics");
        bt_Analytics.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                control = 2;
                cLayOut();
            }
        });

        bt_View = new JButton("View & Reply");
        bt_View.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                control = 3;
                cLayOut();
            }
        });

        bt_PlaceHolder = new JButton();

        bt_LogOut = new JButton("Log out");
        bt_LogOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                int reply = JOptionPane.showConfirmDialog(pMain, "Log out?", "Logging out", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
    				try {
    					Bar.getInstance().Logout();
    				} catch (WrongDataError e) {
    					// TODO Auto-generated catch block
    					JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
    				}
                    
                    setVisible(false);
                }
            }
        });

        btList.add(bt_Manage);
        btList.add(bt_ManageMenu);
        btList.add(bt_Analytics);
        btList.add(bt_View);
        btList.add(bt_PlaceHolder);
        btList.add(bt_LogOut);
    }

    private void cLayOut() {
        pMain.removeAll();  // Clear previous components

        btResetAll();
        switch (control) {
        case 0:
            JPanel panelBManage = new PanelBManage();
            pMain.add(panelBManage, BorderLayout.CENTER);
            
            bt_Manage.setEnabled(false);
            break;
        case 1:
            JPanel panelBMenu = new PanelBMenu();
            pMain.add(panelBMenu, BorderLayout.CENTER);
            
            bt_ManageMenu.setEnabled(false);
            break;
        case 2:
            JPanel panelBAnlysis = new PanelBAnalysis();
            pMain.add(panelBAnlysis, BorderLayout.CENTER);
        
            bt_Analytics.setEnabled(false);
            break;
        case 3:
            JPanel panelBView = new PanelBView();
            pMain.add(panelBView, BorderLayout.CENTER);

            bt_View.setEnabled(false);
            break;
        }

        pMain.revalidate();  // Refresh the panel
        pMain.repaint();
    }

    private void btResetAll() {
        for (JButton bt : btList) {
            bt.setEnabled(true);
        }
        bt_LogOut.setBackground(new Color(250, 210, 210));
        bt_PlaceHolder.setEnabled(false);
    }
}