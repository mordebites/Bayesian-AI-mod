package mc.mod.prove.entity.transfer;

/**
 * Transfer object to pass data from central handler to decisor.
 */
public class EvidenceTO {
	private String playerInSight;
	private String timer;
	private String lightingChange;
	private String stepSound;
	private String blockSound;
	private String stateT;
	
	public EvidenceTO(String playerInSight, String timer, String lightingChange,
			String stepSound, String blockSound, String stateT) {

		this.playerInSight = playerInSight;
		this.timer = timer;
		this.lightingChange = lightingChange;
		this.stepSound = stepSound;
		this.blockSound = blockSound;
		this.stateT = stateT;
	}
	
	public int compareTo(EvidenceTO ev2) {
		int result = -1;
		if(this.playerInSight.compareTo(ev2.playerInSight) == 0
			&& this.timer.compareTo(ev2.timer) == 0
			&& this.lightingChange.compareTo(ev2.lightingChange) == 0
			&& this.stepSound.compareTo(ev2.stepSound) == 0
			&& this.blockSound.compareTo(ev2.blockSound) == 0
			&& this.blockSound.compareTo(ev2.getStateT()) == 0) {
				
			result = 0;
		}
		return result;
	}
	
	public String getStateT() {
		return this.stateT;
	}

	public String toString() {
		String result = "PlayerInSight: " + playerInSight +", "
						+ "Timer: " + timer +", "
						+ "Lighting Change: " + lightingChange + ", "
						+ "Step Sound: " + stepSound + ", "
						+ "Block Sound: " + blockSound + ", "
						+ "StateT: " + stateT;
		return result;
	}

	public String getPlayerInSight() {
		return playerInSight;
	}

	public String getTimer() {
		return timer;
	}

	public String getLightingChange() {
		return lightingChange;
	}

	public String getStepSound() {
		return stepSound;
	}

	public String getBlockSound() {
		return blockSound;
	}	
}
