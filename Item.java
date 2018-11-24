public abstract class Item extends Holdable{


    Item(int i, int j){
        super(i, j);
    }

    abstract void used(PartyMember pm);

     void walkedOn(Ally a, MysteryDungeonGame game, MysteryDungeon dungeon){
         a.take(this, game, dungeon);
     }

     void walkedOn(Player p, MysteryDungeonGame game, MysteryDungeon dungeon){
         p.take(this, game, dungeon);
     }

}