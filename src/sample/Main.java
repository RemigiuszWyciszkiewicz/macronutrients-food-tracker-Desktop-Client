package sample;

import Controller.MainFrameController;
import Controller.ProductListController;
import Controller.TableController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {




    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/MainFrame.fxml"));
        FXMLLoader loaderProductList = new FXMLLoader(getClass().getResource("/fxmls/ProductsList.fxml"));
        FXMLLoader loaderTable = new FXMLLoader(getClass().getResource("/fxmls/Table.fxml"));
        loaderProductList.load();
        loaderTable.load();
        Parent root=loader.load();

        ProductListController productListController = loaderProductList.getController();
        TableController tableController = loaderTable.getController();
        System.out.println(tableController);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setTitle("HappyLife");
       /* Parent root = FXMLLoader.load(getClass().getResource("/fxmls/MainFrame.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setTitle("HappyLife");*/




    }



    public static void main(String[] args) {


     //   Session sesja=HibernateSesionFactory.sesjaFactory.getCurrentSession();

        launch(args);





    }

}
