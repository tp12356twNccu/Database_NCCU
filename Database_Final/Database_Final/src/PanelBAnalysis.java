import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;

public class PanelBAnalysis extends JPanel{
    private JTextArea ta_Content;
    private JPanel pContent,pNorthbtGroup;
    private JScrollPane sMain;
    private JButton bt_Sort1,bt_Sort2,bt_Sort3;

    private JPanel pMain = new JPanel();

    public PanelBAnalysis() {
        cButton();
        cComponent();
        cLayOut();
    }

    private void cComponent() {
        ta_Content = new JTextArea();
        ta_Content.setText("Test message");
        pContent = new JPanel();

        pContent.add(ta_Content);
        
        sMain = new JScrollPane(pContent);
        sMain.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sMain.setPreferredSize(new Dimension(800,500));

        pNorthbtGroup = new JPanel(new FlowLayout());
        pNorthbtGroup.add(bt_Sort1);
        pNorthbtGroup.add(bt_Sort2);
        pNorthbtGroup.add(bt_Sort3);
    }

    private void cButton() {
        bt_Sort1 = new JButton("Sales figures");
        bt_Sort1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //TODO: generate text and update
            	try {
                	ta_Content.setText(Bar.getInstance().showResultSet("Sales figures"));
            	}catch (WrongDataError e) {
            		e.printStackTrace();
            	}
            }
        });

        bt_Sort2 = new JButton("Sales Volume");
        bt_Sort2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //TODO: generate text and update
            	try {
                	ta_Content.setText(Bar.getInstance().showResultSet("Sales Volume"));
            	}catch (WrongDataError e) {
            		e.printStackTrace();
            	}
            }
        });

        bt_Sort3 = new JButton("Net Income");
        bt_Sort3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //TODO: generate text and update
            	try {
                	ta_Content.setText(Bar.getInstance().showResultSet("Net Income"));
            	}catch (WrongDataError e) {
            		e.printStackTrace();
            	}
            }
        });
    }

    private void cLayOut() {
        setLayout(new BorderLayout());

        pMain.add(pNorthbtGroup, BorderLayout.NORTH);
        pMain.add(sMain, BorderLayout.CENTER);

        add(pMain);
    }
}