package com.github.template72.compiler;

import java.util.ArrayList;
import java.util.List;

import com.github.template72.compiler.TemplateIfCommand.IfBlock;
import com.github.template72.data.IDataItem;
import com.github.template72.data.IDataList;
import com.github.template72.data.IDataMap;

public class TemplateEachCommand implements TemplateElement, TemplateElementContainer, ITemplateIfCommand {
	private final String var;
	private final String listName;
	private final String notPrefix;
    private final List<IfBlock> blocks = new ArrayList<>();
	
	public TemplateEachCommand(String var, String list, String notPrefix) {
		this.var = var;
		this.listName = list;
		this.notPrefix = notPrefix;
        blocks.add(new IfBlock("each"));
	}

	@Override
    public void add(TemplateElement element) {
        blocks.get(blocks.size() - 1).getElements().add(element);
	}

	@Override
	public String render(IDataMap data) {
        IDataList list = data.getList(listName);
        if (list.isEmpty()) {
            for (int i = 1; i < blocks.size(); i++) {
                IfBlock block = blocks.get(i);
                String name = block.getName();
                if (name == null) { // else block
                    return block.getElements().render(data);
                }
                boolean expected = true;
                if (name.startsWith(notPrefix)) {
                    expected = false;
                    name = name.substring(notPrefix.length());
                }
                IDataItem item = data.get(name);
                if (TemplateIfCommand.evaluate(item, name, data) == expected) {
                    return block.getElements().render(data);
                }
            }
            return "";
        }
		
		StringBuilder sb = new StringBuilder();
		IDataItem before = data.get(var); // remember (can only be a IDataMap or null)
		var elements = blocks.get(0).getElements();
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

	/**
	 * @return elements of the EACH block
	 */
	public CompiledTemplate getElements() {
		return blocks.get(0).getElements();
	}

	@Override
	public String toString() {
		return "each " + var + " in " + listName;
	}

    @Override
    public void addCondition(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        blocks.add(new IfBlock(name));
    }

    @Override
    public void addConditionElse() {
        blocks.add(new IfBlock(null));
    }
}
