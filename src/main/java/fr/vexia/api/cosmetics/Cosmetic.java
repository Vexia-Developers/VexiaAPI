package fr.vexia.api.cosmetics;

import fr.vexia.api.players.rank.Rank;

public enum Cosmetic {

    /* PARTICULES */

    CLOUD("Nuage", CosmeticType.PARTICULE, 3200, null),
    CROWN("Couronne de Feu", CosmeticType.PARTICULE, 2150, null),
    ENCHANTMENT("Enchantement", CosmeticType.PARTICULE, 2500, null),
    HEART("Cœur", CosmeticType.PARTICULE, 3000, null),
    MUSIC("Musique", CosmeticType.PARTICULE, 3500, null),
    PNJ("PNJ", CosmeticType.PARTICULE, 3500, null),
    POTION("Potion", CosmeticType.PARTICULE, 4500, null),
    SMOKE("Fumée", CosmeticType.PARTICULE, 1200, null),
    SPIRAL("Spiral", CosmeticType.PARTICULE, 4000, null),
    TNT("TNT", CosmeticType.PARTICULE, 0, Rank.VX_PLUS),
    MAGIC("Magie", CosmeticType.PARTICULE, 0, Rank.VX_PLUS),
    WINGS("Ailes", CosmeticType.PARTICULE, 0, Rank.VX_PLUS),
    CAPE("Cape", CosmeticType.PARTICULE, 3300, null),
    BORDER("Bordure", CosmeticType.PARTICULE, 3700, null),

    /* MONTURES */

    /* CASQUES */

    DIAMOND_HELMET("Casque en Diamant", CosmeticType.HELMET, 1000, null),
    GOLD_HELMET("Casque en Or", CosmeticType.HELMET, 1000, null),
    IRON_HELMET("Casque en Fer", CosmeticType.HELMET, 1000, null),

    MAGENTA_HELMET("Casque Violet", CosmeticType.HELMET, 1000, null),
    BLUE_HELMET("Casque Bleu", CosmeticType.HELMET, 1000, null),
    GRAY_HELMET("Casque Gris", CosmeticType.HELMET, 1000, null),
    PINK_HELMET("Casque Rose", CosmeticType.HELMET, 1000, null),
    GREEN_HELMET("Casque Vert", CosmeticType.HELMET, 1000, null),
    ORANGE_HELMET("Casque Orange", CosmeticType.HELMET, 1000, null),

    /* CHESTPLATE */

    DIAMOND_CHESTPLATE("Plastron en Diamant", CosmeticType.CHESTPLATE, 1000, null),
    GOLD_CHESTPLATE("Plastron en Or", CosmeticType.CHESTPLATE, 1000, null),
    IRON_CHESTPLATE("Plastron en Fer", CosmeticType.CHESTPLATE, 1000, null),

    PURPLE_CHESTPLATE("Plastron Violet", CosmeticType.CHESTPLATE, 1000, null),
    BLUE_CHESTPLATE("Plastron Bleu", CosmeticType.CHESTPLATE, 1000, null),
    GRAY_CHESTPLATE("Plastron Gris", CosmeticType.CHESTPLATE, 1000, null),
    PINK_CHESTPLATE("Plastron Rose", CosmeticType.CHESTPLATE, 1000, null),
    LIME_CHESTPLATE("Plastron Vert", CosmeticType.CHESTPLATE, 1000, null),
    ORANGE_CHESTPLATE("Plastron Orange", CosmeticType.CHESTPLATE, 1000, null),

    /* LEGGINGS */

    DIAMOND_LEGGINGS("Jambière en Diamant", CosmeticType.LEGGINGS, 1000, null),
    GOLD_LEGGINGS("Jambière en Or", CosmeticType.LEGGINGS, 1000, null),
    IRON_LEGGINGS("Jambière en Fer", CosmeticType.LEGGINGS, 1000, null),

    PURPLE_LEGGINGS("Jambière Violette", CosmeticType.LEGGINGS, 1000, null),
    BLUE_LEGGINGS("Jambière Bleue", CosmeticType.LEGGINGS, 1000, null),
    GRAY_LEGGINGS("Jambière Grise", CosmeticType.LEGGINGS, 1000, null),
    PINK_LEGGINGS("Jambière Rose", CosmeticType.LEGGINGS, 1000, null),
    LIME_LEGGINGS("Jambière Verte", CosmeticType.LEGGINGS, 1000, null),
    ORANGE_LEGGINGS("Jambière Orange", CosmeticType.LEGGINGS, 1000, null),

    /* BOOTS */

    DIAMOND_BOOTS("Botte en Diamant", CosmeticType.BOOTS, 1000, null),
    GOLD_BOOTS("Botte en Or", CosmeticType.BOOTS, 1000, null),
    IRON_BOOTS("Botte en Fer", CosmeticType.BOOTS, 1000, null),

    PURPLE_BOOTS("Botte Violette", CosmeticType.BOOTS, 1000, null),
    BLUE_BOOTS("Botte Bleue", CosmeticType.BOOTS, 1000, null),
    GRAY_BOOTS("Botte Grise", CosmeticType.BOOTS, 1000, null),
    PINK_BOOTS("Botte Rose", CosmeticType.BOOTS, 1000, null),
    LIME_BOOTS("Botte Verte", CosmeticType.BOOTS, 1000, null),
    ORANGE_BOOTS("Botte Orange", CosmeticType.BOOTS, 1000, null);


    private final String name;
    private final CosmeticType type;
    private final int price;
    private final Rank rank;

    private Cosmetic(String name, CosmeticType type, int price, Rank rank) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public CosmeticType getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public Rank getRank() {
        return rank;
    }

    public enum CosmeticType {

        PARTICULE("Particule"),
        MONTURE("Monture"),
        HELMET("Casque"),
        CHESTPLATE("Plastron"),
        LEGGINGS("Jambière"),
        BOOTS("Botte");

        private String name;

        private CosmeticType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
