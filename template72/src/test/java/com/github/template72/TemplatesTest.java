package com.github.template72;

import org.junit.Assert;
import org.junit.Test;

import com.github.template72.compiler.CompiledTemplates;
import com.github.template72.data.DataMap;
import com.github.template72.loader.ResourceTemplateLoader;

public class TemplatesTest {
	private boolean dev;
	
	@Test
	public void realWorldUse() {
		CompiledTemplates templates = new CompiledTemplates("page1", "page2", "page3");
	
		String html = templates.render("page1", getData1());

		Assert.assertEquals("<html>\r\n" + 
				"	<head>\r\n" + 
				"		<title>The page title</title>\r\n" + 
				"	</head>\r\n" + 
				"	<body>\r\n" + 
				"		<h1>The page title</h1>\r\n" + 
				"\r\n" + 
				"		<!-- content -->\r\n" + 
				"This is page 1.\r\n" + 
				"Version 1.0\r\n" + 
				"\r\n" + 
				"		\r\n" + 
				"		<hr>\r\n" + 
				"		&copy; 2017 by template72\r\n" + 
				"	</body>\r\n" + 
				"</html>\r\n", html);
		// and the same for page2, ...
	}

	@Test
	public void testFileCache() {
		Assert.assertEquals("precondition error", 0, ResourceTemplateLoader.getLoadOperations());
		dev = false;

		CompiledTemplates many = new CompiledTemplates("page1", "page2", "page3") {
			// Need to override devMode(). In practise you would possibly use CompiledTemplatesDev during development.
			@Override
			public boolean devMode() {
				return dev;
			}
		};
		
		DataMap map1 = getData1();
		many.render("page1", map1);
		many.render("page2", map1);
		many.render("page2", map1);
		many.render("page3", map1);

		DataMap map2 = getData2();
		many.render("page1", map2);
		many.render("page2", map2);

		Assert.assertEquals("Too many load operations!\r\nMust be 3 pages + 1 master + 1 include = 5\r\n",
				5, ResourceTemplateLoader.getLoadOperations());

		// now test developer mode
		dev = true;
		many.render("page1", map1);
		Assert.assertEquals(5 + 3, ResourceTemplateLoader.getLoadOperations());
		many.render("page1", map1);
		Assert.assertEquals(5 + 3 + 3, ResourceTemplateLoader.getLoadOperations());
	}

	private DataMap getData1() {
		DataMap map1 = new DataMap();
		map1.put("title", "The page title");
		map1.put("version", "1.0");
		return map1;
	}

	private DataMap getData2() {
		DataMap map2 = new DataMap();
		map2.put("title", "The page title");
		map2.put("version", "1.0");
		return map2;
	}
}
