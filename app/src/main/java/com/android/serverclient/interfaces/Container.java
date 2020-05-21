package com.android.serverclient.interfaces;

import java.util.List;

public interface Container<DataType> {
    void update(List<DataType> updated);
}