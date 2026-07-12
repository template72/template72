package com.github.template72.data;

public class DataFactory {
    public static DataFactory factory = new DataFactory();
    
    public IDataValue createDataValue(String value) {
        return new DataValue(value);
    }
    
    public IDataCondition createDataCondition(boolean condition) {
        return new DataCondition(condition);
    }
    
    public IDataList createDataList() {
        return new DataList();
    }
    
    public IDataMap createDataMap() {
        return new DataMap();
    }
}
