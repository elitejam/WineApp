package com.example.wineapp;

import android.database.Cursor;

import java.util.Locale;

/**
 * Wine
 *
 * This class encapsulates the information stored per type of wine in the database.
 */
public class Wine {
    public enum Color {
        RED,
        WHITE,
        AMBER,
        ROSEE,
        PINK
    }

    private int id;
    private String name;
    private String brand;
    private Color color;
    private double cost;
    private String grape_type; // could make enum?

    /**
     * Bare minimum constructor; other fields set to null
     *
     * @param id Unique primary key from database
     * @param name Name of the wine
     */
    Wine(int id, String name) {
        this.id(id);
        this.name(name);
    }

    /**
     * Full constructor
     *
     * @param name Name of the wine
     * @param brand Brand name of the wine
     * @param color Color of the wine
     * @param cost Price of wine per bottle
     * @param grape_type Type of grape in wine
     */
    Wine(int id, String name, String brand, Wine.Color color, double cost, String grape_type) {
        this.id(id);
        this.name(name);
        this.brand(brand);
        this.color(color);
        this.cost(cost);
        this.grape_type(grape_type);
    }

    /**
     * DB cursor constructor
     * @param c Cursor (from DB query result)
     */
    Wine(Cursor c) {
        // TODO: implement; this might be useful
        // TODO: make sure cursor has at least one result and take the first one (if multiple)? Need to decide how it will work.

        // e.g. super(id, name, brand, color);
    }

    /**
     * ID getter
     * @return unique id of this wine
     */
    public int id() {
        return this.id;
    }

    /**
     * ID setter; note, this is private (don't want just anybody to set this value)
     * @param id new id
     */
    private void id(int id) {
        this.id = id;
    }

    /**
     * Name getter
     * @return current name of wine
     */
    public String name() {
        return this.name;
    }

    /**
     * Name setter
     * @param name New wine name
     */
    public void name(String name) {
        this.name = name;
    }

    /**
     * Brand getter
     * @return brand name of this wine
     */
    public String brand() {
        return this.brand;
    }

    /**
     * Brand setter
     * @param brand new brand of wine
     */
    public void brand(String brand) {
        this.brand = brand;
    }

    /**
     * Color getter
     */
    public Wine.Color color() {
        return this.color;
    }

    /**
     * Color setter
     * @param color color of wine
     */
    public void color(Wine.Color color) {
        this.color = color;
    }

    /**
     * Cost getter
     * @return cost per bottle of wine
     */
    public double cost() {
        return this.cost;
    }

    /**
     * Cost setter
     * @param cost new cost per bottle of wine
     */
    public void cost(double cost) {
        this.cost = cost;
    }

    /**
     * Grape type getter
     * @return
     */
    public String grape_type() {
        return this.grape_type;
    }

    /**
     * Grape type setter
     * @param grape_type
     */
    public void grape_type(String grape_type) {
        this.grape_type = grape_type;
    }

    /**
     * Convert Wine object to string; mainly for debug
     * @return string of wine debug info
     */
    public String toString() {
        return String.format(
                Locale.ENGLISH,
                "[ Wine # %d | name (%s), brand (%s), color (%s), cost (%.2f), grape_type (%s) ]",
                this.id(),
                this.name(),
                this.brand(),
                this.color(),
                this.cost(),
                this.grape_type()
        );
    }
}
