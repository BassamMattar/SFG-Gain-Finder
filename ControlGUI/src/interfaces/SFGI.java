package interfaces;

import java.util.ArrayList;

public interface SFGI {

	public void connectXToY(int x, int y, float gain);

	public int size();

	public void testConnectivity();

	public ArrayList<ArrayList<Integer>> getAllForwardPaths();

	public ArrayList<ArrayList<Integer>> getAllLoops ();

    public ArrayList<ArrayList<Integer>>[] getAllNoneTouchingLoops(ArrayList<ArrayList<Integer>> allNoneRepeatedLoops);
  
    public float getForwardPathGain(int pathID);

    public float getLoopGain(int loopID);
    
    public String getResult();

    public float getDeltaGain (ArrayList<ArrayList<Integer>> nonRepeatedLoops, ArrayList<ArrayList<Integer>>[] nonTouchingLoops);

    public float getDeltaForGivenForwardPath(ArrayList<Integer> forwardPath, ArrayList<ArrayList<Integer>> nonRepeatedLoops, ArrayList<ArrayList<Integer>>[] nonTouchingLoops);
    
    public float getOverAllGain(ArrayList<ArrayList<Integer>> forwardPaths, ArrayList<ArrayList<Integer>> allNoneRepeatedLoops, ArrayList<ArrayList<Integer>>[] nonTouchingLoops);
}
