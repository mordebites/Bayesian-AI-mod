package mc.mod.prove.entity;

import net.minecraft.util.math.BlockPos;

/**
 * Class used to represent activated blocks.
 */
public class BlockEvent{
	private int timer;
	private BlockPos position;
	
	public BlockEvent(int timer, BlockPos pos) {
		this.timer = timer;
		this.position = pos;
	}
	
	public int getTimer() {
		return timer;
	}

	public BlockPos getPos() {
		return position;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}	
}
