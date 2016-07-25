package mc.mod.prove.entity.ai;

import java.util.HashMap;
import java.util.Iterator;

import mc.mod.prove.entity.BlockEvent;
import mc.mod.prove.entity.ai.basic.EntityAILookAround;
import mc.mod.prove.entity.ai.enumerations.EntityDistance;
import mc.mod.prove.entity.ai.enumerations.EntityTimerLeft;
import mc.mod.prove.entity.ai.enumerations.Tricking;
import mc.mod.prove.entity.bayesian.BayesianHandler;
import mc.mod.prove.entity.transfer.EvidenceTO;
import mc.mod.prove.entity.transfer.TrickDeductionTO;
import mc.mod.prove.reasoning.HearingHandler;
import mc.mod.prove.reasoning.SightHandler;
import mc.mod.prove.reasoning.TrickHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNote;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import smile.Network;

public class EntityAILilyCentral extends EntityAIBase{
	
	private EntityCreature entity;
	private EntityPlayer player;
	//TODO RICORDA DI DECIDERE COME GESTIRE IL TIMER PER continue E start
	private int timer = 0;
	private EntityAIBase currentState;
	private Network net;
	private static final int TIMER_THRESHOLD = 1800;
	private static final int DISTANCE_THRESHOLD = 5;
	private static final int MAX_TIMER = 10;
	
	//la chiave è la positione del plate, PosAndTimer contiene la posizione del blocco e il timer
	private HashMap<BlockPos, BlockEvent> lightBlocks = new HashMap<BlockPos, BlockEvent>();
	private HashMap<BlockPos, BlockEvent> soundBlocks = new HashMap<BlockPos, BlockEvent>();
	
	//gestori per i dati e il decisore bayesiano
	private SightHandler sightHandler;
	private HearingHandler hearingHandler;
	private TrickHandler trickHandler;
	private BayesianHandler bayesianHandler;
	private EntityAIFactory factory;
	private EvidenceTO evidence;
	
	//TODO controlla
	//non metto mai a null per simulare memoria
	private BlockEvent lastLight = null;
	private BlockEvent lastSound = null;
	private BlockPos lastPlate = null;
	
	//coordinate bordi del labirinto
	private static final int MIN_X_LAB = 209;
	private static final int MAX_X_LAB = 222;
	private static final int MIN_Z_LAB = 114;
	private static final int MAX_Z_LAB = 127;
	
	private int globalTimer = 5000;
	//TODO RICORDA DI CAMBIARE
	private boolean matchStarted = true;
	
	
	public EntityAILilyCentral(EntityCreature entity, EntityPlayer player) {
		this.entity = entity;
		this.player = player;
		currentState = new EntityAILookAround(entity, 0.5);
		
		sightHandler = new SightHandler(entity);
		hearingHandler = new HearingHandler(entity);
		bayesianHandler = new BayesianHandler();
		trickHandler = new TrickHandler();
			
		//inizializza lista controllando tutte le posizioni del labirinto
		//TODO CAMBIA COORDINATE
		for (int x = MIN_X_LAB; x <= MAX_X_LAB; x++) {
			for (int z = MIN_Z_LAB; z <= MAX_Z_LAB; z++) {
				BlockPos pos = new BlockPos(x, 4, z);
				IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(pos);
				if(blockState.getBlock() instanceof BlockRedstoneLight){
					BlockPos platePos = this.findPressurePlate(pos);
					//se il blocco non è collegato a una pressure plate non viene calcolato in quanto non attivabile
					if(platePos != null){
						lightBlocks.put(platePos, new BlockEvent(0, pos));
					}
				} else if (blockState.getBlock() instanceof BlockNote) {
					BlockPos platePos = this.findPressurePlate(pos);
					if(platePos != null){
						soundBlocks.put(platePos, new BlockEvent(0, pos));
					}
				}
			}
		}
		System.out.println("Done exploring the labyrinth!");
		
		factory = new EntityAIFactory(this);
	}
	
	protected Iterator getLightPlates() {
		return lightBlocks.keySet().iterator();
	}
	
	protected Iterator getSoundPlates() {
		return soundBlocks.keySet().iterator();
	}
	
	protected EntityPlayer getPlayer() {
		return this.player;
	}
	
	protected EntityCreature getEntity(){
		return this.entity;
	}
	
	protected BlockPos getLastPlayerPosition(){
		return lastPlate;
	}
	
	protected int[] getLabyrinthLimits(){
		int[] a = {MIN_X_LAB, MAX_X_LAB, MIN_Z_LAB, MAX_Z_LAB};
		return a;
	}
	
	@Override
	public boolean shouldExecute() {
		return true;
	}
	
	//eseguito ad ogni tick
	private void beforeExecuting() {
		//aggiorna la variabile con l'ultima luce accesa
		if(lastLight != null) {
			int timer = lastLight.getTimer();	
			if(timer > 0) {
				timer--;
				lastLight.setTimer(timer);
			}
		}
		
		//aggiorna la variabile con l'ultimo suono emesso
		if(lastSound != null) {
			int timer = lastSound.getTimer();
			if(timer > 0) {
				timer--;
				lastSound.setTimer(timer);
			}
		}
		
		//indica quali plate sono stati premuti durante il tick e avvia i timer
	    BlockPos playerPos = new BlockPos((int) player.posX, (int) player.posY, (int) player.posZ);

		if(lightBlocks.containsKey(playerPos)){
			BlockPos pos = (lightBlocks.get(playerPos)).getPos();
			lastLight = new BlockEvent(5, pos);
			lastPlate = playerPos;			
		} else if (soundBlocks.containsKey(playerPos)) {
			BlockPos pos = (soundBlocks.get(playerPos)).getPos();
			lastSound = new BlockEvent(1, pos);
			lastPlate = playerPos;
		}
		
		//gestice i dati percepiti, raccolti o dedotti
		this.handleEvidence();
		
		globalTimer--;
				
		// perform bayesian decision every 1/5 of a second
		if(timer == 0) {
			this.bayesian();
			
			currentState.startExecuting();
			timer = MAX_TIMER;
		} else {
			currentState.continueExecuting();
			timer--;
		}
	}

	@Override
	public void startExecuting(){
		beforeExecuting();
	}
	
	@Override
	public boolean continueExecuting(){
		beforeExecuting();
		return true;
	}
	
	private void bayesian(){
		//imposta i dati percepiti o raccolti nel decisore bayesiano
		bayesianHandler.setEvidence(evidence);
		String stateName = bayesianHandler.getDecision();
		System.out.println(stateName);
		currentState = factory.getEntityAI(stateName);
	}
	
	//si occupa di elaborare i dati dei sensi e quelli raccolti o dedotti per dare indicazioni al decisore
	private void handleEvidence() {
		EntityDistance playerInSight, blockSound, lightChange, stepSound;
		EntityTimerLeft timerLeft;
		Tricking playerTricking;
		
		//stabilisce se sta per scadere il tempo
		if(globalTimer > TIMER_THRESHOLD) {
			timerLeft = EntityTimerLeft.Normal;
		} else {
			timerLeft = EntityTimerLeft.RunningOut;
		}
		lightChange = sightHandler.checkLight(lastLight, DISTANCE_THRESHOLD);
		blockSound = hearingHandler.checkBlockSound(lastSound, DISTANCE_THRESHOLD);
		stepSound = hearingHandler.checkStepSound(player, DISTANCE_THRESHOLD);
		playerInSight = sightHandler.checkPlayerInSight(player, DISTANCE_THRESHOLD);
		
		TrickDeductionTO to = new TrickDeductionTO(playerInSight, blockSound,
													lightChange, stepSound, lastLight,
													lastSound, player);
		playerTricking = trickHandler.isPlayerTricking(to);
		
		evidence = new EvidenceTO(playerInSight.name(), timerLeft.name(),
								lightChange.name(), stepSound.name(), blockSound.name(),
								playerTricking.name());
		
		System.out.println("Light Change " + lightChange.toString()
							+ "\nSound Block " + blockSound.toString()
							+ "\nStep Sound " + stepSound.toString()
							+ "\nPlayer in Sight " + playerInSight.toString()
							+ "\nPlayer Tricking " + playerTricking.toString());
		
	}
	
	
	
	//Trova il pressure plate corrispondente a un blocco sonoro o luminoso
	//TODO OFFSET per blocco sonoro al livello del terreno?
	private BlockPos findPressurePlate(BlockPos pos){
		boolean foundPlate = false;
		BlockPos[] positions = new BlockPos[4];
		BlockPos prev = pos;
		int i = 0;
		
		//sistema condizione di uscita
		do {
			boolean foundWire = false;
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			
			positions[0] = new BlockPos(x+1, y, z);
			positions[1] = new BlockPos(x-1, y, z);
			positions[2] = new BlockPos(x, y, z+1);
			positions[3] = new BlockPos(x, y, z-1);
			
			for(i = 0; i < 4 && !foundWire; i++){
				IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(positions[i]);
				Block b = blockState.getBlock();
				if (b instanceof BlockPressurePlate) {
					pos = positions[i];
					foundPlate = true;
					foundWire = true;
				} else if (b instanceof BlockRedstoneWire && positions[i].compareTo(prev) != 0) {
					prev = pos;
					pos = positions[i];
					foundWire = true;
				}
			}
		} while(!foundPlate && i <= 4);
		
		if(i <= 4){
			return pos;
		} else {
			return null;
		}
	}


}
