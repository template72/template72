package com.github.template72;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.github.template72.compiler.CompiledTemplate;
import com.github.template72.compiler.TemplateCompiler;
import com.github.template72.compiler.TemplateCompilerBuilder;
import com.github.template72.data.DataList;
import com.github.template72.data.DataMap;
import com.github.template72.data.DataValue;
import com.github.template72.data.IDataValue;
import com.github.template72.exceptions.MissingContentException;
import com.github.template72.exceptions.UnknownCommandException;
import com.github.template72.loader.ResourceTemplateLoader;
import com.github.template72.loader.TemplateLoader;
import com.github.template72.syntax.TemplateSyntax;
import com.github.template72.syntax.TemplateSyntaxBuilder;

public class TemplateTest {

	@Test
	public void short_coded_style() {
		Template template = Template.createFromFile("template1");
		template.put("name", "Alessio");
		template.put("address", "Massa, Italy");
	
		String result = template.render();
		
		Assert.assertEquals("name: Alessio\r\naddress: Massa, Italy\r\n", result);
	}

	@Test
	public void full_coded_style() {
		TemplateSyntax syntax = new TemplateSyntaxBuilder("${", "}").build();
		TemplateLoader loader = new ResourceTemplateLoader("/", ".txt");
		TemplateCompiler compiler = new TemplateCompilerBuilder().withSyntax(syntax).withLoader(loader).build();

		CompiledTemplate compiledTemplate = compiler.compileFile("velocity");
	
		DataMap data = new DataMap();
		data.put("name", "Carles");
		data.put("address", "Barcelona, Catalonia");
		
		String result = compiledTemplate.render(data);
		
		Assert.assertEquals("name: Carles\r\naddress: Barcelona, Catalonia\r\n", result);
	}

	@Test
	public void medium_coded_style() {
		TemplateCompiler compiler = new TemplateCompilerBuilder().withSyntax("${", "}").withLoader("/", ".txt").build();

		CompiledTemplate compiledTemplate = compiler.compileFile("velocity");
	
		DataMap data = new DataMap();
		data.put("name", "Carles");
		data.put("address", "Barcelona, Catalonia");
		
		String result = compiledTemplate.render(data);
		
		Assert.assertEquals("name: Carles\r\naddress: Barcelona, Catalonia\r\n", result);
	}

	@Test
	public void test_withMap() {
		Map<String, String> map = new HashMap<>();
		map.put("name", "Josef");
		map.put("address", "Duisburg, Germany");
		
		String result = Template.createFromString("name:    {{name}}\r\n" + 
					"address: {{address}}\r\n")
				.putAll(map)
				.render();
		
		Assert.assertEquals("name:    Josef\r\n" + 
				"address: Duisburg, Germany\r\n", result);
	}

	@Test
	public void test01() {
		DataMap data = new DataMap();
		data.put("name", "Sven");
		data.put("address", "Stockholm, Sweden");
		
		Template template = new Template();
		template.compileFile("test01");
		template.withData(data);
		String result = template.render();
		
		Assert.assertEquals("name:    Sven\r\n" + 
				"address: Stockholm, Sweden\r\n", result);
	}

	@Test(expected = MissingContentException.class)
	public void test_missingVar() {
		Map<String, String> map = new HashMap<>();
		map.put("address", "Stockholm, Sweden");
		
		Template template = new Template();
		template.compileFile("test01");
		template.putAll(map);
		template.render();
	}

	@Test
	public void test02() {
		TemplateSyntax syntax = new TemplateSyntaxBuilder("[[",  "]]").withFieldSeparator("->").withNotPrefix("!").withEndIf("fi").build();
		Template template = new Template(syntax);
		template.put("linux", false);
		DataMap person = template.map("person");
		person.put("name", "Gerard");
		person.put("address", "Paris, France");
		
		template.compile("name:    [[person->name]] (address: [[person->address]])[[if !linux]]\r\n[[fi]]");
		
		String result = template.render();
		
		Assert.assertEquals("name:    Gerard (address: Paris, France)\r\n", result);
	}

	@Test(expected = MissingContentException.class)
	public void test_missingObject() {
		Template template = new Template();
		template.compile("{{a.b}}\r\n");
		template.render();
	}

	@Test
	public void test_each() {
		Template template = new Template();
		template.compileFile("test_each");

		DataList list = template.list("list");

		DataMap person = list.add();
		person.put("name", "Gerard");
		person.put("address", "Cannes");

		person = list.add();
		person.put("name", "Det");
		person.put("address", "Berlin");
		
		String result = template.render();
		
		Assert.assertEquals("Header\r\n" + 
				"------\r\n" + 
				"   name:    Gerard\r\n" +
				"   address: Cannes\r\n" + 
				"   name:    Det\r\n" + 
				"   address: Berlin\r\n" + 
				"------\r\n" + 
				"footer\r\n" + 
				"", result);
	}

	@Test
	public void test_if() {
		Template template = new Template();
		template.compile("abc{{if cond}} yes{{/if}} def");

		template.put("cond", true);
		String result = template.render();
		Assert.assertEquals("abc yes def", result);

		template.put("cond", false);
		result = template.render();
		Assert.assertEquals("abc def", result);
	}

	@Test(expected = MissingContentException.class)
	public void test_if_condition_missing() {
		Template template = new Template();
		template.compile("{{if cond}}*{{/if}}");
		template.render();
	}

	@Test
	public void test_if_not() {
		Template template = new Template();
		template.compile("abc{{if not cond}} yes{{/if}} def");

		template.put("cond", true);
		String result = template.render();
		Assert.assertEquals("abc def", result);

		template.put("cond", false);
		result = template.render();
		Assert.assertEquals("abc yes def", result);
	}

	@Test
	public void test_elseif_not() {
		Template template = new Template();
		template.compile("A{{if c1}}B{{else if not c2}}*C*{{/if}}D");

		template.put("c1", false);
		template.put("c2", false);
		String result = template.render();
		Assert.assertEquals("A*C*D", result);

		template.put("c2", true);
		result = template.render();
		Assert.assertEquals("AD", result);
	}

	@Test
	public void test_if_else() {
		Template template = new Template();
		template.compile("abc{{if cond}} yes{{else}} not{{/if}} def");

		template.put("cond", true);
		String result = template.render();
		Assert.assertEquals("abc yes def", result);

		template.put("cond", false);
		result = template.render();
		Assert.assertEquals("abc not def", result);
	}

	@Test
	public void test_elseif() {
		Template template = new Template();
		template.compile("abc"
				+ "{{if cond}} yes"
				+ "{{else if cond2}} BBB"
				+ "{{else if cond3}}_ccc"
				+ "{{else}} not"
				+ "{{/if}} def");

		template.put("cond", true);
		String result = template.render();
		Assert.assertEquals("abc yes def", result);

		template.put("cond", false);
		template.put("cond2", true);
		result = template.render();
		Assert.assertEquals("abc BBB def", result);

		template.put("cond", false);
		template.put("cond2", false);
		template.put("cond3", true);
		result = template.render();
		Assert.assertEquals("abc_ccc def", result);

		template.put("cond3", false);
		result = template.render();
		Assert.assertEquals("abc not def", result);
	}

	@Test
	public void test_no_else() {
		Template template = new Template();
		template.compile("abc"
				+ "{{if switch_1}} yes"
				+ "{{else if switch_2}} 2nd branch"
				+ "{{/if}} def");

		template.put("switch_1", false);
		template.put("switch_2", false);
		String result = template.render();
		Assert.assertEquals("abc def", result);
	}
	
	@Test
	public void test_if_if() {
		Template template = new Template();
		template.compile("abc{{if cond}} 0{{if cond_2}}AAA {{else}}BBB {{/if}}{{else if PLUS}}+{{else}}_{{/if}}def");

		template.put("cond", true);
		template.put("cond_2", false);
		template.put("PLUS", false);
		String result = template.render();
		Assert.assertEquals("abc 0BBB def", result);

		template.put("cond_2", true);
		result = template.render();
		Assert.assertEquals("abc 0AAA def", result);

		template.put("cond", false);
		template.put("PLUS", true);
		result = template.render();
		Assert.assertEquals("abc+def", result);

		template.put("PLUS", false);
		result = template.render();
		Assert.assertEquals("abc_def", result);
	}
	
	@Test
	public void test_each_if() {
		Template template = new Template();
		template.compile("persons:{{each it in persons}}"
				+ "\r\n- {{it.title}} {{it.name}}"
				+ "{{if it.hasAddress}}\r\n\taddress: {{it.address}}{{/if}}"
				+ "{{/each}}\r\n(end)");

		DataList persons = template.list("persons");
		DataMap person = persons.add();
		person.put("name", "Miller");
		person.put("title", "Mr.");
		person.put("address", "Berchtesgaden");
		person.put("hasAddress", true);

		person = persons.add();
		person.put("name", "Williams");
		person.put("title", "Mrs.");
		person.put("address", "");
		person.put("hasAddress", false);
		
		String result = template.render();
		Assert.assertEquals("persons:\r\n- Mr. Miller\r\n\taddress: Berchtesgaden\r\n- Mrs. Williams\r\n(end)", result);
	}
	
	@Test
	public void test_if_each() {
		Template template = new Template();
		template.compile("{{if hasPersons}}persons:{{each it in persons}}"
				+ "\r\n- {{it.name}}"
				+ "{{/each}}{{else}}- no persons -{{/if}}");

		DataList persons = template.list("persons");
		persons.add().put("name", "Miller");
		persons.add().put("name", "Williams");

		template.put("hasPersons", !persons.isEmpty());
		
		String result = template.render();
		Assert.assertEquals("persons:\r\n- Miller\r\n- Williams", result);

		persons.clear();
		template.put("hasPersons", !persons.isEmpty());
		result = template.render();
		Assert.assertEquals("- no persons -", result);
	}
	
	@Test
	public void test_each_each() {
		Template template = new Template();
		template.compile("{{each P in persons}}"
				+ "\r\n- {{P.name}}{{each C in P.children}}, {{C.name}}{{/each}}"
				+ "{{/each}}");

		DataList persons = template.list("persons");
		putPerson(persons, "Miller", "Sue", "Anton");
		putPerson(persons, "Williams");
		
		String result = template.render();
		Assert.assertEquals("\r\n- Miller, Sue, Anton\r\n- Williams", result);
	}
	
	@Test
	public void test_each_each_arrow() {
		TemplateSyntax syntax = new TemplateSyntaxBuilder().withFieldSeparator("->").build();
		Template template = new Template(syntax);
		template.compile("{{if M->ok}}{{each P in persons}}"
				+ "\r\n- {{P->name}}{{each C in P->children}}, {{C->name}}{{/each}}"
				+ "{{/each}}{{else if not M->xx}}else if not{{/if}}");

		DataMap m = template.map("M");
		m.put("ok", true);
		DataList persons = template.list("persons");
		putPerson(persons, "Miller", "Sue", "Anton");
		putPerson(persons, "Williams");
		
		String result = template.render();
		Assert.assertEquals("\r\n- Miller, Sue, Anton\r\n- Williams", result);

		m.put("ok", false);
		m.put("xx", false);
		result = template.render();
		Assert.assertEquals("else if not", result);
	}
	
	private void putPerson(DataList persons, String name, String ... childrenNames) {
		DataMap person = persons.add();
		person.put("name", name);
		DataList children = person.list("children");
		for (String childName : childrenNames) {
			children.add().put("name", childName);
		}
	}
	
	@Test(expected = UnknownCommandException.class)
	public void test_illegalVarName() {
		Template template = new Template();
		template.compile("{{if écond}}yes{{/if}}");
		template.render();
	}

	@Test
	public void test_include() {
		DataMap data = new DataMap();
		data.put("title", "Info page");
		data.put("version", "1.0");
		
		Template template = new Template();
		template.compileFile("test_include");
		template.withData(data);
		String result = template.render();
		
		Assert.assertEquals("<head>\r\n"
				+ "<title>Info page</title>\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "\tVersion: 1.0\r\n"
				+ "</body>", result);
	}

	@Test
	public void test_2xinclude() {
		DataMap data = new DataMap();
		data.put("title", "M");
		
		Template template = new Template();
		template.compile("A{{include: includes/title}}B{{include: includes/title}}C");
		template.withData(data);
		String result = template.render();
		
		Assert.assertEquals("A<head>\r\n"
				+ "<title>M</title>\r\n"
				+ "</head>B"
				+ "<head>\r\n"
				+ "<title>M</title>\r\n"
				+ "</head>C", result);
	}

	@Test(expected = UnknownCommandException.class)
	public void backslash() {
		Template.createFromString("{{include: includes\\title}}");
	}

	@Test
	public void test_include_in_include() {
		DataMap data = new DataMap();
		data.put("title", "Q");
		data.put("version", "1.1");
		
		Template template = new Template();
		template.compile("A{{include: test_include}}B");
		template.withData(data);
		String result = template.render();
		
		Assert.assertEquals("A<head>\r\n" + 
				"<title>Q</title>\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n" + 
				"	Version: 1.1\r\n" + 
				"</body>B", result);
	}

	@Test
	public void test_use_start_tag() {
		Template template = new Template();
		template.put("formula", true);
		template.put("_start", "{{");
		String t = "{{if formula}}{{1, 2}, {4}}{{/if}}";
		template.compile(t.replace("{{", "{{_start}}"));
		String result = template.render();
		Assert.assertEquals(t, result);
	}

	@Test
	public void test_master() {
		DataMap data = new DataMap();
		data.put("title", "People list");
		DataList persons = data.list("persons");
		put(persons, "Alex", "Lead Developer");
		put(persons, "Alessio", "");
		put(persons, "Christian", "Developer");
		put(persons, "Kilian", "Developer");
		put(persons, "Marcus", "");
		
		Template template = new Template();
		template.compileFile("page");
		template.withData(data);
		String result = template.render();
		
		Assert.assertEquals("<html>\r\n" + 
				"	<head>\r\n" + 
				"		<title>People list</title>\r\n" + 
				"	</head>\r\n" + 
				"	<body>\r\n" + 
				"		<h1>People list</h1>\r\n" + 
				"\r\n" + 
				"		<!-- content -->\r\n" + 
				"		<table>\r\n" + 
				"			<tr>\r\n" + 
				"				<td>Alex</td>\r\n" + 
				"				<td>Lead Developer</td>\r\n" + 
				"			</tr>\r\n" + 
				"			<tr>\r\n" + 
				"				<td>Alessio</td>\r\n" + 
				"			</tr>\r\n" + 
				"			<tr>\r\n" + 
				"				<td>Christian</td>\r\n" + 
				"				<td>Developer</td>\r\n" + 
				"			</tr>\r\n" + 
				"			<tr>\r\n" + 
				"				<td>Kilian</td>\r\n" + 
				"				<td>Developer</td>\r\n" + 
				"			</tr>\r\n" + 
				"			<tr>\r\n" + 
				"				<td>Marcus</td>\r\n" + 
				"			</tr>\r\n" + 
				"		</table>\r\n" + 
				"		\r\n" + 
				"		<hr>\r\n" + 
				"		&copy; 2017 by template72\r\n" + 
				"	</body>\r\n" + 
				"</html>\r\n", result);
	}
	
	private void put(DataList persons, String name, String role) {
		DataMap person = persons.add();
		person.put("name", name);
		person.put("hasRole", !role.isEmpty());
		person.put("role", role);
	}
	
	@Test
	public void test_master_in_master() {
		DataMap data = new DataMap();
		data.put("title", "People list");
		data.put("subject", "Here is the people list");
		put(data.list("persons"), "Oliver", "");
		String result = Template.createFromFile("page_mim").withData(data).render();
		
		Assert.assertTrue("outer master is missing", result.contains("subject: Here"));
		Assert.assertTrue("inner master is missing", result.contains("<title>People list"));
		Assert.assertTrue("template content is missing", result.contains("<td>Oliver"));
	}
	
	@Test
	public void test_lazy() {
		DataMap map = new DataMap() {
			@Override // override top-level DataMap
			public IDataValue getValue(String name) {
				final String pattern = "_";
				if (name.startsWith(pattern)) { // You can define your own pattern.
					switch (name.substring(pattern.length())) {
					case "lazy1":
						// could be resource bundle access
						return new DataValue("RB-data-1");
					case "lazy.2": // Even the fieldSep char '.' is allowed.
						return new DataValue("RB-data-2");
					}
				}
				return super.getValue(name);
			}
		};
		map.put("notlazy", "dynamic-data");
		map.map("person").put("name", "Dynamic-Otto");

		String result = Template.createFromString("[{{notlazy}} / {{_lazy1}} / {{_lazy.2}} / {{person.name}}]").withData(map).render();
		Assert.assertEquals("[dynamic-data / RB-data-1 / RB-data-2 / Dynamic-Otto]", result);
	}

	@Test
	public void test_commentsRemoval() {
		Template template = Template.createFromFile("comments");
		template.put("value", "0");
	
		String result = template.render();
		
		Assert.assertFalse("comment start tag must not be in the result", result.contains("{{-- "));
		Assert.assertFalse("comment end tag must not be in the result", result.contains(" --}}"));
		Assert.assertFalse("comment content must not be in the result", result.contains("comment"));
		Assert.assertTrue("master content missing", result.contains("header"));
		Assert.assertTrue("footer missing", result.contains("footer"));
	}

	@Test
	public void testNullValue() {
		Template template = Template.createFromString("name: '{{name}}'");
		template.put("name", null);
		Assert.assertEquals("name: ''", template.render());
	}

	@Test(expected = MissingContentException.class)
	public void testMissingValue() {
		Template.createFromString("{{x}}").render();
	}

	@Test
	public void exceptionMustHaveFilename() {
		check_exceptionMustHaveFilename("broken1");
	}

	@Test
	public void exceptionMustHaveFilename_include() {
		check_exceptionMustHaveFilename("broken2"); // broken2 includes broken1
	}

	private void check_exceptionMustHaveFilename(final String file) {
		try {
			Template.createFromFile(file);
			Assert.fail("Exception expected");
		} catch (Exception e) {
			if (e.getMessage() == null || !e.getMessage().contains(file) || e.getMessage().contains("file not found")) {
				throw e;
			}
		}

	}
}
