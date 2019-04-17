package sample;

import Controller.MainFrameController;
import Controller.ProductListController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {




    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/MainFrame.fxml"));

        Parent root=loader.load();



        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setTitle("HappyLife");
       /* Parent root = FXMLLoader.load(getClass().getResource("/fxmls/MainFrame.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setTitle("HappyLife");*/
   /*     FXMLLoader loaderProductList = new FXMLLoader(getClass().getResource("/fxmls/ProductsList.fxml"));
        FXMLLoader loaderTable = new FXMLLoader(getClass().getResource("/fxmls/Table.fxml"));
        MainFrameController productListController = loader.getController();
        System.out.println(productListController);*/

    }



    public static void main(String[] args) {


     //   Session sesja=HibernateSesionFactory.sesjaFactory.getCurrentSession();

        launch(args);





    }

}
