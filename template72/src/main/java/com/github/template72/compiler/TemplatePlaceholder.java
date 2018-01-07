package com.github.template72.compiler;

import com.github.template72.data.IDataMap;

public class TemplatePlaceholder implements TemplateElement {
	private final String name;
	
	public TemplatePlaceholder(String name) {
		this.name = name;
	}

	@Override
	public String render(IDataMap data) {
		return data.getValue(name).toString();
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Placeholder: " + name;
	}
}
