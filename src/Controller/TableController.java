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
    @FXML Label sumCaloriesLabel;
    @FXML Label sumOfProteinLabel;
    @FXML Button addButton;
    @FXML Button deleteButton;

    ObservableList<String> daysObservableList=FXCollections.observableArrayList();


    ProductListController productListController;
    //List<ProductsORM> productsORMList;
    List productsORM;
    ObservableList<Products> productsOfCurrentDay =FXCollections.observableArrayList();
    ListProperty listofdaysPropList=new SimpleListProperty();

    int sumCalories =0;
    float sumprotein=0;


    public TableController(){


    }


    @FXML
    void initialize() {

        convertJsonArrayToObservableList();

        addButton.disableProperty().bind(amountTexTfield.textProperty().isEmpty().or(productNameTextField.textProperty().isEmpty()));
        sumOfProteinLabel.setText(sumprotein +"");
        sumCaloriesLabel.setText(sumCalories +"");


        daysComboBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                productsOfCurrentDay.clear();
                try {
                    fetchProductsOfDay(newValue.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//
//                sumOfProteinLabel.setText("0");
//                sumCaloriesLabel.setText("0");
//                sumprotein =0;
//                sumCalories =0;

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
            HttpRequests.sendJsonToApi(jsonObject,"http://localhost:8080/App/connection");
            productsOfCurrentDay.addAll(
                    ProductListController.listOfProductsObservable.filtered((a)-> a.getNazwaProduktu().equals(productNameTextField.getText())));
        }catch (JSONException|IOException e)
        {e.printStackTrace(); }
    }

     void fetchProductsOfDay(String date) throws IOException, JSONException {
       JSONArray jsonArray = HttpRequests.readJsonFromUrl("http://localhost:8080/App/productsOfDay?date="+date);

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
    }

     Boolean checkIfDateAlreadyExist(String data)
    {
        if(daysObservableList.size()>0) {
            if (daysObservableList.get(daysObservableList.size() - 1).equals(data)) return true;
            else return false;
        }else return false;
    }

    @FXML
    void dodajDzien() {
    try {

        SimpleDateFormat formated=new SimpleDateFormat("yyyy-MM-dd");
        String now=formated.format(new Date());

        if(!checkIfDateAlreadyExist(now)) {
            JSONObject jsonObject = new JSONObject().put("date", now);
            HttpRequests.sendJsonToApi(jsonObject, "http://localhost:8080/App/day");
            daysObservableList.add(now);
        }else ProductListController.ErrorAlert("Nowy dzień już został dodany");

    }catch (JSONException|IOException e){ e.printStackTrace(); }

    }

    void convertJsonArrayToObservableList()
    {
        try {
        JSONArray jsonArray=HttpRequests.readJsonFromUrl("http://localhost:8080/App/days");
        for (int i = 0; i < jsonArray.length(); i++) {

            daysObservableList.add(jsonArray.getJSONObject(i).getString("date"));
        }
            } catch (JSONException|IOException e) {
                e.printStackTrace();
            }

    }

    @FXML
    void usunDayProduct()
    {
      //  try {
//            HttpRequests.deleteRequest("http://localhost:8080/App/productsOfDay?name="
//                    + URLEncoder.encode(tablewiew.getSelectionModel().getSelectedItem().toString(),"UTF-8")
//                    +"&day="+daysComboBox.getSelectionModel().getSelectedItem().toString()+"&amount="+);
         //   System.out.println(((Products)tablewiew.getSelectionModel().getSelectedItem()).getIloscProduktu());
       // } catch (IOException e) { e.printStackTrace(); }
    }




    void pobierzListeProduktów(int id)
    {
//        productsOfCurrentDay.clear();
//        SessionFactory factory=HibernateSesionFactory.sesjaFactory;
//        Session sesja=factory.getCurrentSession();
//        sesja.beginTransaction();
//        DayORM pobrany=sesja.get(DayORM.class,id);
//        pobrany.getProducts().size();
//        productsORMList =pobrany.getProducts();
//        productsORMList = removeDuplicats(productsORMList);
//
//        for(ProductsORM produkt: productsORMList)
//        {
//
//            List<DayProductsLinking> pobranyDay_productsLinking = sesja
//                    .createQuery("select p from DayProductsLinking p where p.produkt_id ="+produkt.getId()+"and p.day_of_life_id="+id, DayProductsLinking.class).list();
//
//            for(int i = 0; i< pobranyDay_productsLinking.size(); i++) {
//                productsOfCurrentDay.add(new Products(produkt.getNazwa(), countCalories(pobranyDay_productsLinking.get(i).getIlosc(),produkt.getKcal())
//                        ,obliczbiałko(pobranyDay_productsLinking.get(i).getIlosc(),produkt.getBialko()), pobranyDay_productsLinking.get(i).getIlosc(),produkt));
//
//                sumCalories = sumCalories + countCalories(pobranyDay_productsLinking.get(i).getIlosc(),produkt.getKcal());
//                sumprotein = sumprotein +obliczbiałko(pobranyDay_productsLinking.get(i).getIlosc(),produkt.getBialko());
//                updateCounter();
//            }
//        }
//        sesja.getTransaction().commit();
//
    }

//    List removeDuplicats(List list)
//    {
//        HashSet nowalistya=new HashSet<ProductsORM>(list);
//        return new ArrayList(nowalistya);
//    }


//    List fetchDayList()
//    {
//        SessionFactory factory=HibernateSesionFactory.sesjaFactory;
//        Session sesja=factory.getCurrentSession();
//        sesja.beginTransaction();
//        List<DayORM> listaDni=sesja.createQuery("from DayORM").list();
//
//        sesja.getTransaction().commit();
//        return  listaDni;
//    }

    int countCalories(int amount, int calorific)
    {
        float wsp=(float) amount/100;
        float floatResult = wsp*calorific;

        return (int)floatResult;
    }

    int obliczbiałko(int ilosc,float bialko)
    {
        float wsp=(float) ilosc/100;
        float wynikfloat = wsp*bialko;

        return (int)wynikfloat;
    }

    void updateCounter()
    {
        sumCaloriesLabel.setText(sumCalories +"");
        sumOfProteinLabel.setText((int) sumprotein +"");
    }

}


