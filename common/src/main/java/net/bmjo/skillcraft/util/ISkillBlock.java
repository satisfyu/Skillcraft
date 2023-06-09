package net.bmjo.skillcraft.util;

import net.bmjo.skillcraft.skill.SkillLevel;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;

public interface ISkillBlock {
    boolean hasRequiredLevel(PlayerEntity player, Block block);
    void addSkillLevel(SkillLevel skillLevel);
}
