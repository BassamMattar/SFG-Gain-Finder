package sfg;

import java.util.ArrayList;

import interfaces.SFGI;

public class Main {

	public static void main(String[] args) {
        SFGI g = new SFG(2);
        g.connectXToY(0,1, 2);
        g.connectXToY(0,1, 5);
//        g.connectXToY(2,3 ,3);
//        g.connectXToY(3,4 ,4);
//        g.connectXToY(4,5 ,5);
//        g.connectXToY(3,3 ,6);
//        g.connectXToY(4,1 ,7);
//        g.connectXToY(2,1 ,8);
//        g.connectXToY(2,4 ,9);
//        g.connectXToY(3,1 ,10);

        //g.testConnectivity();

        System.out.println(g.getResult());
	}

}
