package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

public class PanelGornyController {

    public PanelGornyController() {
    }



    MainFrameController mainFrameController;
    @FXML
    ToggleButton productsListButton;

    @FXML
    ToggleButton tableButton;
    @FXML
    ToggleButton weightButton;

    @FXML
    void initialize()
    {

    }
    @FXML
    void launchTable()
    {
        mainFrameController.setCentre("/fxmls/Table.fxml");
    }
    @FXML
    void launchProductList()
    {

        mainFrameController.setCentre("/fxmls/ProductsList.fxml");

    }


    public void setMainFrameController(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }
}
