package org.lend.chatping;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;


public class Chatping implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) -> {
            String text = message.getContent().getString().toLowerCase();

            if (text.contains("@everyone")) {
                for (ServerPlayerEntity player : sender.getServer().getPlayerManager().getPlayerList()) {
                    pingPlayer(player);
                }
                return;
            }

            for (ServerPlayerEntity player : sender.getServer().getPlayerManager().getPlayerList()) {
                String name = player.getGameProfile().getName().toLowerCase();

                if (text.contains(name)) {
                    pingPlayer(player);
                }
            }
        });
    }

    private void pingPlayer(ServerPlayerEntity player) {
        RegistryEntry<SoundEvent> sound = Registries.SOUND_EVENT.getEntry(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
        player.networkHandler.sendPacket(
                new PlaySoundS2CPacket(
                        sound,
                        SoundCategory.MASTER,
                        player.getX(), player.getY(), player.getZ(),
                        1.0F, 1.0F, player.getRandom().nextLong()
                )
        );

        // System.out.println("[ChatPing] Звук отправлен игроку " + player.getName().getString());
    }
}
