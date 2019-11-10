import java.util.ArrayList;

/**
 * c'est ici qu'on trouve tous ce qu'on veut sur l'espace de jeu
 * Resultat du parseur stocké ici
 * @author Oussama, achraf, louiza
 */
class FileData {
    private final int format;
    private final int taille_monde;
    private final int[] cube;
    private final ArrayList<int[]> cubes;
    
    /**
     * Constructeur, struct data
     * @param format
     * @param taille_cube
     * @param emptyOrFull int 0 ou 1 qui represente un cube - null si format est different de 1
     * @param c couleur rgb des cubes, null si le format est different de 2
     */
    public FileData(int format, int taille_cube, int[] emptyOrFull, ArrayList<int[]> c) {
        this.format = format;
        this.taille_monde = taille_cube;
        this.cube = emptyOrFull;
        this.cubes = c;
    }
    
    //---------le reste ci dessous c'est des getteur et setteur-----------//
    /**
     * Cube size pour format 1
     * @return 
     */
    public int cubeSize(){
        return cube.length;
    }
    
    /**
     * cube size pour format 2
     * @return 
     */
    public int cubesSize(){
        return cubes.size();
    }
    
    public int getFormat() {
        return format;
    }
    
    public int[] getColorById(int i){
        return cubes.get(i);
    }
    
    public void setColorById(int i, int r, int g, int b){
        this.cubes.get(i)[0] = r;
        this.cubes.get(i)[1] = g;
        this.cubes.get(i)[2] = b;
    }
    
    public int getTaille_monde(){
        return taille_monde;
    }
    
    /**
     * met le cube a 0 pour le format 1
     * @param i index du cube
     */
    public void destroyCube(int i){
        this.cube[i] = 0;
    }
    /**
     * met le cube a 1 pour le format 1
     * @param i index du cube
     */
    public void buildCube(int i){
        this.cube[i] = 1;
    }
    public int getcube(int i){
        return cube[i];
    }
    
}
