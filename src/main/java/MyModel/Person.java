package MyModel;

import java.util.Date;
import java.util.Objects;

public class Person {
    private Integer id;
    private String name;
    private int number;
    private Date date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        final Person model = (Person) obj;
        return this.getId().equals(model.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId().toString(), getName());
    }
}
