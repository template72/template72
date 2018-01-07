package com.github.template72.compiler;

import com.github.template72.data.IDataItem;
import com.github.template72.data.IDataList;
import com.github.template72.data.IDataMap;

public class TemplateEachCommand implements TemplateElement, TemplateElementContainer {
	private final String var;
	private final String listName;
	private final CompiledTemplate elements = new CompiledTemplate();
	
	public TemplateEachCommand(String var, String list) {
		this.var = var;
		this.listName = list;
	}

	@Override
	public void add(TemplateElement element) {
		elements.add(element);
	}

	@Override
	public String render(IDataMap data) {
		IDataList list = data.getList(listName);
		StringBuilder sb = new StringBuilder();
		IDataItem before = data.get(var); // remember (can only be a IDataMap or null)
		for (IDataMap o : list) {
			data.put(var, o);
			sb.append(elements.render(data));
		}
		if (before instanceof IDataMap) {
			data.put(var, (IDataMap) before); // restore
		}
		return sb.toString();
	}
	
	public String getVar() {
		return var;
	}

	public String getListName() {
		return listName;
	}

	public CompiledTemplate getElements() {
		return elements;
	}

	@Override
	public String toString() {
		return "each " + var + " in " + listName;
	}
}
