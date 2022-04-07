package dbmgr.mySqlAccess.model;

public class Item {
    public String key;
    public Integer doc_count;

    public Item(){

    }

    public Item(String key, Integer doc_count) {
        this.key = key;
        this.doc_count = doc_count;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getDoc_count() {
        return doc_count;
    }

    public void setDoc_count(Integer doc_count) {
        this.doc_count = doc_count;
    }
}
