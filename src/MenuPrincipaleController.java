import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.System.exit;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * FXML Controller class
 * Control l'interface graphique et tous ses composents
 * @author Oussama, achraf, louiza
 */
public class MenuPrincipaleController implements Initializable {
    /**
     * Declaration de class et d'objet FXML comme bouton, text...etc
     */
    @FXML
    private AnchorPane mainAnchor;                 
    @FXML
    private Button newgame, loadgame, exitgame;
    @FXML
    private CheckBox soleil, lightOn, sound;
    @FXML
    private Spinner spacing;
    private DialogPopUp dialog = new DialogPopUp();
    private FileData worldData;
    private Stage newGame;
    private static int[] coordinates;
    private static int[] sphere;
    private static int space = 1;//minimum 0 max 10
    private static final int cubeSize = 16;
    
    /**
     * Commence un nouveau jeu a partir d'un fichier txt
     * @param event 
     */
    @FXML
    private void loadGame(ActionEvent event) {
        setSpace((int)spacing.getValue());
        try{
            int count = 1;
            int format = -1;
            int taille = -1;
            ArrayList<int[]> color = new ArrayList<>();
            int[] cube = null;
            File file = this.dialog.openFile(mainAnchor);
            if (file != null) {
                try (
                    BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null){
                        switch (count) {
                            case 1:
                                format = Integer.parseInt(line);
                                count++;
                                continue;
                            case 2:
                                taille = Integer.parseInt(line);
                                count++;
                                continue;
                            case 3:
                                if(format == 1){
                                    String[] tmp = line.split(" ");
                                    cube = new int[tmp.length];
                                    for(int i=0; i<tmp.length; i++){
                                        cube[i] = Integer.parseInt(tmp[i]);
                                    }
                                }else if(format == 2){
                                    String[] tmp2 = line.split(" ");
                                    for(int i=0; i<(tmp2.length); i+=3){
                                        int[] a = {Integer.parseInt(tmp2[i]),Integer.parseInt(tmp2[i+1]),Integer.parseInt(tmp2[i+2])};
                                        color.add(a);
                                    }
                                    count++;
                                    cube = new int[0];
                                }   break;
                            default:
                                break;
                        }
                    }
                    this.worldData = new FileData(format, taille, cube, color);
                
                } catch (IOException ex) {
                    this.dialog.showAlert(ex, "Error reading file");
                }
                /**
                 * APrés parsage on charge la partie avec les données recuperer
                 */
                if(this.worldData.cubeSize() < Math.pow(this.worldData.getTaille_monde(), 3) && 
                    this.worldData.cubesSize() < Math.pow(this.worldData.getTaille_monde(), 3)){
                    this.dialog.showMessage("Il y a moins de cube que ce qu'il devait etre dans le monde\n\n"
                        + "Veuillez verifier le fichier suivant: "+file);
                }else{
                    if(this.worldData.getTaille_monde() > 1 &&
                        (this.worldData.getTaille_monde() & (this.worldData.getTaille_monde() - 1)) == 0){
                        showInfo();
                        final SwingNode swingNode = new SwingNode();
                        SwingUtilities.invokeLater(() -> {
                            newGame = new Stage(soleil.isSelected(), lightOn.isSelected(), sound.isSelected(), worldData, 0);
                            JFrame frame = new JFrame();
                            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
                            frame.add(newGame);
                            frame.setUndecorated(true);
                            frame.setSize(newGame.screenSize);
                            frame.setVisible(true);
                        }); 
                    }else{
                        this.dialog.showMessage("la taille du monde n'est pas une puissance de 2,"
                            + "ça ne correspond pas a l'architecture de l'octree\n"
                            + "Veuillez verifier le fichier suivant: "+file);
                    }
                }
            }
        }catch(Exception e){
            this.dialog.showAlert(e, "Error loading a game !\n"
                    + "Le format du fichier est peut etre mal ecrite");
        }
    }
    
    /**
     * Charge une partie déja existante depuis un fichier txt
     * @param event 
     */
    @FXML
    private void newGame(ActionEvent event) {
        setSpace((int)spacing.getValue());
        SwingUtilities.invokeLater(() -> {
            newGame = new Stage(soleil.isSelected(), lightOn.isSelected(), sound.isSelected(), null, 1);
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
            frame.add(newGame);
            frame.setUndecorated(true);
            frame.setSize(newGame.screenSize);
            frame.setVisible(true);
        });
    }
    
    /**
     * Crée un monde avec une ligne droite dedans avec l'algo de Bresenham
     * @param event 
     */
    @FXML
    private void CreateLineInCube(ActionEvent event) {
        setSpace((int)spacing.getValue());
        this.coordinates = this.dialog.getBresenhamCoordStartEnd();
        if(sum(coordinates) != 0){
            if((this.coordinates[6] - 1) * (cubeSize + space) >= max(this.coordinates, 2)){//coord doit etre < (worldSize-1 * espace)
                SwingUtilities.invokeLater(() -> {
                    newGame = new Stage(soleil.isSelected(), lightOn.isSelected(), sound.isSelected(), null, 2);
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
                    frame.add(newGame);
                    frame.setUndecorated(true);
                    frame.setSize(newGame.screenSize);
                    frame.setVisible(true);
                });
            }else{
                this.dialog.showMessage("Les coordonnés entrés sont supérieur a la taille du monde !");
            }
        }
    }
    
    @FXML
    private void CreateCubeInCube(ActionEvent event) {
        setSpace((int)spacing.getValue());
        this.coordinates = this.dialog.getCubeInCubeCoord();
        if(sum(coordinates) != 0){
           if((coordinates[0] - 1) * (coordinates[4] + space) >= max(coordinates, 3)){
                SwingUtilities.invokeLater(() -> {
                    newGame = new Stage(soleil.isSelected(), lightOn.isSelected(), sound.isSelected(), null, 3);
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
                    frame.add(newGame);
                    frame.setUndecorated(true);
                    frame.setSize(newGame.screenSize);
                    frame.setVisible(true);
                });
            }else{
                this.dialog.showMessage("Les coordonnés entrés sont supérieur a la taille du monde !");
            }
       }
        
    }
    
    @FXML
    private void CreateSphereInCube(ActionEvent event) {
        setSpace((int)spacing.getValue());
        this.sphere = this.dialog.getDDACoordRadius();
        if(sum(sphere) != 0){
            int coordMax = ((this.sphere[0] - 1) * (space + cubeSize));
            if(coordMax >= this.sphere[1] && coordMax >= this.sphere[2] && coordMax >= this.sphere[3]){
                final SwingNode swingNode = new SwingNode();
                SwingUtilities.invokeLater(() -> {
                    newGame = new Stage(soleil.isSelected(), lightOn.isSelected(), sound.isSelected(), null, 4);
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
                    frame.add(newGame);
                    frame.setUndecorated(true);
                    frame.setSize(newGame.screenSize);
                    frame.setVisible(true);
                });
            }else{
                this.dialog.showMessage("Les coordonnés entrés sont supérieur a la taille du monde !");
            }
        }
    }
    
    /**
     * ------------des affichages console pour informations---------
     */
    private void showInfo(){
        System.out.println("-----------------------------------");
        System.out.println("format: "+worldData.getFormat());
        System.out.println("taille: "+worldData.getTaille_monde());
        System.out.print("cube colors: ");
        if(worldData.getFormat() == 2){
            System.out.println(worldData.cubesSize());
            for(int i=0; i<worldData.cubesSize(); i++){
                System.out.println(worldData.getColorById(i)[0]+" "+worldData.getColorById(i)[1]+" "+worldData.getColorById(i)[2]);
            }
            System.out.println("-----------------------------------");
        }
        if(worldData.getFormat() == 1){
            System.out.println(worldData.cubeSize());
            for(int i=0; i<worldData.cubeSize(); i++){
                System.out.print(worldData.getcube(i)+" ");
            }
            System.out.println("\n---------------------------------");
        }
    }
    
    /**
     * Ferme le jeu
     * @param event 
     */
    @FXML
    private void exitGame(ActionEvent event) {
        System.out.println("******************************************");
        System.out.println("***               GoodBye              ***");
        System.out.println("******************************************");
        Platform.exit();
        exit(0);
    }
    
    /**
     * Ajoute les input clavier au menu principale
     * @since start
     */
    public void setKeyEvent(){
        Scene scene = mainAnchor.getScene();
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            switch (event.getCode()) {
                case L:
                    loadgame.fire();
                    break;
                case ESCAPE:
                    exitgame.fire();
                    break;
                case N:
                    newgame.fire();
                    break;
                default:
                    break;
            }
        });
    }
    
    /**
     * Renvoi la somme d'un tableau
     * @param a
     * @return un int
     */
    private int sum(int[] a){
        int sum = 0;
        for(int i = 0; i < a.length; i++) sum+=a[i];
        return sum;
    }
    
    /**
     * Renvoi le max value d'un tableau
     * @param a
     * @return un int
     */
    private int max(int[] a, int mode){
        switch(mode){
            case 2:
                int[] tmp = new int[]{a[0],a[1],a[2],a[3],a[4],a[5]}; 
                int max = tmp[0];
                for (int i = 1; i < tmp.length; i++) {
                    if (tmp[i] > max) {
                        max = tmp[i];
                    }
                }
                return max;
            case 3:
                int[] tmp2 = new int[]{a[1],a[2],a[3]}; 
                int max2 = tmp2[0];
                for (int i = 1; i < tmp2.length; i++) {
                    if (tmp2[i] > max2) {
                        max2 = tmp2[i];
                    }
                }
                return max2;
        }
        return 0;
    }
    
    public static int[] getCoordinates() {
        return coordinates;
    }
    
    public static int[] getSphere() {
        return sphere;
    }
    
    public static int getSpace() {
        return space;
    }
    
    public static void setSpace(int space) {
        MenuPrincipaleController.space = space;
    }
    
    public static int getCubeSize() {
        return cubeSize;
    }
    
    /**
     * Initialize constructor
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PauseTransition delay = new PauseTransition(Duration.millis(1000));
        delay.setOnFinished(event -> {
            setKeyEvent();
            //pour le spinner
            final int initialValue = 1;
            SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, initialValue);
 
            spacing.setValueFactory(valueFactory);
        } );
        delay.play();
    }    
    
}
