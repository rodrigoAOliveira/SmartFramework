package com.arcthos.arcthosmart.shared.picklist;

import com.arcthos.arcthosmart.shared.picklist.constants.PicklistConstants;
import com.arcthos.arcthosmart.annotations.SObject;
import com.arcthos.arcthosmart.annotations.Sync;
import com.arcthos.arcthosmart.smartorm.SmartObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@SObject(PicklistConstants.PICKLIST)
public class Picklist extends SmartObject {

    @Sync(up = false, down = false)
    private String sobject;

    @Sync(up = false, down = false)
    private String fieldName;

    @Sync(up = false, down = false)
    private String active;

    @Sync(up = false, down = false)
    private String defaultValue;

    @Sync(up = false, down = false)
    private String label;

    @Sync(up = false, down = false)
    private String value;

    @Sync(up = false, down = false)
    private String validFor;

    public Picklist() {
        super(Picklist.class);
    }

    public String getCustomId() {
        if(sobject == null) {
            return "";
        }

        if(fieldName == null) {
            return "";
        }

        if(value == null) {
            return "";
        }

        return sobject + "_" + fieldName + "_" + value;
    }

    public String getSobject() {
        return sobject;
    }

    public void setSobject(String sobject) {
        this.sobject = sobject;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValidFor() {
        return validFor;
    }

    public void setValidFor(String validFor) {
        this.validFor = validFor;
    }
}
