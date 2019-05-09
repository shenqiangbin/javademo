package fileDemo;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

public class BrowseFileFilter implements FileFilter {

    private List<String> allowExts;
    private String content;

    public BrowseFileFilter(List<String> allowExts,String content) {
        this.allowExts = allowExts;
        this.content = content;
    }

    public List<String> getAllowExts() {
        return allowExts;
    }

    public void setAllowExts(List<String> allowExts) {
        this.allowExts = allowExts;
    }

    @Override
    public boolean accept(File file) {

        if(file.isHidden())
            return false;

        if(!file.canRead())
            return false;

        if(file.isDirectory())
            return true;

        if(content!=null && StringUtils.isNotEmpty(content)){
            if(file.getName().contains(content))
                return true;
            else
                return false;
        }

        if(allowExts!=null && allowExts.size()>0){
            String ext = BrowseHelper.getFieExt(file);
            if(allowExts.contains(ext))
                return true;
            else
                return false;
        }

        return true;
    }
}
