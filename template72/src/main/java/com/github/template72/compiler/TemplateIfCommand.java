package com.github.template72.compiler;

import java.util.ArrayList;
import java.util.List;

import com.github.template72.data.IDataMap;

public class TemplateIfCommand implements TemplateElement, TemplateElementContainer {
	private final List<IfBlock> blocks = new ArrayList<>();
	private final String notPrefix;
	
	public TemplateIfCommand(String name, String notPrefix) {
		if (name == null || notPrefix == null || notPrefix.trim().isEmpty()) {
			throw new IllegalArgumentException();
		}
		blocks.add(new IfBlock(name));
		this.notPrefix = notPrefix;
	}

	public void addCondition(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		blocks.add(new IfBlock(name));
	}

	public void addConditionElse() {
		blocks.add(new IfBlock(null));
	}

	@Override
	public void add(TemplateElement element) {
		blocks.get(blocks.size() - 1).getElements().add(element);
	}

	@Override
	public String render(IDataMap data) {
		return getElementsOfFirstMatchingBlock(data).render(data);
	}
	
	private CompiledTemplate getElementsOfFirstMatchingBlock(IDataMap data) {
		for (IfBlock block : blocks) {
			String name = block.getName();
			if (name == null) { // else block
				return block.getElements();
			}
			boolean expected = true;
			if (name.startsWith(notPrefix)) {
				expected = false;
				name = name.substring(notPrefix.length());
			}
			if (data.getCondition(name).isTrue() == expected) {
				return block.getElements();
			}
		}
		return new CompiledTemplate();
	}
	
	@Override
	public String toString() {
		return "if " + blocks.get(0).getName();
	}
	
	public List<IfBlock> getBlocks() {
		return blocks;
	}

	public static class IfBlock {
		private final String name;
		private final CompiledTemplate elements = new CompiledTemplate();
		
		/**
		 * @param name null for else branch
		 */
		IfBlock(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public CompiledTemplate getElements() {
			return elements;
		}
	}
}
