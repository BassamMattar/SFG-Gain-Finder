package sfg;

import java.util.ArrayList;

import interfaces.SFGI;

public class Main {

	public static void main(String[] args) {
        SFGI g = new SFG(10);
        g.connectXToY(0,1, 1);
        g.connectXToY(1,2, 1);
        g.connectXToY(2,3, 1);
        g.connectXToY(3,4, 1);
        g.connectXToY(4,5, 1);
        g.connectXToY(5,6, 1);
        g.connectXToY(6,7, 1);
        g.connectXToY(7,8, 1);
        g.connectXToY(8,9,1);

//        g.connectXToY(3,6, 1);
//        g.connectXToY(5,7, 1);
//        g.connectXToY(3,5, 1);
        g.connectXToY(6,5, -1);
        g.connectXToY(8,7, -1);
        g.connectXToY(4,3, -1);
        g.connectXToY(2,1, -1);




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
