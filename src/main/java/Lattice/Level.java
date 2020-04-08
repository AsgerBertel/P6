package Lattice;

import java.util.Objects;

public class Level {
    private String name;
    private int rows;

    public Level(String name, int rows) {
        this.name = name;
        this.rows = rows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Level level = (Level) o;
        return rows == level.rows &&
                Objects.equals(name, level.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, rows);
    }
}
