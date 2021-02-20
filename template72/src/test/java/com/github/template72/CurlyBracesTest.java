package com.github.template72;

import org.junit.Assert;
import org.junit.Test;

public class CurlyBracesTest {

    @Test
    public void testSimple() {
        String result = Template.createFromString("println '{{hello}}'").put("hello", "Hello World!").render();
        
        Assert.assertEquals("println 'Hello World!'", result);
    }

    @Test
    public void testTrailing() {
        String result = Template.createFromString("if (a == b) { {{block}}} //").put("block", "a = 12").render();
        
        Assert.assertEquals("if (a == b) { a = 12} //", result);
    }

    @Test
    public void testLeading() {
        String result = Template.createFromString("if (a == b) {{{block}} } //").put("block", "a = 12").render();
        
        Assert.assertEquals("if (a == b) {a = 12 } //", result);
    }

    @Test
    public void testLeadingAndTrailing() {
        String result = Template.createFromString("if (a == b) {{{{block}}} //").put("block", "a = 12").render();
        
        Assert.assertEquals("if (a == b) {{a = 12} //", result);
    }
}
