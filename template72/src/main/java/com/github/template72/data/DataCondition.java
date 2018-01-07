package com.github.template72.data;

public class DataCondition implements IDataCondition {
	private final boolean condition;
	
	public DataCondition(boolean condition) {
		this.condition = condition;
	}

	@Override
	public boolean isTrue() {
		return condition;
	}
}
