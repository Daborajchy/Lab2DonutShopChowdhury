
/**
 * Project: Lab2DonutShopChowdhury  Oak Donuts (OD) - Donut Shop Menu & Ordering System - Donut_Tables.sql
 * Purpose Details:  Database tables for menu items, orders and order lines (Apache Derby)
 * Course: CMPSC 221
 * Author: Daboraj Chowdhury
 * Date Developed: 11/10/2026
 * Last Date Changed: 11/14/2026
 * Revision: 1.0
 */




-- Menu items: donuts and drinks
CREATE TABLE OD_Menu_Item (
                              Item_ID     INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              Item_Name   VARCHAR(60)  NOT NULL,
                              Category    VARCHAR(20)  NOT NULL,
                              Flavor      VARCHAR(30)  NOT NULL,
                              Price       DECIMAL(6,2) NOT NULL,
                              In_Stock    BOOLEAN      NOT NULL
);

-- Orders: one row per order with its transaction ID, date/time and totals
CREATE TABLE OD_Orders (
                           Order_ID        INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           Transaction_ID  VARCHAR(20)  NOT NULL UNIQUE,
                           Order_Date_Time TIMESTAMP    NOT NULL,
                           Customer_Name   VARCHAR(60),
                           Subtotal        DECIMAL(8,2) NOT NULL,
                           Tax             DECIMAL(8,2) NOT NULL,
                           Order_Total     DECIMAL(8,2) NOT NULL
);

-- Order lines: the items in each order (deleted automatically with their order)
CREATE TABLE OD_Order_Line (
                               Line_ID        INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               Transaction_ID VARCHAR(20)  NOT NULL,
                               Item_Name      VARCHAR(60)  NOT NULL,
                               Options        VARCHAR(120),
                               Quantity       INT          NOT NULL,
                               Unit_Price     DECIMAL(6,2) NOT NULL,
                               FOREIGN KEY (Transaction_ID) REFERENCES OD_Orders(Transaction_ID) ON DELETE CASCADE
);