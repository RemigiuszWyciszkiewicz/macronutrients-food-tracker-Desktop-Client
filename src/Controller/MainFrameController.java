package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainFrameController {

    @FXML
    StackPane stackPaneGłówny;
    @FXML
    public PanelGornyController PanelGornyController;

    @FXML
    void initialize()
    {
        setCentre("/fxmls/Table.fxml");

        PanelGornyController.setMainFrameController(this);
    }

    void setCentre(String adresFormatki)
    {
        stackPaneGłówny.getChildren().clear();

        FXMLLoader loader=new FXMLLoader(getClass().getResource(adresFormatki));
        Parent root=null;
        try {
            root=loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stackPaneGłówny.getChildren().add(root);

    }



}
