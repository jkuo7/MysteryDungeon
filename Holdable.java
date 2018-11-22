public abstract class Holdable extends FlatOccupant{

    Holdable(int i, int j){
        super(i, j);
    }


    void walkedOn(Enemy e, MysteryDungeonGame game, MysteryDungeon dungeon){
        e.take(this, dungeon);
    }

    void droppedAt(int i, int j){
        x = i;
        y = j;
    }
}