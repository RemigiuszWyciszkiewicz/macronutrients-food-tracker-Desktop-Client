package Controller;

import DAO.Products;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;

public class ProductListController {

    @FXML TextField proteinTextField;
    @FXML TextField caloriesTextField;
    @FXML TextField nameTextField;
    @FXML TextField fatsTextField;
    @FXML TextField carbsTextField;

    @FXML TableView produktyTableView;
    @FXML TableColumn NazwaColumn;
    @FXML TableColumn caloriesColumn;
    @FXML TableColumn proteinColumn;
    @FXML TableColumn fatsColumn;
    @FXML TableColumn carbsColumn;


    @FXML Button addPrzycisk;
    @FXML Button deleteButton;


   static ObservableList<Products> listOfProductsObservable = FXCollections.observableArrayList();

    public ProductListController() {
    }

    @FXML
    void initialize() {

        listOfProductsObservable.clear();
        try {
            convertJsonToProductObject();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        addPrzycisk.disableProperty().bind(proteinTextField.textProperty().isEmpty()
                .or(nameTextField.textProperty().isEmpty()
                        .or(caloriesTextField.textProperty().isEmpty()
                                .or(fatsTextField.textProperty().isEmpty()
                                        .or(carbsTextField.textProperty().isEmpty())))));

        produktyTableView.setItems(listOfProductsObservable);
        NazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwaProduktu"));
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("kcalProduktu"));
        proteinColumn.setCellValueFactory(new PropertyValueFactory<>("białkoProduktu"));
        fatsColumn.setCellValueFactory(new PropertyValueFactory<>("tluszczeProduktu"));
        carbsColumn.setCellValueFactory(new PropertyValueFactory<>("weglowodanyProduktu"));

    }



    @FXML
    void addProduct() {

        try {

            Optional<JSONObject> jsonObject=Optional.ofNullable(HttpRequests.sendJsonToApi(createJSONfromTextFields(),"http://localhost:8080/App/product"));


           /* jsonObject.ifPresent((a) -> {
                try {
                    if(a.getInt("status")==400) ErrorAlert("Taki produkt już istnienie.");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });*/

           if(jsonObject.isPresent()) {
               if (jsonObject.get().getInt("status") == 400) ErrorAlert("Taki produkt już istnienie.");
           }else { listOfProductsObservable.add(new Products(
                    nameTextField.getText()
                    ,Integer.valueOf(caloriesTextField.getText())
                    ,Double.valueOf(proteinTextField.getText())
                    ,Double.valueOf(fatsTextField.getText())
                    ,Double.valueOf(carbsTextField.getText())));}

        }catch (IOException|JSONException z){ z.printStackTrace(); }
        clearTextFields();


    }
    private void clearTextFields()
    {
        carbsTextField.clear();
        fatsTextField.clear();
        caloriesTextField.clear();
        proteinTextField.clear();
        nameTextField.clear();
    }

    private JSONObject createJSONfromTextFields()
    {

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("name", nameTextField.getText());
            jsonObject.put("calories", caloriesTextField.getText());
            jsonObject.put("protein", proteinTextField.getText());
            jsonObject.put("fats", fatsTextField.getText());
            jsonObject.put("carbohydrates", carbsTextField.getText());
        }catch (JSONException e)
        {e.getCause();}
        return jsonObject;
    }

    @FXML
    void deleteProduct(){

        String productName= null;

        try {
             productName = produktyTableView.getSelectionModel().getSelectedItem().toString();
            System.out.println(productName);
        }catch (Exception e)
        {
            ErrorAlert("Nie wybrałeś produktu.");
        }

        if(productName!= null) {
            try {
                HttpRequests.deleteRequest("http://localhost:8080/App/product?name="+ URLEncoder.encode(productName, "UTF-8"));
                listOfProductsObservable.remove(produktyTableView.getSelectionModel().getSelectedItem());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void convertJsonToProductObject() throws JSONException, IOException {

        JSONArray jsonValues = HttpRequests.readJsonFromUrl("http://localhost:8080/App/products");

        for (int i = 0; i < jsonValues.length(); i++) {

            listOfProductsObservable.add(new Products(
                    (String) jsonValues.getJSONObject(i).get("name")
                    ,jsonValues.getJSONObject(i).getInt("calories")
                    ,jsonValues.getJSONObject(i).getDouble("protein")
                    ,jsonValues.getJSONObject(i).getDouble("fats")
                    ,jsonValues.getJSONObject(i).getDouble("carbohydrates")));
        }
    }

   static void ErrorAlert(String msg)
    {
        Alert error=new Alert(Alert.AlertType.INFORMATION);
        error.setHeaderText(msg);
        error.show();

    }

}
