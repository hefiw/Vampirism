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
    private int displayTime = 20000;
    private boolean done = false;

    public SkillUnlockOverlay(ISkill<?> skill) {
        this.skill = skill;
        this.title = skill.getName();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (done || displayTime <= 0) return;

        Minecraft mc = Minecraft.getInstance();
        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();

        int x = w / 2 - 110;
        int y = h / 2 - 90;

        // Фон
        graphics.fill(x - 5, y - 5, x + 225, y + 175, 0x07000000);

        // Название золотым с glow
        int tx = w / 2;
        graphics.drawCenteredString(mc.font, title, tx, y + 15, 0xFFDDAA00);

        RenderSystem.enableBlend();
        for (int i = 1; i <= 3; i++) {
            graphics.drawCenteredString(mc.font, title, tx + (i % 2), y + 15 + (i / 2), 0x55FFCC44);
        }

        // Иконка — исправлено (используем подход из SkillNodeScreen)
        ResourceLocation icon = VResourceLocation.mod("textures/skills/" + getSkillIconPath(skill) + ".png");
        graphics.blit(icon, x + 85, y + 50, 0, 0, 32, 32, 32, 32);

        // Описание
        Component desc = skill.getDescription() != null ? skill.getDescription() : Component.empty();
        graphics.drawWordWrap(mc.font, desc, x + 25, y + 105, 180, 0xFFEEEEEE);

        displayTime--;
    }

    private String getSkillIconPath(ISkill<?> skill) {
        String key = skill.getTranslationKey();
        key = key.replace("skill.vampirism.", "").replace("skill.", "").replace(".", "_");
        return key;
    }

    public boolean onKeyPressed(int keyCode) {
        if (keyCode == 256) { // ESC
            displayTime = 0;
            return true;
        }
        return false;
    }

    public void setDone() {
        done = true;
    }

    public boolean isDone() {
        return done || displayTime <= 0;
    }
}