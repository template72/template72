package com.github.template72.data;

public class DataValue implements IDataValue {
	private final String value;

	public DataValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value == null ? "" : value;
	}
}
