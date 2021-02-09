package MyImage.Model;

import java.util.List;

public class ParseModel {
    private List<Indicator> indicators;
    private List<Excel> excels;


    public List<Indicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<Indicator> indicators) {
        this.indicators = indicators;
    }

    public List<Excel> getExcels() {
        return excels;
    }

    public void setExcels(List<Excel> excels) {
        this.excels = excels;
    }
}
