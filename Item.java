public abstract class Item extends Holdable{


    Item(int i, int j){
        super(i, j);
    }

    abstract boolean used(PartyMember pm, MysteryDungeonGame game);

    void walkedOn(Ally a, MysteryDungeonGame game, MysteryDungeon dungeon){
        a.take(this, game, dungeon);
    }

    void walkedOn(Player p, MysteryDungeonGame game, MysteryDungeon dungeon){
        p.take(this, game, dungeon);
    }

}