package com.homework.notebookstable.model;

import java.util.List;

public interface DAO<E, K> {

    String JDBC_URL = "jdbc:postgresql://localhost:6543/homework";
    String USERNAME = "admin";
    String PASSWORD = "admin";
    String JDBC_DRIVER = "org.postgresql.Driver";

    void connect();
    void disconnect();
    List<E> getAll();
    boolean create(E entity);
    boolean update(E entity);
    boolean delete(K key);
}
