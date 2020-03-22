package com.github.template72.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class DataMapTest {

    @Test
    public void putInt() {
        DataMap map = new DataMap();
        map.putInt("x", 7123);
        
        Assert.assertEquals("7123", ((DataValue) map.get("x")).toString());
    }

    @Test
    public void putSize() {
        DataMap map = new DataMap();
        List<String> list = new ArrayList<>();
        list.add("x");
        map.putSize("n", list);
        
        Assert.assertEquals("1", ((DataValue) map.get("n")).toString());
    }

    @Test
    public void putHas_null() {
        DataMap map = new DataMap();
        map.putHas("johnDoe", null);
        
        Assert.assertFalse(((DataCondition) map.get("hasJohnDoe")).isTrue());
    }

    @Test
    public void putHas_arraylist() {
        DataMap map = new DataMap();
        map.putHas("johnDoe", new ArrayList<String>());
        List<String> list = new ArrayList<String>();
        list.add("a");
        map.putHas("FULL", list);

        Assert.assertFalse(((DataCondition) map.get("hasJohnDoe")).isTrue());
        Assert.assertTrue(((DataCondition) map.get("hasFULL")).isTrue());
    }

    @Test
    public void putHas_array() {
        DataMap map = new DataMap();
        map.putHas("johnDoe", new String[0]);
        map.putHas("full", new String[] { "a" });
        
        Assert.assertFalse(((DataCondition) map.get("hasJohnDoe")).isTrue());
        Assert.assertTrue(((DataCondition) map.get("hasFull")).isTrue());
    }

    @Test
    public void putHas_map() {
        DataMap map = new DataMap();
        map.putHas("johnDoe", new HashMap<String, String>());
        Map<String, Object> aMap = new HashMap<>();
        aMap.put("x", Integer.valueOf(1));
        map.putHas("map", aMap);
        
        Assert.assertFalse(((DataCondition) map.get("hasJohnDoe")).isTrue());
        Assert.assertTrue(((DataCondition) map.get("hasMap")).isTrue());
    }

    @Test
    public void putHas_string() {
        DataMap map = new DataMap();
        map.putHas("johnDoe", "");
        map.putHas("sue", "Sue");
        
        Assert.assertFalse(((DataCondition) map.get("hasJohnDoe")).isTrue());
        Assert.assertTrue(((DataCondition) map.get("hasSue")).isTrue());
    }

    @Test
    public void putHas_unsupportedType() {
        DataMap map = new DataMap();
        map.putHas("int", Integer.valueOf(0)); // Even a zero returns true because the type isn't supported.
        
        Assert.assertTrue(((DataCondition) map.get("hasInt")).isTrue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putHas_emptyName() {
        new DataMap().putHas("", "a");
    }
}
