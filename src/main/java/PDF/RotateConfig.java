package PDF;

public class RotateConfig {
    private int page;
    private int rotate;

    public RotateConfig(int page, int rotate){
        this.page = page;
        this.rotate = rotate;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }
}
