package Controller;

import DAO.Products;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.TextFields;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sample.Main;


import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.Object;


public class TableController {


    @FXML TableColumn produktColumn;
    @FXML TableColumn amountColumns;
    @FXML TableColumn caloriesColumn;
    @FXML TableColumn proteinColumn;
    @FXML TableColumn fatsColumn;
    @FXML TableColumn carbsColumn;
    @FXML TableView tablewiew;
    @FXML TextField productNameTextField;
    @FXML ComboBox daysComboBox;
    @FXML TextField amountTexTfield;
    @FXML Button addButton;
    @FXML Button deleteButton;
    @FXML Label sumCaloriesLabel;
    @FXML Label sumOfProteinLabel;
    @FXML Label sumOfCarbsLabel;
    @FXML Label sumOfFatsLabel;

    ObservableList<String> daysObservableList=FXCollections.observableArrayList();
    ObservableList<Products> productsOfCurrentDay =FXCollections.observableArrayList();

    ListProperty listofdaysPropList=new SimpleListProperty();

    String selelectedDate;

    double proteinSum=0;
    double carbsSum=0;
    double fatsSum=0;
    int caloriesSum=0;


    public TableController(){}


    @FXML
    void initialize() {
        System.out.println("test");

        convertJsonArrayToObservableList();

        addButton.disableProperty().bind(amountTexTfield.textProperty().isEmpty().or(productNameTextField.textProperty().isEmpty()));

        daysComboBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                productsOfCurrentDay.clear();
                try {
                    selelectedDate=newValue.toString();
                    fetchProductsOfDay(selelectedDate);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


           //     int id=((DayORM)newValue).getId();
//                pobierzListeProduktów(id);

            }
        });


        TextFields.bindAutoCompletion(productNameTextField, ProductListController.listOfProductsObservable);

        tablewiew.setItems(productsOfCurrentDay);
        produktColumn.setCellValueFactory(new PropertyValueFactory<>("nazwaProduktu"));
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("kcalProduktu"));
        proteinColumn.setCellValueFactory(new PropertyValueFactory<>("białkoProduktu"));
        carbsColumn.setCellValueFactory(new PropertyValueFactory<>("weglowodanyProduktu"));
        fatsColumn.setCellValueFactory(new PropertyValueFactory<>("tluszczeProduktu"));
        amountColumns.setCellValueFactory(new PropertyValueFactory<>("iloscProduktu"));


        listofdaysPropList.set(daysObservableList);
        daysComboBox.itemsProperty().bindBidirectional(listofdaysPropList);
        daysComboBox.getSelectionModel().selectLast();


    }


    @FXML
    void dodajDoListy()
    {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("amount",Integer.valueOf(amountTexTfield.getText()));
            jsonObject.put("day",daysComboBox.getValue().toString());
            jsonObject.put("product",productNameTextField.getText());
            System.out.println(jsonObject);
            JSONObject responseJson=HttpRequests.sendJsonToApi(jsonObject,"http://localhost:8080/App/productOfDay/"+Main.currentUserId);

            Optional.ofNullable(responseJson).ifPresent((a) -> { ProductListController.ErrorAlert("Proszę wybrać produkt z listy."); });

            productsOfCurrentDay.clear();
            fetchProductsOfDay(selelectedDate);

           /* productsOfCurrentDay.addAll(
                    ProductListController.listOfProductsObservable.filtered((a)-> a.getNazwaProduktu().equals(productNameTextField.getText())));*/

        }catch (JSONException|IOException e)
        {e.printStackTrace(); }
        catch (NumberFormatException e)
        {
            ProductListController.ErrorAlert("Wpisz wagę!");
        }
    }

     private void fetchProductsOfDay(String date) throws IOException, JSONException {
       JSONArray jsonArray = HttpRequests.readJsonFromUrl("http://localhost:8080/App/productOfDay/"+ Main.currentUserId+"?date="+date);

       //  System.out.println(jsonArray.getJSONObject(1).get("connectionsList"));

        for (int i = 0; i < jsonArray.length(); i++) {
            productsOfCurrentDay.add(new Products(
                    jsonArray.getJSONObject(i).getString("name")
                    ,jsonArray.getJSONObject(i).getInt("calories")
                    ,jsonArray.getJSONObject(i).getDouble("protein")
                    ,jsonArray.getJSONObject(i).getDouble("fats")
                    ,jsonArray.getJSONObject(i).getDouble("carbohydrates")
                    ,jsonArray.getJSONObject(i).getInt("amount")
                    ));
        }
         sumMacrosAndUpdateLabels();
    }

    private Boolean checkIfDateAlreadyExist(String data)
    {
        if(daysObservableList.size()>0) {
            if (daysObservableList.get(daysObservableList.size() - 1).equals(data)) return true;
            else return false;
        }else return false;
    }

    private void resetMAcros()
    {
        proteinSum=0;
        fatsSum=0;
        carbsSum=0;
        caloriesSum=0;
    }

    private void sumMacrosAndUpdateLabels()
    {
        resetMAcros();

        productsOfCurrentDay.forEach((a) -> caloriesSum=caloriesSum+a.getKcalProduktu());
        productsOfCurrentDay.forEach((a) -> proteinSum=proteinSum+a.getBiałkoProduktu());
        productsOfCurrentDay.forEach((a) -> fatsSum=fatsSum+a.getTluszczeProduktu());
        productsOfCurrentDay.forEach((a) -> carbsSum=carbsSum+a.getWeglowodanyProduktu());

        sumCaloriesLabel.setText(caloriesSum+"");
        sumOfProteinLabel.setText(proteinSum+"");
        sumOfCarbsLabel.setText(carbsSum+"");
        sumOfFatsLabel.setText(fatsSum+"");
    }
    @FXML
    void dodajDzien() {
    try {

        SimpleDateFormat formated=new SimpleDateFormat("yyyy-MM-dd");
        String now=formated.format(new Date());

        if(!checkIfDateAlreadyExist(now)) {
            JSONObject jsonObject = new JSONObject().put("date", now);
            HttpRequests.sendJsonToApi(jsonObject, "http://localhost:8080/App/day/"+Main.currentUserId+"?date="+now);
            daysObservableList.add(now);
        }else ProductListController.ErrorAlert("Nowy dzień już został dodany");

    }catch (JSONException|IOException e){ e.printStackTrace(); }

    }

    private void convertJsonArrayToObservableList()
    {

        try {
        JSONArray jsonArray=HttpRequests.readJsonFromUrl("http://localhost:8080/App/days/"+Main.currentUserId);

        for (int i = 0; i < jsonArray.length(); i++) {

            daysObservableList.add(jsonArray.getJSONObject(i).getString("date"));
        }
            } catch (JSONException|IOException e) {
                e.printStackTrace();
            }

    }

    @FXML
    void deleteProduct()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            HttpRequests.deleteRequest("http://localhost:8080/App/productsOfDay/"+Main.currentUserId);
            jsonObject.put("name",URLEncoder.encode(tablewiew.getSelectionModel().getSelectedItem().toString(),"UTF-8"));
            jsonObject.put("day",daysComboBox.getSelectionModel().getSelectedItem().toString());
            jsonObject.put("amount",((Products)tablewiew.getSelectionModel().getSelectedItem()).getIloscProduktu());

            productsOfCurrentDay.remove(tablewiew.getSelectionModel().getSelectedItem());
        } catch (IOException|JSONException e) { e.printStackTrace(); }
    }



}


