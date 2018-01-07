package com.github.template72.codegenerator;

import org.junit.Assert;
import org.junit.Test;

import com.github.template72.compiler.CompiledTemplate;
import com.github.template72.compiler.TemplateCompiler;
import com.github.template72.compiler.TemplateCompilerBuilder;
import com.github.template72.loader.ResourceTemplateLoader;
import com.github.template72.syntax.TemplateSyntaxBuilder;

public class FillDataCodeGeneratorTest {

	@Test
	public void generate() {
		CompiledTemplate template = getCompiler().compileFile("index");
		String code = new FillDataCodeGenerator().generate(template);
		System.out.println(code);
		Assert.assertTrue(code.contains("\tsublist.forEach(it -> {\r\n" + 
				"\t\tDataMap map = sublistList.add(\"X\");"));
	}

	@Test
	public void fieldSep() {
		TemplateCompiler compiler = new TemplateCompilerBuilder()
				.withSyntax(new TemplateSyntaxBuilder().withFieldSeparator("->").build())
				.withLoader(getLoader()).build();
		CompiledTemplate template = compiler.compileFile("fieldsep");
		
		String code = new FillDataCodeGenerator().generate(template);
		
		System.out.println(code);
		Assert.assertTrue(code.contains("\"obj\""));
		Assert.assertTrue(code.contains("\"field\""));
	}
	
	/** Generate code for the CodeGenerator itself :-) */
	@Test
	public void self() {
		FillDataCodeGenerator codeGenerator = new FillDataCodeGenerator();
		System.out.println(codeGenerator.generate(codeGenerator.getOwnTemplate()));
	}

	private TemplateCompiler getCompiler() {
		return new TemplateCompilerBuilder().withLoader(getLoader()).build();
	}

	private ResourceTemplateLoader getLoader() {
		return new ResourceTemplateLoader("/templates/bookmarks/", ".html");
	}
}
