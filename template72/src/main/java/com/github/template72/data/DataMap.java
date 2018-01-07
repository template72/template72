package com.github.template72.data;

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
	 * @param name
	 * @param value
	 * @return this
	 */
	public DataMap put(String name, String value) {
		addToObjects(name, new DataValue(value));
		return this;
	}

	public void putAll(Map<String, String> values) {
		for (Map.Entry<String, String> e : values.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	/**
	 * @param name
	 * @param condition
	 * @return this
	 */
	public DataMap put(String name, boolean condition) {
		addToObjects(name, new DataCondition(condition));
		return this;
	}

	/**
	 * Creates and adds a list.
	 * @param name
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
	 * @param name
	 * @return new created DataMap
	 */
	public DataMap map(String name) {
		DataMap map = new DataMap();
		put(name, map);
		return map;
	}
	// TODO map() or createObject()?
	public DataMap createObject(String name) {
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
}
