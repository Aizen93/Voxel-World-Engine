import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * C'est la class principal du projet qui gere le rendering 
 * et l'initialisation de toutes les class necessaires
 * @author Oussama, achraf & louiza
 */
public class Stage extends JPanel{
    static JPanel jpanel;
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static ArrayList<Cube> cubes;
    static ArrayList<Sphere> sphere;
    static ArrayList<Prism> prism;
    static ArrayList<Pyramid> pyramid;
    static ObjectFace hoverOnFace;
    ArrayList<Cube> cubesDecore;

    static double viewFrom[], viewTo[];
    static double rayCasting[];//direction de la lumiere

    static double vitesse; 
    static double zoom, minZoom, maxZoom;//focal

    double maxFPS, rafraichissement;
    double vueVertical, vueHorizontal, tailleViseur, vitesseRotHorizontal, vitesseRotVertical;
    double posSoleil;
    static boolean soleil, lightOn;
    boolean sound; 
    Robot r;
    static int mode, cubeSize, universSize;
    static int screenWidthDiv2;
    static int screenHeightDiv2;
    
    static boolean contour;
    static boolean[] keyBoard;
    long repaintTime;
    FileData worldData;
    static Calculus calculus;
    
    /**
     * @param s
     * @param l
     * @param sound
     * @param worldData2
     * @param mode le mode de jeu (0->Load game) (1->New game) (2->LineInCube) ...etc
     */
    public Stage(boolean s, boolean l, boolean sound, FileData worldData2, int mode){
        this.jpanel = this;
        this.screenHeightDiv2 = (int)screenSize.getHeight()/2;
        this.screenWidthDiv2 = (int)screenSize.getWidth()/2;
        this.cubes = new ArrayList<>();
        this.sphere = new ArrayList<>();
        this.pyramid = new ArrayList<>();
        this.prism = new ArrayList<>();
        this.cubesDecore = new ArrayList<>();
        this.hoverOnFace = null;
        
        this.viewFrom = new double[] {-35, -35, 35};
        this.viewTo = new double[3];
        this.rayCasting = new double[] {1, 1, 1};

        this.vitesse = 3;//0.7 
        this.zoom = 1000; this.minZoom = 500; this.maxZoom = 2500;

        this.maxFPS = 1000;
        this.rafraichissement = 0; 
        
        this.vueVertical = -0.5; this.vueHorizontal = 0.8; this.vitesseRotHorizontal = 1000; this.vitesseRotVertical = 2200;
        this.tailleViseur = 15;
        this.posSoleil = 0; this.soleil = s; this.lightOn = l;
        
        this.cubeSize = 16;
        this.sound = sound;
        this.contour = true;
        this.keyBoard = new boolean[7];
        this.repaintTime = 0;
        
        
        this.universSize = 8;
        
        
        if(mode == 0){
            this.mode = mode;
            this.worldData = worldData2;
            generateWorldFromFile();
        }else if(mode == 1){
            this.mode = mode;
            generateNewWorld(universSize);
            setDataForSave(mode);
        }else if(mode == 2){
            this.mode = mode;
            generateLineInCubeWorld();
            setDataForSave(mode);
        }else if(mode == 3){
            this.mode = mode;
            generateCubeInCube();
        }else{
            this.mode = mode;
            generateSphereInCube();
            setDataForSave(mode);
        }
        setFocusable(true);
        cacherSouris();
        construireDecor();
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if(me.getButton() == MouseEvent.BUTTON1){
                    if(hoverOnFace != null){
                        hoverOnFace.getPere().getForm().getCube().showCube();
                        if(sound) playSound("src/assets/build.wav");
                        if(worldData != null){//c'est pour la sauvegarde on update notre bdd
                            if(worldData.getFormat() == 1){
                                worldData.buildCube(hoverOnFace.getPere().getForm().getCube().getId());
                            }else if(worldData.getFormat() == 2){
                                worldData.setColorById(hoverOnFace.getPere().getForm().getCube().getId(), 
                                        hoverOnFace.getPere().getForm().getCube().getC().getRed(), 
                                        hoverOnFace.getPere().getForm().getCube().getC().getGreen(), 
                                        hoverOnFace.getPere().getForm().getCube().getC().getBlue());
                            }
                        }
                    }
                }else if(me.getButton() == MouseEvent.BUTTON3){
                    if(hoverOnFace != null){
                        hoverOnFace.getPere().getForm().getCube().hideCube();
                        if(sound) playSound("src/assets/smashing.wav");
                        if(worldData != null){//c'est pour la sauvegarde on update notre bdd
                            if(worldData.getFormat() == 1){
                                worldData.destroyCube(hoverOnFace.getPere().getForm().getCube().getId());
                            }else if(worldData.getFormat() == 2){
                                worldData.setColorById(hoverOnFace.getPere().getForm().getCube().getId(), -1, -1, -1);
                            }
                        }
                    }
                }
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                mouvementCamera(me.getX(), me.getY());
                centerMouse();
            }
        });
         
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_Z)
                    keyBoard[0] = true;
                if(e.getKeyCode() == KeyEvent.VK_A)
                    keyBoard[1] = true;
                if(e.getKeyCode() == KeyEvent.VK_S)
                    keyBoard[2] = true;
                if(e.getKeyCode() == KeyEvent.VK_E)
                    keyBoard[3] = true;
                if(e.getKeyCode() == KeyEvent.VK_O)
                    contour = !contour;
                if(e.getKeyCode() == KeyEvent.VK_SPACE)
                    keyBoard[4] = true;
                if(e.getKeyCode() == KeyEvent.VK_F){
                    keyBoard[5] = true;
                }
                if(e.getKeyCode() == KeyEvent.VK_H){
                    keyBoard[6] = true;
                }
                if(e.getKeyCode() == KeyEvent.VK_K){
                    viewFrom = new double[]{-35, -35, 35}; 
                }
                if(e.getKeyCode() == KeyEvent.VK_UP){
                    //if(vitesse <= (universSize * 4)/16) vitesse += 0.2;
                    if(vitesse <= 12) vitesse += 0.2;
                }
                if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    if(vitesse >= 0) vitesse -= 0.2;
                    if(vitesse <= 0) vitesse = 0;
                }
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    //allcubes.clear();
                    cubes.clear();
                    init();
                    Window win = SwingUtilities.getWindowAncestor((JComponent)e.getSource());
                    win.dispose();
                }
                if((e.getKeyCode() == KeyEvent.VK_X) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
                    if(mode != 3) SaveGame();
                }
                if(e.getKeyCode() == KeyEvent.VK_N){
                    for(Cube c : cubes){
                        c.hideCube();
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_B){
                    for(Cube c : cubes){
                        c.showCube();
                    }
                }
                if((e.getKeyCode() == KeyEvent.VK_I) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
                    BufferedImage image = r.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                    try {
                        ImageIO.write(image, "png", new File("screenshot.png"));
                    } catch (IOException ex) {
                        System.err.println("Failed to take a screenshot !");
                    }
                }
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_Z)
                    keyBoard[0] = false;
                if(e.getKeyCode() == KeyEvent.VK_A)
                    keyBoard[1] = false;
                if(e.getKeyCode() == KeyEvent.VK_S)
                    keyBoard[2] = false;
                if(e.getKeyCode() == KeyEvent.VK_E)
                    keyBoard[3] = false;
                if(e.getKeyCode() == KeyEvent.VK_SPACE)
                    keyBoard[4] = false;
                if(e.getKeyCode() == KeyEvent.VK_F){
                    keyBoard[5] = false;
                }
                if(e.getKeyCode() == KeyEvent.VK_H){
                    keyBoard[6] = false;
                }
            }
        }); 
        
        addMouseWheelListener((MouseWheelEvent me) -> {
            if(me.getUnitsToScroll()>0){
                if(zoom > minZoom) zoom -= 25 * me.getUnitsToScroll();
            }else{
                if(zoom < maxZoom) zoom -= 25 * me.getUnitsToScroll();	
            }
        });
    }
    
    @Override
    public void paintComponent(Graphics g){
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, (int)getWidth(), (int)getHeight());
        
        projectionCamera();
        this.calculus = new Calculus();
        
        if(soleil){
            sunLighting();//--------Lumiere control.
        }
        
        for(Cube cub : cubesDecore) {
            cub.updateCube();
            cub.drawCube(g);
        }
        
        // met a jour chaque cube pour la position du camera
        for(Cube cub : cubes) {
            cub.updateCube();
        }
        for(Sphere s : sphere) {
            s.updateSphere();
        }
        for(Pyramid s : pyramid) {
            s.updatePyramid();
        }
        for(Prism s : prism) {
            s.updatePrism();
        }
       
        //Tri les cubes pour que le cube le plus proche soit dessiné en dernier
        sortingCubes();      
        
        //dessine les faces dans l'ordre du sorting
        for(int i = cubes.size()-1; i >= 0; i--){
            //if(cubes.get(i).getAvrgDistance() <= 300)
                cubes.get(i).drawCube(g);
        }
        for(int i = pyramid.size()-1; i >= 0; i--){
           pyramid.get(i).drawPyramid(g);
        }
        for(int i = prism.size()-1; i >= 0; i--){
           prism.get(i).drawPrism(g);
        }
        for(int i = sphere.size()-1; i >= 0; i--){
           sphere.get(i).drawSphere(g);
        }
        
        cursorOnFace();
        showInfo(g);
        if(keyBoard[6]) help(g);
        drawViseur(g);
        frameRate();
    }
    
    /**
     * Remplie notre structure de donnés par le nouveau monde 
     * en preparation pour la sauvegarde selon le mode de jeu
     */
    private void setDataForSave(int mode){
        ArrayList<int[]> initCubes = new ArrayList<>();
        cubes.sort(Comparator.comparing(Cube::getId));
        switch(mode){
            case 1://mode 1 New Game
                for(int i = 0; i<cubes.size(); i++){
                    initCubes.add(new int[]{cubes.get(i).getC().getRed(),cubes.get(i).getC().getGreen(), cubes.get(i).getC().getBlue()});
                }
                this.worldData = new FileData(0, universSize, new int[0], initCubes);
                break;
            case 2://mode 2 Bresenham LineInCube
                
                for(int i = 0; i<cubes.size(); i++){
                    initCubes.add(new int[]{cubes.get(i).getC().getRed(),cubes.get(i).getC().getGreen(), cubes.get(i).getC().getBlue()});
                }
                this.worldData = new FileData(0, MenuPrincipaleController.getCoordinates()[6], new int[0], initCubes);
                break;
            case 3://mode 3 CubeInCube
                /* on ne peut pas sauvegarder ce mode parce que la taille du cube est differente
                 * de l'architecture de la géneration de monde de notre programme a moins
                 * qu'on puisse ajouter une ligne pour la taille au format de nos fichiers
                */
                break;
            case 4://mode 4 SphereInCube
                for(int i = 0; i<cubes.size(); i++){
                    initCubes.add(new int[]{cubes.get(i).getC().getRed(),cubes.get(i).getC().getGreen(), cubes.get(i).getC().getBlue()});
                }
                this.worldData = new FileData(0, MenuPrincipaleController.getSphere()[0], new int[0], initCubes);
                break;
        }
        
    }
    
    private void generateNewWorld(double taille){
        Random rd = new Random();
        int count = 0;
        int space = MenuPrincipaleController.getSpace();
        //cubes.add(new Cube(60, 60, 60, cubeSize, new Color(rd.nextInt(255),rd.nextInt(255),rd.nextInt(255)), false, -1, count));
        for(int x = 0; x < taille * (cubeSize + space); x+=(cubeSize + space)){
            for(int y = 0; y < taille * (cubeSize + space); y+=(cubeSize + space)){
                for(int z = 0; z < taille * (cubeSize + space); z+=(cubeSize + space)){
                    cubes.add(new Cube(x, y, z, cubeSize, new Color(rd.nextInt(255),rd.nextInt(255),rd.nextInt(255)), false, -1, count));
                    count++;
                }
            }
        }
        sphere.add(new Sphere(30, -30, 0, 50, 50, cubeSize/2));
        pyramid.add(new Pyramid(30, -60, 0, 16, 16, 16, Color.YELLOW));
        prism.add(new Prism(30, -60, 30, 16, 16, 16, Color.ORANGE));
    }
    
    private void generateWorldFromFile(){
        int space = MenuPrincipaleController.getSpace();
        int count;
        switch(worldData.getFormat()){
            case 1:
                count = 0;
                for(int x = 0; x < worldData.getTaille_monde()*(cubeSize + space); x+=(cubeSize + space)){
                    for(int y = 0; y <  worldData.getTaille_monde()*(cubeSize + space) ; y+=(cubeSize + space)){
                        for(int z = 0; z <  worldData.getTaille_monde()*(cubeSize + space); z+=(cubeSize + space)){
                            cubes.add(new Cube(x, y, z, cubeSize, new Color(89,171,227), (worldData.getcube(count) != 1), -1, count));
                            count++;                              
                        }
                    }
                }
                break;
            case 2:
                count = 0;
                for(int x = 0; x < worldData.getTaille_monde()*(cubeSize + space); x+=(cubeSize + space)){
                    for(int y = 0; y <  worldData.getTaille_monde()*(cubeSize + space) ; y+=(cubeSize + space)){
                        for(int z = 0; z <  worldData.getTaille_monde()*(cubeSize + space); z+=(cubeSize + space)){
                            int[] c = worldData.getColorById(count);
                            if(c[0] == -1 && c[1] == -1 && c[2] == -1){
                                cubes.add(new Cube(x, y, z, cubeSize, new Color(89,171,227), true, -1, count));
                                count++;
                            }else{
                                cubes.add(new Cube(x, y, z, cubeSize, new Color(c[0],c[1],c[2]), false, -1, count));
                                count++;  
                            }
                        }
                    }
                }
                break;
            case 3:
                //pour les textures
                break;
        } 
    }
    
    /**
     * format de données (n, x,y,z, radius, R,G,B)
     */
    private void generateSphereInCube(){
        int space = MenuPrincipaleController.getSpace();
        int[] sphere = MenuPrincipaleController.getSphere();
        int worldSize = sphere[0];
        int x1 = sphere[1]; int y1 = sphere[2]; int z1 = sphere[3];
        int radius = sphere[4];
        int R = sphere[5]; int G = sphere[6]; int B = sphere[7];
        int count = 0;
        for(int x = 0; x < worldSize * (cubeSize + space); x+=(cubeSize + space)){
            for(int y = 0; y < worldSize * (cubeSize + space); y+=(cubeSize + space)){
                for(int z = 0; z < worldSize * (cubeSize + space); z+=(cubeSize + space)){
                    cubes.add(new Cube(x, y, z, cubeSize, new Color(R, G, B), true, -1, count));
                    count++;
                }
            }
        }
                
        for(int X = x1 - radius * (cubeSize + space); X < x1 + radius * (cubeSize + space); X+=(cubeSize + space)){
            for(int Y = y1 - radius * (cubeSize + space); Y < y1 + radius * (cubeSize + space); Y+=(cubeSize + space)){
                for(int Z = z1 - radius * (cubeSize + space); Z < z1 + radius * (cubeSize + space); Z+=(cubeSize + space)){
                    int distX = x1 - X;
                    int distY = y1 - Y;
                    int distZ = z1 - Z;
                    if(Math.sqrt((distX * distX)+(distY * distY)+(distZ * distZ)) < radius*(cubeSize + space)){
                        Cube d = getCubeOfCoord(X, Y, Z);
                        if(d != null){
                            d.showCube();
                        }
                    }
                }
            }
        }
    }
    
    /**
     * format de donnés (n, x,y,z, width)
     */
    private void generateCubeInCube(){
        int space = MenuPrincipaleController.getSpace();
        int[] tmp = MenuPrincipaleController.getCoordinates();
        int worldSize = tmp[0];
        cubeSize = tmp[4];
        int count = 0;
        for(int x = 0; x < worldSize * (cubeSize + space); x+= cubeSize + space){
            for(int y = 0; y < worldSize * (cubeSize + space) ; y+= cubeSize + space){
                for(int z = 0; z < worldSize * (cubeSize + space); z+= cubeSize + space){
                    cubes.add(new Cube(x, y, z, cubeSize, new Color(89, 171, 227), true, -1, count)); 
                    count++;
                }
            }
        }
        getCubeOfCoord(tmp[1], tmp[2], tmp[3]).showCube();
    }
    
    private Cube getCubeOfCoord(int x, int y, int z){
        for(int i = 0; i < cubes.size(); i++){
            if(cubes.get(i).getX() == x && cubes.get(i).getY() == y && cubes.get(i).getZ() == z){
                return cubes.get(i);
            }
        }
        return null;
    }
    
    private void generateLineInCubeWorld(){
        int space = MenuPrincipaleController.getSpace();
        int[] tmp = MenuPrincipaleController.getCoordinates();
        Random rd = new Random();
        int worldSize = tmp[6];
        int count = 0;
        for(int x = 0; x < worldSize * (cubeSize + space); x+=(cubeSize + space)){
            for(int y = 0; y < worldSize * (cubeSize + space) ; y+=(cubeSize + space)){
                for(int z = 0; z < worldSize * (cubeSize + space); z+=(cubeSize + space)){
                    cubes.add(new Cube(x, y, z, cubeSize, new Color(rd.nextInt(255),rd.nextInt(255),rd.nextInt(255)), true, -1, count)); 
                    count++;
                }
            }
        }
        int sp = (cubeSize + space);
        int x1 = tmp[0], y1 = tmp[1], z1 = tmp[2];
        int x2 = tmp[3], y2 = tmp[4], z2 = tmp[5];
        int i, dx, dy, dz, l, m, n, x_inc, y_inc, z_inc, err_1, err_2, dx2, dy2, dz2;
        int[] point = new int[3];
        point[0] = x1; point[1] = y1; point[2] = z1;
        dx = x2 - x1;
        dy = y2 - y1;
        dz = z2 - z1;
        x_inc = (dx < 0) ? -sp : sp;
        l = Math.abs(dx);
        y_inc = (dy < 0) ? -sp : sp;
        m = Math.abs(dy);
        z_inc = (dz < 0) ? -sp : sp;
        n = Math.abs(dz);
        dx2 = l << 1;
        dy2 = m << 1;
        dz2 = n << 1;
        
        System.out.println("Listes des points generé par Bresenham 3D Algo: ");
        if((l >= m) && (l >= n)) {
            err_1 = dy2 - l;
            err_2 = dz2 - l;
            for (i = 0; i < l; i+=sp) {
                System.out.println("("+point[0]+", "+point[1]+", "+point[2]+")");
                getCubeOfCoord(point[0], point[1], point[2]).showCube();
                if (err_1 > 0) {
                    point[1] += y_inc;
                    err_1 -= dx2;
                }
                if (err_2 > 0) {
                    point[2] += z_inc;
                    err_2 -= dx2;
                }
                err_1 += dy2;
                err_2 += dz2;
                point[0] += x_inc;
            }
        }else if ((m >= l) && (m >= n)) {
            err_1 = dx2 - m;
            err_2 = dz2 - m;
            for (i = 0; i < m; i+=sp) {
                System.out.println("("+point[0]+", "+point[1]+", "+point[2]+")");
                getCubeOfCoord(point[0], point[1], point[2]).showCube();
                if (err_1 > 0) {
                    point[0] += x_inc;
                    err_1 -= dy2;
                }
                if (err_2 > 0) {
                    point[2] += z_inc;
                    err_2 -= dy2;
                }
                err_1 += dx2;
                err_2 += dz2;
                point[1] += y_inc;
            }
        }else {
            err_1 = dy2 - n;
            err_2 = dx2 - n;
            for (i = 0; i < n; i+=sp) {
                System.out.println("("+point[0]+", "+point[1]+", "+point[2]+")");
                getCubeOfCoord(point[0], point[1], point[2]).showCube();
                if (err_1 > 0) {
                    point[1] += y_inc;
                    err_1 -= dz2;
                }
                if (err_2 > 0) {
                    point[0] += x_inc;
                    err_2 -= dz2;
                }
                err_1 += dy2;
                err_2 += dx2;
                point[2] += z_inc;
            }
        }
        System.out.println("("+x2+", "+y2+", "+z2+")");
        getCubeOfCoord(x2, y2, z2).showCube();
    }
    
    private void SaveGame(){
        cubes.sort(Comparator.comparing(Cube::getId));
        File file = new File("src/Maps/" + "level1" + ".txt");
        int increase=1;
        while(file.exists()){
            increase++;
            file = new File("src/Maps/" + "level" + increase+ ".txt");
        } 
        if(!file.exists()) {
           try {
                String format = "";
                String taille = "";
                String cube = "";
                switch(worldData.getFormat()){
                    case 0://format du nouveau monde créer par le moteur de jeu
                        format = "2\n";//a la sauvegarde on lui donne format 2 par defaut pour garder les couleures
                        taille = worldData.getTaille_monde()+"\n";
                        cube = "";
                        for(int i = 0; i<cubes.size(); i++){
                            if(cubes.get(i).getFace(0).getFace().isCacher()){
                                cube += "-1 -1 -1 ";
                            }else{
                                cube += worldData.getColorById(i)[0]+" "
                                    +worldData.getColorById(i)[1]+" "
                                    +worldData.getColorById(i)[2]+" "; 
                            }
                        }
                        break;
                    case 1://format du prof (cube remplie ou vide)
                        format = "1\n";
                        taille = worldData.getTaille_monde()+"\n";
                        cube = "";
                        for(int i = 0; i<cubes.size(); i++){
                            if(cubes.get(i).getFace(0).getFace().isCacher()){
                                cube += "0 ";
                            }else{
                                cube += "1 "; 
                            }
                        }
                        break;
                    case 2://format du prof RGB()
                        format = "2\n";
                        taille = worldData.getTaille_monde()+"\n";
                        cube = "";
                        for(int i = 0; i<cubes.size(); i++){
                            if(cubes.get(i).getFace(0).getFace().isCacher()){
                                cube += "-1 -1 -1 ";
                            }else{
                                cube += worldData.getColorById(i)[0]+" "
                                    +worldData.getColorById(i)[1]+" "
                                    +worldData.getColorById(i)[2]+" "; 
                            }
                        }
                        break;
                    case 3://format pour les textures
                        //Not implemented yet
                        break;
                }
                file.createNewFile();
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(format);
                bw.write(taille);
                bw.write(cube);
                bw.close();
            }catch(Exception ex){
                System.err.println("Error: Partie non sauvegardé");
            }
        }
    }
    
    /**
     * Tri tous les faces des cubes selon la distance entre eux et la camera
     * le plus proche de la camera est mis en premier plan et le plus loin en arriere plan ainsi de suite
     * sa veux dire le plus loin est dessiner en premier et le plus proche en dernier
     */
    private void sortingCubes(){
        
        this.cubes.sort(Comparator.comparing(Cube::getAvrgDistance));
        this.sphere.sort(Comparator.comparing(Sphere::getAvrgDistance));
    }
    
    final void cacherSouris(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        BufferedImage curseur = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT); 
        Cursor cursor = toolkit.createCustomCursor(curseur, new Point(0,0), "cursor");        
        setCursor(cursor);
    }

    private void drawViseur(Graphics g) {
        g.setColor(Color.white);
        g.drawOval(screenWidthDiv2 - 8, screenHeightDiv2 - 8, 16, 16);
        g.fillOval(screenWidthDiv2 - 2, screenHeightDiv2 - 2, 4, 4);
        g.drawLine((int)(screenWidthDiv2 - tailleViseur), screenHeightDiv2, (int)(screenWidthDiv2 + tailleViseur), screenHeightDiv2);
        g.drawLine(screenWidthDiv2, (int)(screenHeightDiv2 - tailleViseur), screenWidthDiv2, (int)(screenHeightDiv2 + tailleViseur));			
    }
    
    /**
     * pour maintenir plus ou moins 60FPS mais c'est pas trés precis
     */
    private void frameRate(){
        long currentTime = (long)(System.currentTimeMillis() - rafraichissement);
        if(currentTime < 1000/maxFPS){
            try{
                Thread.sleep((long) (1000/maxFPS - currentTime));
            }catch (InterruptedException e) {
            }	
        }
        rafraichissement = System.currentTimeMillis();
        repaint();
    }

    private void sunLighting() {
        if(lightOn){
            posSoleil += 0.08;
        }
        double worldSize = cubes.size() * 2;
        rayCasting[0] = worldSize/2 - (worldSize/2 + Math.cos(posSoleil) * worldSize * 10);
        rayCasting[1] = worldSize/2 - (worldSize/2 + Math.sin(posSoleil) * worldSize * 10);
        rayCasting[2] = -200;
    }

    private void projectionCamera(){
        Vector vecteurVu = new Vector(viewTo[0] - viewFrom[0], viewTo[1] - viewFrom[1], viewTo[2] - viewFrom[2]);
        double depX = 0, depY = 0, depZ = 0;
        Vector vecteurVertical = new Vector (0, 0, 1);
        Vector vecteurVuCote = vecteurVu.produitVectorielle(vecteurVertical);//c'est le vecteur straf pour "A" et "E"
        if(keyBoard[0]){ //Z
            depX += vecteurVu.getX();
            depY += vecteurVu.getY();
            depZ += vecteurVu.getZ();
        }
        if(keyBoard[2]){//S
            depX -= vecteurVu.getX();
            depY -= vecteurVu.getY();
            depZ -= vecteurVu.getZ();
        }
        if(keyBoard[1]) {//A
            depX += vecteurVuCote.getX();
            depY += vecteurVuCote.getY();
            depZ += vecteurVuCote.getZ();
        }
        if(keyBoard[3]) {//E
            depX -= vecteurVuCote.getX();
            depY -= vecteurVuCote.getY();
            depZ -= vecteurVuCote.getZ();
        }
        if(keyBoard[4]) {//ESPACE
            if(depZ >= 0) depZ -= vecteurVu.getZ();
            else depZ += vecteurVu.getZ();
        }

        Vector vecteurDeplacement = new Vector(depX, depY, depZ);
        moveTo(viewFrom[0] + vecteurDeplacement.getX() * vitesse, viewFrom[1] 
            + vecteurDeplacement.getY() * vitesse, viewFrom[2] 
            + vecteurDeplacement.getZ() * vitesse);
    }

    private void moveTo(double x, double y, double z){
        viewFrom[0] = x;
        viewFrom[1] = y;
        if(z >= -260) viewFrom[2] = z;
        else viewFrom[2] = -260;
        updateView();
    }
    
    private void cursorOnFace(){
        if(hoverOnFace != null)hoverOnFace.getPere().getForm().getCube().setSelected(false);
        hoverOnFace = null;
        for(int i = 0; i < cubes.size(); i++){
            if(keyBoard[5]){
                ObjectFace face = cubes.get(i).faceHover();
                if(face != null){
                    if(face.hover() &&  face.isDraw() && face.isVisible()){
                        hoverOnFace = face;
                        hoverOnFace.getPere().getForm().getCube().setSelected(true);
                        break;
                    }
                }
            }else{
                ObjectFace face = cubes.get(i).faceHover();
                if(face != null){
                    if(face.hover() && face.isDraw() && !face.isCacher() && face.isVisible()){
                        hoverOnFace = face;
                        hoverOnFace.getPere().getForm().getCube().setSelected(true);
                        break;
                    }
                }
            }
        }
    }

    private void mouvementCamera(double mouseX, double mouseY){		
        double difX = (mouseX - screenWidthDiv2);
        double difY = (mouseY - screenHeightDiv2);
        difY *= 6 - Math.abs(vueVertical) * 5;
        vueVertical -= difY  / vitesseRotVertical;
        vueHorizontal += difX / vitesseRotHorizontal;
        
        //c'est pour bloquer la camera pour ne pas faire 360 degrés
        if(vueVertical>0.999) vueVertical = 0.999;
        if(vueVertical<-0.999) vueVertical = -0.999;
        
        updateView();
    }

    private void updateView(){
        double vuVertical = Math.sqrt(1 - sqr(vueVertical));
        viewTo[0] = viewFrom[0] + vuVertical * Math.cos(vueHorizontal);
        viewTo[1] = viewFrom[1] + vuVertical * Math.sin(vueHorizontal);		
        viewTo[2] = viewFrom[2] + vueVertical;
    }

    private void centerMouse(){
        try {
            r = new Robot();
            r.mouseMove(screenWidthDiv2, screenHeightDiv2);
        }catch (AWTException e) {
        }
    }
    
    private void playSound(String url) {
        AudioInputStream audioInputStream = null;
        try {
            String soundName = url;
            audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException ex) {
            System.err.println("Sound effect error 1");
        } catch (IOException ex) {
            System.err.println("Sound effect error 2");
        } catch (LineUnavailableException ex) {
            System.err.println("Sound effect error 3");
        } finally {
            try {
                audioInputStream.close();
            } catch (IOException ex) {
                System.err.println("Sound effect error 4 closing sound");
            }
        }
    }
    
    private void showInfo(Graphics g){
        //Info display
        g.setColor(Color.white);
        g.setFont(new Font("default", Font.BOLD, 12));
        g.drawString("Soleil: "+soleil+" -- Animation (sunrise): "+lightOn, 40, 55);
        g.drawString("Focal: "+zoom, 40, 70);
        g.drawString("Nombre de cubes afficher/total: "+cubes.size()+" / "+cubes.size(), 40, 85);
        g.drawString("Localisation: ("+(int)viewFrom[0]+", "+(int)viewFrom[1]+", "+(int)viewFrom[2]+")", 40, 115);
        g.drawString("Maintenir enfoncé touche H pour menu Help !", 40, 130);
        g.drawString("World Engine - Cubic - Version 0.1.5", 40, getHeight() - 60);
        if(vitesse < 12 && vitesse > 0){
            g.drawString("Vitesse: "+vitesse, 40, 100);
        }else if(vitesse >= 12){
            g.setColor(Color.red);
            g.drawString("Vitesse maximal atteinte", 40, 100);
        }else if(vitesse == 0){
            g.setColor(Color.green);
            g.drawString("Vue perspective, Mouvement desactivé !", 40, 100);
        }
        
    }
    
    private void help(Graphics g){
        g.setColor(Color.green);
        g.setFont(new Font("default", Font.BOLD, 32));
        g.drawString("Menu Help :", getWidth()/2 - 100, getHeight()/2 - 150);
        
        g.setColor(Color.white);
        g.setFont(new Font("comic sans ms", Font.BOLD, 24));
        g.drawString("Pour ce deplacer: Z, S, E, A, ESPACE: ", getWidth()/2 - 200, getHeight()/2 - 120);
        g.drawString("Pour regler la vitesse: touche UP, DOWN: ", getWidth()/2 - 200, getHeight()/2 - 90);
        g.drawString("Pour afficher les emplacements vides: F ", getWidth()/2 - 200, getHeight()/2 - 60);
        g.drawString("Pour afficher/cacher les contours: O ", getWidth()/2 - 200, getHeight()/2 - 30);
        g.drawString("Pour sauvegarder la partie: CTRL+X", getWidth()/2 - 200, getHeight()/2);
        g.drawString("Pour detruire un cube: Click droit", getWidth()/2 - 200, getHeight()/2 + 30);
        g.drawString("Pour construire un cube: F + Click gauche", getWidth()/2 - 200, getHeight()/2 + 60);
        g.drawString("Pour revenir au point de départ: K", getWidth()/2 - 200, getHeight()/2 + 90);
        g.drawString("Pour quitter: ESCAPE", getWidth()/2 - 200, getHeight()/2 + 120);
        g.drawString("Pour supprimer tous les cubes: N", getWidth()/2 - 200, getHeight()/2 + 150);
        g.drawString("Pour construire tous les cubes: B", getWidth()/2 - 200, getHeight()/2 + 180);
        g.drawString("Pour une capture d'ecran: CTRL+I", getWidth()/2 - 200, getHeight()/2 + 210);
    }
    
    private double sqr(double nbr){
        return nbr * nbr;
    }

    private void construireDecor() {
        Random rd = new Random();
        int low = 1800;
        int high = 2800;
        int x = 0, y = 0, z = 0;
        for(int i = 0; i < 40; i++){
            if(rd.nextInt(2) == 0) x = (rd.nextInt(high-low) + low) * -1;
            else x = rd.nextInt(high-low) + low;
            
            if(rd.nextInt(2) == 0) y = (rd.nextInt(high-low) + low) * -1;
            else y = rd.nextInt(high-low) + low;
            
            if(rd.nextInt(2) == 0) z = (rd.nextInt(high-low) + low) * -1;
            else z = rd.nextInt(high-low) + low;
            
            cubesDecore.add(new Cube(x, y, z, 50, new Color(255,255,255), false, -1, -1));
        }
    }
    
    private void init(){
        this.hoverOnFace = null;      
        this.viewFrom = new double[] {-35, -35, 35};
        this.viewTo = new double[3];
        this.rayCasting = new double[] {1, 1, 1};
        this.vitesse = 0.7; 
        this.zoom = 1000; this.minZoom = 500; this.maxZoom = 2500;
        this.maxFPS = 1000;
        this.rafraichissement = 0;       
        this.vueVertical = -0.5; this.vueHorizontal = 0.8; this.vitesseRotHorizontal = 1000; this.vitesseRotVertical = 2200;
        this.tailleViseur = 15;
        this.posSoleil = 0;       
        this.cubeSize = 16;
        this.universSize = 32;
        this.sound = sound;
        this.contour = true;
        this.repaintTime = 0;
    }
}
