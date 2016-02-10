package com.supprojectstarter.dao;

import java.util.List;

public abstract class DAO<T> {
    public abstract void insert(T obj);

    public abstract void update(T obj);

    public abstract T find(int id);

    public abstract T findByName(String string);

    public abstract List<T> findAll();
}
