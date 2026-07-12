package com.github.template72.data;

import java.util.ArrayList;

public class DataList extends ArrayList<IDataMap> implements IDataList {
	
	public DataMap add() {
		DataMap map = (DataMap) DataFactory.factory.createDataMap();
		add(map);
		return map;
	}
}
