package mc.mod.prove.entity.transfer;

public class EvidenceTO {
	private String playerInSight;
	private String timer;
	private String lightingChange;
	private String stepSound;
	private String blockSound;
	private String stateT;
	private String playerTricking;
	
	public EvidenceTO(String playerInSight, String timer,
			String lightingChange, String stepSound, String blockSound,
			String stateT, String playerTricking) {

		this.playerInSight = playerInSight;
		this.timer = timer;
		this.lightingChange = lightingChange;
		this.stepSound = stepSound;
		this.blockSound = blockSound;
		this.stateT = stateT;
		this.playerTricking = playerTricking;
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

	public String getStateT() {
		return stateT;
	}

	public String getPlayerTricking() {
		return playerTricking;
	}
	
}
