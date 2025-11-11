package org.lend.chatping;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class Chatping implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) -> {
            String text = message.getContent().getString().toLowerCase();

            MinecraftServer server = sender.getEntityWorld().getServer(); // ГАРАНТИРОВАНО есть!

            if (text.contains("@everyone")) {
                for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                    pingPlayer(player);
                }
            } else {
                for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                    String name = player.getGameProfile().name().toLowerCase();
                    if (text.contains(name)) {
                        pingPlayer(player);
                    }
                }
            }
        });
    }

    private void pingPlayer(ServerPlayerEntity player) {
        player.playSoundToPlayer(
                SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                SoundCategory.MASTER,
                1.0F,
                1.0F
        );

        // System.out.println("[ChatPing] Звук отправлен игроку " + player.getName().getString());
    }
}