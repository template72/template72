package com.github.template72;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.github.template72.codegenerator.FillDataCodeGeneratorTest;
import com.github.template72.compiler.TemplateCompilerBuilderTest;
import com.github.template72.compiler.TemplateCompilerTest;
import com.github.template72.compiler.TemplateIfCommandTest;
import com.github.template72.data.DataMapTest;
import com.github.template72.loader.ResourceTemplateLoaderTest;
import com.github.template72.syntax.TemplateSyntaxBuilderTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TemplateTest.class,
	TemplatesTest.class,
	TemplateCompilerBuilderTest.class,
	TemplateCompilerTest.class,
	TemplateIfCommandTest.class,
	DataMapTest.class,
	TemplateSyntaxBuilderTest.class,
	DefaultTemplateSyntaxTest.class,
	ResourceTemplateLoaderTest.class,
	FillDataCodeGeneratorTest.class,
})
public class AllTests {

}
