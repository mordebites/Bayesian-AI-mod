package mc.mod.prove.entity;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.entity.ai.EntityAILilyCentral;
import mc.mod.prove.match.InventoryContentHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityLilyMob extends EntityPig {

	public EntityLilyMob(World worldIn) {
		super(worldIn);
		initTasks();
		MainRegistry.lily = this;
		InventoryContentHandler.removeLilyEggs(Minecraft.getMinecraft().thePlayer);

		//IBlockState blockState = Blocks.dirt.getDefaultState();
		/*List<ItemStack> subBlocks = new ArrayList<ItemStack>();
		Blocks.dirt.getSubBlocks(null, null, subBlocks);
		IBlockState blockState = Blocks.dirt.getStateFromMeta(subBlocks.get(1).getMetadata());
		
		for(int x = 150; x <= 200; x++) {
			for(int z = 728; z <= 778; z++) {
				BlockPos pos = new BlockPos(x, 3, z);
				IBlockState currentState = Minecraft.getMinecraft().theWorld.getBlockState(pos);
				Minecraft.getMinecraft().theWorld.setBlockState(new BlockPos(x, 2, z), blockState, 2);
				if(!(currentState.getBlock() instanceof BlockRedstoneLight)){
					Minecraft.getMinecraft().theWorld.setBlockState(pos, blockState, 2);
				}
			}
		}*/
	}

	private void initTasks() {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;

		if (player != null) {
			// appena spawnato l'npc controllo che il player abbia abbastanza
			// emeralds per giocare la partita

			this.tasks.addTask(0, new EntityAILilyCentral(this, player));
			this.tasks.addTask(0, new EntityAIWander(this, 1));
		}
	}

	public void jump() {
		this.motionY = 0.2;

		System.out.println("Sprinting, baby!");
		this.motionX += 0.4 * Math.signum(this.motionX);
		this.motionZ += 0.4 * Math.signum(this.motionX);

		this.isAirBorne = true;
		net.minecraftforge.common.ForgeHooks.onLivingJump(this);
	}
/*
	protected SoundEvent getAmbientSound() {
		return null;
	}
	*/
	
	@Override
	public void setDead() {
		super.setDead();
		MainRegistry.lily = null;
		InventoryContentHandler.insertLilyEggs();
	}
}
