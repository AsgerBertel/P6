package GUI.ContextMenu;

import GUI.Controller.MainSceneController;
import Sql.ConnectionManager;
import Sql.QueryManager;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchBarContextMenu extends ContextMenu {
    MainSceneController mainSceneController;
    ConnectionManager connectionManager;
    ResultSet resultSet;

    public SearchBarContextMenu(MainSceneController mainSceneController) {
        this.mainSceneController = mainSceneController;
    }

    public void loadTopics(String searchWord, boolean isTopTopic, boolean isSupTopicTrue, TextField txtCurrentSearchBar) throws SQLException {
        if (isTopTopic)
            resultSet = connectionManager.selectSQL(QueryManager.selectTopTopics(searchWord));
        else if(isSupTopicTrue)
            resultSet = connectionManager.selectSQL(QueryManager.selectSubTopics(searchWord));
        else
            return;

            while (resultSet.next()) {
                MenuItem menuItem = new MenuItem(resultSet.getString(1));
                menuItem.setOnAction(event -> mainSceneController.setAddProductText(menuItem.getText(),txtCurrentSearchBar));
                this.getItems().addAll(menuItem);
            }
    }
    public void loadOpinions(String searchWord, boolean isOpinion, TextField txtCurrentSearchBar) throws SQLException {
        if (isOpinion)
            resultSet = connectionManager.selectSQL(QueryManager.selectOpinions(searchWord));
           else
            return;

        while (resultSet.next()) {
            MenuItem menuItem = new MenuItem(resultSet.getString(1));
            menuItem.setOnAction(event -> mainSceneController.setAddProductText(menuItem.getText(),txtCurrentSearchBar));
            this.getItems().addAll(menuItem);
        }
    }
    public void loadLocation(String searchWord, boolean isDistrict,boolean isCounty,boolean isCity,boolean IsCountry , TextField txtCurrentSearchBar) throws SQLException {
        if(isDistrict)
            resultSet = connectionManager.selectSQL(QueryManager.selectDistrict(searchWord));
          else if(isCounty)
            resultSet = connectionManager.selectSQL(QueryManager.selectCounty(searchWord));
        else if(isCity)
            resultSet = connectionManager.selectSQL(QueryManager.selectCity(searchWord));
        else if(IsCountry)
            resultSet = connectionManager.selectSQL(QueryManager.selectCountry(searchWord));
        else
            return;

        while (resultSet.next()) {
            MenuItem menuItem = new MenuItem(resultSet.getString(1));
            menuItem.setOnAction(event -> mainSceneController.setAddProductText(menuItem.getText(),txtCurrentSearchBar));
            this.getItems().addAll(menuItem);
        }
    }

    public void loadDate(String searchWord, boolean isDay,boolean isMonth,boolean isYear, TextField txtCurrentSearchBar) throws SQLException {
        if(isDay)
            resultSet = connectionManager.selectSQL(QueryManager.selectDay(searchWord));
        else if(isMonth)
            resultSet = connectionManager.selectSQL(QueryManager.selectMonth(searchWord));
        else if(isYear)
            resultSet = connectionManager.selectSQL(QueryManager.selectYear(searchWord));
         else
            return;

        while (resultSet.next()) {
            MenuItem menuItem = new MenuItem(resultSet.getInt(1)+"");
            menuItem.setOnAction(event -> mainSceneController.setAddProductText(menuItem.getText(),txtCurrentSearchBar));
            this.getItems().addAll(menuItem);
        }
    }

}
