package com.github.template72.compiler;

import java.util.Collection;
import java.util.Map;

import com.github.template72.data.IDataMap;
import com.github.template72.exceptions.MissingContentException;

public class TemplateSizeCommand implements TemplateElement {
    private final String var;

    public TemplateSizeCommand(String var) {
        if (var == null || var.isBlank()) {
            throw new IllegalArgumentException("size: argument must not be empty");
        }
        this.var = var;
    }

    @Override
    public String render(IDataMap data) {
        Object o = data.get(var);
        if (o == null) {
            throw new MissingContentException(var);
        } else if (o instanceof Map map) {
            return "" + map.size();
        } else if (o instanceof Collection c) {
            return "" + c.size();
        }
        throw new RuntimeException("Argument '" + var + "' must be a Collection or a Map.");
    }
    
    @Override
    public String toString() {
        return "size " + var;
    }
}
