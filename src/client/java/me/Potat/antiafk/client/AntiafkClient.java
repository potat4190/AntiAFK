package me.Potat.antiafk.client;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.Random;

public class AntiafkClient implements ClientModInitializer {

    public static boolean enabled = false;
    private static final Random random = new Random();
    private static int movementDuration = 0; // ticks remaining for current random input
    private static final int MIN_DURATION = 20; // 1 second
    private static final int MAX_DURATION = 100; // 5 seconds

    @Override
    public void onInitializeClient() {
        registerCommands();
        registerEvents();
    }

    private void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("antiafk")
                .then(ClientCommandManager.argument("enabled", BoolArgumentType.bool())
                        .executes(context -> {
                            enabled = context.getArgument("enabled", Boolean.class);
                            MinecraftClient client = MinecraftClient.getInstance();
                            if (client.player == null) {
                                context.getSource().sendError(Text.literal("You must be in a world to use this command."));
                                return 0;
                            }

                            if (enabled) {
                                movementDuration = 0; // start immediately
                                context.getSource().sendFeedback(Text.literal("Anti-AFK has been enabled."));
                            } else {
                                resetMovementKeys(client);
                                context.getSource().sendFeedback(Text.literal("Anti-AFK has been disabled."));
                            }
                            return 1;
                        }))));
    }

    private void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!enabled || client.player == null) {
                return;
            }

            if (movementDuration > 0) {
                movementDuration--;
                // keep current key state until duration ends
                // occasional small random jump while moving
                client.options.jumpKey.setPressed(random.nextInt(100) < 3);
                return;
            }

            // duration ended -> choose new random movement or a pause
            applyRandomMovement(client);
        });
    }

    private void applyRandomMovement(MinecraftClient client) {
        // small chance to pause (no movement) for a short duration
        boolean pause = random.nextInt(100) < 15; // 15% chance to pause
        if (pause) {
            resetMovementKeys(client);
            movementDuration = MIN_DURATION + random.nextInt(40); // short pause
            return;
        }

        // Choose random combination of WASD. Avoid opposite pairs both true (W+S or A+D)
        boolean forward = random.nextInt(100) < 60; // 60% chance
        boolean back = !forward && (random.nextInt(100) < 25); // if not forward, small chance to go back
        boolean left = random.nextInt(100) < 35;
        boolean right = !left && (random.nextInt(100) < 35);

        // Occasionally strafe and move together
        if (!forward && !back && (random.nextInt(100) < 25)) {
            forward = true;
        }

        client.options.forwardKey.setPressed(forward);
        client.options.backKey.setPressed(back);
        client.options.leftKey.setPressed(left);
        client.options.rightKey.setPressed(right);

        // Randomly jump more often while moving
        client.options.jumpKey.setPressed(random.nextInt(100) < 15);

        movementDuration = MIN_DURATION + random.nextInt(MAX_DURATION - MIN_DURATION + 1);
    }

    private void resetMovementKeys(MinecraftClient client) {
        client.options.forwardKey.setPressed(false);
        client.options.backKey.setPressed(false);
        client.options.leftKey.setPressed(false);
        client.options.rightKey.setPressed(false);
        client.options.jumpKey.setPressed(false);
        client.options.sneakKey.setPressed(false);
    }
}
