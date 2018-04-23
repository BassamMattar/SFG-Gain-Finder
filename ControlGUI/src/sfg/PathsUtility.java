package sfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PathsUtility {
	private SFG sfg;
	private ArrayList<ArrayList<Integer>> forwardPaths;
	private ArrayList<ArrayList<Integer>> loopsOfCertainNode;
	private ArrayList<Float> forwardPathsGain;
	private ArrayList<Float> loopsGain;
	private final float INITIAL_GAIN = 1;

	public PathsUtility(SFG sfg) {
		this.sfg = sfg;
	}

	public ArrayList<ArrayList<Integer>> getAllForwardPaths() {
		this.forwardPathsGain = new ArrayList<Float>();
		forwardPaths = new ArrayList<ArrayList<Integer>>();
		boolean[] isVisited = new boolean[this.sfg.size()];
		ArrayList<Integer> pathList = new ArrayList<Integer>();
		pathList.add(sfg.getSOURCE_NOOD());
		getAllPaths(sfg.getSOURCE_NOOD(), sfg.getSINK_NOOD(), isVisited, pathList, INITIAL_GAIN);
		return forwardPaths;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Integer>>[] getAllLoops() {
		loopsGain = new ArrayList<Float>();
		ArrayList<ArrayList<Integer>>[] loops;
		loops = new ArrayList[sfg.size()];
		for (int i = 0; i < sfg.size(); i++) {
			loopsOfCertainNode = new ArrayList<ArrayList<Integer>>();
			loops[i] = getAllLoopsForCertainNode(i, INITIAL_GAIN);
		}
		return loops;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Integer>> removeDuplicatedLoops(ArrayList<ArrayList<Integer>>[] allLoops) {
		ArrayList<ArrayList<Integer>> nonRepeatedLoops = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> loopsOfCertainNode = allLoops[0];
		HashMap<String, String> hashMap = new HashMap<String, String>();
		for (int i = 0; i < loopsOfCertainNode.size(); i++) {
			ArrayList<Integer> loopToBeSorted = (ArrayList<Integer>) loopsOfCertainNode.get(i).clone();
			Collections.sort(loopToBeSorted);
			hashMap.put(loopToBeSorted.toString(), null);
			nonRepeatedLoops.add(loopsOfCertainNode.get(i));
		}
		int offset = allLoops[0].size();
		int numOfRemovedItems = 0;
		for (int i = 1; i < allLoops.length; i++) {
			loopsOfCertainNode = allLoops[i];
			for (int j = 0; j < loopsOfCertainNode.size(); j++) {
				ArrayList<Integer> loopToBeSorted = (ArrayList<Integer>) loopsOfCertainNode.get(j).clone();
				Collections.sort(loopToBeSorted);
				if (!hashMap.containsKey(loopToBeSorted.toString())) {
					hashMap.put(loopToBeSorted.toString(), null);
					nonRepeatedLoops.add(loopsOfCertainNode.get(j));
				} else {
					this.loopsGain.remove(offset + j - numOfRemovedItems);
					numOfRemovedItems++;
				}
			}
			offset += allLoops[i].size();
		}
		return nonRepeatedLoops;
	}

	public ArrayList<Float> getLoopsGain() {
		return loopsGain;
	}

	public ArrayList<ArrayList<Integer>>[] getAllNoneTouchingLoops(ArrayList<ArrayList<Integer>> allNoneRepeatedLoops) {
		CombinationsUtility combinationUtility = new CombinationsUtility();
		ArrayList<ArrayList<Integer>>[] allCombinations;
		allCombinations = combinationUtility.getAllCombinationsOfN(allNoneRepeatedLoops.size());
		for (int i = 0; i < allCombinations.length; i++) {
			ArrayList<ArrayList<Integer>> groupOfCombinations = allCombinations[i];
			boolean allCurrentCombinationsAreTouching = true;
			for (int j = 0; j < groupOfCombinations.size(); j++) {
				ArrayList<Integer> combination = groupOfCombinations.get(j);
				boolean touching = isTouching(combination, allNoneRepeatedLoops);
				allCurrentCombinationsAreTouching = allCurrentCombinationsAreTouching && touching;
				if (touching) {
					groupOfCombinations.remove(j);
					j--;
					// System.out.println("in");
				}
			}
			if (allCurrentCombinationsAreTouching) {
				for (int j = i + 1; j < allCombinations.length; j++) {
					allCombinations[j].clear();
				}
				break;
			}
		}
		// After removing touching combinations.
		return allCombinations;
	}

	@SuppressWarnings("unchecked")
	private boolean isTouching(ArrayList<Integer> combination, ArrayList<ArrayList<Integer>> allNoneRepeatedLoops) {
		boolean touching = false;
		HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
		// System.out.println(combination + " <=============");
		for (int k = 0; k < combination.size() && !touching; k++) {
			ArrayList<Integer> oneLoopOfTheCombinationToBeChecked = (ArrayList<Integer>) allNoneRepeatedLoops
					.get(combination.get(k)).clone();
			// System.out.println(oneLoopOfTheCombinationToBeChecked + " <==");

			for (int i = 0; i < oneLoopOfTheCombinationToBeChecked.size(); i++) {
				if (!hashMap.containsKey(oneLoopOfTheCombinationToBeChecked.get(i))) {
					hashMap.put(oneLoopOfTheCombinationToBeChecked.get(i), null);
				} else {
					touching = true;
				}
			}
		}
		// System.out.println(touching);
		return touching;
	}

	private ArrayList<ArrayList<Integer>> getAllLoopsForCertainNode(int nodeId, float loopGain) {
		boolean[] isVisited = new boolean[this.sfg.size()];
		ArrayList<Integer> pathList = new ArrayList<Integer>();
		pathList.add(nodeId);
		getLoopsOfNode(nodeId, nodeId, isVisited, pathList, INITIAL_GAIN);
		return loopsOfCertainNode;
	}

	@SuppressWarnings("unchecked")
	private void getAllPaths(Integer source, Integer sink, boolean[] isVisited, ArrayList<Integer> localPathList, float pathGain) {
		isVisited[source] = true;
		if (source.equals(sink)) {
			this.forwardPaths.add((ArrayList<Integer>) localPathList.clone());
			this.forwardPathsGain.add(pathGain);
		}
		ArrayList<Node> currentNoodSuccessors = sfg.getAdjacencyListOf(source);
		for (Node nextNode : currentNoodSuccessors) {
			if (!isVisited[nextNode.getNextNodeId()]) {
				localPathList.add(nextNode.getNextNodeId());
				float nextPathGain = pathGain * nextNode.getNextNodeGain();
				getAllPaths(nextNode.getNextNodeId(), sink, isVisited, localPathList, nextPathGain);
				localPathList.remove(localPathList.size() - 1);
			}
		}
		isVisited[source] = false;
	}

	public ArrayList<Float> getForwardPathsGain() {
		return forwardPathsGain;
	}

	@SuppressWarnings("unchecked")
	private void getLoopsOfNode(Integer source, Integer sink, boolean[] isVisited, ArrayList<Integer> localPathList, float loopGain) {
		isVisited[source] = true;
		ArrayList<Node> currentNoodSuccessors = sfg.getAdjacencyListOf(source);
		for (Node nextNode : currentNoodSuccessors) {
			if (!isVisited[nextNode.getNextNodeId()]) {

				localPathList.add(nextNode.getNextNodeId());
				float nextLoopGain = loopGain * nextNode.getNextNodeGain();
				getLoopsOfNode(nextNode.getNextNodeId(), sink, isVisited, localPathList, nextLoopGain);
				localPathList.remove(localPathList.size() - 1);

			} else if (sink.equals(nextNode.getNextNodeId())) {
				this.loopsOfCertainNode.add((ArrayList<Integer>) localPathList.clone());
				this.loopsGain.add(loopGain * nextNode.getNextNodeGain());
			}
		}
		isVisited[source] = false;
	}

	public boolean isTouchingTheForwardPath(ArrayList<Integer> forwardPath, ArrayList<Integer> pathToCompareWith) {
		boolean touching = false;
		HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
		for (Integer nodeId : forwardPath) {
			hashMap.put(nodeId, null);
		}
		for (Integer nodeId : pathToCompareWith) {
			if (hashMap.containsKey(nodeId)) {
				touching = true;
				break;
			}
		}
		return touching;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Integer>> removeLoopsTouchingTheForwardPath(ArrayList<Integer> forwardPath,
			ArrayList<ArrayList<Integer>> nonRepeatedLoops) {
		ArrayList<ArrayList<Integer>> nonRepeatedLoopsWithoutTouchingForwardPath = (ArrayList<ArrayList<Integer>>) nonRepeatedLoops
				.clone();

		for (int i = 0; i < nonRepeatedLoopsWithoutTouchingForwardPath.size(); i++) {
			if (isTouchingTheForwardPath(forwardPath, nonRepeatedLoopsWithoutTouchingForwardPath.get(i))) {
				nonRepeatedLoopsWithoutTouchingForwardPath.remove(i);
				i--;
			}
		}

		return nonRepeatedLoopsWithoutTouchingForwardPath;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Integer>>[] removeMultipleLoopsTouchingTheForwardPath(ArrayList<Integer> forwardPath,
			ArrayList<ArrayList<Integer>> nonRepeatedLoops, ArrayList<ArrayList<Integer>>[] nonTouchingLoops) {
		ArrayList<ArrayList<Integer>>[] nonTouchingLoopsWithoutTouchingForwardPath = new ArrayList[nonTouchingLoops.length];

		for (int i = 0; i < nonTouchingLoopsWithoutTouchingForwardPath.length; i++) {
			nonTouchingLoopsWithoutTouchingForwardPath[i] = (ArrayList<ArrayList<Integer>>) nonTouchingLoops[i].clone();
			for (int j = 0; j < nonTouchingLoopsWithoutTouchingForwardPath[i].size(); j++) {
				nonTouchingLoopsWithoutTouchingForwardPath[i].set(j, (ArrayList<Integer>) nonTouchingLoops[i].get(j).clone());
			}
		}

		for (int i = 0; i < nonTouchingLoopsWithoutTouchingForwardPath.length; i++) {
			for (int j = 0; j < nonTouchingLoopsWithoutTouchingForwardPath[i].size(); j++) {
				ArrayList<Integer> combination = nonTouchingLoopsWithoutTouchingForwardPath[i].get(j);
				for (int k = 0; k < combination.size(); k++) {
					if (isTouchingTheForwardPath(forwardPath, nonRepeatedLoops.get(combination.get(k)))) {
						nonTouchingLoopsWithoutTouchingForwardPath[i].get(j).remove(k);
						k--;
					}
				}
				if (combination.size() < i + 2) {
					nonTouchingLoopsWithoutTouchingForwardPath[i].remove(j);
					j--;
					if (combination.size() > 1) {
						nonTouchingLoopsWithoutTouchingForwardPath[combination.size() - 2].add(combination);
					}
				}
			}
		}
		return nonTouchingLoopsWithoutTouchingForwardPath;
	}

}
