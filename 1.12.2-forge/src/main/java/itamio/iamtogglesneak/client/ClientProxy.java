package itamio.iamtogglesneak.client;

import itamio.iamtogglesneak.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

/**
 * Client-side toggle-sneak logic for 1.12.2.
 *
 * <p>Vanilla clears the sneak binding on every physical key release, so the
 * desired state is re-applied on every client tick. The rising edge is read
 * from the real keyboard state ({@link Keyboard#isKeyDown(int)}), never from
 * the binding's own pressed flag, because this mod drives that flag itself.</p>
 */
public class ClientProxy extends CommonProxy {

    private boolean toggleActive;
    private boolean lastPhysicalDown;
    private boolean resyncEdge;

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        // Applied at both START and END: vanilla processes keyboard events in
        // the middle of the tick and may clear the binding in between.
        update();
    }

    private void update() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.world == null) {
            reset();
            return;
        }

        // Opening a GUI unpresses all keys in vanilla; reset our latch too so
        // sneak can never be stuck on.
        if (mc.currentScreen != null) {
            if (toggleActive) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
            }
            reset();
            resyncEdge = true;
            return;
        }

        KeyBinding sneak = mc.gameSettings.keyBindSneak;
        int keyCode = sneak.getKeyCode();
        boolean physicalDown = keyCode > 0 && Keyboard.isKeyDown(keyCode);

        if (resyncEdge) {
            resyncEdge = false;
            lastPhysicalDown = physicalDown; // suppress phantom rising edge
        } else {
            if (physicalDown && !lastPhysicalDown) {
                toggleActive = !toggleActive;
            }
            lastPhysicalDown = physicalDown;
        }

        KeyBinding.setKeyBindState(keyCode, physicalDown || toggleActive);
    }

    private void reset() {
        toggleActive = false;
        lastPhysicalDown = false;
    }
}
