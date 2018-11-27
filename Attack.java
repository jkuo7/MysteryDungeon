interface Attack {
    Type getType();
    int calculateDamage(int atk, Type atkType, int def, Type defType);
    String inHTML();
    String getName();
}
