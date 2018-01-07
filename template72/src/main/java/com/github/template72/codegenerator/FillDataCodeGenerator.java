package com.github.template72.codegenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.github.template72.compiler.CompiledTemplate;
import com.github.template72.compiler.TemplateCompilerBuilder;
import com.github.template72.compiler.TemplateEachCommand;
import com.github.template72.compiler.TemplateElement;
import com.github.template72.compiler.TemplateIfCommand;
import com.github.template72.compiler.TemplatePlaceholder;
import com.github.template72.data.DataList;
import com.github.template72.data.DataMap;
import com.github.template72.syntax.TemplateSyntax;

public class FillDataCodeGenerator {
	private final CompiledTemplate ownTemplate = new TemplateCompilerBuilder().withLoader("/templates/", "").build().compileFile("code");

	public String generate(String filename) {
		return generate(new TemplateCompilerBuilder().build().compileFile("index"));
	}

	public String generate(CompiledTemplate template) {
		return generate(template, null, "model");
	}

	private String generate(CompiledTemplate template, String removePrefix, String model) {
		List<FillDataCommand> commands = buildCommands(template, removePrefix, model);
		DataMap data = createModel(commands);
		return getOwnTemplate().render(data);
	}

	public CompiledTemplate getOwnTemplate() {
		return ownTemplate;
	}

	private DataMap createModel(List<FillDataCommand> commands) {
		DataMap model = new DataMap();
		DataList elementsList = model.list("elements");
		commands.forEach(command -> {
			DataMap map = elementsList.add();
			map.put("isVar", command.isVar()); // boolean
			map.put("isObjVar", command.isObjVar()); // boolean
			map.put("name", command.getName());
			map.put("model", command.getModel());
			map.put("comment", command.getComment());
			map.put("getter", command.getGetter());
			map.put("isEach", command.isEach()); // boolean
			map.put("lastName", command.getLastName());
			map.put("listVar", command.getListVar());
			map.put("call", command.getBlock());
		});
		return model;
	}
	
	/**
	 * @param template template elements
	 * @param removePrefix remove prefix if it exists
	 * @param model parent variable name
	 * @return fill data commands
	 */
	public List<FillDataCommand> buildCommands(CompiledTemplate template, String removePrefix, String model) {
		List<FillDataCommand> commands = new ArrayList<>();
		for (TemplateElement e : template) {
			if (e instanceof TemplatePlaceholder) {
				addVar(((TemplatePlaceholder) e).getName(), removePrefix, model, "", commands);

			} else if (e instanceof TemplateIfCommand) {
				addIfCommand(removePrefix, model, (TemplateIfCommand) e, commands);
			
			} else if (e instanceof TemplateEachCommand) {
				addEachCommand(removePrefix, model, (TemplateEachCommand) e, commands);
			}
		}
		return commands;
	}

	private void addVar(final String name, final String removePrefix, final String model, final String comment, List<FillDataCommand> commands) {
		FillDataCommand command = new FillDataCommand();
		command.setObjVar(name.contains(TemplateSyntax.internalFieldSep));
		if (command.isObjVar() && removePrefix != null && name.startsWith(removePrefix)) {
			command.setName(name.substring(removePrefix.length()));
		} else {
			command.setName(name);
		}
		command.setModel(model);
		command.setComment(comment);
		
		if (!commands.contains(command)) {
			commands.add(command);
		}
	}

	private void addIfCommand(String removePrefix, String model, TemplateIfCommand ifc, List<FillDataCommand> commands) {
		ifc.getBlocks().forEach(block -> {
			String name = block.getName();
			if (name != null) { // not a "else" block
				addVar(name, removePrefix, model, " // boolean", commands);
			}
			
			commands.addAll(
					buildCommands(block.getElements(), removePrefix, model).stream()
						.filter(command -> !commands.contains(command))
						.collect(Collectors.toList()));
		});
	}
	
	private void addEachCommand(String removePrefix, String model, TemplateEachCommand each, List<FillDataCommand> result) {
		String name = each.getListName();
		if (removePrefix != null && name.startsWith(removePrefix)) {
			name = name.substring(removePrefix.length());
		}
		
		FillDataCommand command = new FillDataCommand();
		command.setEach(true);
		command.setName(name);
		command.setModel(model);
		command.setListVar(each.getVar());
		command.setBlock(makeBlock(each));
		result.add(command);
	}
	
	private String makeBlock(TemplateEachCommand each) {
		String text = generate(each.getElements(), each.getVar() + TemplateSyntax.internalFieldSep, "map");
		return Arrays.asList(text.split("\r\n")).stream().map(a -> "\t" + a + "\r\n").collect(Collectors.joining());
	}
}
