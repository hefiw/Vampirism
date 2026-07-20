package de.teamlapen.vampirism.entity.player;

import de.teamlapen.vampirism.api.event.PlayerFactionEvent;
import de.teamlapen.vampirism.entity.player.hunter.HunterPlayer;
import de.teamlapen.vampirism.entity.player.hunter.skills.HunterSkills;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.player.vampire.skills.VampireSkills;
import net.neoforged.bus.api.SubscribeEvent;

public class AutoSkillUnlockHandler {
    @SubscribeEvent
    public void onLevelChanged(PlayerFactionEvent.FactionLevelChanged event) {
        if (event.getNewLevel() <= event.getOldLevel()) return;
        if (event.getNewLevel() > 4) return;

        event.getCurrentFaction().getPlayerCapability(event.getPlayer().asEntity()).ifPresent(factionPlayer -> {

                    if (factionPlayer instanceof VampirePlayer vampire) {
                        if (event.getNewLevel() >= 2 &&
                                !vampire.getSkillHandler().isSkillEnabled(VampireSkills.NIGHT_VISION.get())) {
                            vampire.getSkillHandler().enableSkill(VampireSkills.NIGHT_VISION.get());
                        }

                        if (event.getNewLevel() >= 3 &&
                                !vampire.getSkillHandler().isSkillEnabled(VampireSkills.VAMPIRE_REGENERATION.get())) {
                            vampire.getSkillHandler().enableSkill(VampireSkills.VAMPIRE_REGENERATION.get());
                        }

                        if (event.getNewLevel() >= 4 &&
                                !vampire.getSkillHandler().isSkillEnabled(VampireSkills.FLEDGLING.get())) {
                            vampire.getSkillHandler().enableSkill(VampireSkills.FLEDGLING.get());
                        }
                    }

                    if (factionPlayer instanceof HunterPlayer hunter) {
                        if (event.getNewLevel() >= 2)
                            hunter.getSkillHandler().enableSkill(HunterSkills.STAKE1.get());

                        if (event.getNewLevel() >= 3)
                            hunter.getSkillHandler().enableSkill(HunterSkills.WEAPON_TABLE.get());

                        if (event.getNewLevel() >= 4)
                            hunter.getSkillHandler().enableSkill(HunterSkills.HUNTER_DISGUISE.get());
                    }
                });
    }
}