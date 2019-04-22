package sample;

import Controller.MainFrameController;
import Controller.ProductListController;
import Controller.TableController;
import Controller.startController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {


    static public Stage startStage=new Stage();
    static public Stage stage=new Stage();
    static public int currentUserId;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/MainFrame.fxml"));
        Parent root=loader.load();

        FXMLLoader loader_2 = new FXMLLoader(getClass().getResource("/fxmls/startPage.fxml"));
        Parent root_2=loader_2.load();

        stage=primaryStage;

        Scene appScene=new Scene(root);

        Scene startScene=new Scene(root_2);
        startStage.setScene(startScene);
        startStage.show();

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(appScene);
        primaryStage.setTitle("HappyLife");
      //  primaryStage.show();

    }



    public static void main(String[] args) {


     //   Session sesja=HibernateSesionFactory.sesjaFactory.getCurrentSession();

        launch(args);


    }


}
