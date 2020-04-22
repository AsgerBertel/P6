package GUI.ContextMenu;

import GUI.Controller.MainSceneController;
import Sql.ConnectionManager;
import Sql.QueryManager;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchBarContextMenu extends ContextMenu {
    MainSceneController mainSceneController;
    ConnectionManager connectionManager;
    ResultSet resultSet;
    public SearchBarContextMenu(MainSceneController mainSceneController) {
        this.mainSceneController = mainSceneController;
    }

    public void loadProducts(String searchWord) throws SQLException {
        //Todo find out why this query returns the wrong five rows in the program but the corect rows in ssms
        resultSet = connectionManager.selectSQL(QueryManager.selectTopTopics(searchWord));

        while (resultSet.next()) {
            MenuItem menuItem = new MenuItem(resultSet.getString(1));
            menuItem.setOnAction(event -> mainSceneController.setAddProductText(menuItem.getText()));
            this.getItems().addAll(menuItem);
        }
    }
}
