package com.github.template72.compiler;

import java.util.Stack;
import java.util.regex.Matcher;

import com.github.template72.exceptions.TemplateStructureException;
import com.github.template72.exceptions.UnknownCommandException;
import com.github.template72.loader.TemplateLoader;
import com.github.template72.syntax.TemplateSyntax;

public class TemplateCompiler {
	private final TemplateCompilerContext ctx;
	private CompiledTemplate elements;
	private Stack<TemplateElement> parents;
	
	public TemplateCompiler(TemplateCompilerContext ctx) {
		if (ctx == null) {
			throw new IllegalArgumentException("ctx must not be null");
		}
		this.ctx = ctx;
	}
	
	public CompiledTemplate compileFile(String filename) {
		try {
			return compile(ctx.loadTemplate(filename));
		} catch (Exception e) {
			throw new RuntimeException("Error compiling template file '" + filename + "': " + e.getMessage(), e);
		}
	}
	
	public CompiledTemplate compile(String template) {
		template = ctx.preprocess(template);
		
		elements = new CompiledTemplate();
		parents = new Stack<>();
		final String start = ctx.getSyntax().start();
		final String end = ctx.getSyntax().end();

		// SEARCH FOR TAGS
		int o = template.indexOf(start);
		while (o >= 0) {
            while (template.startsWith(start, o + 1)) { // handle case "{{{var}}"
                o++;
            }
			if (o > 0) {
				addElement(new TemplateText(template.substring(0, o)));
			}
			o += start.length();
			int oo = template.indexOf(end, o);
			if (oo >= 0) {
				createCommand(template.substring(o, oo));
				template = template.substring(oo + end.length());
			} else {
				throw new TemplateStructureException("closing tag '" + end + "' is missing in template");
			}
			
			o = template.indexOf(start);
		}
		if (!template.isEmpty()) {
			addElement(new TemplateText(template));
		}
		
		CompiledTemplate ret = elements;
		elements = null;
		parents = null;
		return ret;
	}

	protected void createCommand(String cmd) {
		TemplateSyntax syntax = ctx.getSyntax();
		
		// EQUAL-CHECKS ----
		// ELSE
		if (syntax.isElse(cmd)) {
			getCurrentIf().addConditionElse();
			return;
		}
		
		// END-IF
		if (syntax.isEndIf(cmd)) {
			if (parents.isEmpty() || !(parents.peek() instanceof TemplateIfCommand)) {
				throw new TemplateStructureException("END-IF without previous IF found");
			}
			parents.pop();
			return;
		}
		
		// END-EACH
		if (syntax.isEndEach(cmd)) {
			if (parents.isEmpty() || !(parents.peek() instanceof TemplateEachCommand)) {
				throw new TemplateStructureException("END-EACH without previous EACH found");
			}
			parents.pop();
			return;
		}
		
		// PATTERN-CHECKS ----
		// VAR
		Matcher m = syntax.varPattern(cmd);
		if (m.matches()) {
			addElement(new TemplatePlaceholder(syntax.split(cmd)));
			return;
		}
		
		// IF
		m = syntax.ifPattern(cmd);
		if (m.matches()) {
			TemplateIfCommand c = new TemplateIfCommand(syntax.split(m.group(1)), syntax.notPrefix());
			addElement(c);
			parents.push(c);
			return;
		}

		// ELSE-IF
		m = syntax.elseifPattern(cmd);
		if (m.matches()) {
			getCurrentIf().addCondition(syntax.split(m.group(1)));
			return;
		}

		// EACH
		m = syntax.eachPattern(cmd);
		if (m.matches()) {
			int varIndex = 1;
			int listIndex = 2;
			TemplateEachCommand loop = new TemplateEachCommand(m.group(varIndex), syntax.split(m.group(listIndex)));
			addElement(loop);
			parents.push(loop);
			return;
		}
		
		throw new UnknownCommandException(cmd);
	}
	
	private void addElement(TemplateElement element) {
		if (parents.isEmpty()) {
			elements.add(element);
		} else {
			((TemplateElementContainer) parents.peek()).add(element);
		}
	}

	private TemplateIfCommand getCurrentIf() {
		if (parents.isEmpty() || !(parents.peek() instanceof TemplateIfCommand)) {
			throw new TemplateStructureException("ELSE or ELSE-IF without previous IF found");
		}
		return ((TemplateIfCommand) parents.peek());
	}
	
	public void setFirstLoader(TemplateLoader loader) {
		ctx.setFirstLoader(loader);
	}
}
