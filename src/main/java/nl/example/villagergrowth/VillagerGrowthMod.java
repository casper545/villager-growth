package nl.example.villagergrowth;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class VillagerGrowthMod implements ModInitializer {

    /** Aantal golden carrots dat een pasgeboren baby nodig heeft. */
    public static final int CARROTS_FOR_NEWBORN = 20;

    /** Een baby begint op leeftijd -24000 ticks (20 minuten). */
    public static final int BABY_AGE_TICKS = 24000;

    /** Elke golden carrot versnelt de groei met 1200 ticks. */
    public static final int TICKS_PER_CARROT = BABY_AGE_TICKS / CARROTS_FOR_NEWBORN;

    @Override
    public void onInitialize() {
        UseEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> {
            if (!(entity instanceof Villager villager) || !villager.isBaby()) {
                return InteractionResult.PASS;
            }

            ItemStack stack = player.getItemInHand(hand);
            if (!stack.is(Items.GOLDEN_CARROT)) {
                return InteractionResult.PASS;
            }

            if (!(level instanceof ServerLevel serverLevel)) {
                return InteractionResult.SUCCESS; // client: alleen animatie
            }

            // Leeftijd is negatief bij baby's: -24000 (net geboren) .. 0 (volwassen).
            // Elke carrot schuift de leeftijd 1200 ticks op naar 0, dus hoe dichter
            // bij volwassen, hoe minder carrots er nog nodig zijn.
            int newAge = Math.min(0, villager.getAge() + TICKS_PER_CARROT);
            villager.setAge(newAge);
            stack.consume(1, player); // doet niets in creative

            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    villager.getX(), villager.getY() + 1.0, villager.getZ(),
                    5, 0.3, 0.3, 0.3, 0.0);
            serverLevel.playSound(null, villager.blockPosition(),
                    SoundEvents.VILLAGER_YES, SoundSource.NEUTRAL, 1.0F, 1.0F);

            if (newAge >= 0) {
                player.displayClientMessage(Component.translatable("message.villagergrowth.grown"), true);
            } else {
                int remaining = (int) Math.ceil(-newAge / (double) TICKS_PER_CARROT);
                player.displayClientMessage(
                        Component.translatable("message.villagergrowth.remaining", remaining), true);
            }

            return InteractionResult.SUCCESS;
        });
    }
}
