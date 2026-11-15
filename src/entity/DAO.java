

/**
 * Project: Lab2DonutShopChowdhury  Oak Donuts (OD) - Donut Shop Menu & Ordering System - DAO
 * Purpose Details:  Common CRUD methods (create, read, update, delete) that every DAO class must have
 * Course: CMPSC 221
 * Author: Daboraj Chowdhury
 * Date Developed: 11/10/2026
 * Last Date Changed: 11/14/2026
 * Revision: 1.0
 */









package entity;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> get(int id);
    List<T> getAll();
    void insert(T t);
    void update(T t);
    void delete(T t);
    List<String> getColumnNames();
}