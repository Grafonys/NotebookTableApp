package com.homework.notebookstable.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
@Getter
@AllArgsConstructor
public class Notebook {

    private long id;
    private String name;
    private String brand;
    private String country;
    private String cover;
    private String pageType;
    private int pageAmount;

    public Notebook(ResultSet row) {
        try {
            id = row.getLong("id");
            brand = row.getString("Brand");
            name = row.getString("Name");
            pageAmount = row.getInt("PageAmount");
            cover = row.getString("Cover");
            country = row.getString("Country");
            pageType = row.getString("PageType");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Notebook(String notebookString) {
        StringBuilder stroke = new StringBuilder(notebookString);
        stroke.deleteCharAt(0).deleteCharAt(stroke.length() - 1);
        String[] values = stroke.toString().split(", ");
        id = Long.parseLong(values[0]);
        name = validate(values[1]);
        brand = validate(values[2]);
        country = validate(values[3]);
        cover = validate(values[4]);
        pageType = validate(values[5]);
        pageAmount = Integer.parseInt(values[6]);
    }

    private String validate(String value) {
        if (value.charAt(0) == '\'') {
            StringBuilder temp = new StringBuilder(value);
            return temp.deleteCharAt(0)
                    .deleteCharAt(temp.length() - 1)
                    .toString();
        } else {
            return value;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (!(obj instanceof Notebook)) { return false; }
        Notebook that = (Notebook) obj;
        return this.name.equals(that.name)
                && this.brand.equals(that.brand)
                && this.country.equals(that.country)
                && this.cover.equals(that.cover)
                && this.pageType.equals(that.pageType)
                && this.pageAmount == that.pageAmount;
    }

    @Override
    public String toString() {
        return String.format("(%d, '%s', '%s', '%s', '%s', '%s', %d)",
                id, name, brand, country, cover, pageType, pageAmount);
    }

    public String toStringWithoutId() {
        return String.format("('%s', '%s', '%s', '%s', '%s', %d)",
                name, brand, country, cover, pageType, pageAmount);
    }
}
