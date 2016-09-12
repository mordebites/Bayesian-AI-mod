package mc.mod.prove.entity.ai;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.entity.BlockEvent;
import mc.mod.prove.entity.transfer.EvidenceTO;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAILilyProva extends EntityAILilyCentral {
	private static final double AVERAGE_SPEED = 1/20;
	private EvidenceTO stateStartEvidence;
	private String prevState = "LookAround";
	private String currentState = "LookAround";
	private LinkedList<PlayerInfo> info = new LinkedList<PlayerInfo>();
	private double selfDistance = 0;
	private Vec3d firstLilyPos;

	public EntityAILilyProva(EntityCreature entity, EntityPlayer player) {
		super(entity, player);
		stateStartEvidence = new EvidenceTO("Close", "Normal", "None", "None",
				"None", "LookAround");
		firstLilyPos = entity.getPositionVector();
	}

	@Override
	protected void beforeExecuting() {
		if (MainRegistry.match.getMinutesTime() == MainRegistry.match.MAX_ROUND_TIME
				&& MainRegistry.match.getSecsTime() == 0) {
			prevState = "LookAround";
			currentState = "LookAround";
		}
		
		super.beforeExecuting();
		PlayerInfo currentInfo = new PlayerInfo(tickTimer,
				lily.getPositionVector(), lily.isSneaking(), lily.isSprinting());
		info.addLast(currentInfo);
		
		
		// se sono cambiate le variabili esterne
		if (currentEvidence.compareTo(stateStartEvidence) != 0) {
			this.determineState2();
			// segna il log con prevState che è state_T alla prima occorrenza
			// e poi state_T diventa currentState
			info = new LinkedList<PlayerInfo>();
			prevState = currentState;
			stateStartEvidence = currentEvidence;
			firstLilyPos = lily.getPositionVector();
		} else {
			if (currentState.compareTo("LookAround") == 0) {
				double x = lily.getPositionVector().xCoord;
				double z = lily.getPositionVector().zCoord;
				
				if(Math.abs(x - firstLilyPos.xCoord) > 2
						|| Math.abs(z - firstLilyPos.zCoord) > 2) {
					prevState = currentState;
					currentState = "Trick";
				}	
			}
			
			if(currentState.compareTo("Trick") == 0
				&& (lily.motionX == 0 && lily.motionZ == 0)) {
				prevState = currentState;
				currentState = "LookAround";
				firstLilyPos = lily.getPositionVector();
			}
		}

		System.out.println(prevState + " " + currentEvidence.toString()
				+ " " + currentState);
		System.out.println(lily.getPosition());

	}

	private void determineState() {

		// TODO meglio prevState o LookAround?
		currentState = prevState;
		double playerDist1, playerDist2, selfDist;
		BlockPos lastPos = super.getLastPlayerPosition();
		Vec3d playerLastVec = new Vec3d(lastPos.getX(), lastPos.getY(),
				lastPos.getZ());
		playerDist1 = info.getFirst().playerPos.distanceTo(playerLastVec);
		playerDist2 = info.getLast().playerPos.distanceTo(playerLastVec);
		selfDist = info.getFirst().playerPos
				.distanceTo(info.getLast().playerPos);

		if (stateStartEvidence.getPlayerInSight().compareTo("None") != 0) {
			if (playerDist1 > playerDist2) {
				currentState = "Hunt";
			} else {
				currentState = "Flee";
			}
		} else {
			if (prevState.compareTo("Hunt") == 0) {
				if (playerDist1 < playerDist2) {
					currentState = "Suspect";
				}
			} else if (prevState.compareTo("Flee") == 0) {
				if (playerDist1 < playerDist2) {
					currentState = "Flee";
				} else {
					currentState = "LookAround";
				}
			}

			if (stateStartEvidence.getBlockSound().compareTo("None") != 0
					|| stateStartEvidence.getLightingChange().compareTo("None") != 0) {
				if (selfDist < 5) {
					currentState = "LookAround";
				} else if (playerDist1 < playerDist2) {
					currentState = "Suspect";
				}
			}

			if (currentState.compareTo("LookAround") == 0) {
				Iterator<Entry<BlockPos, BlockEvent>> itr;
				boolean found = false;
				for (int i = 0; i <= 1 && !found; i++) {
					if (i == 0) {
						itr = super.getLightPlates();
					} else {
						itr = super.getSoundPlates();
					}

					while (itr.hasNext() && !found) {
						BlockPos pos = itr.next().getKey();
						Vec3d vec = new Vec3d(pos.getX(), pos.getY(),
								pos.getZ());
						playerDist1 = info.getFirst().playerPos.distanceTo(vec);
						playerDist2 = info.getLast().playerPos.distanceTo(vec);

						if (playerDist2 > playerDist1 && playerDist2 < 5) {
							currentState = "Trick";
							found = true;
						}
					}
				}
			}
		}
	}

	private void determineState2() {

		currentState = prevState;
		double playerLastPosDist1, playerLastPosDist2, selfDist;
		BlockPos lastPos = super.getLastPlayerPosition();
		Vec3d playerLastVec;
		if(lastPos != null) {
			playerLastVec = new Vec3d(lastPos.getX(), lastPos.getY(),
				lastPos.getZ());
		} else {
			playerLastVec = opponent.getPositionVector();
		}
		
		
		int timeInterval = info.getLast().tick - info.getFirst().tick;	
		double expectedSelfDist = this.AVERAGE_SPEED * timeInterval;
		
		playerLastPosDist1 = info.getFirst().playerPos
				.distanceTo(playerLastVec);
		playerLastPosDist2 = info.getLast().playerPos.distanceTo(playerLastVec);
		selfDist = info.getFirst().playerPos
				.distanceTo(info.getLast().playerPos);
		

		System.out.println();
		double x = lily.getPositionVector().xCoord;
		double z = lily.getPositionVector().zCoord;
		
		if(Math.abs(x - firstLilyPos.xCoord) <= 2
				&& Math.abs(z - firstLilyPos.zCoord) <= 2) {
			currentState = "LookAround";
		} else {
			if (stateStartEvidence.getPlayerInSight().compareTo("None") != 0) {
				if (playerLastPosDist1 > playerLastPosDist2 && selfDist >= expectedSelfDist) {
					currentState = "Hunt";
				} else if (playerLastPosDist1 < playerLastPosDist2) {
					currentState = "Flee";
				}
			} else {
				if (prevState.compareTo("Hunt") == 0) {
					if (playerLastPosDist1 < playerLastPosDist2) {
						currentState = "Suspect";
					}
				} else if (prevState.compareTo("Flee") == 0) {
					if (playerLastPosDist1 < playerLastPosDist2) {
						currentState = "Flee";
					}
				}
			}

			if (stateStartEvidence.getPlayerInSight().compareTo("None") != 0) {
				if (playerLastPosDist1 > playerLastPosDist2 
					&& selfDist >= expectedSelfDist) {
					currentState = "Hunt";
				} else if (playerLastPosDist1 < playerLastPosDist2
						&& selfDist >= expectedSelfDist) {
					currentState = "Flee";
				}
			} else {
				if (prevState.compareTo("Hunt") == 0) {
					if (playerLastPosDist1 < playerLastPosDist2 
						&& selfDist > expectedSelfDist) {
						currentState = "Suspect";
					}
				} else if (prevState.compareTo("Flee") == 0) {
					if (playerLastPosDist1 < playerLastPosDist2 
						&& selfDist >= expectedSelfDist) {
						currentState = "Flee";
					} else {
						currentState = "LookAround";
					}
				}

				if (stateStartEvidence.getBlockSound().compareTo("None") != 0
						|| stateStartEvidence.getLightingChange().compareTo(
								"None") != 0) {
					if (playerLastPosDist1 < playerLastPosDist2) {
						currentState = "Suspect";
					} else {
						currentState = "Trick";
					}
				} else {
					currentState = "Trick";
				}
			}
		}
	}
}
