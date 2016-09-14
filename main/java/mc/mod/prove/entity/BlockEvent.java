package mc.mod.prove.entity;

import net.minecraft.util.math.BlockPos;

public class BlockEvent{
	private int timer;
	private BlockPos pos;
	private boolean perceived = false;
	
	public BlockEvent(int timer, BlockPos pos) {
		this.timer = timer;
		this.pos = pos;
	}
	
	public int getTimer() {
		return timer;
	}

	public BlockPos getPos() {
		return pos;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public void setPerceived(boolean perceived){
		perceived = true;
	}
	
}
