package net.satisfy.skillcraft.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.satisfy.skillcraft.SkillcraftIdentifier;

public class SkillButton extends TexturedButtonWidget {
    public final static int SKILL_BUTTON_WIDTH = 30;
    public final static int SKILL_BUTTON_HEIGHT = 24;
    public final static Identifier TEXTURE = new SkillcraftIdentifier("textures/gui/skillcraft_book_left.png");

    public SkillButton(int x, int y, PressAction pressAction, Text text) {
        super(x, y, SKILL_BUTTON_WIDTH, SKILL_BUTTON_HEIGHT, 154, 36, 28, TEXTURE, 256, 256, pressAction, text);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        super.renderButton(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, this.active ? 16777215 : 10526880 | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }
}