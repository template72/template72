package com.github.template72.compiler;

import com.github.template72.data.IDataMap;

public class TemplateIfCommand extends TemplateElseCommand implements TemplateElement {
	
	public TemplateIfCommand(String name, String notPrefix) {
	    super(name, notPrefix);
	}

	@Override
	public String render(IDataMap data) {
		return getElementsOfFirstMatchingBlock(data, 0).render(data);
	}
	
	@Override
	public String toString() {
		return "if " + getBlocks().get(0).getName();
	}
}
