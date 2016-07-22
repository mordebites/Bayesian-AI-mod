package mc.mod.prove.match;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class MatchHandler {
	// tempo del countdown iniziale

	private static final int MAX_START_TIME = 3;
	private int countDownTime = MAX_START_TIME;

	// minuti del round

	private static final int MAX_ROUND_TIME = 5;
	private int minutesTime = MAX_ROUND_TIME;
	private int secsTime = 0;

	// variabili di stato del match e del round

	private boolean matchStarted = false;
	private boolean roundStarted = false;

	private int maxRound;
	private int currentRound = 1;

	// variabili che serviranno per capire quando uno dei due
	// giocatori (mob o player) vice

	private static final int MAX_SIGHT_VALUE = 10;
	private int sightValue = 0;
	private boolean opponentHit = false;

	private boolean gamePaused = false;

	public int getCountDownTime() {
		return countDownTime;
	}

	public void setCountDownTime(int startTime) {
		this.countDownTime = startTime;
	}

	public boolean isGamePaused() {
		return gamePaused;
	}

	public void setGamePaused(boolean gamePaused) {
		this.gamePaused = gamePaused;
	}

	public int getMaxRound() {
		return maxRound;
	}

	public void setMaxRound(int maxRound) {
		this.maxRound = maxRound;
	}

	public void startMatch() {
		if (maxRound == 0) {
			throw new RuntimeException("Manca numero di round!");
		}
		
		// resetto il timer del countdown
		countDownTime = MAX_START_TIME;
		
		matchStarted = true;

		// TODO CODICE PER CAMBIARE LA POSIZIONE CORRENTE DEL PLAYER E ALZARLO
		// sull'asse y
		EntityPlayer playerIn = Minecraft.getMinecraft().thePlayer;

		playerIn.setPositionAndUpdate(playerIn.posX, playerIn.posY + 40,
				playerIn.posZ);
	}

	public void stopMatch() {
		matchStarted = false;
		roundStarted = false;
	}

	public boolean isMatchStarted() {
		return matchStarted;
	}

	public boolean isRoundStarted() {
		return roundStarted;
	}

	public int getMinutesTime() {
		return minutesTime;
	}

	public void setMinutesTime(int minutesTime) {
		this.minutesTime = minutesTime;
	}

	public int getSecsTime() {
		return secsTime;
	}

	public void setSecsTime(int secsTime) {
		this.secsTime = secsTime;
	}

	public void startRound() {
		// resetto il timer del round
		minutesTime = MAX_ROUND_TIME;
		secsTime = 0;

		roundStarted = true;
	}

	public int getMaxSightValue() {
		return MAX_SIGHT_VALUE;
	}

	public int getSightValue() {
		return sightValue;
	}

	public void setSightValue(int sightValue) {
		this.sightValue = (sightValue > MAX_SIGHT_VALUE) ? MAX_SIGHT_VALUE
				: sightValue;
	}
}
