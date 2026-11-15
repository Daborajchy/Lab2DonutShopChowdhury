



/**
 * Project: Lab2DonutShopChowdhury  Oak Donuts (OD) - Donut Shop Menu & Ordering System - Donut
 * Purpose Details: What makes up the donut, its content
 * Course: CMPSC 221
 * Author: Daboraj Chowdhury
 * Date Developed: 11/10/2026
 * Last Date Changed: 11/14/2026
 * Revision: 1.0
 */




package entity;

public class Donut {
    private int id;
    private String name;
    private Category category;
    private DonutFlavors donutFlavors;
    private double price;
    private boolean inStock;

    public Donut(int id, String name, Category category, DonutFlavors donutFlavors, double price, boolean inStock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.donutFlavors = donutFlavors;
        this.price = price;
        this.inStock = inStock;
    }

    public Donut(String name, Category category, DonutFlavors donutFlavors, double price, boolean inStock) {
        this(0, name, category, donutFlavors, price, inStock);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public DonutFlavors getDonutFlavors() { return donutFlavors; }
    public void setDonutFlavors(DonutFlavors donutFlavors) { this.donutFlavors = donutFlavors; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isInStock() { return inStock; }
    public void setInStock(boolean inStock) { this.inStock = inStock; }

    @Override
    public String toString() {
        return name + " — $" + String.format("%.2f", price);
    }
}