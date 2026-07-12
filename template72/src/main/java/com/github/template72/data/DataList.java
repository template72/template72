package com.github.template72.data;

import java.util.ArrayList;
import java.util.Comparator;

public class DataList extends ArrayList<IDataMap> implements IDataList {
	
	public DataMap add() {
		DataMap map = (DataMap) DataFactory.factory.createDataMap();
		add(map);
		return map;
	}

	/**
	 * ignore case, ascending sort order
	 * @param fieldName -
	 */
	public void sort(String fieldName) {
	    sort(fieldName, true, true);
	}

	/**
	 * @param fieldName -
	 * @param ignoreCase false: do not ignore case
	 * @param asc false: descending sort order
	 */
    public void sort(String fieldName, boolean ignoreCase, boolean asc) {
        Comparator<IDataMap> c = (a, b) -> {
            String strA = a.get(fieldName).toString();
            String strB = b.get(fieldName).toString();
            return ignoreCase ? strA.compareToIgnoreCase(strB) : strA.compareTo(strB);
        };
        sort(asc ? c : c.reversed());
    }
    
    /**
     * Ignore case German sorting
     * @param fieldName -
     * @param asc false: descending sort order
     */
    public void sortGerman(String fieldName, boolean asc) {
        Comparator<IDataMap> c = (a, b) -> german(a, fieldName).compareTo(german(b, fieldName));
        sort(asc ? c : c.reversed());
    }
    
    private String german(IDataMap map, String fieldName) {
        return map.get(fieldName).toString().toLowerCase()
                .replace("ä", "ae")
                .replace("ö", "oe")
                .replace("ü", "ue")
                .replace("ß", "ss");
    }
    
    public void removeLast() {
        int n = size();
        if (n > 0) {
            remove(n - 1);
        }
    }
}
