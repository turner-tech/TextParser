package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;



public class Controller implements Initializable {

    @FXML
    public TextArea originalText;
    public TextArea newText;

    public Button openFileButton;
    public Button saveFileButton;

    public TextField removeText;
    public TextField keepText;
    public TextField removeBetweenText;

    public File originalFile;

    public Label infoLabel;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("Starting Application");
        //handleButtonAction(new ActionEvent());
        infoLabel.setText("Please select a text file to edit!");

    }

    //This method controls the exiting of the app when you press File -> Close
    public void exitApp(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Warning! Please read the information below!");
        alert.setContentText("Are you sure you want to close this application?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            System.exit(0);
        }
    }

    //Creates a new filechooser window
    public File openTheFile(FileChooser fileChooser, Stage stage){
        return fileChooser.showOpenDialog(stage);

    }

    //This method has the user choose a file to solve, and then copies the contents to a string to be used later
    public void openFile(){

        String str = null;
        StringBuilder sb = new StringBuilder();


        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setTitle("Pick a Text File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))    //"user.dir"
        );
        fileChooser.getExtensionFilters().addAll(
                //new FileChooser.ExtensionFilter("Text Files", "*.*"),
                new FileChooser.ExtensionFilter(".txt file", "*.txt"),
                new FileChooser.ExtensionFilter(".csv file", "*.csv")
                //new FileChooser.ExtensionFilter(".doc", "*.doc"),
                //new FileChooser.ExtensionFilter(".docx", "*.docx")
        );
        File file = openTheFile(fileChooser, stage);


        while (((file == null))){
            file = openTheFile(fileChooser, stage);
            if (!(file == null)){
               // startButton.setDisable(false);
                break;
            }
        }

        try {
            originalFile = file;
            BufferedReader in = new BufferedReader(new FileReader(originalFile));

            while ((str = in.readLine()) != null) {
                //System.out.println(str);
                sb.append(str+ "\n");

            }

            in.close();
        } catch (IOException e) {
            System.out.println("File Read Error");

        }
        String result = sb.toString();
        //result = result.replaceAll("\\s+","");
        originalText.setText(result);
        infoLabel.setText("File opened, press create file after you make your changes.");

    }

    public void readFile(){
        String str = null;
        StringBuilder sb = new StringBuilder();

        try {
        BufferedReader in = new BufferedReader(new FileReader(originalFile));


        while ((str = in.readLine()) != null) {
            //System.out.println(str);
            if ((removeText.getText().length() > 0) && (str.contains(removeText.getText()))){
                str = str.replace(removeText.getText(), "");
            }

           // String s1 = String.valueOf(removeBetweenText.getText().charAt(0));
            if ((removeBetweenText.getText().length() > 0) && (str.contains(String.valueOf(removeBetweenText.getText().charAt((0)))))
                && (str.contains(String.valueOf(removeBetweenText.getText().charAt((1))))))
            {
                //System.out.println("Entering between text");
                char[] line = str.toCharArray();
                String tempStr = "";
                boolean betweenChars = false;
                for (char c: line){
                    if (betweenChars) {
                        tempStr = tempStr + c;
                    }
                    if (c ==removeBetweenText.getText().charAt(0)){
                        betweenChars = true;
                        tempStr = tempStr + c;
                    }
                    if (c == removeBetweenText.getText().charAt(1)){
                        betweenChars = false;
                        //System.out.println(tempStr);
                        break;
                    }
                }

                str = str.replace(tempStr, keepText.getText());
            }


            sb.append(str+ "\n");

        }


            in.close();


        String result = sb.toString();
        newText.setText(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        infoLabel.setText("Please click Save File to save your changes!");
    }

    public void saveFile(){
        try {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Your File");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))    //"user.dir"
            );
            fileChooser.getExtensionFilters().addAll(
                    //new FileChooser.ExtensionFilter("Text Files", "*.*"),
                    new FileChooser.ExtensionFilter(".txt file", "*.txt")
                    //new FileChooser.ExtensionFilter(".csv file", "*.csv"),
                    //new FileChooser.ExtensionFilter(".doc", "*.doc"),
                    //new FileChooser.ExtensionFilter(".docx", "*.docx")
            );
            File file = fileChooser.showSaveDialog(stage);
            FileWriter fileWriter = null;
            fileWriter = new FileWriter(file);
            fileWriter.write(newText.getText());
            infoLabel.setText("You have successfully created a new file!");
            fileWriter.close();
        }
        catch (IOException ex) {
        }


    }
}
