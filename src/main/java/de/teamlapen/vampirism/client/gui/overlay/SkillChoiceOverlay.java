package de.teamlapen.vampirism.client.gui.overlay;

import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.api.util.VResourceLocation;
import de.teamlapen.vampirism.client.VampirismModClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SkillChoiceOverlay implements Renderable {
    private final List<ISkill<?>> skills;
    private boolean done = false;
    private final int cardWidth = 100;
    private final int cardHeight = 160;

    public SkillChoiceOverlay(List<ISkill<?>> skills) {
        this.skills = skills;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (done) return;

        Minecraft mc = Minecraft.getInstance();
        int baseX = mc.getWindow().getGuiScaledWidth() / 2 - (skills.size() * 120 / 2);
        int baseY = mc.getWindow().getGuiScaledHeight() / 2 - 85;

        for (int i = 0; i < skills.size(); i++) {
            ISkill<?> skill = skills.get(i);
            int x = baseX + i * 120;
            int y = baseY;

            boolean hovered = mouseX > x && mouseX < x + cardWidth && mouseY > y && mouseY < y + cardHeight;

            graphics.fill(x - 2, y - 2, x + cardWidth + 2, y + cardHeight + 2, 0x88000000);
            graphics.fill(x, y, x + cardWidth, y + cardHeight, hovered ? 0xCC555555 : 0xAA222222);

            graphics.drawCenteredString(mc.font, skill.getName(), x + cardWidth/2, y + 12, 0xFFDDAA00);

            ResourceLocation icon = VResourceLocation.mod("textures/gui/skills/" + getIconPath(skill) + ".png");
            graphics.blit(icon, x + 34, y + 40, 0, 0, 32, 32, 32, 32);

            // ЗАЩИТА ОТ NULL
            Component desc = skill.getDescription() != null ? skill.getDescription() : Component.empty();
            graphics.drawWordWrap(mc.font, desc, x + 8, y + 85, cardWidth - 16, 0xFFEEEEEE);
        }
    }

    private String getIconPath(ISkill<?> skill) {
        String key = skill.getTranslationKey();
        key = key.replace("skill.vampirism.", "").replace("skill.", "").replace(".", "_");
        return key;
    }

    public boolean mouseClicked(double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        int baseX = mc.getWindow().getGuiScaledWidth() / 2 - (skills.size() * 60);
        int baseY = mc.getWindow().getGuiScaledHeight() / 2 - 85;

        for (int i = 0; i < skills.size(); i++) {
            int x = baseX + i * 120;
            if (mouseX > x && mouseX < x + cardWidth && mouseY > baseY && mouseY < baseY + cardHeight) {
                ISkill<?> selected = skills.get(i);
                // Показываем подтверждение выбора
                VampirismModClient.getINSTANCE().getOverlay().showSkillUnlock(selected, false);
                done = true;
                return true;
            }
        }
        return false;
    }

    public boolean isDone() {
        return done;
    }
}