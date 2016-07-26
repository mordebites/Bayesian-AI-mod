package mc.mod.prove.match;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class MatchHandler {
	// tempo del countdown iniziale
	// il tempo del countdown e' stato settato a 4 poiche' a volte
	// il il numero 3 a causa del thread di Timer non e' mostrato
	private static final int MAX_COUNTDOWN_TIME = 4;
	private int countDownTime = MAX_COUNTDOWN_TIME;

	// minuti e secondi del round corrente

	public static final int MAX_ROUND_TIME = 5;
	private int minutesTime = MAX_ROUND_TIME;
	private int secsTime = 0;

	// variabili di stato del match e del round

	private boolean matchStarted = false;
	private boolean roundStarted = false;

	public static final int MAX_ROUNDS = 5;
	public static final int MIN_ROUNDS = 3;

	// roundsNumber verra' settato quando si comincia la scommessa
	// settiamo quindi il numero di round da giocare in tutto il match
	// e possibile scegliere tra 5 e 3 round se cosi' non fosse abbiamo una
	// eccezione

	private int roundsNumber;
	private int currentRound = 0;

	// variabili che serviranno per capire quando uno dei due
	// giocatori (mob o player) vince

	private static final int MAX_SIGHT_VALUE = 10;
	private int sightValue = 0;

	// variabili che gestiscono chi dei due giocatori ha vinto
	public static final int WINNER_PLAYER = 1;
	public static final int WINNER_LILY = 2;
	public static final int WINNER_NOBODY = 3;
	private int winner = WINNER_NOBODY;

	private boolean gamePaused = false;

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}

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

	public int getCurrentRound() {
		return currentRound;
	}

	public void setCurrentRound(int currentRound) {
		this.currentRound = currentRound;
	}

	public int getRoundsNumber() {
		return roundsNumber;
	}

	public void setRoundsNumber(int chosenRound) {
		if (chosenRound != MAX_ROUNDS && chosenRound != MIN_ROUNDS) {
			throw new RuntimeException("Numero di round errati!");
		}

		this.roundsNumber = chosenRound;
	}

	public void startMatch() {
		if (roundsNumber == 0) {
			throw new RuntimeException("Manca numero di round!");
		}

		// resetto il round corrente
		currentRound = 0;

		matchStarted = true;
	}

	public void stopMatch() {
		matchStarted = false;
		roundStarted = false;

		// resetto il timer del countdown
		countDownTime = MAX_COUNTDOWN_TIME;
	}

	public void stopRound() {
		roundStarted = false;

		// resetto il timer del countdown
		countDownTime = MAX_COUNTDOWN_TIME;
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

		// aumento il numero del round attuale
		this.setCurrentRound(this.getCurrentRound() + 1);

		// resetto il boolean che indica se il player ha toccato
		// l'entity e quindi ha vinto la partita

		winner = WINNER_NOBODY;
		sightValue = 0; // azzaro la visione del maialino

		roundStarted = true;

		// TODO CODICE PER CAMBIARE LA POSIZIONE CORRENTE DEL PLAYER E ALZARLO
		// sull'asse y
		EntityPlayer playerIn = Minecraft.getMinecraft().thePlayer;

		playerIn.setPositionAndUpdate(playerIn.posX, playerIn.posY + 40,
				playerIn.posZ);
	}

	public int getMaxSightValue() {
		return MAX_SIGHT_VALUE;
	}

	public int getSightValue() {
		return sightValue;
	}

	public void setSightValue(int sightValue) {
		if (this.isRoundStarted()) {
			this.sightValue = (sightValue > MAX_SIGHT_VALUE) ? MAX_SIGHT_VALUE
					: sightValue;
		}
	}
}
