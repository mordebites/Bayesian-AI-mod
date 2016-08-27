package mc.mod.prove.entity.movement;

import mc.mod.prove.entity.EntityLilyMob;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;

public class JumpHelper {

	/*public static void pathHelper(EntityLiving entity) {
		if(entity.motionX != 0 && entity.motionZ != 0){
			BlockPos nextPos = new BlockPos(entity.posX + Math.signum(entity.motionX), 4, entity.posZ + (int)Math.signum(entity.motionZ));
			IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(nextPos);
			if(blockState.getBlock() instanceof BlockPressurePlate){
				System.out.println("Found Plate!");
				((EntityLilyMob) entity).jump();
				System.out.println("Jumped!");
			}
		}
	}*/
}
