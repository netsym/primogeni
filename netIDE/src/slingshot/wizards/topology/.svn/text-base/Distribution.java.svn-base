package slingshot.wizards.topology;

/*
 * Copyright (c) 2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 *
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * non-infringement.  In no event shall Florida International
 * University be liable for any claim, damages or other liability,
 * whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other
 * dealings in the software.
 *
 * This software is developed and maintained by
 *
 *   Modeling and Networking Systems Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, Florida 33199, USA
 *
 * You can find our research at http://www.primessf.net/.
 */

import java.util.Random;
import java.lang.Math;

/**
 * @author Hao Jiang
 */
final class Distribution 
{ 
    public static float getUniformRandom(Random r) 
    {
    	return r.nextFloat();
    }

    public static float getParetoRandom (Random r, double K, double P, double ALPHA) 
    {	
		float x = r.nextFloat();
		double    den =Math.pow(1.0-x+x*Math.pow(K/P, ALPHA), 1/ALPHA); 
		while (den==0)
		    den =Math.pow(1.0-x+x*Math.pow(K/P, ALPHA), 1/ALPHA); 
		
		return (float) (K/den);	
    }

    public static float getParetoRandom (Random r, double scale, double shape) 
    {
		float x = r.nextFloat();
		double    den =Math.pow(1.0-x+x*Math.pow(1.0/scale, shape), 1/shape); 
		while (den==0)
		    den =Math.pow(1.0-x+x*Math.pow(1.0/scale, shape), 1/shape); 
		
		return (float) (1/den);	
    }
    
    /*returns a rand between low (exclusive) and high (inclusive)*/
    public static int getUniformRandom(Random r, int low, int high) 
    {
    	if (low == high)
    		return low;
		int n=0;
		while (n == 0) 
		    n = r.nextInt(high); /*this gives me a number between 0 and high, inclusive*/
		return n+low; /*shift*/
    }

    public static float getExponentialRandom(Random r, float lambda) 
    {
		float u = getUniformRandom(r);
		return ( -1* (float) Math.log(u) / lambda);
    }
}

