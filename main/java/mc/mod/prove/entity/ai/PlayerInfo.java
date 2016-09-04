package mc.mod.prove.entity.ai;

import net.minecraft.util.math.Vec3d;


class PlayerInfo {
	int tick;
	Vec3d playerPos;
	boolean isSneaking;
	boolean isSprinting;
	
	public PlayerInfo(int tick, Vec3d playerPos, boolean isSneaking,
			boolean isSprinting) {
		this.tick = tick;
		this.playerPos = playerPos;
		this.isSneaking = isSneaking;
		this.isSprinting = isSprinting;
	}
}
