package com.github.template72.compiler;

import java.util.Iterator;

import com.github.template72.data.IDataMap;

public class CompiledTemplate implements Iterable<TemplateElement> {
	private Entry first;
	private Entry last;

	public void add(TemplateElement element) {
		Entry n = new Entry(element);
		if (first == null) {
			first = n;
		} else {
			last.next = n;
		}
		last = n;
	}

	public String render(IDataMap data) {
		if (data == null) {
			throw new IllegalArgumentException("data must not be null");
		}
		StringBuilder sb = new StringBuilder();
		Entry i = first;
		while (i != null) {
			i = i.render(data, sb);
		}
		return sb.toString();
	}
	
	static class Entry {
		TemplateElement element;
		Entry next;

		Entry(TemplateElement element) {
			this.element = element;
		}
		
		Entry render(IDataMap data, StringBuilder sb) {
			sb.append(element.render(data));
			return next;
		}
	}

	@Override
	public Iterator<TemplateElement> iterator() {
		return new ElementIterator(first);
	}
	
	static class ElementIterator implements Iterator<TemplateElement> {
		private Entry next;

		ElementIterator(Entry first) {
			next = first;
		}
		
		@Override
		public boolean hasNext() {
			return next != null && next.element != null;
		}

		@Override
		public TemplateElement next() {
			TemplateElement ret = next.element;
			next = next.next;
			return ret;
		}
	}
}
