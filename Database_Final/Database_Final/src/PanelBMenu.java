import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class PanelBMenu extends JPanel{
    private JPanel pContent;
    private JScrollPane sMain;
    private ArrayList<MenuItemObject> listMenu = new ArrayList<>();
    private boolean build = true;

    private JPanel pMain = new JPanel(new BorderLayout());
    
    public PanelBMenu() {
        buildMenuList();
        cComponent();
        cLayOut();
    }
    
    private void cComponent() {
        pContent = new JPanel();
        pContent.setLayout(new BoxLayout(pContent, BoxLayout.Y_AXIS));

        for (int i = 0;i < listMenu.size();i++) {
            JPanel pTemp = new JPanel();
            pTemp.setLayout(new BoxLayout(pTemp, BoxLayout.Y_AXIS));
            pTemp.setPreferredSize(new Dimension(700,200));
            
            JLabel lblMenuID = new JLabel(Integer.toString(listMenu.get(i).getmenuID()));
            JLabel lblItem = new JLabel(listMenu.get(i).getItem());
            JLabel lblPrice = new JLabel(Integer.toString(listMenu.get(i).getPrice()));
            JLabel lblCost = new JLabel(Integer.toString(listMenu.get(i).getCost()));
            
            JLabel lblText1 = new JLabel("MenuID: ");
            JLabel lblText2 = new JLabel("Item: ");
            JLabel lblText3 = new JLabel("Price: ");
            JLabel lblText4 = new JLabel("Cost: ");
            
            JTextField tf_Item = new JTextField(10);
            JTextField tf_Price = new JTextField(10);
            JTextField tf_Cost = new JTextField(10);
            
            JPanel pMenuID = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
            JPanel pItem = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
            JPanel pPrice = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
            JPanel pCost = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
            
            pMenuID.add(lblText1);pMenuID.add(lblMenuID);
            pItem.add(lblText1);pItem.add(lblItem);pItem.add(tf_Item);
            pPrice.add(lblText2);pPrice.add(lblPrice);pPrice.add(tf_Price);
            pCost.add(lblText3);pCost.add(lblCost);pCost.add(tf_Cost);

            JButton bt_Edit = new JButton("Edit");
            bt_Edit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                	try {
                		if (!tf_Item.getText().isEmpty()) {
                            String updatedItem = tf_Item.getText();
                            lblItem.setText(updatedItem);
                            Bar.getInstance().UpdateMenuDB(Integer.parseInt(lblMenuID.getText()), "Item", updatedItem);
                        } else {
                            String updatedItem = lblItem.getText();
                            lblItem.setText(updatedItem);
                        }

                        if (!tf_Price.getText().isEmpty()) {
                            String updatedPrice = tf_Price.getText();
                            lblPrice.setText(updatedPrice);
                            Bar.getInstance().UpdateMenuDB(Integer.parseInt(lblMenuID.getText()), "Price", updatedPrice);
                        } else {
                            String updatedPrice = lblPrice.getText();
                            lblPrice.setText(updatedPrice);
                        }

                        if (!tf_Cost.getText().isEmpty()) {
                            String updatedCost = tf_Cost.getText();
                            lblCost.setText(updatedCost);
                            Bar.getInstance().UpdateMenuDB(Integer.parseInt(lblMenuID.getText()), "Cost", updatedCost);
                        } else {
                            String updatedCost = lblCost.getText();
                            lblCost.setText(updatedCost);
                        }
                	}catch (WrongDataError e) {
                		e.printStackTrace();
                	}
                	
                    
                    //TODO: update DB with three Strings
                }
            });
            
            JPanel pUpper = new JPanel(new FlowLayout(FlowLayout.LEFT,40,0));
            JPanel pLower = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));

            pUpper.add(pItem);pUpper.add(pPrice);
            pUpper.add(pCost);pUpper.add(bt_Edit);

            pTemp.add(pUpper);
            pTemp.add(pLower);

            pContent.add(pTemp);
        }

        pContent.setPreferredSize(new Dimension(700,listMenu.size()*75));
        sMain = new JScrollPane(pContent);
        sMain.setPreferredSize(new Dimension(800,550));
    }

    private void cLayOut() {
        pMain.add(sMain, BorderLayout.CENTER);

        add(pMain);
    }

    private void buildMenuList() {
        if (build) {
            //TODO: build from database
        	try {
        		Bar.getInstance().buildListMenu(listMenu);
        	}catch (WrongDataError e) {
        		e.printStackTrace();
        	}
        }
        
        build = false;
    }
}