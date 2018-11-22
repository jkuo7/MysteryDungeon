public abstract class FlatOccupant extends Occupant{
    boolean isTrap = false;

    FlatOccupant(int i, int j){
        super(i, j);
        canBeWalkedOn = true;
    }

    abstract void walkedOn(Enemy a, MysteryDungeonGame game, MysteryDungeon dungeon);

    abstract void walkedOn(Ally c, MysteryDungeonGame game, MysteryDungeon dungeon);

    abstract void walkedOn(Player p, MysteryDungeonGame game, MysteryDungeon dungeon);
}