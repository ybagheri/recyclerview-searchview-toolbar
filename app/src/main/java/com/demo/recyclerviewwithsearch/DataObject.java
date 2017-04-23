package com.demo.recyclerviewwithsearch;

public class DataObject {
    String heading,description;

    public String getHeading() {
        return heading;
    }

    public String getDescription(){
        return description;
    }

    public DataObject(String heading,String description) {
        this.heading = heading;
        this.description = description;
    }
}
