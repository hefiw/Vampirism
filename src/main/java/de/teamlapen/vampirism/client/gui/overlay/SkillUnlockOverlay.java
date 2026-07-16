package de.teamlapen.vampirism.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.api.util.VResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SkillUnlockOverlay implements Renderable {
    private final ISkill<?> skill;
    private final Component title;
    private final Component description;
    private int displayTime = 200;
    private boolean choiceMode = false;

    public SkillUnlockOverlay(ISkill<?> skill, boolean choiceMode) {
        this.skill = skill;
        this.title = skill.getName();
        this.description = skill.getDescription() != null ? skill.getDescription() : Component.empty();
        this.choiceMode = choiceMode;
        this.displayTime = choiceMode ? Integer.MAX_VALUE : 200;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (displayTime <= 0 && !choiceMode) return;

        Minecraft mc = Minecraft.getInstance();
        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();

        int x = w / 2 - 110;
        int y = h / 2 - 90;

        // Фон
        graphics.fill(x - 5, y - 5, x + 225, y + 175, 0xCC111111);
        graphics.fill(x, y, x + 220, y + 170, 0xAA000000);

        // Название золотым с glow
        int tx = w / 2;
        graphics.drawCenteredString(mc.font, title, tx, y + 15, 0xFFDDAA00);

        RenderSystem.enableBlend();
        for (int i = 1; i <= 3; i++) {
            graphics.drawCenteredString(mc.font, title, tx + (i % 2), y + 15 + (i / 2), 0x55FFCC44);
        }

        // Иконка — исправлено (используем подход из SkillNodeScreen)
        ResourceLocation icon = VResourceLocation.mod("textures/gui/skills/" + getSkillIconPath(skill) + ".png");
        graphics.blit(icon, x + 85, y + 50, 0, 0, 32, 32, 32, 32);

        // Описание
        graphics.drawWordWrap(mc.font, description, x + 25, y + 105, 180, 0xFFEEEEEE);

        if (choiceMode) {
            graphics.drawCenteredString(mc.font, Component.translatable("text.vampirism.skill.choose_branch"), tx, y + 145, 0xFFFFFF55);
        }

        displayTime--;
    }

    private String getSkillIconPath(ISkill<?> skill) {
        // Безопасный fallback, так как getRegistryName() отсутствует в интерфейсе
        String id = skill.getTranslationKey().replace("skill.", "").replace(".", "_");
        return id;
    }

    public boolean onKeyPressed(int keyCode) {
        if (keyCode == 256) { // ESC
            displayTime = 0;
            return true;
        }
        return false;
    }

    public boolean isDone() {
        return displayTime <= 0 && !choiceMode;
    }
}