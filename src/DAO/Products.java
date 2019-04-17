package DAO;

import javafx.beans.property.*;
import lombok.Data;


public class Products {

    private StringProperty nazwaProduktu;
    private IntegerProperty kcalProduktu;
    private DoubleProperty białkoProduktu;
    private IntegerProperty iloscProduktu;
    private DoubleProperty tluszczeProduktu;
    private DoubleProperty weglowodanyProduktu;




    public Products(String nazwaProduktu, int kcalProduktu, double białkoProduktu,double tluszcze, double weglowodany) {
        this.nazwaProduktu=new SimpleStringProperty(nazwaProduktu);
        this.kcalProduktu=new SimpleIntegerProperty(kcalProduktu);
        this.białkoProduktu=new SimpleDoubleProperty(białkoProduktu);
        this.tluszczeProduktu=new SimpleDoubleProperty(tluszcze);
        this.weglowodanyProduktu=new SimpleDoubleProperty(weglowodany);

    }

    public Products(String nazwaProduktu, int kcalProduktu, double białkoProduktu,double tluszcze, double weglowodany, int ilość) {

         this(nazwaProduktu,kcalProduktu,białkoProduktu,tluszcze,weglowodany);

        this.iloscProduktu=new SimpleIntegerProperty(ilość);

    }

    public String getNazwaProduktu() {
        return nazwaProduktu.get();
    }

    public StringProperty nazwaProduktuProperty() {
        return nazwaProduktu;
    }

    public void setNazwaProduktu(String nazwaProduktu) {
        this.nazwaProduktu.set(nazwaProduktu);
    }

    public int getKcalProduktu() {
        return kcalProduktu.get();
    }

    public IntegerProperty kcalProduktuProperty() {
        return kcalProduktu;
    }

    public void setKcalProduktu(int kcalProduktu) {
        this.kcalProduktu.set(kcalProduktu);
    }

    public double getBiałkoProduktu() {
        return białkoProduktu.get();
    }

    public DoubleProperty białkoProduktuProperty() {
        return białkoProduktu;
    }

    public void setBiałkoProduktu(double białkoProduktu) {
        this.białkoProduktu.set(białkoProduktu);
    }

    public int getIloscProduktu() {
        return iloscProduktu.get();
    }

    public IntegerProperty iloscProduktuProperty() {
        return iloscProduktu;
    }

    public void setIloscProduktu(int iloscProduktu) {
        this.iloscProduktu.set(iloscProduktu);
    }

    public double getTluszczeProduktu() {
        return tluszczeProduktu.get();
    }

    public DoubleProperty tluszczeProduktuProperty() {
        return tluszczeProduktu;
    }

    public void setTluszczeProduktu(double tluszczeProduktu) {
        this.tluszczeProduktu.set(tluszczeProduktu);
    }

    public double getWeglowodanyProduktu() {
        return weglowodanyProduktu.get();
    }

    public DoubleProperty weglowodanyProduktuProperty() {
        return weglowodanyProduktu;
    }

    public void setWeglowodanyProduktu(double weglowodanyProduktu) {
        this.weglowodanyProduktu.set(weglowodanyProduktu);
    }


    public String toString() {
        return nazwaProduktu.getValue();
    }
}
