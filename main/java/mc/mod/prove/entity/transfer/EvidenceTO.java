package mc.mod.prove.entity.transfer;

public class EvidenceTO {
	private String playerInSight;
	private String timer;
	private String lightingChange;
	private String stepSound;
	private String blockSound;
	
	public EvidenceTO(String playerInSight, String timer, String lightingChange,
			String stepSound, String blockSound) {

		this.playerInSight = playerInSight;
		this.timer = timer;
		this.lightingChange = lightingChange;
		this.stepSound = stepSound;
		this.blockSound = blockSound;
	}
	
	public int compareTo(EvidenceTO ev2) {
		int result = -1;
		if(this.playerInSight.compareTo(ev2.playerInSight) == 0
			&& this.timer.compareTo(ev2.timer) == 0
			&& this.lightingChange.compareTo(ev2.lightingChange) == 0
			&& this.stepSound.compareTo(ev2.stepSound) == 0
			&& this.blockSound.compareTo(ev2.blockSound) == 0) {
				
			result = 0;
		}
		return result;
	}
	
	public String toString() {
		String result = "PlayerInSight: " + playerInSight +", "
						+ "Timer: " + timer +", "
						+ "Lighting Change: " + lightingChange + ", "
						+ "Step Sound: " + stepSound + ", "
						+ "Block Sound: " + blockSound;
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
