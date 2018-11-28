import java.awt.Color;

public class Ally extends PartyMember{
    int allyNumber;
    static int allyTotal = 1;
    boolean swapped;
    static Type[] allyTypes = {Type.ELECTRIC, Type.WATER, Type.GRASS};
    static Attack[] startAttack = {VariableAttack.THUNDER_SHOCK, VariableAttack.BUBBLE, VariableAttack.VINE_WHIP};

    Ally(int i, int j, Player p){
        super(i, j);
        symbol = "⍥";
        player = p;
        player.party.add(this);
        player.allies.add(this);
        allyNumber = allyTotal;
        type = allyTypes[allyNumber % allyTypes.length];
        textColor = type.getColor();
        allyTotal++;
        name = "Ally " + allyNumber;
        attacks.add(new LearnedAttack(VariableAttack.TACKLE));
        attacks.add(new LearnedAttack(startAttack[allyNumber % startAttack.length]));
    }

    void checkHealth(MysteryDungeonGame game){
        super.checkHealth(game);
        if(curHP == 0){
            player.allies.remove(this);
        }
    }

    void checkCritical(MysteryDungeonGame game){
        if(curHP <= maxHP * 0.2 && !critical){
            critical = true;
            game.addMessage(String.format("%s is low on health", name), Color.RED);
        } else if (curHP > maxHP * 0.2 && critical) {
            critical = false;
        }
    }

    void take(Item i, MysteryDungeonGame game, MysteryDungeon dungeon){
        player.putInBag(i, game, dungeon, this);
    }

}