package com.pao.project.elearning.model;
import java.util.Objects;

public final class CodCurs {
    private final String cod;

    public CodCurs(String cod) {
        this.cod = cod;
    }

    public String getCod() {
        return cod;
    }

    @Override
    public String toString() {
        return cod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CodCurs)) {
            return false;
        }

        CodCurs codCurs = (CodCurs) o;
        return Objects.equals(cod, codCurs.cod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cod);
    }
}