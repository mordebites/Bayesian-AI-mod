package mc.mod.prove.gui.KeyHandler;

import mc.mod.prove.gui.MasterInterfacer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyInputHandler {
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (KeyBindings.pauseKey.isPressed()) {
			System.out.println("Pressed P");
			
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			World world  = Minecraft.getMinecraft().theWorld;
			
			player.openGui(MasterInterfacer.instance, 0, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		}
	}
}
