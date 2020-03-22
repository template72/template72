package com.github.template72.compiler;

import java.util.HashMap;
import java.util.Map;

import com.github.template72.data.IDataMap;
import com.github.template72.loader.TemplateFileCache;
import com.github.template72.loader.TemplateLoader;

public class CompiledTemplates {
	private final Map<String, CompiledTemplate> compiledTemplates = new HashMap<>();
	private final TemplateCompiler compiler;
	private boolean development = false;
	
	public CompiledTemplates(String ... templateFilenames) {
		this(false, templateFilenames);
	}

	public CompiledTemplates(boolean development, String ... templateFilenames) {
		this(new TemplateCompilerBuilder().build(), new TemplateFileCache(), development, templateFilenames);
	}

	public CompiledTemplates(TemplateCompiler compiler, TemplateLoader cache, boolean development, String ... templateFilenames) {
		this.compiler = compiler;
		this.development = development;

		if (!devMode()) {
			this.compiler.setFirstLoader(cache);
			for (String templateFilename : templateFilenames) {
				try {
                    compiledTemplates.put(templateFilename, this.compiler.compileFile(templateFilename));
                } catch (RuntimeException e) {
                    handleException(templateFilename, e);
                }
			}
		}
	}
	
	protected void handleException(String templateFilename, RuntimeException e) { // Template method
	    throw e;
	}
	
	public String render(String templateFilename, IDataMap data) {
		CompiledTemplate compiledTemplate;
		if (devMode()) {
			compiler.setFirstLoader(null);
			compiledTemplate = compiler.compileFile(templateFilename);
		} else {
			compiledTemplate = compiledTemplates.get(templateFilename);
			if (compiledTemplate == null) {
				// This only happens after switching development from false to true to false.
				compiledTemplate = compiler.compileFile(templateFilename);
				compiledTemplates.put(templateFilename, compiledTemplate);
			}
		}
		return compiledTemplate.render(data);
	}
	
	public void setDevMode(boolean development) {
		this.development = development;
	}
	
	// may be overwritten
	public boolean devMode() {
		return development;
	}
}
