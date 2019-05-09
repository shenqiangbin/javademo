package fileDemo;

import common.P;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BrowseHelper {

    private List<String> allowExts;
    private String content;

    public BrowseHelper(){
        this.content = "";
        this.allowExts = new ArrayList<>();
    }

    public BrowseHelper(String content,String... ext){

        this.content = content;
        allowExts = new ArrayList<>();

        for (String s : ext) {
            allowExts.add(s);
        }
    }

    public BrowseHelper(String... ext){
        allowExts = new ArrayList<>();

        for (String s : ext) {
            allowExts.add(s);
        }
    }

    public static void main(String[] args) {
        BrowseHelper helper = new BrowseHelper();
        List<TreeItem> list = helper.browse("d:\\");
        helper.printList(list);
    }

    public List<TreeItem> browse(String path){
        if(StringUtils.isEmpty(path)){
            return getRoot();
        }
        else{
            return browseFile(path);
        }
    }

    private List<TreeItem> getRoot() {

        List<TreeItem> list = new ArrayList<>();

        File[] roots = File.listRoots();
        for (int i = 0; i < roots.length; i++) {
            File item = roots[i];
            list.add(new TreeItem(
                    item.getAbsolutePath(),
                    item.toString().replace("\\",""),
                    isLeaf(item)));
        }

        return list;
    }

    public void printList(List<TreeItem> list) {
        for (TreeItem item : list) {
            P.print(item);
        }
    }

    private boolean isLeaf(File file) {
        File[] files = file.listFiles();
        if (files == null || files.length == 0)
            return true;
        else
            return  false;
    }

    private String getIcon(File file){
        if(file.isDirectory())
            return "my-dir";
        else
            return "my-file";
    }

    private List<TreeItem> browseFile(String path) {

        List<TreeItem> list = new ArrayList<>();

        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles(new BrowseFileFilter(allowExts,content));
            if (files != null && files.length != 0) {
                for (File file2 : files) {
                    list.add(
                            new TreeItem(
                                file2.getAbsolutePath(),
                                file2.getName(),
                                isLeaf(file2),
                                getIcon(file2)
                            )
                    );
                    //browseFile(file2.getAbsolutePath());
                }
            }
        } else {
            //nothing
        }

        return list;
    }


    public static String getFieExt(File file){
        return file.getName().substring(file.getName().lastIndexOf('.'));
    }
}
