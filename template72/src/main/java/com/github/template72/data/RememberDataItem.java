package com.github.template72.data;

public interface RememberDataItem {

    IDataItem _remember(String name);
    
    void _restore(String name, IDataItem item);
}
