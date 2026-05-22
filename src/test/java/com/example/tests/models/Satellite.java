package com.example.tests.models;

public class Satellite {
    private String id;
    private String name;
    private String type; // "communication" или "imaging"
    private String state; // "ACTIVE", "DECOMMISSIONED" и т.д.

    // constructors, getters, setters
    public Satellite() {}
    public Satellite(String name, String type, String state) {
        this.name = name;
        this.type = type;
        this.state = state;
    }
    // геттеры и сеттеры для всех полей
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
}
