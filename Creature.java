import java.util.ArrayList;

public abstract class Creature extends Occupant{
    boolean isEnemy;
    boolean isPlayer = false;
    int maxHP, attack, def;
    double curHP;
    Creature curTarget;
    Type type;
    ArrayList<LearnedAttack> attacks = new ArrayList<>(4);
    Pokemon pokemon;

    Creature(int i, int j, Pokemon p){
        super(i, j);
        canBeWalkedOn = false;
        pokemon = p;
        type = pokemon.getType();
        textColor = type.getColor();
        name = pokemon.getName();
        symbol = pokemon.getSymbol();
        maxHP = pokemon.getMaxHP();
        attack = pokemon.getAtk();
        def = pokemon.getDef();
        for(Attack a : pokemon.getStartAttacks()){
            attacks.add(new LearnedAttack(a));
        }
    }

    void move(int dx, int dy, MysteryDungeonGame game){
        move(dx, dy);
        checkHealth(game);
    }

    void move(int dx, int dy){
        x += dx;
        y += dy;
    }

    abstract void checkHealth(MysteryDungeonGame game);

    void attacks(Attack a, Creature c, MysteryDungeonGame game){
        a.usedOn(this, c, game);
    }

    void attackedFor(int a, MysteryDungeonGame game){
        curHP = Math.max(0, curHP - a);
        checkHealth(game);
    }

    abstract int expGiven();
}