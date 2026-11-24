package itamio.iamtogglesneak;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.lwjgl.glfw.GLFW;

/**
 * Toggle Sneak - Itamio Port (Fabric 1.20.1).
 *
 * <p>1.20.1 has no native toggle-sneak feature, so there is nothing to
 * "revert" here. This adds the behaviour instead. It is entirely client-side
 * and uses only public APIs - no mixins are required.</p>
 *
 * <p>The mod re-applies the desired pressed state every client tick because
 * vanilla clears the binding whenever the physical key is released. The
 * rising edge is taken from raw GLFW input rather than from the binding's
 * own pressed flag, since this mod itself drives that flag.</p>
 *
 * <p>Names follow the mappings this project compiles against (Loom's layered
 * mappings): {@code KeyMapping} and {@code setDown} are the sneak binding and
 * its public pressed-state setter.</p>
 */
public final class ToggleSneakClient implements ClientModInitializer {

    private boolean toggleActive;
    private boolean lastPhysicalDown;
    private boolean resyncEdge;
    private int boundKeyCode = -1;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(Minecraft mc) {
        Options options = mc.options;
        KeyMapping sneak = options.keyShift;
        long window = mc.getWindow().getWindow();

        boolean inGame = mc.player != null
                && mc.screen == null
                && GLFW.glfwGetWindowAttrib(window, GLFW.GLFW_FOCUSED) != GLFW.GLFW_FALSE;

        if (!inGame) {
            // GUI opened / focus lost: vanilla unpresses all keys, so drop the
            // latch too. Otherwise sneak could remain stuck on. Re-sync the
            // edge detector on return so a key still held across the GUI does
            // not look like a fresh tap.
            if (toggleActive) {
                toggleActive = false;
                sneak.setDown(false);
            }
            lastPhysicalDown = false;
            resyncEdge = true;
            return;
        }

        int keyCode = resolveBoundKeyCode(sneak);
        boolean physicalDown = isPhysicallyDown(window, sneak, keyCode);

        if (resyncEdge) {
            resyncEdge = false;
            lastPhysicalDown = physicalDown; // suppress phantom rising edge
        } else {
            if (physicalDown && !lastPhysicalDown) {
                toggleActive = !toggleActive;
            }
            lastPhysicalDown = physicalDown;
        }

        sneak.setDown(physicalDown || toggleActive);
    }

    /**
     * Resolves the raw GLFW code currently bound to sneak. The backing
     * {@code key} field is private in 1.20.1, so the public
     * {@link KeyMapping#matches(int, int)} / {@link KeyMapping#matchesMouse(int)}
     * API is used instead. Returns -1 when the key is unbound (rather than
     * falling back to the default key, which would be wrong for a rebind).
     */
    private int resolveBoundKeyCode(KeyMapping binding) {
        if (boundKeyCodeIsCurrent(binding)) {
            return boundKeyCode;
        }
        for (int code = 0; code <= 512; code++) {
            if (binding.matches(code, code)) {
                boundKeyCode = code;
                return code;
            }
        }
        for (int button = 0; button <= 16; button++) {
            if (binding.matchesMouse(button)) {
                boundKeyCode = button;
                return button;
            }
        }
        boundKeyCode = -1;
        return -1;
    }

    private boolean boundKeyCodeIsCurrent(KeyMapping binding) {
        if (boundKeyCode < 0) {
            return false;
        }
        return binding.matches(boundKeyCode, boundKeyCode) || binding.matchesMouse(boundKeyCode);
    }

    /** Reads the real, physical key/mouse-button state, independent of the binding's own flag. */
    private boolean isPhysicallyDown(long window, KeyMapping binding, int code) {
        if (code < 0) {
            return false;
        }
        if (binding.matchesMouse(code)) {
            return GLFW.glfwGetMouseButton(window, code) == GLFW.GLFW_PRESS;
        }
        return InputConstants.isKeyDown(window, code);
    }
}
