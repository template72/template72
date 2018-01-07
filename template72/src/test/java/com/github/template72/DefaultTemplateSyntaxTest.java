package com.github.template72;

import org.junit.Assert;
import org.junit.Test;

import com.github.template72.syntax.TemplateSyntax;

public class DefaultTemplateSyntaxTest {
	private static final TemplateSyntax syntax = TemplateSyntax.DEFAULT;
	
	@Test
	public void baseSyntax() {
		Assert.assertEquals("{{", syntax.start());
		Assert.assertEquals("}}", syntax.end());
		Assert.assertEquals(".", syntax.fieldSep());
	}

	@Test
	public void varPatterns() {
		Assert.assertTrue(syntax.varPattern("a").matches());
		Assert.assertTrue(syntax.varPattern("aa.bb").matches());
		Assert.assertTrue(syntax.varPattern("aZ_09").matches());
		Assert.assertFalse(syntax.varPattern("aZ 09").matches());
		Assert.assertFalse(syntax.varPattern(" abc ").matches());
		Assert.assertFalse(syntax.varPattern("\tabc\t").matches());
		Assert.assertFalse(syntax.varPattern("a\tb").matches());
		Assert.assertFalse(syntax.varPattern("-").matches());
		Assert.assertFalse(syntax.varPattern("a->b").matches());
		Assert.assertFalse(syntax.varPattern("a::b").matches());
		Assert.assertTrue(syntax.varPattern("_z").matches());
		
		// even those are acceptable:
		Assert.assertTrue(syntax.varPattern("1").matches());    
		Assert.assertTrue(syntax.varPattern("1k").matches());
		Assert.assertTrue(syntax.varPattern("1.00").matches());
	}

	@Test
	public void ifPatterns() {
		Assert.assertTrue(syntax.ifPattern("if a.b").matches());
		Assert.assertEquals("not ", syntax.notPrefix());
		Assert.assertTrue(syntax.elseifPattern("else if a.b").matches());
		Assert.assertTrue(syntax.isElse("else"));
		Assert.assertTrue(syntax.isEndIf("/if"));
	}

	@Test
	public void eachPatterns() {
		Assert.assertTrue(syntax.eachPattern("each a in b").matches());
		Assert.assertTrue(syntax.isEndEach("/each"));
	}
	
	@Test
	public void masterPatterns() {
		Assert.assertTrue(syntax.masterPattern("{{master: c/a.b}}\r\n").matches());
	}
	
	@Test
	public void masterPatterns_wrongBegin() {
		Assert.assertFalse(syntax.masterPattern(" {{master: abc}}").matches());
	}
	
	@Test
	public void masterPatterns_wrongEnd() {
		Assert.assertFalse(syntax.masterPattern("{{master: abc}} ").matches());
	}
	
	@Test
	public void masterPatterns_linux() {
		Assert.assertTrue(syntax.masterPattern("{{master: c/a.b}}\n").matches());
	}
	
	@Test
	public void masterPatterns_illegalChar() {
		Assert.assertFalse(syntax.masterPattern("{{master: http://a.b/c}}\r\n").matches());
	}
	
	@Test
	public void includePatterns() {
		Assert.assertTrue(syntax.includePattern("{{include: c/a.b}}").matches());
		Assert.assertFalse(syntax.includePattern("{{include: http://a.b/c}}").matches());
	}

	@Test
	public void comments() {
		Assert.assertEquals("{{-- ", syntax.commentStart());
		Assert.assertEquals(" --}}", syntax.commentEnd());
	}
}
