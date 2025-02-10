package com.example.expensestracker.model;

public class Budget {

    private int id;
    private double budget_target;
    private String start_date;
    private String end_date;
    private int user_id;

    public Budget(){

    }

    public Budget(int id, double budget_target, String start_date, String end_date, int user_id){
        this.id = id;
        this.budget_target = budget_target;
        this.start_date = start_date;
        this.end_date = end_date;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public double getBudget_target() {
        return budget_target;
    }
    public void setBudget_target(double budget_target) {
        this.budget_target = budget_target;
    }
    public String getStart_date(){
        return start_date;
    }
    public void setStart_date(String start_date){
        this.start_date = start_date;
    }
    public String getEnd_date(){
        return end_date;
    }
    public void setEnd_date(String end_date){
        this.end_date = end_date;
    }
    public int getUser_id(){
        return user_id;
    }
    public void setUser_id(int user_id){
        this.user_id = user_id;
    }

}