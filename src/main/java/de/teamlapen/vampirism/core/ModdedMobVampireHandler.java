package de.teamlapen.vampirism.core;

import de.teamlapen.vampirism.config.HostileToVampireConfig;
import de.teamlapen.vampirism.entity.ai.goals.NearestTargetGoalModifier;
import de.teamlapen.vampirism.mixin.accessor.GoalSelectorAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class ModdedMobVampireHandler {

    @SubscribeEvent
    public void onEntityJoin(EntityJoinLevelEvent event) {

        if (!(event.getEntity() instanceof Monster monster))
            return;

        ResourceLocation id =
                BuiltInRegistries.ENTITY_TYPE.getKey(monster.getType());

        if (HostileToVampireConfig.isException(id))
            return;

        for (WrappedGoal wrapped :
                ((GoalSelectorAccessor) monster.targetSelector)
                        .getAvailableGoals()) {

            Goal goal = wrapped.getGoal();

            if (goal instanceof NearestAttackableTargetGoal<?> target &&
                    goal instanceof NearestTargetGoalModifier modifier) {

                modifier.ignoreVampires();
            }
        }
    }
}