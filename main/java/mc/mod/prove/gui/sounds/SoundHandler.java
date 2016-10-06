package mc.mod.prove.gui.sounds;

import mc.mod.prove.MainRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Class that manages custom sounds.
 */
public class SoundHandler {
	public static SoundEvent lily_alert;

	/**
	 * Register the {@link SoundEvent}s.
	 */
	public static void init() {
		lily_alert = registerSound("lily_alert");
	}

	/**
	 * Register a {@link SoundEvent}.
	 *
	 * @param soundName
	 *            The SoundEvent's name without the testmod3 prefix
	 * @return The SoundEvent
	 */
	private static SoundEvent registerSound(String soundName) {
		final ResourceLocation soundID = new ResourceLocation(MainRegistry.MODID, soundName);
		return GameRegistry.register(new SoundEvent(soundID)
				.setRegistryName(soundID));
	}
	
	//plays sound for when the NPC saw the player
	public static void handlePlayerInSightSound(EntityPlayer player) {
		player.worldObj.playSound(player, player.posX, player.posY,
				player.posZ, SoundHandler.lily_alert,	SoundCategory.AMBIENT, 2.0F, 1.0F);
	}
}