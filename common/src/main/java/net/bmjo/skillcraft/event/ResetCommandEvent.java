package net.bmjo.skillcraft.event;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.bmjo.skillcraft.Skillcraft;
import net.bmjo.skillcraft.skill.SkillData;
import net.bmjo.skillcraft.util.IEntityDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ResetCommandEvent implements CommandRegistrationEvent {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registry, CommandManager.RegistrationEnvironment selection) {
        dispatcher.register(CommandManager.literal("skillcraft")
                .then(CommandManager.literal("reset")
                .executes(context -> {
                    if (context.getSource().getPlayer() instanceof IEntityDataSaver) {
                        for (Identifier identifier : Skillcraft.SKILLS.keySet()) {
                            SkillData.resetSkill((IEntityDataSaver) context.getSource().getPlayer(), identifier.toString());
                        }
                    } else {
                        context.getSource().sendError(Text.literal("Player not found."));
                    }
                    return 1;
                }))
        );
    }
}
