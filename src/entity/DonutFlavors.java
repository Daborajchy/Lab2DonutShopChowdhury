

/**
 * Project: Lab2DonutShopChowdhury  Oak Donuts (OD) - Donut Shop Menu & Ordering System - DonutFlavors
 * Purpose Details: Flavors of the donut that the user can filter and choose
 * Course: CMPSC 221
 * Author: Daboraj Chowdhury
 * Date Developed: 11/10/2026
 * Last Date Changed: 11/14/2026
 * Revision: 1.0
 */




package entity;

public enum DonutFlavors {
     NONE("None"),
     CHOCOLATE("Chocolate"),
     GLAZED("Glazed"),
     VANILLA("Vanilla"),
     BOSTON_CREAM_PIE("Boston Cream Pie"),
     STRAWBERRY("Strawberry"),
     LEMON("Lemon"),
     MINT("Mint"),
     PENN_STATE_NITANNY_LION("Penn State Nitanny Lion"),
     BLUEBERRY("Blueberry");

     private final String displayName;

     DonutFlavors(String displayName) {
          this.displayName = displayName;
     }

     @Override
     public String toString() {
          return displayName;
     }
}