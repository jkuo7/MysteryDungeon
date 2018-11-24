import java.awt.*;

public class Goal extends FlatOccupant{
    static Color purple = new Color(153,50,204);


    Goal(int i, int j){
        super(i, j);
        symbol = "▣";
        textColor = purple;
        name = "Goal";
        description = "It's the stairs! \nYou can go to the next floor.";
    }

    void walkedOn(Ally a, MysteryDungeonGame game, MysteryDungeon dungeon){}

    void walkedOn(Enemy e, MysteryDungeonGame game, MysteryDungeon dungeon){}

    void walkedOn(Player p, MysteryDungeonGame game, MysteryDungeon dungeon){
        game.askNextFloor();
    }

}