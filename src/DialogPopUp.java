import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * Permet d'ouvrir des dialogs avec users pour communiquer des erreur ou demander des infos
 * @author Oussama, achraf & louiza
 */
public class DialogPopUp {
    
    /**
     * Constructeur d'alerte
     * Permet d'ouvrir des dialogs avec users pour communiquer des erreur ou demander des infos
     * @see Dialog & alert java class
     */
    public DialogPopUp() {
        //requires nothing
    }
    
    /**
     * Afiiche un message d'erreur avec l'exception catché a l'utilisateur
     * @param ex
     * @param context 
     */
    public void showAlert(Exception ex, String context){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Alert, an exception catched somewhere");
        alert.setContentText(context);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace :");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
    
    /**
     * Affiche un message a l'utilisateur pour l'informer
     * @param m 
     */
    public void showMessage(String m){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(m);

        alert.showAndWait();
    }
    
    /**
     * renvoie les coordonnées des point de depart et d'arrivé pour la ligne Bresenham en 3D
     * @return tableau de int (x,y,z, x2,y2,z2, taille)
     */
    public int[] getBresenhamCoordStartEnd(){
        int space = MenuPrincipaleController.getSpace();
        int cubeSize = MenuPrincipaleController.getCubeSize();
        int[] coordinates = new int[7];
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Bresenham Draw Line of cubes in a 3D space !");
        ButtonType obuttonType = new ButtonType("OK");
        ButtonType cbuttonType = new ButtonType("Cancel");
        dialog.getDialogPane().getButtonTypes().addAll(obuttonType, cbuttonType);
        
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(obuttonType);
        final Button btCancel = (Button) dialog.getDialogPane().lookupButton(cbuttonType);
        Group groupe = new Group();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        
        Text m = new Text();
        m.setFill(Color.RED);
        m.setFont(Font.loadFont("file:src/assets/Mario and luigi.ttf", 20));
        TextField n = new TextField();
        n.setPrefWidth(230);
        n.setMaxWidth(230);
        n.setPromptText("world space size...");
        TextField x1 = new TextField();
        x1.setPromptText("Coord X1 of start point");
        x1.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(Integer.parseInt(newValue) % (space + cubeSize) != 0 || Integer.parseInt(newValue) < 0){
                x1.setStyle("-fx-background-color: red;");
                m.setText("value x1 must be a multiple of "+(space + cubeSize)+" and > 0");
                btOk.setDisable(true);
                }else{
                    x1.setStyle("-fx-background-color: green;");
                    btOk.setDisable(false);
                }
            }catch(NumberFormatException e){
            }
        });
        TextField y1 = new TextField();
        y1.setPromptText("Coord Y1 of start point");
        y1.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(Integer.parseInt(newValue) % (space + cubeSize) != 0 || Integer.parseInt(newValue) < 0){
                y1.setStyle("-fx-background-color: red;");
                m.setText("value y1 must be a multiple of "+(space + cubeSize)+" and > 0");
                btOk.setDisable(true);
                }else{
                    y1.setStyle("-fx-background-color: green;");
                    btOk.setDisable(false);
                }
            }catch(NumberFormatException e){
            }
        });
        TextField z1 = new TextField();
        z1.setPromptText("Coord Z1 of start point");
        z1.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(Integer.parseInt(newValue) % (space + cubeSize) != 0 || Integer.parseInt(newValue) < 0){
                z1.setStyle("-fx-background-color: red;");
                m.setText("value z1 must be a multiple of "+(space + cubeSize)+" and > 0");
                btOk.setDisable(true);
                }else{
                    z1.setStyle("-fx-background-color: green;");
                    btOk.setDisable(false);
                }
            }catch(NumberFormatException e){
            }
        });
        TextField x2 = new TextField();
        x2.setPromptText("Coord X2 of end point");
        x2.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(Integer.parseInt(newValue) % (space + cubeSize) != 0 || Integer.parseInt(newValue) < 0){
                x2.setStyle("-fx-background-color: red;");
                m.setText("value x2 must be a multiple of "+(space + cubeSize)+" and > 0");
                btOk.setDisable(true);
                }else{
                    x2.setStyle("-fx-background-color: green;");
                    btOk.setDisable(false);
                }
            }catch(NumberFormatException e){
            }
        });
        TextField y2 = new TextField();
        y2.setPromptText("Coord Y2 of end point");
        y2.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(Integer.parseInt(newValue) % (space + cubeSize) != 0 || Integer.parseInt(newValue) < 0){
                y2.setStyle("-fx-background-color: red;");
                m.setText("value y2 must be a multiple of "+(space + cubeSize)+" and > 0");
                btOk.setDisable(true);
                }else{
                    y2.setStyle("-fx-background-color: green;");
                    btOk.setDisable(false);
                }
            }catch(NumberFormatException e){
            }
        });
        TextField z2 = new TextField();
        z2.setPromptText("Coord Z2 of end point");
        z2.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(Integer.parseInt(newValue) % (space + cubeSize) != 0 || Integer.parseInt(newValue) < 0){
                z2.setStyle("-fx-background-color: red;");
                m.setText("value z2 must be a multiple of "+(space + cubeSize)+" and > 0");
                btOk.setDisable(true);
                }else{
                    z2.setStyle("-fx-background-color: green;");
                    btOk.setDisable(false);
                }
            }catch(NumberFormatException e){
            }
        });
        
        gridPane.add(n, 0, 0);
        gridPane.add(new Label("Coordinates max value: "+(space+cubeSize)+" * (worldSize - 1)"), 0, 1);
        gridPane.add(x1, 0, 2);
        gridPane.add(y1, 1, 2);
        gridPane.add(z1, 2, 2);
        gridPane.add(x2, 0, 4);
        gridPane.add(y2, 1, 4);
        gridPane.add(z2, 2, 4);
        
        groupe.getChildren().addAll(m, gridPane);
        
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            try{
                int taille = Integer.parseInt(n.getText());
                if(taille > 1 && ((taille & (taille - 1)) == 0)){
                    coordinates[0] = Integer.parseInt(x1.getText());
                    coordinates[1] = Integer.parseInt(y1.getText());
                    coordinates[2] = Integer.parseInt(z1.getText());
                    coordinates[3] = Integer.parseInt(x2.getText());
                    coordinates[4] = Integer.parseInt(y2.getText());
                    coordinates[5] = Integer.parseInt(z2.getText());
                    coordinates[6] = Integer.parseInt(n.getText());
                    if(coordinates[0] < 0 || coordinates[1] < 0 || coordinates[2] < 0 | coordinates[3] < 0 || coordinates[4] < 0 || coordinates[5] < 0){
                        m.setText("One or more coordinate fields have negative values, positive values only !");
                        event.consume();
                    }else{
                       dialog.close(); 
                    }
                }else{
                    m.setText("Wrong value of world size, must be a power of 2 for the octree architecture");
                    n.clear();
                    n.setPromptText("wrong value, must be greater than 1");
                    n.setStyle("-fx-prompt-text-fill: red;");
                    event.consume();
                }
            }catch(NumberFormatException e){
                m.setText("Bad format of one of the textfields below !");
                event.consume();
            }
        });
        
        btCancel.addEventFilter(ActionEvent.ACTION, event -> {
            dialog.close();
        });
        
        dialog.getDialogPane().setContent(groupe);
        dialog.showAndWait();
        
        return coordinates;
    }
    
    /**
     * renvoie les coordonnés du centre, le rayon et les couleurs R G B de la sphere
     * @return tbleau de int (n, x,y,z, radius, R,G,B)
     */
    public int[] getDDACoordRadius(){
        int space = MenuPrincipaleController.getSpace();
        int cubeSize = MenuPrincipaleController.getCubeSize();
        int[] coordinates = new int[8];
        Dialog dialog = new Dialog<>();
        dialog.setTitle("DDA Draw Sphere of cubes in a 3D space !");
        ButtonType obuttonType = new ButtonType("OK");
        ButtonType cbuttonType = new ButtonType("Cancel");
        dialog.getDialogPane().getButtonTypes().addAll(obuttonType, cbuttonType);
        
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(obuttonType);
        final Button btCancel = (Button) dialog.getDialogPane().lookupButton(cbuttonType);
        Group groupe = new Group();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        
        Text m = new Text();
        m.setFill(Color.RED);
        m.setFont(Font.loadFont("file:src/assets/Mario and luigi.ttf", 20));
        
        TextField n = new TextField();
        n.setPrefWidth(230);
        n.setMaxWidth(230);
        n.setPromptText("world space size...");
        
        TextField r = new TextField();
        r.setPromptText("Radius...");
        r.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(Integer.parseInt(newValue) < 2){
                    r.setStyle("-fx-background-color: red;");
                    m.setText("Radius must be equal or greater than 2");
                    btOk.setDisable(true);
                }else{
                    r.setStyle("-fx-background-color: green;");
                    btOk.setDisable(false);
                }
            }catch(NumberFormatException e){
            }
        });
        
        TextField x1 = new TextField();
        x1.setPromptText("Coord X1 of center point");
        x1.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(Integer.parseInt(newValue) % (space + cubeSize) != 0 || Integer.parseInt(newValue) < 0){
                    x1.setStyle("-fx-background-color: red;");
                    m.setText("value x1 must be a multiple of "+(space + cubeSize)+" and > 0");
                    btOk.setDisable(true);
                }else{
                    x1.setStyle("-fx-background-color: green;");
                    btOk.setDisable(false);
                }
            }catch(NumberFormatException e){
            }
        });
        
        TextField y1 = new TextField();
        y1.setPromptText("Coord Y1 of center point");
        y1.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(Integer.parseInt(newValue) % (space + cubeSize) != 0 || Integer.parseInt(newValue) < 0){
                    y1.setStyle("-fx-background-color: red;");
                    m.setText("value y1 must be a multiple of "+(space + cubeSize)+" and > 0");
                    btOk.setDisable(true);
                }else{
                    y1.setStyle("-fx-background-color: green;");
                    btOk.setDisable(false);
                }
            }catch(NumberFormatException e){
            }
        });
        
        TextField z1 = new TextField();
        z1.setPromptText("Coord Z1 of center point");
        z1.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(Integer.parseInt(newValue) % (space + cubeSize) != 0 || Integer.parseInt(newValue) < 0){
                    z1.setStyle("-fx-background-color: red;");
                    m.setText("value z1 must be a multiple of "+(space + cubeSize)+" and > 0");
                    btOk.setDisable(true);
                }else{
                    z1.setStyle("-fx-background-color: green;");
                    btOk.setDisable(false);
                }
            }catch(NumberFormatException e){
            }
        });
        
        TextField R = new TextField();
        R.setPromptText("R..");
        R.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(Integer.parseInt(newValue) > 255 || Integer.parseInt(newValue) < 0){
                    R.setStyle("-fx-background-color: red;");
                    m.setText("value R must be between 0 and 255");
                    btOk.setDisable(true);
                }else{
                    R.setStyle("-fx-background-color: green;");
                    btOk.setDisable(false);
                }
            }catch(NumberFormatException e){
            }
        });
        
        TextField G = new TextField();
        G.setPromptText("G...");
        G.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(Integer.parseInt(newValue) > 255 || Integer.parseInt(newValue) < 0){
                    G.setStyle("-fx-background-color: red;");
                    m.setText("value G must be between 0 and 255");
                    btOk.setDisable(true);
                }else{
                    G.setStyle("-fx-background-color: green;");
                    btOk.setDisable(false);
                }
            }catch(NumberFormatException e){
            }
        });
        
        TextField B = new TextField();
        B.setPromptText("B...");
        B.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(Integer.parseInt(newValue) > 255 || Integer.parseInt(newValue) < 0){
                    B.setStyle("-fx-background-color: red;");
                    m.setText("value B must be between 0 and 255");
                    btOk.setDisable(true);
                }else{
                    B.setStyle("-fx-background-color: green;");
                    btOk.setDisable(false);
                }
            }catch(NumberFormatException e){
            }
        });
        
        gridPane.add(n, 0, 0);
        gridPane.add(r, 2, 0);
        gridPane.add(new Label("Coordinates max value: "+(space+cubeSize)+" * (worldSize - 1)"), 0, 1);
        gridPane.add(x1, 0, 2);
        gridPane.add(y1, 1, 2);
        gridPane.add(z1, 2, 2);
        gridPane.add(R, 0, 4);
        gridPane.add(G, 1, 4);
        gridPane.add(B, 2, 4);
        
        groupe.getChildren().addAll(m, gridPane);
        
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            try{
                int taille = Integer.parseInt(n.getText());
                if(taille > 1 && ((taille & (taille - 1)) == 0)){
                    coordinates[0] = Integer.parseInt(n.getText());
                    coordinates[1] = Integer.parseInt(x1.getText());
                    coordinates[2] = Integer.parseInt(y1.getText());
                    coordinates[3] = Integer.parseInt(z1.getText());
                    coordinates[4] = Integer.parseInt(r.getText());
                    coordinates[5] = Integer.parseInt(R.getText());
                    coordinates[6] = Integer.parseInt(G.getText());
                    coordinates[7] = Integer.parseInt(B.getText());
                    if(coordinates[0] < 0 || coordinates[1] < 0 || coordinates[2] < 0 | coordinates[3] < 0 || coordinates[4] < 0 || coordinates[5] < 0){
                        m.setText("One or more coordinate fields have negative values, positive values only !");
                        event.consume();
                    }else{
                       dialog.close(); 
                    }
                }else{
                    m.setText("Wrong value of world size, must be a power of 2 for the octree architecture");
                    n.clear();
                    n.setPromptText("wrong value, must be greater than 1");
                    n.setStyle("-fx-prompt-text-fill: red;");
                    event.consume();
                }
            }catch(NumberFormatException e){
                m.setText("Bad format of one of the textfields below !");
                event.consume();
            }
        });
        
        btCancel.addEventFilter(ActionEvent.ACTION, event -> {
            dialog.close();
        });
        
        dialog.getDialogPane().setContent(groupe);
        dialog.showAndWait();
        
        return coordinates;
    }
    
    /**
     * renvoie les coordonnées d'un cube et sa taille
     * @return tableau de int (n, x,y,z, width)
     */
    public int[] getCubeInCubeCoord(){
        int space = MenuPrincipaleController.getSpace();
        int[] coordinates = new int[8];
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Draw a cube of specific length in a 3D space !");
        ButtonType obuttonType = new ButtonType("OK");
        ButtonType cbuttonType = new ButtonType("Cancel");
        dialog.getDialogPane().getButtonTypes().addAll(obuttonType, cbuttonType);
        
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(obuttonType);
        final Button btCancel = (Button) dialog.getDialogPane().lookupButton(cbuttonType);
        Group groupe = new Group();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        
        Text m = new Text();
        m.setFill(Color.RED);
        m.setFont(Font.loadFont("file:src/assets/Mario and luigi.ttf", 20));
        
        TextField n = new TextField();
        n.setPrefWidth(230);
        n.setMaxWidth(230);
        n.setPromptText("world space size...");
        
        TextField width = new TextField();
        width.setPromptText("Width...");
        
        
        TextField x1 = new TextField();
        x1.setPromptText("Coord X1 of cube");
        x1.setDisable(true);
        x1.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(Integer.parseInt(newValue) % (space + Integer.parseInt(width.getText())) != 0 || Integer.parseInt(newValue) < 0){
                    x1.setStyle("-fx-background-color: red;");
                    m.setText("value x1 must be a multiple of "+(space + Integer.parseInt(width.getText()))+" and > 0");
                    btOk.setDisable(true);
                }else{
                    x1.setStyle("-fx-background-color: green;");
                    btOk.setDisable(false);
                }
            }catch(NumberFormatException e){
            }
        });
        
        TextField y1 = new TextField();
        y1.setPromptText("Coord Y1 of cube");
        y1.setDisable(true);
        y1.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(Integer.parseInt(newValue) % (space + Integer.parseInt(width.getText())) != 0 || Integer.parseInt(newValue) < 0){
                    y1.setStyle("-fx-background-color: red;");
                    m.setText("value y1 must be a multiple of "+(space + Integer.parseInt(width.getText()))+" and > 0");
                    btOk.setDisable(true);
                }else{
                    y1.setStyle("-fx-background-color: green;");
                    btOk.setDisable(false);
                }
            }catch(NumberFormatException e){
            }
        });
        
        TextField z1 = new TextField();
        z1.setPromptText("Coord Z1 of cube");
        z1.setDisable(true);
        z1.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(Integer.parseInt(newValue) % (space + Integer.parseInt(width.getText())) != 0 || Integer.parseInt(newValue) < 0){
                    z1.setStyle("-fx-background-color: red;");
                    m.setText("value z1 must be a multiple of "+(space + Integer.parseInt(width.getText()))+" and > 0");
                    btOk.setDisable(true);
                }else{
                    z1.setStyle("-fx-background-color: green;");
                    btOk.setDisable(false);
                }
            }catch(NumberFormatException e){
            }
        });
        
        width.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if(Integer.parseInt(newValue) > 1 && ((Integer.parseInt(newValue) & (Integer.parseInt(newValue) - 1)) == 0)){
                    width.setStyle("-fx-background-color: green;");
                    btOk.setDisable(false);
                    x1.setDisable(false);
                    y1.setDisable(false);
                    z1.setDisable(false);
                }else{
                    width.setStyle("-fx-background-color: red;");
                    m.setText("Width must be a power of 2 and greater than 1");
                    btOk.setDisable(true);
                    x1.setDisable(true);
                    y1.setDisable(true);
                    z1.setDisable(true);
                }
            }catch(NumberFormatException e){
            }
        });
        
        gridPane.add(n, 0, 0);
        gridPane.add(width, 2, 0);
        gridPane.add(new Label("Coordinaes max value: (space + width) * (worldSize - 1)"), 0, 1);
        gridPane.add(x1, 0, 2);
        gridPane.add(y1, 1, 2);
        gridPane.add(z1, 2, 2);
        
        groupe.getChildren().addAll(m, gridPane);
        
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            try{
                int taille = Integer.parseInt(n.getText());
                if(taille > 1 && ((taille & (taille - 1)) == 0)){
                    coordinates[0] = Integer.parseInt(n.getText());
                    coordinates[1] = Integer.parseInt(x1.getText());
                    coordinates[2] = Integer.parseInt(y1.getText());
                    coordinates[3] = Integer.parseInt(z1.getText());
                    coordinates[4] = Integer.parseInt(width.getText());
                    if(coordinates[0] < 0 || coordinates[1] < 0 || coordinates[2] < 0 | coordinates[3] < 0 || coordinates[4] < 0 || coordinates[5] < 0){
                        m.setText("One or more coordinate fields have negative values, positive values only !");
                        event.consume();
                    }else{
                       dialog.close(); 
                    }
                }else{
                    m.setText("Wrong value of world size, must be a power of 2 for the octree architecture");
                    n.clear();
                    n.setPromptText("wrong value, must be greater than 1");
                    n.setStyle("-fx-prompt-text-fill: red;");
                    event.consume();
                }
            }catch(NumberFormatException e){
                m.setText("Bad format of one of the textfields below !");
                event.consume();
            }
        });
        
        btCancel.addEventFilter(ActionEvent.ACTION, event -> {
            dialog.close();
        });
        
        dialog.getDialogPane().setContent(groupe);
        dialog.showAndWait();
        
        return coordinates;
    }
    
    /**
     * filechooser configuration
     * @param fileChooser 
     */
    public static void configureFileChooser(final FileChooser fileChooser){                           
        fileChooser.setTitle("View home files");
    }
    
    /**
     * ouvre un fichier 
     * @param mainAnchor
     * @return un fichier
     */
    public File openFile(AnchorPane mainAnchor){
        final FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(mainAnchor.getScene().getWindow());
        return file;
    }
    
}
