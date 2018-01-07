package com.github.template72;

import java.util.Map;

import com.github.template72.compiler.CompiledTemplate;
import com.github.template72.compiler.TemplateCompiler;
import com.github.template72.compiler.TemplateCompilerBuilder;
import com.github.template72.data.DataList;
import com.github.template72.data.DataMap;
import com.github.template72.syntax.TemplateSyntax;

public class Template {
	private final TemplateCompiler compiler;
	private CompiledTemplate compiledTemplate;
	private DataMap data = new DataMap();
	
	public Template() {
		this(new TemplateCompilerBuilder().build());
	}

	public Template(TemplateSyntax syntax) {
		this(new TemplateCompilerBuilder().withSyntax(syntax).build());
	}

	public Template(TemplateCompiler compiler) {
		this.compiler = compiler;
	}
	
	public static Template createFromString(String template) {
		return new Template().compile(template);
	}
	
	public static Template createFromFile(String filename) {
		return new Template().compileFile(filename);
	}
	
	public Template compile(String template) {
		compiledTemplate = compiler.compile(template);
		return this;
	}

	public Template compileFile(String filename) {
		compiledTemplate = compiler.compileFile(filename);
		return this;
	}

	public Template withData(DataMap data) {
		this.data = data;
		return this;
	}

	public Template put(String name, String value) {
		data.put(name, value);
		return this;
	}

	public Template putAll(Map<String, String> values) {
		data.putAll(values);
		return this;
	}

	public Template put(String name, boolean condition) {
		data.put(name, condition);
		return this;
	}

	public DataList list(String name) {
		return data.list(name);
	}

	public DataMap map(String name) {
		return data.map(name);
	}

	public DataMap createObject(String name) {
		return data.createObject(name);
	}

	public String render() {
		if (compiledTemplate == null) {
			throw new RuntimeException("There is no compiled template. Call compile() or compileFile().");
		}
		return compiledTemplate.render(data);
	}
}
