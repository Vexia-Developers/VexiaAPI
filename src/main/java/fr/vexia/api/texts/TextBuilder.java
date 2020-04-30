package fr.vexia.api.texts;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class TextBuilder {

    private List<String> components = new ArrayList<>();

    public TextBuilder(String text, ChatColor color){
        TextComponent component = new TextComponent(text);
        component.setColor(color);
        addExtra(component);
    }

    public TextBuilder(String text){
        TextComponent component = new TextComponent(text);
        addExtra(component);
    }

    public TextBuilder(TextComponent component){
        addExtra(component);
    }

    public TextBuilder(TextComponent[] component){
        for(TextComponent forComponent : component){
            addExtra(forComponent);
        }
    }

    public TextBuilder(){}

    public TextBuilder addTexte(TextComponent component){
        addExtra(component);
        return this;
    }

    public TextBuilder addTexte(String text){
        addExtra(new TextComponent(text));
        return this;
    }

    public TextBuilder addTexte(String text, ChatColor color){
        TextComponent component = new TextComponent(text);
        component.setColor(color);
        addExtra(component);
        return this;
    }

    public TextBuilder addTexteLien(String text, HoverEvent hover, ClickEvent click){
        TextComponent component = new TextComponent(text);
        component.setHoverEvent(hover);
        component.setClickEvent(click);
        addExtra(component);
        return this;
    }

    public TextBuilder addTexteLien(String text, ChatColor color, HoverEvent hover, ClickEvent click){
        TextComponent component = new TextComponent(text);
        component.setColor(color);
        component.setHoverEvent(hover);
        component.setClickEvent(click);
        addExtra(component);
        return this;
    }

    public TextBuilder resetColor(){
        addExtra(new TextComponent(ChatColor.RESET+""));
        return this;
    }

    public TextComponent build(){
        TextComponent component = null;
        for(String forComponents : this.components){
            for(BaseComponent list : TextComponent.fromLegacyText(forComponents)) {
                if(component == null){
                    component = new TextComponent(list);
                }else{
                    component.addExtra(list);
                }
            }
        }
        return component;
    }

    private void addExtra(TextComponent component){
        this.components.add(TextComponent.toLegacyText(component));
    }

}
