package components;
import javax.swing.*;
import java.awt.*;


public class App extends JFrame {
    private JPanel mainPanel;
    private JMenuBar menuBar;

    public App() {
        setTitle("Inventory Management System");
        setSize(800,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createMenuBar();
        createMainPanel();
        createNavigationPanel();

        setResizable(false);
        setVisible(true);
    }

    //menu bar
    private void createMenuBar() {
        menuBar = new JMenuBar();

        //View Menu
        JMenu viewMenu = new JMenu("View");
        JMenuItem invReport = new JMenuItem("Inventory Report");
        JMenuItem viewAll = new JMenuItem("All Products");

        invReport.addActionListener(e -> new InvReport());
        viewAll.addActionListener(e -> showPanel(new ViewAll()));

        viewMenu.add(invReport);
        viewMenu.add(viewAll);

        menuBar.add(viewMenu);
        setJMenuBar(menuBar);
    }

    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout()); // ensure proper layout management

        showPanel(new Dashboard()); // set Dashboard as the default view
        add(mainPanel, BorderLayout.CENTER);
    }

    private void showPanel(JPanel panel) {
        mainPanel.removeAll();
        panel.setPreferredSize(mainPanel.getSize()); // ensure panel takes up full space
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    //navigation buttons under each panel
    private void createNavigationPanel() {
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    
        JButton dashboardBtn = new JButton("Dashboard");
        JButton allProductsBtn = new JButton("All Products");
        JButton usersBtn = new JButton("Sales");

        Dimension buttonSize = new Dimension(150, 40);
        dashboardBtn.setPreferredSize(buttonSize);
        allProductsBtn.setPreferredSize(buttonSize);
        usersBtn.setPreferredSize(buttonSize);
    
        dashboardBtn.addActionListener(e -> showPanel(new Dashboard()));
        allProductsBtn.addActionListener(e -> showPanel(new ViewAll()));
        usersBtn.addActionListener(e -> showPanel(new SalesUI()));
    
        navPanel.add(dashboardBtn);
        navPanel.add(allProductsBtn);
        navPanel.add(usersBtn);
    
        add(navPanel, BorderLayout.SOUTH);
    }
}