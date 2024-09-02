package com.homework.notebookstable.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotebookService extends NotebookDAO {
    public static final String[] TABLE_FIELDS_TO_DISPLAY = {"Name", "Brand", "Country", "Cover", "PageType", "PageAmount"};
    public static final String TABLE_NAME = "notebooks_t";

    private Connection connection;
    private boolean isConnected;


    @Override
    public void connect() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            isConnected = true;
        } catch (SQLException e) {
            System.err.println("CONNECT TO DATABASE EXCEPTION");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            System.err.println("CANNOT FIND JDBC DRIVER");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void disconnect() {
        try {
            connection.close();
            isConnected = false;
        } catch (SQLException e) {
            System.err.println("DISCONNECT FROM DATABASE EXCEPTION");
            throw new RuntimeException(e);
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    private List<Notebook> executeQuery(String query) {
        try (Statement statement = connection.createStatement()) {
            statement.executeQuery(query);

            try (ResultSet row = statement.getResultSet()) {
                List<Notebook> notebooks = new ArrayList<>();

                while (row.next()) {
                    notebooks.add(new Notebook(row));
                }
                return notebooks;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getQueryFilter(String columnName, String[] conditions) {
        StringBuilder queryFilter = new StringBuilder("where ");
        queryFilter.append(columnName).append(" = ");

        for (int i = 0; i < conditions.length; i++) {
            if (i != 0) {
                queryFilter.append("or ")
                        .append(columnName)
                        .append(" = ");
            }
            queryFilter.append("'")
                    .append(conditions[i])
                    .append("'");
        }
        return queryFilter.toString();
    }

    public String getPageAmountFilter(String from, String to) {
        return String.format("where PageAmount >= %s and PageAmount <= %s", from, to);
    }

    @Override
    public List<Notebook> getAll() {
        String query = String.format("select * from %s;", TABLE_NAME);
        return executeQuery(query);
    }

    public List<Notebook> getAll(String filter) {
        String query = String.format("select * from %s %s;", TABLE_NAME, filter);
        return executeQuery(query);
    }

    public List<String> getData(String columnName) {
        try (Statement statement = connection.createStatement()) {
            String query = String.format("select distinct %s from %s;", columnName, TABLE_NAME);
            statement.executeQuery(query);

            try (ResultSet row = statement.getResultSet()) {
                List<String> result = new ArrayList<>();

                while (row.next()) {
                    result.add(row.getString(columnName));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getNotebooksAmount(String columnName) {
        try (Statement statement = connection.createStatement()) {
            String query = String.format(
                    "select %s, count(*) as NotebooksAmount" +
                            " from %s group by %s;",
                    columnName,
                    TABLE_NAME,
                    columnName
            );
            statement.executeQuery(query);

            try (ResultSet row = statement.getResultSet()) {
                List<String> result = new ArrayList<>();

                while (row.next()) {
                    result.add(row.getString(columnName)
                            + "!!!" + row.getString("NotebooksAmount"));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMaxNotebooksColumn(String columnName) {
        try (Statement statement = connection.createStatement()) {
            String query = String.format(
                    "select distinct %s, count(*) as NotebooksAmount from %s " +
                    "group by %s " +
                    "order by NotebooksAmount desc " +
                    "limit 1",
                    columnName,
                    TABLE_NAME,
                    columnName
            );
            statement.executeQuery(query);

            try (ResultSet row = statement.getResultSet()) {
                row.next();
                return row.getString(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMinNotebooksColumn(String columnName) {
        try (Statement statement = connection.createStatement()) {
            String query = String.format(
                    "select distinct %s, count(*) as NotebooksAmount from %s " +
                            "group by %s " +
                            "order by NotebooksAmount asc " +
                            "limit 1",
                    columnName,
                    TABLE_NAME,
                    columnName
            );
            statement.executeQuery(query);

            try (ResultSet row = statement.getResultSet()) {
                row.next();
                return row.getString(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean create(Notebook notebook) {
        try (Statement statement = connection.createStatement()) {
            String query = String.format(
                    "insert into %s (Name, Brand, Country, Cover, PageType, PageAmount) values %s;",
                    TABLE_NAME,
                    notebook.toStringWithoutId());
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            System.err.println("INSERT QUERY EXCEPTION");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Notebook notebook) {
        try (Statement statement = connection.createStatement()) {
            String query = String.format(
                    "update %s set " +
                            "Name = '%s', " +
                            "Brand = '%s', " +
                            "Country = '%s', " +
                            "Cover = '%s', " +
                            "PageType = '%s', " +
                            "PageAmount = %d " +
                            "where id = %d;",
                    TABLE_NAME,
                    notebook.getName(),
                    notebook.getBrand(),
                    notebook.getCountry(),
                    notebook.getCover(),
                    notebook.getPageType(),
                    notebook.getPageAmount(),
                    notebook.getId());
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            System.err.println("UPDATE QUERY EXCEPTION");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Statement statement = connection.createStatement()) {
            String query = String.format("delete from %s where id = %d;", TABLE_NAME, id);
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            System.err.println("DELETE QUERY EXCEPTION");
            e.printStackTrace();
            return false;
        }
    }

    public boolean isExist(Notebook notebook) {
        List<Notebook> notebooks = getAll();

        for (Notebook existingNotebook : notebooks) {
            if (notebook.equals(existingNotebook)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAnyEmptyInput(String... inputs) {
        for (String input : inputs) {
            if (input.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
