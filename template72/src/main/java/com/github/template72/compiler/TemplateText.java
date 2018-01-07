package com.github.template72.compiler;

import com.github.template72.data.IDataMap;

public class TemplateText implements TemplateElement {
	private final String text;

	public TemplateText(String text) {
		this.text = text;
	}

	@Override
	public String render(IDataMap data) {
		return text;
	}
	
	@Override
	public String toString() {
		return text;
	}
}
