package com.github.template72.compiler;

import com.github.template72.data.IDataMap;

public interface TemplateElement {

	String render(IDataMap data);
}
