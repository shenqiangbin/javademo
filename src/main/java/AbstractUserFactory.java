public abstract class AbstractUserFactory {
    private String user;
    protected String language;

    public String GetUser() {
        return this.user;
    }

    public abstract void setLanguage();
}


class Chinese extends AbstractUserFactory {

    @Override
    public void setLanguage() {
        this.language = "chinese";
    }
}

class EnglishPerson extends  AbstractUserFactory{

    @Override
    public void setLanguage() {
        this.language = "english";
    }
}
