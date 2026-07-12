package com.github.template72.compiler;

import java.util.ArrayList;
import java.util.List;

import com.github.template72.data.DataMap;
import com.github.template72.data.IDataCondition;
import com.github.template72.data.IDataItem;
import com.github.template72.data.IDataList;
import com.github.template72.data.IDataMap;
import com.github.template72.data.IDataValue;
import com.github.template72.exceptions.MissingContentException;

/**
 * Base class for IF and EACH commands. Containing support for ELSE-IF and ELSE.
 */
public abstract class TemplateElseCommand implements TemplateElementContainer {
    private final String notPrefix;
    private final List<IfBlock> blocks = new ArrayList<>();
    
    public TemplateElseCommand(String name, String notPrefix) {
        if (name == null || name.isBlank() || notPrefix == null || notPrefix.isBlank()) {
            throw new IllegalArgumentException();
        }
        this.notPrefix = notPrefix;
        blocks.add(new IfBlock(name));
    }

    @Override
    public void add(TemplateElement element) {
        blocks.get(blocks.size() - 1).getElements().add(element);
    }

    public void addCondition(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        blocks.add(new IfBlock(name));
    }

    public void addConditionElse() {
        blocks.add(new IfBlock(null));
    }
 
    protected final CompiledTemplate getElementsOfFirstMatchingBlock(IDataMap data, int start) {
        for (int i = start; i < blocks.size(); i++) {
            var block = blocks.get(i);
            String name = block.getName();
            if (name == null) { // else block
                return block.getElements();
            }
            boolean expected = true;
            if (name.startsWith(notPrefix)) {
                expected = false;
                name = name.substring(notPrefix.length());
            }
            IDataItem item = data.get(name);
            if (evaluate(item, name, data) == expected) {
                return block.getElements();
            }
        }
        return new CompiledTemplate();
    }
    
    private boolean evaluate(IDataItem item, String name, IDataMap data) {
        if (item instanceof IDataCondition) { // typical case of if command
            return data.getCondition(name).isTrue();
        } else if (item instanceof IDataList l) { // is list not empty?
            return !l.isEmpty();
        } else if (item instanceof IDataValue v) { // is String not empty?
            return v.toString() != null && !v.toString().isBlank();
        } else if (item instanceof DataMap map) {
            return !map.isEmpty();
        } else if (item == null) {
            throw new MissingContentException(name);
        } else {
            throw new RuntimeException("if: The Variable '" + name + "' has unexpected type " + item.getClass().getSimpleName());
        }
    }
    
    public List<IfBlock> getBlocks() {
        return blocks;
    }

    public static class IfBlock {
        private final String name;
        private final CompiledTemplate elements = new CompiledTemplate();
        
        /**
         * @param name null for else branch
         */
        IfBlock(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public CompiledTemplate getElements() {
            return elements;
        }
    }
}
