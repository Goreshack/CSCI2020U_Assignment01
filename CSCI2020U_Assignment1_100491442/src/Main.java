

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/*
*
*
* Made by: Andrew Dale
* Made in: IntelliJ IDEA 2017
*
* Date: March 4, 2018
*
* TA: Martin
*
*
* */

public class Main extends Application {

    // Global initialization of different objects
    static ArrayList<String> words = new ArrayList<>();
    static List<TestFile> results = new ArrayList<>();
    TextField path = new TextField();
    TextField accuracyField = new TextField();
    TextField precisionField = new TextField();
    TextField load = new TextField();


    double accu = 0;
    double prec = 0;
    int corGuesses = 0;
    int incGuesses = 0;
    int correctSpamGuesses = 0;
    String accuracyString, precisionString;




    // Hashmaps
    static Map<String, Integer> trainHamFreq = new HashMap<>();
    static Map<String, Integer> trainSpamFreq = new HashMap<>();
    static Map<String, Float> hamProb = new HashMap<>();
    static Map<String, Float> spamProb = new HashMap<>();
    static Map<String, Float> probability = new HashMap<>();

    TableColumn<TestFile, String> filenameColumn = new TableColumn("File");
    TableColumn<TestFile, String> classColumn = new TableColumn("Actual Class");
    TableColumn<TestFile, Double> probColumn = new TableColumn("Spam Probability");

    TableView table = new TableView();


    // Clears all objects of value. Allows user to test another set given the
    // pathing remains the same -- the largest design flaw.
    public void clearTable(ActionEvent event){
        table.getItems().clear();
        trainHamFreq.clear();
        trainSpamFreq.clear();
        hamProb.clear();
        spamProb.clear();
        probability.clear();
        words.clear();
        results.clear();

        accu = 0;
        prec = 0;
        corGuesses = 0;
        incGuesses = 0;
        correctSpamGuesses = 0;

        accuracyString = "";
        precisionString = "";

        accuracyField.setText(String.valueOf(accuracyString));
        precisionField.setText(String.valueOf(precisionString));
        path.setText("");
    }

    // Stage init
    public void start(Stage primaryStage) throws Exception{

        // Non-resizable to keep table consistent and window pretty
        primaryStage.setResizable(false);
        // Main pane.
        Pane root = new Pane();
        // A weeb's dream come true.
        primaryStage.setTitle("Martin's Dream: Comic Sans&Pikachu&SpamFightingMachine");
        primaryStage.setScene(new Scene(root, 800, 625));
        primaryStage.getIcons().add(new Image("pikaCute.png"));

        // Styling our pane with CSS sheet
        root.getStylesheets().add("Style.css");

        // Choose directory button which initializes training/test of data.
        Button chooseDir = new Button("Choose Dir...");
        chooseDir.setPrefWidth(100);
        chooseDir.setOnAction(e -> spamCheck(e));

        // Clear data button (duh)
        Button clear = new Button("Clear Data");
        clear.setOnAction(e -> clearTable(e));
        clear.getStyleClass().add("exit-button");

        // No text over Pikachu
        table.setPlaceholder(new Label(""));
        path.setPrefWidth(240);
        // Can't play with this one Martin
        path.setEditable(false);

        // Our main stage layout
        BorderPane layout = new BorderPane();
        root.getChildren().add(layout);


        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(5, 0, 5, 0));
        hbox.getChildren().add(chooseDir);
        hbox.getChildren().add(path);
        hbox.getChildren().add(clear);
        layout.setTop(hbox);


        layout.setPadding(new Insets(0, 0, 0,5));
        layout.setCenter(table);
        table.setMinHeight(500);
        table.setItems(DataSource.getAllFiles(results));


        TableColumn<TestFile, String> filenameColumn = new TableColumn("File");
        TableColumn<TestFile, String> classColumn = new TableColumn("Actual Class");
        TableColumn<TestFile, Double> probColumn = new TableColumn("Spam Probability");

        filenameColumn.setMinWidth(350);
        classColumn.setMinWidth(100);
        probColumn.setMinWidth(350);

        // Cell factories
        filenameColumn.setCellValueFactory(new PropertyValueFactory<TestFile, String>("filename"));
        classColumn.setCellValueFactory(new PropertyValueFactory<TestFile, String>("actualClass"));
        probColumn.setCellValueFactory(new PropertyValueFactory<TestFile, Double>("spamProbability"));

        // Populating table with columns
        table.getColumns().addAll(filenameColumn, classColumn, probColumn);

        
        GridPane bottom = new GridPane();
        bottom.setPadding(new Insets(20,0,20,10));
        bottom.setVgap(10); bottom.setHgap(10);
        layout.setBottom(bottom);



        // Bottom labels and textfields
        Label accuracyLabel = new Label("Accuracy: ");
        bottom.add(accuracyLabel, 0,0);
        bottom.add(accuracyField, 1, 0);
        Label precisionLabel = new Label("Precision: ");
        bottom.add(precisionLabel, 0, 1);
        bottom.add(precisionField, 1, 1);


        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    //checking for spam
    public void spamCheck(ActionEvent e) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(null);
        if(mainDirectory != null) {
            path.setText(mainDirectory.getAbsolutePath());
            File spamFolder = new File(mainDirectory + "/train/spam");
            File hamFolder = new File(mainDirectory + "/train/ham");
            File[] spamFiles = spamFolder.listFiles();
            File[] hamFiles = hamFolder.listFiles();
            DecimalFormat df = new DecimalFormat("0.00000");
            //spam training
            if (spamFolder.length() > 0 && hamFolder.length() > 0) {
                for (int i = 0; i < spamFiles.length; i++) {
                    try {
                        Scanner scanner = new Scanner(spamFiles[i]);
                        while (scanner.hasNext()) {
                            load.setText(String.valueOf(spamFiles[i]));
                            String nextWord = scanner.next();
                            addToMap(nextWord, trainSpamFreq);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                //ham training
                for (int i = 0; i < hamFiles.length; i++) {
                    try {
                        Scanner scanner = new Scanner(hamFiles[i]);
                        while (scanner.hasNext()) {
                            load.setText(String.valueOf(hamFiles[i]));
                            String nextWord = scanner.next();
                            addToMap(nextWord, trainHamFreq);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                Iterator it;
                int wordFreq;

                // Spam probability
                it = trainSpamFreq.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    wordFreq = (Integer) pair.getValue();
                    float prob = (float) wordFreq / spamFiles.length;
                    df.format(prob);
                    spamProb.put((String) pair.getKey(), prob);
                }

                // Ham probability
                it = trainHamFreq.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    wordFreq = (Integer) pair.getValue();
                    float prob = (float) wordFreq / hamFiles.length;
                    df.format(prob);
                    hamProb.put((String) pair.getKey(), prob);
                }

                //probability
                for (int i = 0; i < words.size(); i++) {
                    if (hamProb.containsKey(words.get(i)) &&
                            spamProb.containsKey(words.get(i))) {
                        float prob = spamProb.get(words.get(i)) /
                                (spamProb.get(words.get(i)) +
                                        hamProb.get(words.get(i)));
                        df.format(prob);
                        probability.put(words.get(i), prob);
                    }
                }

                spamDetect(mainDirectory);
            }
            else{
                path.setText("Invalid Directory");
                System.out.println("Ensure you're selecting the correct directory (Data)");

            }
        }
        else{
            path.setText("Invalid Directory");
            System.out.println("Ensure you're selecting the correct directory");
        }
    }

    public void spamDetect(File mainDirectory) {
        File spamFolder = new File(mainDirectory + "/test/spam");
        File hamFolder = new File(mainDirectory + "/test/ham");
        if(spamFolder.exists() && hamFolder.exists()) {
            File[] spamFiles = spamFolder.listFiles();
            File[] hamFiles = hamFolder.listFiles();

            // Detection for Ham Files
            for (int i = 0; i < hamFiles.length; i++) {
                try {
                    double n = 0;
                    Scanner scanner = new Scanner(hamFiles[i]);
                    while (scanner.hasNext()) {
                        String nextWord = scanner.next();
                        n += addToRes(nextWord);
                    }

                    // Probability given file is spam
                    double spamProb = 1 / (1 + Math.pow(Math.E, n));
                    DecimalFormat df = new DecimalFormat("0.00000");

                    // Correct or Incorrect count update
                    if (spamProb < 0.5) {
                        corGuesses++;
                    } else if (spamProb > 0.5) {
                        incGuesses++;
                    }
                    df.format(spamProb);
                    TestFile nextFile;
                    nextFile = new TestFile(hamFiles[i].getName(), df.format(spamProb), "Ham");
                    results.add(nextFile);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            // Spam Detection
            for (int i = 0; i < spamFiles.length; i++) {
                try {
                    double n = 0;
                    Scanner scanner = new Scanner(spamFiles[i]);
                    while (scanner.hasNext()) {
                        String nextWord = scanner.next();
                        n += addToRes(nextWord);
                    }

                    // Prob. of Spam
                    double spamProb = 1 / (1 + Math.pow(Math.E, n));

                    // Correct/Incorrect count update
                    if (spamProb > 0.5) {
                        corGuesses++;
                        correctSpamGuesses++;
                    }
                    else {
                        incGuesses++;
                    }
                    DecimalFormat df = new DecimalFormat("0.00000");
                    df.format(spamProb);
                    TestFile nextFile;
                    nextFile = new TestFile(hamFiles[i].getName(), df.format(spamProb), "Spam");
                    results.add(nextFile);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            // Populating table with files and the values attached therein

            table.getItems().addAll(results);


            DecimalFormat df = new DecimalFormat("0.00000");


            prec = (double) correctSpamGuesses / (double) (incGuesses + correctSpamGuesses);
            accu = (double) corGuesses / ((double) hamFiles.length + (double) spamFiles.length);

            accuracyString = df.format(accu);
            precisionString = df.format(prec);

            accuracyField.setText(String.valueOf(accuracyString));
            precisionField.setText(String.valueOf(precisionString));

            accuracyField.setEditable(false);
            precisionField.setEditable(false);
        }
    }

    // Populating maps
    public static void addToMap(String word, Map<String, Integer> map) {
        if(map.containsKey(word)) {
            int num = map.get(word);
            map.replace(word, num+1);
        } else {
            map.put(word, 1);
            words.add(word);
        }
    }

    public static double addToRes(String word) {
        DecimalFormat df = new DecimalFormat("0.00000");
        if(probability.containsKey(word)) {
            double n = Math.log(1-probability.get(word)) - Math.log(probability.get(word));
            df.format(n);
            return n;
        } else {
            return 0;
        }
    }


}
