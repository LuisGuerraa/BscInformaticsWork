/**
 * Each object of this class is a row of best scores table.
 */
public class Score {
    private String name; //Campos de instancia
    private int points;


    public Score(String name, int points) { // Construtor
        this.name=name;
        this.points=points;

    }
    public int getPoints() {
        return points;
    } //points

    public String getName() {
        return name;
    }//name

    public String toString(){ //quando usarmos prints nao aparecer a referencia do objeto mas sim o que esta escrito
        return  name + ","+ points;
    } // name,points
}