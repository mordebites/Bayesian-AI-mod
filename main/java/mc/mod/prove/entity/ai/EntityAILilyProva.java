package mc.mod.prove.entity.ai;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import mc.mod.prove.entity.BlockEvent;
import mc.mod.prove.entity.transfer.EvidenceTO;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAILilyProva extends EntityAILilyCentral {
	private EvidenceTO stateStartEvidence;
	private String prevState = "LookAround";
	private String currentState = "LookAround";
	private LinkedList<PlayerInfo> info = new LinkedList<PlayerInfo>();

	public EntityAILilyProva(EntityCreature entity, EntityPlayer player) {
		super(entity, player);
		stateStartEvidence = new EvidenceTO("None", "Normal", "None", "None",
				"None", "Uncertain");
	}

	@Override
	protected void beforeExecuting() {
		super.beforeExecuting();
		PlayerInfo currentInfo = new PlayerInfo(tickTimer,
				lily.getPositionVector(), lily.isSneaking(), lily.isSprinting());
		info.addLast(currentInfo);

		// se sono cambiate le variabili esterne
		if (currentEvidence.compareTo(stateStartEvidence) != 0) {
			this.determineState();
			// segna il log con prevState che è state_T alla prima occorrenza
			// e poi state_T diventa currentState
			info = new LinkedList<PlayerInfo>();
			prevState = currentState;
			stateStartEvidence = currentEvidence;

			System.out.println(prevState + " " + currentEvidence.toString()
					+ " " + currentState);
		}
		
		System.out.println("MotionX: " + lily.motionX + " MotionZ: " + lily.motionZ);

	}

	private void determineState() {

		currentState = prevState;
		double playerDist1, playerDist2, selfDist;
		BlockPos lastPos = super.getLastPlayerPosition();
		Vec3d playerLastVec = new Vec3d(lastPos.getX(), lastPos.getY(), lastPos.getZ());
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
						playerDist1 = info.getFirst().playerPos
								.distanceTo(vec);
						playerDist2 = info.getLast().playerPos
								.distanceTo(vec);

						if (playerDist2 > playerDist1 && playerDist2 < 5) {
							currentState = "Trick";
							found = true;
						}
					}
				}
			
			}
		}

	}
}
