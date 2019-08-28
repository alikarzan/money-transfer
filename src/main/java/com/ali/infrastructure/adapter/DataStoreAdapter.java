package com.ali.infrastructure.adapter;

import java.util.Collection;

import com.ali.common.ViewInfo;

public interface DataStoreAdapter<V extends ViewInfo> {
    void configure();
    V save(V v);
    V get(String id);
    Collection<V> getAll();

    
}