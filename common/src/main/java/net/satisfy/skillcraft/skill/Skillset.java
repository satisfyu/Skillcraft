package net.satisfy.skillcraft.skill;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Skillset {
    private final Identifier id;
    @Nullable
    public final String name;
    @Nullable
    private final String description;
    private final Map<Integer, SkillLevel> levels;

    public Skillset(Identifier id, @Nullable String name, @Nullable String description, Map<Integer, SkillLevel> levels) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.levels = levels;
    }

    public Identifier getId() {
        return id;
    }

    public String getName() {
        return name == null ? id.getPath() : name;
    }

    public String getDescription() {
        return description == null ? "Add a description to the Json File." : description;
    }

    public int getMaxLevel() {
        return levels.keySet().stream().max(Comparator.comparingInt(x -> x)).orElse(0);
    }

    public boolean isMax(int level) {
        return level > getMaxLevel();
    }

    public int getLevelAmount(int currentLevel, int xp, boolean creative) {
        int cost = 0;
        int amount = 0;
        while (!this.isMax(currentLevel + amount + 1)) {
            int nextCost = cost + this.nextLevelCost(currentLevel + amount + 1);

            if (!creative && nextCost > xp) {
                break;
            }

            cost = nextCost;
            amount++;
        }
        return amount;
    }

     public int getLevelCost(int currentLevel, int amount) {
        int cost = 0;

        for (int i = 0; i < amount; i++) {
            cost += this.nextLevelCost(++currentLevel);
        }

        return cost;
    }

    protected int nextLevelCost(int level) {
        // Formula for calculating the required xp/level for the next level.
        return isMax(level) ? 0 : ((level * level) / (getMaxLevel() + 2)) + 2;
    }

    public String getLevelName(int level) {
        return isMax(level) ? "Max Level" : levels.containsKey(level) ? levels.get(level).getName() : "Level " + level;
    }

    public String getLevelDescription(int level) {
        return isMax(level) ? "Congrats, you have reached the max level!" : levels.containsKey(level) ? levels.get(level).getDescription() : "";
    }

    public List<Item> getUnlockItems(int level) {
        return levels.containsKey(level) ? levels.get(level).getUnlockItems() : Lists.newArrayList();
    }

    public List<Block> getUnlockBlocks(int level) {
        return levels.containsKey(level) ? levels.get(level).getUnlockBlocks() : Lists.newArrayList();
    }

    @Override
    public String toString() {
        return "Skillset: " + " ID: " + id + " / name: " + getName() +  ", levels: " + levels + ';';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Skillset skillset)) return false;
        return id.equals(skillset.id) && Objects.equals(this.getName(), skillset.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getName());
    }
}
