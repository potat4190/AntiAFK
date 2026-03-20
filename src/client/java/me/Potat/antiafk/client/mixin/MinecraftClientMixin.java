package me.Potat.antiafk.client.mixin;

import me.Potat.antiafk.client.AntiafkClient;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "onWindowFocusChanged", at = @At("HEAD"), cancellable = true)
    private void onWindowFocusChanged(boolean focused, CallbackInfo ci) {
        if (AntiafkClient.enabled && !focused) {
            ci.cancel();
        }
    }
}
