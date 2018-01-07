package com.github.template72.compiler.preprocessor;

import java.util.regex.Matcher;

import com.github.template72.compiler.TemplateCompilerContext;

public class MasterPreprocessor implements TemplatePreprocessor {
	private final TemplateCompilerContext ctx;
	private final TemplatePreprocessor next;

	public MasterPreprocessor(TemplateCompilerContext ctx, TemplatePreprocessor next) {
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

		Matcher m = ctx.getSyntax().masterPattern(template);
		if (m.find() && m.start() == 0) {
			String master = render(ctx.loadTemplate(m.group(1))); // recursive
			template = master.replace(ctx.getSyntax().content(), template.substring(m.end()));
		}
		return template;
	}
}
