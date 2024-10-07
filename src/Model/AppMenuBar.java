package Model;

import View.CartView;
import View.HistoryAdminView;
import View.HistoryView;
import View.HomeAdminView;
import View.HomeView;
import View.LoginView;
import View.RegisterView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class AppMenuBar {
	static MenuBar menuBar;
    static Menu pageMenu;
    static MenuItem loginMenuItem;
    static MenuItem registerMenuItem;
    static Menu userMenu;
    static MenuBar userMenuBar;
    static MenuItem userHome;
    static MenuItem userCart;
    static MenuItem userHistory;
    static Menu adminMenu;
    static MenuBar adminMenuBar;
    static MenuItem manageProductMenu;
    static MenuItem viewHistoryMenu;
    static MenuItem logout;


    public static MenuBar menuBarReg(Stage primaryStage) {  //menubar untuk register dan login
        menuBar = new MenuBar();
        pageMenu = new Menu("Page");
        loginMenuItem = new MenuItem("Login");
        registerMenuItem = new MenuItem("Register");

        loginMenuItem.setOnAction(e -> LoginView.show());
        registerMenuItem.setOnAction(e -> RegisterView.show(primaryStage));

        pageMenu.getItems().addAll(loginMenuItem, registerMenuItem);
        menuBar.getMenus().add(pageMenu);
        return menuBar;
    }

    public static MenuBar menuBarUser(Stage primaryStage) {        //menubar untuk user
        userMenuBar = new MenuBar();
        userMenu = new Menu("Page");
        logout = new MenuItem("Logout");
        userHome = new MenuItem("Home");
        userCart = new MenuItem("Cart");
        userHistory = new MenuItem("History");

        logout.setOnAction(e -> handleLogout());
        userHome.setOnAction(e -> showHomePage(primaryStage));
        userCart.setOnAction(e -> showCartPage(primaryStage));
        userHistory.setOnAction(e -> showHistoryPage(primaryStage));

        userMenu.getItems().addAll(logout, userHome, userCart, userHistory);
        userMenuBar.getMenus().add(userMenu);

        return userMenuBar;
    }

    public static MenuBar menuBarAdmin(Stage primaryStage) {  // menubar untuk admin
        adminMenuBar = new MenuBar();
        adminMenu = new Menu("Admin");
        
        manageProductMenu = new MenuItem("Manage Product");
        viewHistoryMenu = new MenuItem("View History");
        logout = new MenuItem("Logout");

        manageProductMenu.setOnAction(e -> showManageProductPage(primaryStage));
        viewHistoryMenu.setOnAction(e -> showHistoryProductPageAdmin(primaryStage));
        logout.setOnAction(e -> handleLogout());

        adminMenu.getItems().addAll(manageProductMenu, viewHistoryMenu, logout);
        adminMenuBar.getMenus().add(adminMenu);

        return adminMenuBar;
    }

    private static void handleLogout() {
        LoginView.show();
    }

    private static void showHomePage(Stage primaryStage) {
        HomeView.show(primaryStage);
    }

    private static void showCartPage(Stage primaryStage) {
        CartView.show(primaryStage);
    }

    private static void showHistoryPage(Stage primaryStage) {
        HistoryView.show(primaryStage);
    }

    private static void showManageProductPage(Stage primaryStage) {
        HomeAdminView.show(primaryStage);
    }

    private static void showHistoryProductPageAdmin(Stage primaryStage) {
        HistoryAdminView.show(primaryStage);
    }
}
