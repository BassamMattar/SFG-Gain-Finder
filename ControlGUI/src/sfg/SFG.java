package sfg;

import java.util.ArrayList;

import interfaces.SFGI;

public class SFG implements SFGI {
	private DirectedGraph sfg;
	private final int SOURCE_NOOD;
	private final int SINK_NOOD;
	private PathsUtility pathUtility;

	public SFG(int numOfNodes) {
		sfg = new DirectedGraph(numOfNodes);
		pathUtility = new PathsUtility(this);
		this.SOURCE_NOOD = 0;
		this.SINK_NOOD = numOfNodes - 1;
	}

	public int getSOURCE_NOOD() {
		return SOURCE_NOOD;
	}

	public int getSINK_NOOD() {
		return SINK_NOOD;
	}

	@Override
	public void connectXToY(int x, int y, float gain) {
		sfg.connectXToY(x, y, gain);
	}

	@Override
	public int size() {
		return sfg.size();
	}

	@Override
	public void testConnectivity() {
		System.out.println(this.sfg.toString());
	}

	public ArrayList<Node> getAdjacencyListOf(int nodeId) {
		return sfg.getAdjacencyListOf(nodeId);
	}

	@Override
	public ArrayList<ArrayList<Integer>> getAllForwardPaths() {
		return pathUtility.getAllForwardPaths();
	}

	@Override
	public ArrayList<ArrayList<Integer>> getAllLoops() {
		return removeDuplicatedLoops(pathUtility.getAllLoops());
	}

	private ArrayList<ArrayList<Integer>> removeDuplicatedLoops(ArrayList<ArrayList<Integer>>[] allLoops) {
		return pathUtility.removeDuplicatedLoops(allLoops);
	}

	@Override
	public ArrayList<ArrayList<Integer>>[] getAllNoneTouchingLoops(ArrayList<ArrayList<Integer>> allNoneRepeatedLoops) {
		return pathUtility.getAllNoneTouchingLoops(allNoneRepeatedLoops);
	}

	@Override
	public float getForwardPathGain(int pathID) {
		return pathUtility.getForwardPathsGain().get(pathID);
	}

	@Override
	public float getLoopGain(int loopID) {
		return pathUtility.getLoopsGain().get(loopID);
	}

	@Override
	public float getDeltaGain(ArrayList<ArrayList<Integer>> nonRepeatedLoops,
			ArrayList<ArrayList<Integer>>[] nonTouchingLoops) {
		float deltaGain = 1;
		float sign = -1;
		float loopGainSum = 0;
		for (int i = 0; i < nonRepeatedLoops.size(); i++) {
			loopGainSum += this.getLoopGain(i);
		}
		deltaGain += loopGainSum * sign;

		for (int i = 0; i < nonTouchingLoops.length; i++) {
			ArrayList<ArrayList<Integer>> multipleLoopsCombination = nonTouchingLoops[i];
			float multipleLoopsCombinationGain = 0;
			sign = sign * -1;
			for (int j = 0; j < multipleLoopsCombination.size(); j++) {
				ArrayList<Integer> multileLoops = multipleLoopsCombination.get(j);
				float multipleLoopsGain = 1;
				for (Integer loopId : multileLoops) {
					multipleLoopsGain *= this.getLoopGain(loopId);
				}
				multipleLoopsCombinationGain += multipleLoopsGain;
			}
			deltaGain += multipleLoopsCombinationGain * sign;
		}
		return deltaGain;
	}

	@Override
	public float getDeltaForGivenForwardPath(ArrayList<Integer> forwardPath,
			ArrayList<ArrayList<Integer>> nonRepeatedLoops, ArrayList<ArrayList<Integer>>[] nonTouchingLoops) {
		ArrayList<ArrayList<Integer>> nonRepeatedLoopsNonTouchingForwardPath = pathUtility
				.removeLoopsTouchingTheForwardPath(forwardPath, nonRepeatedLoops);
		ArrayList<ArrayList<Integer>>[] nonTouchingLoopsNonTouchingForwardPath = pathUtility
				.removeMultipleLoopsTouchingTheForwardPath(forwardPath, nonRepeatedLoops, nonTouchingLoops);
		return this.getDeltaGain(nonRepeatedLoopsNonTouchingForwardPath, nonTouchingLoopsNonTouchingForwardPath);
	}

//	private float getGainOfNextNodeById(int nodeId, int nextNodeId) {
//		float gain = 0;
//		ArrayList<Node> adjacencyListOfGivenNood = sfg.getAdjacencyListOf(nodeId);
//		for (Node node : adjacencyListOfGivenNood) {
//			if (node.getNextNodeId() == nextNodeId) {
//				gain = node.getNextNodeGain();
//				break;
//			}
//		}
//		return gain;
//	}

	@Override
	public float getOverAllGain(ArrayList<ArrayList<Integer>> forwardPaths,
			ArrayList<ArrayList<Integer>> nonRepeatedLoops, ArrayList<ArrayList<Integer>>[] nonTouchingLoops) {
		float numeratorOfMansonFormula = 0;
		int pathID = 0;
		for (ArrayList<Integer> forwardPath : forwardPaths) {
			numeratorOfMansonFormula += this.getForwardPathGain(pathID)
					* this.getDeltaForGivenForwardPath(forwardPath, nonRepeatedLoops, nonTouchingLoops);
		pathID++;
		}
		return numeratorOfMansonFormula / this.getDeltaGain(nonRepeatedLoops, nonTouchingLoops);
	}

	@Override
	public String getResult() {
		StringBuilder result = new StringBuilder();
		ArrayList<ArrayList<Integer>> fp = this.getAllForwardPaths();
		ArrayList<ArrayList<Integer>> nonRL = this.getAllLoops();
		ArrayList<ArrayList<Integer>>[] com = this.getAllNoneTouchingLoops(nonRL);
		float OverallGain = this.getOverAllGain(fp, nonRL, com);
		float delta = this.getDeltaGain(nonRL, com);
		float[] forwardPathsDeltas = new float[fp.size()];
		for (int i = 0; i < forwardPathsDeltas.length; i++) {
			forwardPathsDeltas[i] = this.getDeltaForGivenForwardPath(fp.get(i), nonRL, com);
		}

		result.append("Forward paths:\n");
		for (int i = 0; i < fp.size(); i++) {
			result.append(fp.get(i) + " gain: ");
			result.append(this.getForwardPathGain(i) + "\n");
		}

		result.append("\nLoops:\n");
		for (int i = 0; i < nonRL.size(); i++) {
			float loopGain = this.getLoopGain(i);
			nonRL.get(i).add(nonRL.get(i).get(0));
			result.append(nonRL.get(i) + " gain: " + loopGain + "\n");
		}

		result.append("\nAll non touching combinations:\n");
		for (int i = 0; i < com.length; i++) {
			ArrayList<ArrayList<Integer>> c = com[i];
			result.append("\nGroups of " + (i + 2) + " non touching loops:\n");
			for (int j = 0; j < c.size(); j++) {
				result.append(c.get(j));
			}
		}

		result.append("\n\nDelta: \n");
		result.append(delta);

		result.append("\n\nDelta's gains\n");
		for (int i = 0; i < forwardPathsDeltas.length; i++) {
			result.append("Delta (" + (i + 1) + ") gain : " + forwardPathsDeltas[i] + "\n");
		}

		result.append("\nOverallGain: \n");
		result.append(OverallGain);

		return result.toString();
		
	}
}
