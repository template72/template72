package com.github.template72.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.github.template72.exceptions.MissingContentException;
import com.github.template72.syntax.TemplateSyntax;

public class DataMap implements IDataMap {
	private final Map<String, IDataItem> objects = new HashMap<>();
	
	@Override
	public IDataItem get(String name) {
		int o = name.indexOf(TemplateSyntax.internalFieldSep);
		if (o > 0) {
			IDataItem map = objects.get(name.substring(0, o));
			if (map instanceof DataMap) {
				return ((DataMap) map).get(name.substring(o + TemplateSyntax.internalFieldSep.length()));
			} else {
				throw new MissingContentException(name);
			}
		}
		return objects.get(name);
	}

	@Override
	public IDataValue getValue(String name) {
		IDataItem any = get(name);
		if (any instanceof DataValue) {
			return (DataValue) any;
		}
		throw new MissingContentException(name);
	}

	@Override
	public IDataCondition getCondition(String name) {
		IDataItem any = get(name);
		if (any instanceof DataCondition) {
			return (DataCondition) any;
		}
		throw new MissingContentException(name);
	}

	@Override
	public IDataList getList(String name) {
		IDataItem any = get(name);
		if (any instanceof DataList) {
			return (DataList) any;
		}
		throw new MissingContentException(name);
	}

	/**
	 * @param name member name that represents the value
	 * @param value String value
	 * @return this
	 */
	public DataMap put(String name, String value) {
		addToObjects(name, new DataValue(value));
		return this;
	}

	/**
	 * @param values map
	 * @return this
	 */
	public DataMap putAll(Map<String, String> values) {
		for (Map.Entry<String, String> e : values.entrySet()) {
			put(e.getKey(), e.getValue());
		}
		return this;
	}

	/**
	 * @param name member name that represents the value
	 * @param condition true or false
	 * @return this
	 */
	public DataMap put(String name, boolean condition) {
		addToObjects(name, new DataCondition(condition));
		return this;
	}

	/**
	 * Creates and adds a list.
	 * @param name name of the list
	 * @return new created DataList
	 */
	public DataList list(String name) {
		DataList list = new DataList();
		addToObjects(name, list);
		return list;
	}
	
	/**
	 * Creates and adds a sub-object.
	 * 
	 * @param name name of the sub-map
	 * @return new created DataMap
	 */
	public DataMap map(String name) {
		DataMap map = new DataMap();
		put(name, map);
		return map;
	}

	/**
	 * Creates and adds a sub-object.
	 * 
	 * @param name name of the sub-object
	 * @return new created DataMap
	 */
	public DataMap createObject(String name) { // Alias method to map(). I don't know yet which method will survive.
		DataMap map = new DataMap();
		put(name, map);
		return map;
	}
	
	@Override
	public void put(String name, IDataMap map) {
		addToObjects(name, map);
	}

	private void addToObjects(String name, IDataItem item) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("name must not be empty");
		}
		objects.put(name, item);
	}
	
	public DataMap putInt(String name, int number) {
	    put(name, "" + number);
	    return this;
	}
	
	public DataMap putSize(String name, Collection<?> collection) {
	    putInt(name, collection == null ? 0 : collection.size());
        return this;
	}
	
	/**
	 * Does a <code>put("hasName", false)</code> for name="name" if o is null or empty.
	 * @param name at least 1 char
	 * @param o should be a Collection, String or Map
	 * @return this
	 */
	public DataMap putHas(final String name, final Object o) {
	    if (name == null || name.isEmpty()) {
	        throw new IllegalArgumentException("name must not be empty");
	    }
        String has = "has" + name.substring(0, 1).toUpperCase() + name.substring(1);
        if (o == null) {
            put(has, false);
        } else if (o instanceof Collection<?>) { // List, Set, ...
            put(has, !((Collection<?>) o).isEmpty());
	    } else if (o instanceof String) {
	        put(has, !((String) o).isEmpty());
        } else if (o instanceof Map<?,?>) {
            put(has, !((Map<?,?>) o).isEmpty());
        } else if (o instanceof Object[]) { // array
            put(has, ((Object[]) o).length > 0);
	    } else { // It's not null. It's not empty or type is not supported for empty check.
	        put(has, true);
	    }
        return this;
	}
	
	public void clear() {
	    objects.clear();
	}
	
	public boolean isEmpty() {
	    return objects.isEmpty();
	}
	
	public int size() {
	    return objects.size();
	}
}
