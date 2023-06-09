package net.bmjo.skillcraft.client.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bmjo.skillcraft.Skillcraft;
import net.bmjo.skillcraft.SkillcraftIdentifier;
import net.bmjo.skillcraft.skill.Skill;
import net.bmjo.skillcraft.skill.SkillLevel;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CantUseToast implements Toast {
    private static final Identifier TEXTURE = new SkillcraftIdentifier("textures/gui/skill_toast.png");
    private static final int titleColor = 14686475;
    private static final int textColor = 9830400;
    private final Item item;
    private SkillLevel skillLevel;

    private boolean soundPlayed;

    public CantUseToast(SkillLevel skillLevel, Item item) {
        this.skillLevel = skillLevel;
        this.item = item;
    }

    public void setSkillLevel(SkillLevel skillLevel) {
        this.skillLevel = skillLevel;
    }

    @Override
    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());

        Skill skill = Skillcraft.SKILLS.get(this.skillLevel.skill());
        List<OrderedText> lines = manager.getClient().textRenderer.wrapLines(Text.literal("Needs " + skill.getName() + ": " + skill.getLevelName(this.skillLevel.level())), 125);
        if (lines.size() == 1) {
            manager.getClient().textRenderer.draw(matrices, this.item.getName(), 30.0F, 7.0F, titleColor);
            manager.getClient().textRenderer.draw(matrices, lines.get(0), 30.0F, 18.0F, textColor);
        } else {
            if (startTime < 1500L) {
                int color = MathHelper.floor(MathHelper.clamp((float) (1500L - startTime) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                manager.getClient().textRenderer.draw(matrices, this.item.getName(), 30.0F, 11.0F, titleColor | color);
            } else {
                int color = MathHelper.floor(MathHelper.clamp((float) (startTime - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                int halfHeight = this.getHeight() / 2;
                int y = halfHeight - lines.size() * 9 / 2;

                for (Iterator<OrderedText> lineIterator = lines.iterator(); lineIterator.hasNext(); y += 9) {
                    OrderedText line = lineIterator.next();
                    manager.getClient().textRenderer.draw(matrices, line, 30.0F, (float) y, textColor | color);
                    Objects.requireNonNull(manager.getClient().textRenderer);
                }
            }
        }

        if (!this.soundPlayed) {
            this.soundPlayed = true;
            manager.getClient().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_PACKED_MUD_PLACE, 1.0F, 1.0F));
        }

        manager.getClient().getItemRenderer().renderInGui(this.item.getDefaultStack(), 8, 8);
        return startTime >= 5000L ? Visibility.HIDE : Visibility.SHOW;
    }

    public void reload() {
        this.soundPlayed = false;
    }

    @Override
    public Object getType() {
        return this;
    }
}
