import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

// Basically carbon copy of our DataSource class in lab

public class DataSource {

    public static ObservableList<TestFile> getAllFiles(List<TestFile> fileList) {
        ObservableList<TestFile> files = FXCollections.observableArrayList();

        for(int i=0; i<fileList.size(); i++) {
            files.add(fileList.get(i));
        }

        return files;
    }
}
