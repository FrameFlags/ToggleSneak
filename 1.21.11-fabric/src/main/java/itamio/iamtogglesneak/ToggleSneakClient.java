package itamio.iamtogglesneak;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

import java.lang.reflect.Field;

/**
 * Toggle Sneak - Itamio Port (Fabric 26.3).
 *
 * <p>Client-side toggle for the sneak binding. No mixins are required. The
 * rising edge is read from physical keyboard state, not from the binding's
 * own pressed flag, because this mod drives that flag itself.</p>
 *
 * <p>Adapted from the 1.20.1 build for Minecraft 26.3: the screen accessor is
 * {@code Minecraft#gui#screen()}, focus is checked with
 * {@code Minecraft#isWindowActive()}, and physical key state comes from
 * {@code InputConstants#isKeyDown(int)}. Mouse-bound sneak keys are not
 * tracked on 26.3 because no public per-button state API is available.</p>
 */
public final class ToggleSneakClient implements ClientModInitializer {

    private boolean toggleActive;
    private boolean lastPhysicalDown;
    private boolean resyncEdge;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(Minecraft mc) {
        Options options = mc.options;
        KeyMapping sneak = options.keyShift;

        boolean inGame = mc.player != null
                && mc.screen == null
                && mc.isWindowActive();

        if (!inGame) {
            if (toggleActive) {
                toggleActive = false;
                sneak.setDown(false);
            }
            lastPhysicalDown = false;
            resyncEdge = true;
            return;
        }

        boolean physicalDown = isPhysicallyDown(mc.getWindow(), boundKey(sneak));

        if (resyncEdge) {
            resyncEdge = false;
            lastPhysicalDown = physicalDown;
        } else {
            if (physicalDown && !lastPhysicalDown) {
                toggleActive = !toggleActive;
            }
            lastPhysicalDown = physicalDown;
        }

        sneak.setDown(physicalDown || toggleActive);
    }

    /** Reads the currently bound key; falls back to the default binding. */
    private InputConstants.Key boundKey(KeyMapping binding) {
        try {
            Field field = KeyMapping.class.getDeclaredField("key");
            field.setAccessible(true);
            InputConstants.Key key = (InputConstants.Key) field.get(binding);
            if (key != null) {
                return key;
            }
        } catch (ReflectiveOperationException ignored) {
            // Fall through to the default binding.
        }
        return binding.getDefaultKey();
    }

    /** Physical keyboard state, independent of the binding's own pressed flag. */
    private boolean isPhysicallyDown(Window window, InputConstants.Key key) {
        if (key == null || key.getType() == InputConstants.Type.MOUSE) {
            return false;
        }
        return InputConstants.isKeyDown(window, key.getValue());
    }
}
