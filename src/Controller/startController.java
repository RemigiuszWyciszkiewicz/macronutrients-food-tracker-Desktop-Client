package Controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import sample.Main;

import java.io.IOException;

public class startController {

    public startController() { }

    @FXML TextField emailTextField;
    @FXML TextField passwordTextField;
    @FXML Label statmentLabel;
    @FXML Label registerStatmentLabel;
    @FXML StackPane mainPane;
    @FXML TextField registerEmailTextField;
    @FXML TextField registerPasswordTextField;
    @FXML ToggleGroup gender;
    @FXML RadioButton femaleRadioButton;
    @FXML RadioButton maleRadioButton;



    @FXML
    void initialize(){}

    @FXML
    void signIn()
    {
        JSONObject userData=new JSONObject();
        JSONObject responeData = null;
        try {
            userData.put("password",passwordTextField.getText());
            userData.put("email",emailTextField.getText());
            responeData=HttpRequests.sendJsonToApi(userData,"http://localhost:8080/App/user/signIn");

        if(responeData.has("status")) statmentLabel.setText("Błędny login lub hasło");
        else
        {
            Main.currentUserId=responeData.getInt("id");
            Main.startStage.close();
            Main.stage.show();
        }
        }catch (JSONException|IOException a)
        {a.printStackTrace();}
    }

    @FXML
    void goToRegisterPage()
    {
        changePage("/fxmls/registerPage.fxml");

    }
    @FXML
    void register() throws JSONException, IOException {
        femaleRadioButton.setUserData("female");
        maleRadioButton.setUserData("male");

        JSONObject user=new JSONObject();
        user.put("email",registerEmailTextField.getText());
        user.put("password",registerPasswordTextField.getText());
        user.put("gender",gender.getSelectedToggle().getUserData());
        JSONObject response=HttpRequests.sendJsonToApi(user,"http://localhost:8080/App/user/register");
        if(response != null) registerStatmentLabel.setText("Ten email jest już zajęty");
        else changePage("/fxmls/startPage.fxml");
    }


    void changePage(String url)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
        Parent root=null;
        try {
            root=loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainPane.getChildren().clear();
        mainPane.getChildren().add(root);
    }

}
