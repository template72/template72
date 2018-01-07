package com.github.template72.compiler;

import com.github.template72.compiler.preprocessor.TemplatePreprocessor;
import com.github.template72.loader.TemplateLoader;
import com.github.template72.syntax.TemplateSyntax;

/**
 * Everything the TemplateCompiler needs (TemplateSyntax + optional TemplateLoader + TemplatePreprocessor). Also used by TemplatePreprocessors.
 */
public class TemplateCompilerContext {
	private final TemplateSyntax syntax;
	private TemplateLoader specifiedLoader; // optional
	private TemplatePreprocessor preprocessor; // optional
	private TemplateLoader firstLoader;
	
	public TemplateCompilerContext(TemplateSyntax syntax, TemplateLoader loader) {
		if (syntax == null) {
			throw new IllegalArgumentException("syntax must not be null");
		}
		this.syntax = syntax;
		this.specifiedLoader = loader;
	}

	public TemplateSyntax getSyntax() {
		return syntax;
	}
	
	public boolean hasLoader() {
		return specifiedLoader != null;
	}

	public String loadTemplate(String filename) {
		if (specifiedLoader == null) {
			throw new UnsupportedOperationException("No TemplateLoader was specified. Can not load file: " + filename);
		}
		if (firstLoader != null) {
			firstLoader.setParentLoader(specifiedLoader);
			return firstLoader.loadTemplate(filename);
		}
		return specifiedLoader.loadTemplate(filename);
	}

	public void setFirstLoader(TemplateLoader firstLoader) {
		this.firstLoader = firstLoader;
	}

	public void setPreprocessor(TemplatePreprocessor preprocessor) {
		this.preprocessor = preprocessor;
	}

	public String preprocess(String template) {
		if (preprocessor != null) {
			return preprocessor.render(template);
		}
		return template;
	}
}
