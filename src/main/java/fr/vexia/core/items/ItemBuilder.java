package fr.vexia.core.items;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.material.Banner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemBuilder {

    private ItemStack item;

    public ItemBuilder(ItemBuilder itemBuilder) {
        this(itemBuilder.toItemStack());
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public ItemBuilder(Material material, int amount) {
        item = new ItemStack(material, amount);
    }

    public ItemBuilder(Material material, int amount, short data) {
        item = new ItemStack(material, amount, data);
    }

    public ItemBuilder setName(String name) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
        return this;
    }

    public String getName() {
        return item.getItemMeta().getDisplayName();
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        item.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder setItemMeta(ItemMeta meta) {
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        this.item.setType(material);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        skullMeta.setOwner(owner);
        item.setItemMeta(skullMeta);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(enchantment, level, true);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        item.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilder addPatterns(Pattern... patterns){
        BannerMeta banner = (BannerMeta) item.getItemMeta();
        banner.setPatterns(Arrays.asList(patterns));
        item.setItemMeta(banner);
        return this;
    }

    public ItemBuilder setUnbreakable() {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeLoreLine(String line) {
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = new ArrayList<>(itemMeta.getLore());
        if (!lore.contains(line))
            return this;
        lore.remove(line);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeLoreLine(int index) {
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = new ArrayList<>(itemMeta.getLore());
        if ((index < 0) || (index > lore.size()))
            return this;
        lore.remove(index);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (itemMeta.hasLore())
            lore = new ArrayList<>(itemMeta.getLore());
        lore.add(line);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addLoreLine(String line, int pos) {
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = new ArrayList<>(itemMeta.getLore());
        lore.set(pos, line);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLeatherArmorColor(Color color) {
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) item.getItemMeta();
        itemMeta.setColor(color);
        item.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Build ItemStack with the parameters
     *
     * @return ItemStack built
     */
    public ItemStack toItemStack() {
        return item;
    }

    public static ItemBuilder of(Material material, String name) {
        return new ItemBuilder(material).setName(name);
    }

    public ItemBuilder setDyeColor(DyeColor color) {
        item.setDurability(color.getWoolData());
        return this;
    }

    public ItemBuilder setDurability(short i) {
        item.setDurability(i);
        return this;
    }

    public ItemBuilder setGlow(boolean glow) {
        ItemMeta meta = item.getItemMeta();
        if(glow) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            meta.removeEnchant(Enchantment.DURABILITY);
            meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);
        return this;
    }
}
