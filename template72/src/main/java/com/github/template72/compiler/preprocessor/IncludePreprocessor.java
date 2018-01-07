package com.github.template72.compiler.preprocessor;

import java.util.regex.Matcher;

import com.github.template72.compiler.TemplateCompilerContext;

public class IncludePreprocessor implements TemplatePreprocessor {
	private final TemplateCompilerContext ctx;
	private final TemplatePreprocessor next;

	public IncludePreprocessor(TemplateCompilerContext ctx, TemplatePreprocessor next) {
		if (ctx == null) {
			throw new IllegalArgumentException("ctx must not be null");
		} else if (!ctx.hasLoader()) {
			throw new IllegalArgumentException(getClass().getSimpleName() + " can not be used because no loader is specified");
		}
		this.ctx = ctx;
		this.next = next;
	}

	@Override
	public String render(String template) {
		if (next != null) {
			template = next.render(template);
		}
		
		StringBuilder sb = new StringBuilder();
		int nextStart = 0;
		Matcher m = ctx.getSyntax().includePattern(template);
		while (m.find()) {
			sb.append(template.substring(nextStart, m.start()));
			sb.append(render(ctx.loadTemplate(m.group(1)))); // recursive
			nextStart = m.end();
		}
		sb.append(template.substring(nextStart));
		return sb.toString();
	}
}
