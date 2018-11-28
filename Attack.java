public interface Attack {
    void usedOn(Creature attacker, Creature defender, MysteryDungeonGame game);
    String inHTML();
    int getMaxPP();
    void usePP();
}
