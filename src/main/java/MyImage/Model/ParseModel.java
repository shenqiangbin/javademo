package MyImage.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class ParseModel {
    private String year;
    private String area;
    private List<Indicator> indicators;
    private List<ParseExcel> excels;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<Indicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<Indicator> indicators) {
        this.indicators = indicators;
    }

    public List<ParseExcel> getExcels() {
        return excels;
    }

    public void setExcels(List<ParseExcel> excels) {
        this.excels = excels;
    }
}
