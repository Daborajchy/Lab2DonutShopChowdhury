
/**
 * Project: Lab2DonutShopChowdhury  Oak Donuts (OD) - Donut Shop Menu & Ordering System - Filling
 * Purpose Details: What type of personal filling someone wants, stores it and can be filtered by the user
 * Course: CMPSC 221
 * Author: Daboraj Chowdhury
 * Date Developed: 11/10/2026
 * Last Date Changed: 11/14/2026
 * Revision: 1.0
 */





package entity;

public enum Filling {
    NONE("None"),
    CUSTARD("Custard"),
    RED_JELLY("Red Jelly"),
    GREEN_JELLY("Green Jelly"),
    BLUE_JELLY("Blue Jelly"),
    VANILLA_CREAM("Vanilla Cream"),
    CHOCOLATE("Chocolate"),
    GUMMY_BEARS("Gummy Bears");

    private final String displayName;

    Filling(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}