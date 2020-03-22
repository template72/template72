package com.github.template72.compiler;

import com.github.template72.compiler.preprocessor.CommentPreprocessor;
import com.github.template72.compiler.preprocessor.IncludePreprocessor;
import com.github.template72.compiler.preprocessor.MasterPreprocessor;
import com.github.template72.compiler.preprocessor.TemplatePreprocessor;
import com.github.template72.loader.ResourceTemplateLoader;
import com.github.template72.loader.TemplateLoader;
import com.github.template72.syntax.TemplateSyntax;
import com.github.template72.syntax.TemplateSyntaxBuilder;

public class TemplateCompilerBuilder {
	private TemplateSyntax syntax = null;
	private TemplateLoader loader = null;
	private boolean loading = true;
	private TemplatePreprocessor preprocessor = null;
	private boolean preprocessing = true;

	public TemplateCompilerBuilder withSyntax(TemplateSyntax syntax) {
		if (syntax == null) {
			throw new IllegalArgumentException("syntax must not be null");
		}
		this.syntax = syntax;
		return this;
	}

	public TemplateCompilerBuilder withSyntax(String start, String end) {
		this.syntax = new TemplateSyntaxBuilder(start, end).build();
		return this;
	}

	public TemplateCompilerBuilder withLoader(TemplateLoader loader) {
		if (loader == null) {
			throw new IllegalArgumentException("loader must not be null");
		}
		this.loader = loader;
		return this;
	}
	
	public TemplateCompilerBuilder withUTF8Loader() {
	    return withLoader(new ResourceTemplateLoader() {
	        @Override
            public String charsetName() {
                return "UTF-8";
            }
        });
	}

	/**
	 * @param path e.g. "/templates/"
	 * <br>At least "/" is recommended. Avoid "".
	 * @param suffix e.g. ".html"
	 * @return this
	 */
	public TemplateCompilerBuilder withLoader(String path, String suffix) {
		this.loader = new ResourceTemplateLoader(path, suffix);
		return this;
	}

	public TemplateCompilerBuilder withoutLoader() {
		loading = false;
		return this;
	}

	public TemplateCompilerBuilder withPreprocessor(TemplatePreprocessor preprocessor) {
		if (preprocessor == null) {
			throw new IllegalArgumentException("preprocessor must not be null");
		}
		this.preprocessor = preprocessor;
		preprocessing = true;
		return this;
	}

	public TemplateCompilerBuilder withoutPreprocessing() {
		preprocessor = null;
		preprocessing = false;
		return this;
	}

	public TemplateCompiler build() {
		TemplateCompilerContext ctx;
		if (syntax == null) {
			syntax = TemplateSyntax.DEFAULT;
		}
		if (loading) {
			if (loader == null) {
				loader = new ResourceTemplateLoader();
			}
			ctx = new TemplateCompilerContext(syntax, loader);
			if (preprocessor == null && preprocessing) {
				preprocessor = new CommentPreprocessor(ctx,
						new MasterPreprocessor(ctx,
						new IncludePreprocessor(ctx, null)));
			}
		} else {
			ctx = new TemplateCompilerContext(syntax, null);
			if (preprocessor == null && preprocessing) {
				preprocessor = new CommentPreprocessor(ctx, null);
			}
		}
		ctx.setPreprocessor(preprocessor);
		return new TemplateCompiler(ctx);
	}
}
