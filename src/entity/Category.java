

/**
 * Project: Lab2DonutShopChowdhury  Oak Donuts (OD) - Donut Shop Menu & Ordering System - Category
 * Purpose Details: The Categories for the different types of items we offer
 * Course: CMPSC 221
 * Author: Daboraj Chowdhury
 * Date Developed: 11/10/2026
 * Last Date Changed: 11/14/2026
 * Revision: 1.0
 */





package entity;

public enum Category {
    DONUTS("Donuts"),
    DRINKS("Drinks");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}