package dbmgr.mySqlAccess;

public class Field{
    private String name;
    private String display;

    public Field(String name, String display) {
        this.name = name;
        this.display = display;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
