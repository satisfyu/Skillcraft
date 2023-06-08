package net.bmjo.skillcraft.client.screen;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.bmjo.skillcraft.client.SkillcraftClient;
import net.bmjo.skillcraft.json.SkillLoader;
import net.bmjo.skillcraft.skill.Skillset;
import net.bmjo.skillcraft.util.SkillComparator;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class SkillBookScreen extends Screen {
    protected int x;
    protected int y;
    public final static int WIDTH = 294;
    public final static int HEIGHT = 147;
    private Identifier currentSkill;
    private LevelScrollWidget skillLevelsWidget;
    private final List<SkillButton> skillButtons = Lists.newArrayList();

    public SkillBookScreen() {
        super(Text.literal("SKILLS").formatted(Formatting.GOLD));
        currentSkill = SkillcraftClient.lastBookSkill;
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - WIDTH) / 2;
        this.y = (this.height - HEIGHT) / 2;

        createSkillButtons(SkillLoader.REGISTRY_SKILLS);
        SkillsScrollWidget skillsWidget = new SkillsScrollWidget(this.x, this.y, this.skillButtons);
        skillLevelsWidget = new LevelScrollWidget(this.x + WIDTH / 2, this.y, currentSkill, textRenderer);
        reloadSkill(currentSkill);

        this.addDrawableChild(skillsWidget);
        this.addDrawableChild(skillLevelsWidget);
    }

    private void createSkillButtons(Map<Identifier, Skillset> skillsets) {
        int skill = 0;
        for (Identifier identifier : skillsets.keySet().stream().sorted(new SkillComparator()).toList()) {
            SkillButton skillButton = new SkillButton(
                    x + 26 + (SkillButton.WIDTH + 4) * (skill % 3),
                    y + 46 + (SkillButton.HEIGHT + 4) * (skill / 3),
                    (button) -> reloadSkill(identifier),
                    Text.literal(skillsets.get(identifier).getName())
            );
            this.skillButtons.add(skillButton);
            skill++;
        }
    }

    private void reloadSkill(Identifier skill) {
        this.currentSkill = skill;
        SkillcraftClient.lastBookSkill = currentSkill;
        skillLevelsWidget.setSkill(skill);
    }
}