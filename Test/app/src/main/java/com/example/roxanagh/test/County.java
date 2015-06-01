package com.example.roxanagh.test;

/**
 * Created by roxanagh on 3/31/2015.
 */
public class County {
    Long id;
    String code;
    String description;

    public County() {}
    public County(Long id, String code, String description) {

        this.id = id;
        this.code = code;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
