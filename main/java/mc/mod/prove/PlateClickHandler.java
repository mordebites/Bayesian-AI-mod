package mc.mod.prove;

import mc.mod.prove.entity.ai.EntityAILilyCentral;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlateClickHandler {
	@SubscribeEvent
	public void onRightClickBlock(PlayerInteractEvent event) {
		if(event instanceof RightClickBlock){
			IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(event.getPos());
			//if(blockState.getBlock() instanceof BlockRedstoneLight){
				//System.out.println(EntityAIBayesianCentral.findPressurePlate(event.getPos()));
			//}
		}

	}

}