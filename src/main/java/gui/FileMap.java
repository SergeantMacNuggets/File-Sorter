package gui;

import javax.swing.DefaultListModel;
import java.util.HashMap;



public class FileMap extends HashMap<String, DefaultListModel<String>> {
    private static FileMap fileMap;
    private FileMap() {
//        put("Codes", new DefaultListModel<>() {{
//            addElement("c");
//            addElement("java");
//            addElement("cpp");
//            addElement("py");
//
//        }});
//        put("Documents", new DefaultListModel<>(){{
//            addElement("pptx");
//            addElement("docx");
//            addElement("pdf");
//        }});
//        put("Music", new DefaultListModel<>(){{
//            addElement("mp3");
//            addElement("wav");
//            addElement("flag");
//        }});
//        put("Video", new DefaultListModel<>() {{
//            addElement("avi");
//            addElement("mp4");
//            addElement("mkv");
//        }});
    }

    public static FileMap getInstance() {
        if(fileMap == null) {
            fileMap = new FileMap();
        }
        return fileMap;
    }

}

class SortMap extends HashMap<String, String> {

}
