package mc.mod.prove.entity.ai;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.entity.BlockEvent;
import mc.mod.prove.entity.ai.enumerations.EntityDistance;
import mc.mod.prove.entity.ai.enumerations.EntityTimerLeft;
import mc.mod.prove.entity.ai.enumerations.Tricking;
import mc.mod.prove.entity.transfer.EvidenceTO;
import mc.mod.prove.entity.transfer.TrickDeductionTO;
import mc.mod.prove.gui.sounds.SoundHandler;
import mc.mod.prove.match.MatchHandler;
import mc.mod.prove.match.PlayerDefeatHandler;
import mc.mod.prove.reasoning.HearingHandler;
import mc.mod.prove.reasoning.SightHandler;
import mc.mod.prove.reasoning.TrickHandler;
import net.minecraft.block.BlockNote;
import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAILilyFake extends EntityAIBase {
	private EvidenceTO prevEvidence;
	private EvidenceTO currentEvidence;
	private String prevState = "LookAround";
	private String currentState = "LookAround";
	private LinkedList<PlayerInfo> info = new LinkedList<PlayerInfo>();

	private Entity playerLily;
	private Entity playerOpponent;

	private int tickTimer = 0;
	private static final int TIMER_SEC_THRESHOLD = 90;
	private static final int DISTANCE_THRESHOLD = 5;
	private static final int MINS_IN_SEC = 60;

	// la chiave � la positione del plate, PosAndTimer contiene la posizione del
	// blocco e il timer
	private HashMap<BlockPos, BlockEvent> lightBlocks = new HashMap<BlockPos, BlockEvent>();
	private HashMap<BlockPos, BlockEvent> soundBlocks = new HashMap<BlockPos, BlockEvent>();

	// gestori per i dati e il decisore bayesiano
	private SightHandler sightHandler;
	private HearingHandler hearingHandler;
	private TrickHandler trickHandler;

	// non metto mai a null per simulare memoria
	private BlockEvent lastLight = null;
	private BlockEvent lastSound = null;
	private BlockPos lastPlate = null;

	// per settare la barra Lily's sight
	private int sightValue = 0;
	// distanza al quarto di secondo precedente
	private int prevDistance;
	private boolean playerAlreadySeen = false;

	public EntityAILilyFake(Entity playerLily, Entity playerOpponent) {
		this.playerLily = playerLily;
		this.playerOpponent = playerOpponent;
		// currentState = new EntityAILookAround(playerLily, 0.5);

		sightHandler = new SightHandler((EntityLivingBase) playerLily,
				(EntityPlayer) playerOpponent);
		hearingHandler = new HearingHandler(playerLily);
		trickHandler = new TrickHandler();

		prevDistance = (int) playerLily.getPositionVector().distanceTo(
				playerOpponent.getPositionVector());

		// inizializza lista controllando tutte le posizioni del labirinto
		for (int x = MainRegistry.MIN_X_LAB; x <= MainRegistry.MAX_X_LAB; x++) {
			for (int z = MainRegistry.MIN_Z_LAB; z <= MainRegistry.MAX_Z_LAB; z++) {
				BlockPos pos = new BlockPos(x, 4, z);
				IBlockState blockState = Minecraft.getMinecraft().theWorld
						.getBlockState(pos);
				if (blockState.getBlock() instanceof BlockRedstoneLight) {
					BlockPos platePos = EntityAILilyCentral
							.findPressurePlate(pos);
					// se il blocco non � collegato a una pressure plate non
					// viene calcolato in quanto non attivabile
					if (platePos != null) {
						lightBlocks.put(platePos, new BlockEvent(0, pos));
					}
				} else if (blockState.getBlock() instanceof BlockNote) {
					BlockPos platePos = EntityAILilyCentral
							.findPressurePlate(pos);
					if (platePos != null) {
						soundBlocks.put(platePos, new BlockEvent(0, pos));
					}
				}
			}
		}
		System.out.println("Done exploring the labyrinth!");
	}

	@Override
	public boolean shouldExecute() {
		return true;
	}

	// eseguito ad ogni tick
	private void beforeExecuting() {
		// aggiorna la variabile con l'ultima luce accesa
		if (lastLight != null) {
			int timer = lastLight.getTimer();
			if (timer > 0) {
				timer--;
				lastLight.setTimer(timer);
			}
		}

		// aggiorna la variabile con l'ultimo suono emesso
		if (lastSound != null) {
			int timer = lastSound.getTimer();
			if (timer > 0) {
				timer--;
				lastSound.setTimer(timer);
			}
		}

		// indica quali plate sono stati premuti durante il tick e avvia i timer
		BlockPos playerPos = new BlockPos((int) playerOpponent.posX,
				(int) playerOpponent.posY, (int) playerOpponent.posZ);

		if (lightBlocks.containsKey(playerPos)) {
			BlockPos pos = (lightBlocks.get(playerPos)).getPos();
			lastLight = new BlockEvent(5, pos);
			lastPlate = playerPos;
		} else if (soundBlocks.containsKey(playerPos)) {
			BlockPos pos = (soundBlocks.get(playerPos)).getPos();
			lastSound = new BlockEvent(1, pos);
			lastPlate = playerPos;
		}

		// gestice i dati percepiti, raccolti o dedotti
		this.handleEvidence();
		PlayerInfo currentInfo = new PlayerInfo(tickTimer,
				playerLily.getPositionVector(), playerLily.isSneaking(),
				playerLily.isSprinting());
		info.addLast(currentInfo);

		if (currentEvidence.compareTo(prevEvidence) != 0) {
			this.determineState();
			//segna il log con prevState che è state_T alla prima occorrenza
			//e poi state_T diventa currentState
			info = new LinkedList<PlayerInfo>();
			prevState = currentState;
		}

		tickTimer++;
	}

	private void determineState() {
		currentState = "LookAround";
		double distance1, distance2;
		
		if (prevEvidence.getPlayerInSight().compareTo("None") != 0) {
			distance1 = info.getFirst().playerPos.distanceTo(playerOpponent
					.getPositionVector());
			distance2 = info.getLast().playerPos.distanceTo(playerOpponent
					.getPositionVector());

			if (distance1 < distance2) {
				currentState = "Hunt";
			} else {
				currentState = "Flee";
			}
		} else {
			if (prevEvidence.getBlockSound().compareTo("None") != 0
				|| prevEvidence.getLightingChange().compareTo("None") != 0) {
				if (info.getFirst().playerPos.distanceTo(info.getLast().playerPos) < 5) {
					currentState = "LookAround";
				} else {
					distance1 = info.getFirst().playerPos.distanceTo(new Vec3d(
							lastPlate.getX(), lastPlate.getY(), lastPlate.getZ()));
					distance2 = info.getLast().playerPos.distanceTo(new Vec3d(
							lastPlate.getX(), lastPlate.getY(), lastPlate.getZ()));

					if (distance1 < distance2) {
						currentState = "Suspect";
					} else {
						Iterator<Entry<BlockPos, BlockEvent>> itr;
						boolean found = false;
						for (int i = 0; i <= 1 && !found; i++) {
							if (i == 0) {
								itr = lightBlocks.entrySet().iterator();
							} else {
								itr = soundBlocks.entrySet().iterator();
							}
							
							while (itr.hasNext() && !found) {
								BlockPos pos = itr.next().getKey();
								Vec3d vec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
								distance1 = info.getFirst().playerPos.distanceTo(vec);
								distance2 = info.getLast().playerPos.distanceTo(vec);

								if (distance2 > distance1 && distance2 < 5) {
									currentState = "Trick";
									found = true;
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void startExecuting() {
		if (MainRegistry.match.isRoundStarted()) {
			beforeExecuting();
		}
	}

	@Override
	public boolean continueExecuting() {
		if (MainRegistry.match.isRoundStarted()) {
			beforeExecuting();
		}
		return true;
	}

	// si occupa di elaborare i dati dei sensi e quelli raccolti o dedotti per
	// dare indicazioni al decisore
	private void handleEvidence() {
		EntityDistance playerInSight, blockSound, lightChange, stepSound;
		EntityTimerLeft timerLeft;
		Tricking playerTricking;

		// stabilisce se sta per scadere il tempo
		if ((MainRegistry.match.getMinutesTime() * MINS_IN_SEC + MainRegistry.match
				.getSecsTime()) > TIMER_SEC_THRESHOLD) {
			timerLeft = EntityTimerLeft.Normal;
		} else {
			timerLeft = EntityTimerLeft.RunningOut;
		}
		lightChange = sightHandler.checkLight(lastLight, DISTANCE_THRESHOLD);
		blockSound = hearingHandler.checkBlockSound(lastSound,
				DISTANCE_THRESHOLD);
		stepSound = hearingHandler.checkStepSound(
				(EntityPlayer) playerOpponent, DISTANCE_THRESHOLD);

		if (currentEvidence != null && MainRegistry.match.isRoundStarted()) {
			playerAlreadySeen = !currentEvidence.getPlayerInSight().contains(
					"None");
		} else {
			playerAlreadySeen = false;
		}

		playerInSight = sightHandler.checkPlayerInSight(playerOpponent,
				DISTANCE_THRESHOLD);

		TrickDeductionTO to = new TrickDeductionTO(playerInSight, blockSound,
				lightChange, stepSound, lastLight, lastSound,
				(EntityPlayer) playerOpponent);
		playerTricking = trickHandler.isPlayerTricking(to);

		currentEvidence = new EvidenceTO(playerInSight.name(),
				timerLeft.name(), lightChange.name(), stepSound.name(),
				blockSound.name(), playerTricking.name());

		handleSightBar();

	}

	private void handleSightBar() {
		EntityDistance playerInSight = EntityDistance.valueOf(currentEvidence
				.getPlayerInSight());
		// per aggiornare ogni quarto di secondo la barra Lily's Sight
		if (tickTimer % 2 == 0) {
			// TODO costante
			if (playerInSight != EntityDistance.None && sightValue <= 10) {
				int currentDistance = (int) playerLily.getPositionVector()
						.distanceTo(playerOpponent.getPositionVector());
				if (prevDistance >= currentDistance) {
					// TODO costanti
					if ((currentDistance <= 3)
							|| (currentDistance > 3 && currentDistance <= 7 && tickTimer % 6 == 0)
							|| (currentDistance > 7 && tickTimer % 10 == 0)) {
						sightValue++;
					}
				}
			} else if (playerInSight == EntityDistance.None
					&& EntityDistance.valueOf(currentEvidence.getStepSound()) != EntityDistance.None
					&& sightValue > 0) {
				sightValue--;
			}

			MainRegistry.match.setSightValue(sightValue);
			prevDistance = (int) playerLily.getPositionVector().distanceTo(
					playerOpponent.getPositionVector());

			if (tickTimer % 20 == 0) {
				System.out.println("Lily's position: "
						+ playerLily.getPosition().toString());
			}
		}

		if (playerInSight != EntityDistance.None && !playerAlreadySeen
				&& MainRegistry.match.getWinner() == MatchHandler.WINNER_NOBODY
				&& MainRegistry.match.isMatchStarted()) {
			SoundHandler
					.handlePlayerInSightSound((EntityPlayer) playerOpponent);
		}

		// resetta il valore da assegnare alla barra quando ricomincia il round
		if (MainRegistry.match.getMinutesTime() == MatchHandler.MAX_ROUND_TIME) {
			sightValue = 0;
		}

		tickTimer++;

		if (sightValue == 11) {
			PlayerDefeatHandler.onPlayerDefeat();
		}
	}

	protected Iterator getLightPlates() {
		return lightBlocks.keySet().iterator();
	}

	protected Iterator getSoundPlates() {
		return soundBlocks.keySet().iterator();
	}

	protected Entity getPlayer() {
		return this.playerOpponent;
	}

	protected Entity getEntity() {
		return this.playerLily;
	}

	protected BlockPos getLastPlayerPosition() {
		return lastPlate;
	}
}
