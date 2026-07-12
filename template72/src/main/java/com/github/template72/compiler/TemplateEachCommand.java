package com.github.template72.compiler;

import com.github.template72.data.IDataItem;
import com.github.template72.data.IDataList;
import com.github.template72.data.IDataMap;
import com.github.template72.data.RememberDataItem;

public class TemplateEachCommand extends TemplateElseCommand implements TemplateElement {
	private final String var;
	private final String listName;
	
	public TemplateEachCommand(String var, String list, String notPrefix) {
        super("EACH", notPrefix);
        if (var == null || var.isBlank() || list == null || list.isBlank()) {
            throw new IllegalArgumentException();
        }
        this.var = var;
        this.listName = list;
	}

	@Override
	public String render(IDataMap data) {
        IDataList list = data.getList(listName);
        if (list.isEmpty()) {
            // else if, else ----
            return getElementsOfFirstMatchingBlock(data, 1/*omit EACH block*/).render(data);
        }
		
        // each ----
		IDataItem before = null;
		if (data instanceof RememberDataItem r) {
		    before = r._remember(var);
		}
        try {
            StringBuilder sb = new StringBuilder();
            CompiledTemplate elements = getBlocks().get(0).getElements();
            for (IDataMap o : list) {
                data.put(var, o);
                sb.append(elements.render(data));
            }
            return sb.toString();
        } finally {
            if (data instanceof RememberDataItem r) {
                r._restore(var, before);
            }
        }
	}
	
	public String getVar() {
		return var;
	}

	public String getListName() {
		return listName;
	}

	/**
	 * @return elements of the EACH block
	 */
	public CompiledTemplate getElements() {
		return getBlocks().get(0).getElements();
	}

	@Override
	public String toString() {
		return "each " + var + " in " + listName;
	}
}
