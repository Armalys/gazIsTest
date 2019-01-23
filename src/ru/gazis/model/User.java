package ru.gazis.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String lastName;

    public User() {
    }

    public User(String lastName) {
        this(UUID.randomUUID().toString(), lastName);
    }

    public User(String name, String lastName) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(lastName, "lastName must not be null");
        this.name = name;
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!Objects.equals(name, user.name)) return false;
        return Objects.equals(lastName, user.lastName);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }
}
