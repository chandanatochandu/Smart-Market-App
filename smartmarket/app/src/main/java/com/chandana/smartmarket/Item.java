package com.chandana.smartmarket;

public class Item {
    private String name;
    private String barcode;
    private int cost;
    private int quantity;
    public Item(String name, String barcode,int cost, int quantity)
    {
        this.name=name;
        this.barcode=barcode;
        this.cost=cost;
        this.quantity=quantity;
    }

    public String getName()
    {
        return name;
    }
    public String getBarcode()
    {
        return barcode;
    }
    public int getCost()
    {
        return cost;
    }
    public int getQuantity()
    {
        return quantity;
    }
}
