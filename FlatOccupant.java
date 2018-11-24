public abstract class FlatOccupant extends Occupant{
    boolean isTrap = false;
    String description = "";

    FlatOccupant(int i, int j){
        super(i, j);
        canBeWalkedOn = true;
    }

    abstract void walkedOn(Enemy a, MysteryDungeonGame game, MysteryDungeon dungeon);

    abstract void walkedOn(Ally c, MysteryDungeonGame game, MysteryDungeon dungeon);

    abstract void walkedOn(Player p, MysteryDungeonGame game, MysteryDungeon dungeon);

    public String toString(){
        return name + ": " + description;
    }

}