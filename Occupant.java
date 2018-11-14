import java.awt.Color;

public abstract class Occupant{
    int x;
    int y;
    boolean canBeWalkedOn;
    String symbol;
    Color textColor;

    Occupant(int i, int j){
        x = i;
        y = j;
    }


}