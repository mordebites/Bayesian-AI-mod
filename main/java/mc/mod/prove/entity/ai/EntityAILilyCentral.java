package mc.mod.prove.entity.ai;

import java.util.HashMap;
import java.util.Iterator;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.entity.BlockEvent;
import mc.mod.prove.entity.ai.basic.EntityAILookAround;
import mc.mod.prove.entity.ai.decision.DecisorFactory;
import mc.mod.prove.entity.ai.decision.Decisor;
import mc.mod.prove.entity.ai.decision.tree.TreeDecisor;
import mc.mod.prove.entity.ai.enumerations.EntityDistance;
import mc.mod.prove.entity.ai.enumerations.TimerLeft;
import mc.mod.prove.entity.transfer.EvidenceTO;
import mc.mod.prove.gui.sounds.SoundHandler;
import mc.mod.prove.match.MatchHandler;
import mc.mod.prove.match.PlayerDefeatHandler;
import mc.mod.prove.reasoning.HearingHandler;
import mc.mod.prove.reasoning.SightHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNote;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
/**
 * Central handler that manages all Lily's activities.
 */
public class EntityAILilyCentral extends EntityAIBase {
	private static final int TIMER_SEC_THRESHOLD = 90;
	private static final int DISTANCE_THRESHOLD = 5;
	private static final int MAX_BAYESIAN_TIMER = 5;
	private static final int MINS_IN_SEC = 60;
	private static final int OUT_OF_SIGHT_MAX = 15;
	
	//constants used  for sight bar
	private static final int SIGHT_BAR_TIMER_1 = 3;
	private static final int SIGHT_BAR_TIMER_2 = 6;
	private static final int SIGHT_BAR_TIMER_3 = 9;
	private static final int SIGHT_BAR_DISTANCE_1 = 3;
	private static final int SIGHT_BAR_DISTANCE_2 = 7;
	private static final int EXCEEDING_SIGHT_BAR = 11;	
	
	private Entity lily;
	private Entity opponent;

	//timer used to know when to perform the decision for the next state
	private int tickTimer = 0;
	// basic behaviour to execute in a certain moment
	private EntityAIBase currentState;
	

	// key: position of the pressure plate
	// value: BlockEvent representing block position and activation timer
	private HashMap<BlockPos, BlockEvent> lightBlocks = new HashMap<BlockPos, BlockEvent>();
	private HashMap<BlockPos, BlockEvent> soundBlocks = new HashMap<BlockPos, BlockEvent>();

	// data handlers and decisor
	private SightHandler sightHandler;
	private HearingHandler hearingHandler;
	private Decisor decisor;
	private EntityAIFactory factory;
	private EvidenceTO currentEvidence;

	// after initialization, is never set to null
	// to simulate memory 
	private BlockEvent lastLight = null;
	private BlockEvent lastSound = null;
	// contains last activated block position
	// or player's last known position
	private BlockPos lastPosition = null;

	// ot set Lily's sight bar
	private int sightValue = 0;
	// Lily's previous distance from the player
	private int prevPlayerDistance;
	private boolean playerAlreadySeen = false;
	private int outOfSightTimer = 0;

	public EntityAILilyCentral(EntityCreature entity, EntityPlayer player) {
		this.lily = entity;
		this.opponent = player;
		currentState = new EntityAILookAround(entity, 0.4);

		sightHandler = new SightHandler(entity, player);
		hearingHandler = new HearingHandler(entity);
		decisor = DecisorFactory.getDecisor(DecisorFactory.BAYES_AGGRESSIVE);

		prevPlayerDistance = (int) entity.getPositionVector().distanceTo(
				player.getPositionVector());

		// fills dictionaries with all blocks and plates in the labyrinth
		for (int x = MainRegistry.MIN_X_LAB; x <= MainRegistry.MAX_X_LAB; x++) {
			for (int z = MainRegistry.MIN_Z_LAB; z <= MainRegistry.MAX_Z_LAB; z++) {
				BlockPos pos = new BlockPos(x, 4, z);
				IBlockState blockState = Minecraft.getMinecraft().theWorld
						.getBlockState(pos);
				if (blockState.getBlock() instanceof BlockRedstoneLight) {
					BlockPos platePos = this.findPressurePlate(pos);
					// if block is not linked to a pressure plate
					// it's not considered because it can't be activated
					if (platePos != null) {
						lightBlocks.put(platePos, new BlockEvent(0, pos));
					}
				} else if (blockState.getBlock() instanceof BlockNote) {
					BlockPos platePos = this.findPressurePlate(pos);
					if (platePos != null) {
						soundBlocks.put(platePos, new BlockEvent(0, pos));
					}
				}
			}
		}
		System.out.println("Done exploring the labyrinth!");

		factory = new EntityAIFactory(this);
	}

	@Override
	public boolean shouldExecute() {
		return true;
	}

	// executed once per tick, contains main actions
	protected void beforeExecuting() {
		//entity is set to LookAround at the beginning of each round
		if (MainRegistry.match.getMinutesTime() == MatchHandler.MAX_ROUND_TIME) {
			currentState = new EntityAILookAround((EntityCreature) lily, 0.4);
		}

		// updates reference to last activated light block
		if (lastLight != null) {
			int timer = lastLight.getTimer();
			if (timer > 0) {
				timer--;
				lastLight.setTimer(timer);
			}
		}

		// updates reference to last activated sound block
		if (lastSound != null) {
			int timer = lastSound.getTimer();
			if (timer > 0) {
				timer--;
				lastSound.setTimer(timer);
			}
		}

		BlockPos playerPos = new BlockPos((int) opponent.posX,
				(int) opponent.posY, (int) opponent.posZ);
		
		//checks if player is on a pressure plate and activates block timer
		if (lightBlocks.containsKey(playerPos)) {
			BlockPos pos = (lightBlocks.get(playerPos)).getPos();
			lastLight = new BlockEvent(5, pos);
			lastPosition = playerPos;
		} else if (soundBlocks.containsKey(playerPos)) {
			BlockPos pos = (soundBlocks.get(playerPos)).getPos();
			lastSound = new BlockEvent(1, pos);
			lastPosition = playerPos;
		}

		//handles perceived and collected data
		this.handleEvidence();

		// performs decision every 1/5 of a second
		if (tickTimer % MAX_BAYESIAN_TIMER == 0) {
			this.decision();
			System.out.println();
			currentState.startExecuting();
		} else {
			currentState.continueExecuting();
		}

		tickTimer++;
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

	//sets currentState based on evidence
	private void decision() {
		// sends data to decisor and obtains decision
		String stateName = decisor.getDecision(currentEvidence);
		currentState = factory.getEntityAI(stateName);
	}

	// manages perceived or known data needed by the decisor
	private void handleEvidence() {
		EntityDistance playerInSight, blockSound, lightChange, stepSound;
		TimerLeft timerLeft;

		// decides if match is about to be over
		if ((MainRegistry.match.getMinutesTime() * MINS_IN_SEC + MainRegistry.match
				.getSecsTime()) > TIMER_SEC_THRESHOLD) {
			timerLeft = TimerLeft.RunningOut;
		} else {
			timerLeft = TimerLeft.Normal;
		}
		lightChange = sightHandler.checkLight(lastLight, DISTANCE_THRESHOLD);
		blockSound = hearingHandler.checkBlockSound(lastSound,
				DISTANCE_THRESHOLD);
		stepSound = hearingHandler.checkStepSound(opponent, DISTANCE_THRESHOLD);

		//if the player is not in the fov
		//then playerAlreadySeen is set to false
		if (currentEvidence != null && MainRegistry.match.isRoundStarted()) {
			playerAlreadySeen = !currentEvidence.getPlayerInSight().contains(
					"None");
		} else {
			playerAlreadySeen = false;
		}

		playerInSight = sightHandler.checkPlayerInSight(opponent,
				DISTANCE_THRESHOLD, playerAlreadySeen);

		if (playerInSight != EntityDistance.None) {
			lastPosition = opponent.getPosition();
		}

		currentEvidence = new EvidenceTO(playerInSight.name(),
				timerLeft.name(), lightChange.name(), stepSound.name(),
				blockSound.name(), currentState.getClass().getName()
						.substring(37));

		handleSightBar();
	}

	//manages Lily's sight bar that indicates player's level of stealth
	private void handleSightBar() {
		EntityDistance playerInSight = EntityDistance.valueOf(currentEvidence
				.getPlayerInSight());
		// sight bar is empty at the beginning of the round
		if (MainRegistry.match.getMinutesTime() == MatchHandler.MAX_ROUND_TIME) {
			sightValue = 0;
			playerAlreadySeen = false;
			outOfSightTimer = 0;
		} else {
			// to update bar during the match
			if (tickTimer % SIGHT_BAR_TIMER_1 == 0) {
				if (playerInSight != EntityDistance.None && sightValue <= 10) {
					outOfSightTimer = 0;
					int currentDistance = (int) lily.getPositionVector()
							.distanceTo(opponent.getPositionVector());
					if (prevPlayerDistance >= currentDistance) {
						// TODO costanti
						if ((currentDistance <= SIGHT_BAR_DISTANCE_1)
								|| (currentDistance > SIGHT_BAR_DISTANCE_1 && currentDistance <= SIGHT_BAR_DISTANCE_2 
									&& tickTimer % SIGHT_BAR_TIMER_2 == 0)
								|| (currentDistance > SIGHT_BAR_DISTANCE_2 && tickTimer % SIGHT_BAR_TIMER_3 == 0)) {
							sightValue++;
							if (sightValue == EXCEEDING_SIGHT_BAR) {
								PlayerDefeatHandler.onPlayerDefeat();
							}
						}
					}
				} else if (playerInSight == EntityDistance.None
						&& sightValue > 0) {
					outOfSightTimer++;
					//if player is making step sound then it's considered unlinkely to be tricking
					//thus bar can decrease
					if (EntityDistance.valueOf(currentEvidence.getStepSound()) != EntityDistance.None) {
							sightValue--;
					} else {
						if (outOfSightTimer >= OUT_OF_SIGHT_MAX) {
							sightValue--;
						}
					}
				}
			}
		}

		//if Lily sees the player during the match a sound is played
		if (playerInSight != EntityDistance.None && !playerAlreadySeen
				&& MainRegistry.match.getWinner() == MatchHandler.WINNER_NOBODY
				&& MainRegistry.match.isMatchStarted()) {

			SoundHandler.handlePlayerInSightSound((EntityPlayer) opponent);
		}

		MainRegistry.match.setSightValue(sightValue);
		prevPlayerDistance = (int) lily.getPositionVector().distanceTo(
				opponent.getPositionVector());

		tickTimer++;
	}

	// finds pressure plate corresponding to a light or sound block
	protected static BlockPos findPressurePlate(BlockPos pos) {
		boolean foundPlate = false;
		BlockPos[] positions = new BlockPos[4];
		BlockPos prev = pos;
		int i = 0;

		//follows the redstone wire until it gets
		//to the block linked to the plate
		do {
			boolean foundWire = false;
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();

			positions[0] = new BlockPos(x + 1, y, z);
			positions[1] = new BlockPos(x - 1, y, z);
			positions[2] = new BlockPos(x, y, z + 1);
			positions[3] = new BlockPos(x, y, z - 1);

			for (i = 0; i < 4 && !foundWire; i++) {
				IBlockState blockState = 
						Minecraft.getMinecraft().theWorld.getBlockState(positions[i]);
				Block b = blockState.getBlock();
				if (b instanceof BlockPressurePlate) {
					pos = positions[i];
					foundPlate = true;
					foundWire = true;
				} else if (b instanceof BlockRedstoneWire
						&& positions[i].compareTo(prev) != 0) {
					prev = pos;
					pos = positions[i];
					foundWire = true;
				}
			}
		} while (!foundPlate && i < 4);

		if (i < 4) {
			return pos;
		} else {
			return null;
		}
	}

	protected Iterator getLightPlates() {
		return lightBlocks.keySet().iterator();
	}

	protected Iterator getSoundPlates() {
		return soundBlocks.keySet().iterator();
	}

	protected Entity getPlayer() {
		return this.opponent;
	}

	protected Entity getEntity() {
		return this.lily;
	}

	protected BlockPos getLastPlayerPosition() {
		return lastPosition;
	}
}
