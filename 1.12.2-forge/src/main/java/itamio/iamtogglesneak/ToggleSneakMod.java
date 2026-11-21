package itamio.iamtogglesneak;

import itamio.iamtogglesneak.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Toggle Sneak - Itamio Port (Forge 1.12.2).
 *
 * <p>1.12.2 has no native toggle-sneak feature, so there is nothing to
 * "revert". This mod adds the behaviour instead: tapping sneak latches it on,
 * tapping again releases it. Purely client-side.</p>
 */
@Mod(modid = ToggleSneakMod.MOD_ID, name = ToggleSneakMod.MOD_NAME, version = ToggleSneakMod.VERSION)
public class ToggleSneakMod {

    public static final String MOD_ID = "itamiotogglesneak";
    public static final String MOD_NAME = "Toggle Sneak \u2014 Itamio Port";
    public static final String VERSION = "1.0.0";

    @SidedProxy(
            clientSide = "itamio.iamtogglesneak.client.ClientProxy",
            serverSide = "itamio.iamtogglesneak.common.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }
}
