package mc.mod.prove.match;

import java.io.PrintWriter;
import java.util.Random;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.gui.ModGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class MatchHandler {
	public InventoryContentHandler inventory = new InventoryContentHandler();

	// initial countdown timer
	// set to 4 because sometimes number 3
	// isn't shown because of the Timer thread
	private static final int MAX_COUNTDOWN_TIME = 4;
	private int countDownTime = MAX_COUNTDOWN_TIME;

	// minutes and seconds of the current round
	public static final int MAX_ROUND_TIME = 5;
	private int minutesTime = MAX_ROUND_TIME;
	private int secsTime = 0;

	private boolean matchStarted = false;
	private boolean roundStarted = false;

	public static final int MAX_ROUNDS = 5;
	public static final int MIN_ROUNDS = 3;

	// roundsNumber set when starting the bet, represents number of
	// rounds in the match. The player can choose between 3 and 5 rounds,
	// if a different number is set an exception is thrown

	private int roundsNumber = 0;
	private int currentRound = 0;
	private int roundsWon = 0;

	private static final int MAX_SIGHT_VALUE = 10;
	private int sightValue = 0;

	// variables that represent the possible winners
	public static final int WINNER_PLAYER = 1;
	public static final int WINNER_LILY = 2;
	public static final int WINNER_NOBODY = 3;
	private int winner = WINNER_NOBODY;

	private static final int START_MATCH_TELEPORT = 0;
	private static final int STOP_ROUND_TELEPORT = 1;
	private static final int START_ROUND_TELEPORT = 2;
	private static final int STOP_MATCH_TELEPORT = 3;

	private boolean gamePaused = false;

	private final double[][] coord = new double[4][4];
	private int lastPlayerSpawnIndex = -1;
	
	private int matchCounter = 1;

	// initialization of the matrix with the coordinates
	// where to move player at the beginning of the round
	public MatchHandler() {
		coord[0][0] = 180.5;
		coord[0][1] = 193.143;
		coord[0][2] = 710.083;
		coord[0][3] = 707.075;

		coord[1][0] = 193.894;
		coord[1][1] = 181.889;
		coord[1][2] = 696.974;
		coord[1][3] = 701.326;

		coord[2][0] = 197.692;
		coord[2][1] = 187.726;
		coord[2][2] = 712.339;
		coord[2][3] = 699.857;

		coord[3][0] = coord[0][1];
		coord[3][1] = coord[0][0];
		coord[3][2] = coord[0][3];
		coord[3][3] = coord[0][2];
	}

	public int getRoundsWon() {
		return this.roundsWon;
	}

	public void setRoundsWon(int rounds) {
		this.roundsWon = rounds;
	}

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

		roundsWon = 0;
		currentRound = 0;

		matchStarted = true;
		inventory.emptyInventory(Minecraft.getMinecraft().thePlayer);

		handleTeleport(START_MATCH_TELEPORT);
	}

	public void stopMatch() {
		matchStarted = false;
		roundStarted = false;

		// resets countdown timer
		countDownTime = MAX_COUNTDOWN_TIME;
		inventory.refillInventory();

		EntityPlayer playerIn = Minecraft.getMinecraft().thePlayer;

		int roundsLost = roundsNumber - roundsWon;

		//calculates number of emeralds earned or lost
		if (roundsWon > (roundsNumber / 2)) {
			AwardHandler.addEmeralds(playerIn, (roundsWon * 2));
			ModGuiHandler.createGui(ModGuiHandler.GUI_WON_MATCH);
			
		} else {
			AwardHandler.removeEmeralds(playerIn, roundsLost);
			ModGuiHandler.createGui(ModGuiHandler.GUI_LOST_MATCH);
		}
		handleTeleport(STOP_MATCH_TELEPORT);
	}

	public void stopRound() {
		roundStarted = false;

		// resets countdown timer
		countDownTime = MAX_COUNTDOWN_TIME;

		this.handleTeleport(STOP_ROUND_TELEPORT);
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
		// resets countdown timer
		minutesTime = MAX_ROUND_TIME;
		secsTime = 0;

		// increases current round number
		this.setCurrentRound(this.getCurrentRound() + 1);

		// resets winner 
		winner = WINNER_NOBODY;
		// resets the value for the sight bar
		sightValue = 0;

		roundStarted = true;

		// teleports the player
		this.handleTeleport(START_ROUND_TELEPORT);
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

	private void handleTeleport(int teleportType) {

		if (teleportType == START_MATCH_TELEPORT
				|| teleportType == STOP_ROUND_TELEPORT) {
			Minecraft.getMinecraft().thePlayer
					.setPositionAndUpdate(188, 8, 705);
		} else if (teleportType == START_ROUND_TELEPORT) {
			Random rand = new Random();
			int n = rand.nextInt(coord.length);

			MainRegistry.lily.getNavigator().setPath(null, 0);
			
			boolean playerSet = false;
			int i = 1;
			//teleports player in a position distant enought from the npc
			while(!playerSet && i <=4) {
				if (lastPlayerSpawnIndex != -1
						&& (((new Vec3d(coord[n][0], 4, coord[n][2]))
								.distanceTo(MainRegistry.lily.getPositionVector()) < 10)
					|| lastPlayerSpawnIndex == n)) {
					System.out.println("Changed n!");
					n = (n + 1) % 4;
				} else {
					playerSet = true;
				}
				i++;
			}
			
			Minecraft.getMinecraft().thePlayer.setPositionAndUpdate(
					coord[n][0], 4, coord[n][2]);
			lastPlayerSpawnIndex = n;
			System.out.println("Player Spawn: " + coord[n][0] + ", "
			+ coord[n][2]);

			
			System.out.println("Lily Spawn: " + MainRegistry.lily.getPosition());
		} else if (teleportType == STOP_MATCH_TELEPORT) {
			Minecraft.getMinecraft().thePlayer.setPositionAndUpdate(
					MainRegistry.LAB_PLATE.getX() - 2, 4,
					MainRegistry.LAB_PLATE.getZ());
		}
	}
}
