package com.github.template72.compiler.preprocessor;

import com.github.template72.compiler.TemplateCompilerContext;
import com.github.template72.exceptions.TemplateStructureException;

public class CommentPreprocessor implements TemplatePreprocessor {
	private final TemplateCompilerContext ctx;
	private final TemplatePreprocessor next;
	
	public CommentPreprocessor(TemplateCompilerContext ctx, TemplatePreprocessor next) {
		if (ctx == null) {
			throw new IllegalArgumentException("ctx must not be null");
		}
		this.ctx = ctx;
		this.next = next;
	}

	@Override
	public String render(String template) {
		if (next != null) {
			template = next.render(template);
		}

		final String startTag = ctx.getSyntax().commentStart();
		final String endTag = ctx.getSyntax().commentEnd();
		int start = 0;
		int o = template.indexOf(startTag, start);
		if (o < 0) {
			return template;
		}
		StringBuilder sb = new StringBuilder();
		do {
			int end = template.indexOf(endTag, o + startTag.length());
			if (end < 0) {
				throw new TemplateStructureException("missing " + endTag);
			}
			sb.append(template.substring(start, o));
			start = end + endTag.length();

			o = template.indexOf(startTag, start);
		} while (o >= 0);
		sb.append(template.substring(start));
		return sb.toString();
	}
}
