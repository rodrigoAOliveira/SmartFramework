package com.arcthos.arcthosmart.smartorm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Vinicius Damiati on 06-Oct-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attributes implements Serializable {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
