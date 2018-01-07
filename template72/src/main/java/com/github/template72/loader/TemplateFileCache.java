package com.github.template72.loader;

import java.util.HashMap;
import java.util.Map;

public class TemplateFileCache implements TemplateLoader {
	private final Map<String, String> cache = new HashMap<>();
	private TemplateLoader parentLoader;

	@Override
	public String loadTemplate(String filename) {
		String template = cache.get(filename);
		if (template == null) {
			template = parentLoader.loadTemplate(filename);
			cache.put(filename, template);
		}
		return template;
	}

	@Override
	public void setParentLoader(TemplateLoader parentLoader) {
		this.parentLoader = parentLoader;
	}
}
