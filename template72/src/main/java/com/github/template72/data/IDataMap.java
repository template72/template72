package com.github.template72.data;

public interface IDataMap extends IDataItem {
	
	IDataValue getValue(String name);
	
	IDataCondition getCondition(String name);
	
	IDataList getList(String name);
	
	IDataItem get(String name);
	
	void put(String name, IDataMap map);
}
