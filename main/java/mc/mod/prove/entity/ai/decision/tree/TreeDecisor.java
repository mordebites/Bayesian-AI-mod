package mc.mod.prove.entity.ai.decision.tree;

import java.util.HashMap;
import java.util.Random;

import mc.mod.prove.entity.ai.decision.IDecisor;
import mc.mod.prove.entity.ai.enumerations.EntityDistance;
import mc.mod.prove.entity.ai.enumerations.TimerLeft;
import mc.mod.prove.entity.transfer.EvidenceTO;

public class TreeDecisor implements IDecisor {
	private Node root;
	
	public TreeDecisor() {
		SingleChoiceNode hunt, flee, suspect, lookAround, trick;
		hunt = new SingleChoiceNode("Hunt");
		flee = new SingleChoiceNode("Flee");
		suspect = new SingleChoiceNode("Suspect");
		lookAround = new SingleChoiceNode("LookAround");
		trick = new SingleChoiceNode("Trick");

		Node[] look_trick = { lookAround, trick };
		Node[] look_trick_flee = { lookAround, trick, flee };
		Node[] suspect_look = { suspect, lookAround };
		MultipleChoiceNode lookTrick = new MultipleChoiceNode(look_trick);
		MultipleChoiceNode lookTrickFlee = new MultipleChoiceNode(
				look_trick_flee);
		MultipleChoiceNode suspectLook = new MultipleChoiceNode(suspect_look);

		Node[] rdm1 = {lookAround, lookAround, lookAround, lookTrick, lookTrick};
		Node[] rdm2 = {lookAround, lookAround, lookAround, lookTrickFlee, lookTrickFlee};
		Node[] rdm3 = {suspectLook, lookAround, lookAround, lookAround, lookAround};
		StateNode stateLT = new StateNode(rdm1);
		StateNode stateLTF = new StateNode(rdm2);
		StateNode stateSL = new StateNode(rdm3);

		Node[] states_LT_LTF = { stateLT, stateLTF };
		Node[] hunt_flee = { hunt, flee };
		Node[] suspect_flee = { suspect, flee };
		Node[] suspect_look_flee = { stateSL, flee };
		TimerNode time1 = new TimerNode(states_LT_LTF); // 4 occorrenze
		TimerNode time2 = new TimerNode(hunt_flee); // 2 occorrenze
		TimerNode time3 = new TimerNode(suspect_flee); // 1 occorrenza
		TimerNode time4 = new TimerNode(suspect_look_flee); //1 occorrenza

		Node[] arrBlock1 = {stateLT, time1, time4};
		Node[] arrBlock2 = {stateSL, stateLT, time2};
		Node[] arrBlock3 = {stateLT, time1, time1};
		DistanceNode block1 = new DistanceNode(arrBlock1, DistanceNode.SOUND_BLOCK);
		DistanceNode block2 = new DistanceNode(arrBlock2, DistanceNode.SOUND_BLOCK);
		DistanceNode block3 = new DistanceNode(arrBlock3, DistanceNode.SOUND_BLOCK);
		
		Node[] arrLight1 = {block1, block2, block1};
		Node[] arrLight2 = {block3, time1, time1};
		DistanceNode light1 = new DistanceNode(arrLight1, DistanceNode.LIGHTING_CHANGE);
		DistanceNode light2 = new DistanceNode(arrLight2, DistanceNode.LIGHTING_CHANGE);
		
		Node[] arrStep = {light2, light1, time3};
		DistanceNode step = new DistanceNode(arrStep, DistanceNode.STEP_SOUND);
		
		Node[] arrRoot = {step, time2, time2};
		root = new DistanceNode(arrRoot, DistanceNode.PLAYER_IN_SIGHT);
	}

	public String getDecision(EvidenceTO evidence) {
		return root.getDecision(evidence);
	}
	
	abstract class Node {
		boolean isLeaf;

		abstract String getDecision(EvidenceTO evidence);
	}

	class TimerNode extends Node {
		HashMap<String, Node> children = new HashMap<String, Node>();

		TimerNode(Node[] pChildren) {
			if (pChildren.length != 2) {
				throw new RuntimeException();
			}
			super.isLeaf = false;
			children.put(TimerLeft.Normal.name(), pChildren[0]);
			children.put(TimerLeft.RunningOut.name(), pChildren[1]);
		}

		@Override
		String getDecision(EvidenceTO evidence) {
			return children.get(evidence.getTimer()).getDecision(evidence);
		}
	}

	class DistanceNode extends Node {
		HashMap<String, Node> children = new HashMap<String, Node>();
		int type = -1;
		static final int PLAYER_IN_SIGHT = 0;
		static final int STEP_SOUND = 1;
		static final int SOUND_BLOCK = 2;
		static final int LIGHTING_CHANGE = 3;

		DistanceNode(Node[] pChildren, int pType) {
			if (pChildren.length != EntityDistance.values().length) {
				throw new RuntimeException();
			}
			super.isLeaf = false;
			type = pType;
			children.put(EntityDistance.None.name(), pChildren[0]);
			children.put(EntityDistance.Far.name(), pChildren[1]);
			children.put(EntityDistance.Close.name(), pChildren[2]);
		}

		@Override
		String getDecision(EvidenceTO evidence) {
			String value = null;
			switch (type) {
			case PLAYER_IN_SIGHT: {
				value = evidence.getPlayerInSight();
				break;
			}
			case STEP_SOUND: {
				value = evidence.getStepSound();
				break;
			}
			case SOUND_BLOCK: {
				value = evidence.getBlockSound();
				break;
			}
			case LIGHTING_CHANGE: {
				value = evidence.getBlockSound();
				break;
			}
			}
			return children.get(value).getDecision(evidence);
		}
	}

	class StateNode extends Node {
		HashMap<String, Node> children = new HashMap<String, Node>();

		StateNode(Node[] pChildren) {
			if (pChildren.length != 5) {
				throw new RuntimeException();
			}
			super.isLeaf = false;
			children.put("Hunt", pChildren[0]);
			children.put("Flee", pChildren[1]);
			children.put("Suspect", pChildren[2]);
			children.put("Trick", pChildren[3]);
			children.put("LookAround", pChildren[4]);
		}

		@Override
		String getDecision(EvidenceTO evidence) {
			return children.get(evidence.getStateT()).getDecision(evidence);
		}
	}

	class SingleChoiceNode extends Node {
		String choice;

		SingleChoiceNode(String choice) {
			if (choice.compareTo("Hunt") != 0 
				&& choice.compareTo("Flee") != 0
				&& choice.compareTo("Suspect") != 0
				&& choice.compareTo("Trick") != 0
				&& choice.compareTo("LookAround") != 0) {
				throw new RuntimeException();
			}

			this.choice = choice;
			isLeaf = true;
		}

		@Override
		String getDecision(EvidenceTO evidence) {
			return choice;
		}
	}

	class MultipleChoiceNode extends Node {
		Node[] choices;

		MultipleChoiceNode(Node[] choices) {
			for (int i = 0; i < choices.length; i++) {
				if (!(choices[i] instanceof SingleChoiceNode)) {
					throw new RuntimeException();
				}
			}
			this.choices = choices;
			isLeaf = true;
		}

		@Override
		String getDecision(EvidenceTO evidence) {
			Random rdm = new Random();
			return choices[rdm.nextInt(choices.length)].getDecision(evidence);
		}
	}
}