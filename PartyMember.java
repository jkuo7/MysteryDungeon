import java.awt.Color;

public abstract class PartyMember extends Creature{
    private double hPRegen = 0.005;
    int maxBelly = 100;
    double curBelly = maxBelly;
    Player player;
    boolean critical = false;
    int level = 1;
    int exp = 0;
    private String attacksString = "";

    PartyMember(int i, int j){
        super(i, j);
        maxHP = 200;
        curHP = maxHP;
        attack = 10;
        isEnemy = false;
    }

    void move(int dx, int dy, MysteryDungeonGame game){
        checkHunger();
        super.move(dx, dy, game);
        checkCritical(game);
    }

    private void checkHunger(){
        if(curBelly <= 0){
            curHP = Math.max(0, curHP - 1);
        } else {
            curBelly = Math.max(0, curBelly - 0.1);
            if(curHP < maxHP){
                curHP = Math.min(maxHP, curHP + maxHP * hPRegen);
            }
        }
    }

    void checkHealth(MysteryDungeonGame game){
        if(curHP == 0){
            game.outOfHP(this);
            player.party.remove(this);
            game.addMessage(String.format("%s fainted!", name), Color.RED);
        }
    }

    void attacks(Attack a, Creature c, MysteryDungeonGame game){
        super.attacks(a, c, game);
        if(c.curHP <= 0){
            for(PartyMember pm: player.party){
                pm.getExp(c.expGiven(), game);
            }
        }
        checkHunger();
        checkHealth(game);
    }

    private void getExp(int e, MysteryDungeonGame game){
        exp += e;
        while(exp >= level * 100){
            levelUp(game);
        }
    }

    private void levelUp(MysteryDungeonGame game){
        exp -= level * 100;
        level++;
        maxHP += 20;
        attack += 2;
        curHP = maxHP;
        game.addMessage(String.format("%s leveled up to level %d! Max HP +%d, Attack +%d", name, level, 20, 2), Color.GREEN);
    }

    abstract void checkCritical(MysteryDungeonGame game);

    void take(Coin c, MysteryDungeonGame game, MysteryDungeon dungeon){
        player.money += c.value;
        dungeon.removeFlat(c);
        game.addMessage(String.format("%s picked up %s", name, c.name));
    }

    abstract void take(Item i, MysteryDungeonGame game, MysteryDungeon dungeon);

    void useFromBag(Item i, MysteryDungeonGame game){
        i.used(this);
        player.bag.remove(i);
        game.addMessage(String.format("%s used %s", name, i.name));
        checkCritical(game);
    }

    int expGiven(){
        return 0;
    }

    void activatedTrap(Trap t, MysteryDungeonGame game, String effect){
        game.addMessage(String.format("%s stepped on a %s: %s!", name, t.name, effect), Color.RED);
    }

    public String toString(){
        String color;
        if(critical){
            color = "<html><font color=\"red\">";
        } else {
            color = String.format("<html><font color=\"#%02x%02x%02x\">",
                    textColor.getRed(), textColor.getGreen(), textColor.getBlue());
        }
        return String.format("%s%s (%s) (HP: %d/%d, Belly: %d/%d) </font></html>\n%s%s Type, Lv. %d, Exp to next level: %d </font></html>\n%s",
                color, name, symbol, (int) Math.ceil(curHP), maxHP, (int) Math.ceil(curBelly), maxBelly, color, type, level, level * 100 - exp, attacksString);
    }

    void setAttacksString(){
        String s= "<html>";
        for(Attack a: attacks){
            s += a.inHTML() + ", ";
        }
        s = s.substring(0, s.length() - 2);
        s += "</html>";
        attacksString = s;
    }

}