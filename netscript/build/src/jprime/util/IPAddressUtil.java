package jprime.util;


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
import java.math.BigInteger;
import java.util.Comparator;

/**
 * @author Nathanael Van Vorst
 */
public class IPAddressUtil {
	public static final double log2=Math.log(2);
	
	public static long cidrs[] = {
		(new BigInteger("11111111111111111111111111111111",2)).longValue(),//0
		(new BigInteger("11111111111111111111111111111110",2)).longValue(),//1
		(new BigInteger("11111111111111111111111111111100",2)).longValue(),//2
		(new BigInteger("11111111111111111111111111111000",2)).longValue(),//3
		(new BigInteger("11111111111111111111111111110000",2)).longValue(),//4
		(new BigInteger("11111111111111111111111111100000",2)).longValue(),//5
		(new BigInteger("11111111111111111111111111000000",2)).longValue(),//6
		(new BigInteger("11111111111111111111111110000000",2)).longValue(),//7
		(new BigInteger("11111111111111111111111100000000",2)).longValue(),//8
		(new BigInteger("11111111111111111111111000000000",2)).longValue(),//9
		(new BigInteger("11111111111111111111110000000000",2)).longValue(),//10
		(new BigInteger("11111111111111111111100000000000",2)).longValue(),//11
		(new BigInteger("11111111111111111111000000000000",2)).longValue(),//12
		(new BigInteger("11111111111111111110000000000000",2)).longValue(),//13
		(new BigInteger("11111111111111111100000000000000",2)).longValue(),//14
		(new BigInteger("11111111111111111000000000000000",2)).longValue(),//15
		(new BigInteger("11111111111111110000000000000000",2)).longValue(),//16
		(new BigInteger("11111111111111100000000000000000",2)).longValue(),//17
		(new BigInteger("11111111111111000000000000000000",2)).longValue(),//18
		(new BigInteger("11111111111110000000000000000000",2)).longValue(),//19
		(new BigInteger("11111111111100000000000000000000",2)).longValue(),//20
		(new BigInteger("11111111111000000000000000000000",2)).longValue(),//21
		(new BigInteger("11111111110000000000000000000000",2)).longValue(),//22
		(new BigInteger("11111111100000000000000000000000",2)).longValue(),//23
		(new BigInteger("11111111000000000000000000000000",2)).longValue(),//24
		(new BigInteger("11111110000000000000000000000000",2)).longValue(),//25
		(new BigInteger("11111100000000000000000000000000",2)).longValue(),//26
		(new BigInteger("11111000000000000000000000000000",2)).longValue(),//27
		(new BigInteger("11110000000000000000000000000000",2)).longValue(),//28
		(new BigInteger("11100000000000000000000000000000",2)).longValue(),//29
		(new BigInteger("11000000000000000000000000000000",2)).longValue(),//30
		(new BigInteger("10000000000000000000000000000000",2)).longValue(),//31
		(new BigInteger("00000000000000000000000000000000",2)).longValue(),//32
	};
	
	public static class ReverseComparator implements Comparator<Long> {
		public int compare(Long a, Long b) {
			if(a==b) return 0;
			if(a<b) return 1;
			return -1;
		}
	}

	/**
	 * @param i
	 * @return
	 */
	public static String int2IP(long ip) {
	      StringBuffer sb = new StringBuffer( 15 );
	      for ( int shift=24; shift >0; shift-=8 )
	         {
	         // process 3 bytes, from high order byte down.
	         sb.append( Long.toString( (ip >>> shift) & 0xff ));
	         sb.append('.');
	         }
	      sb.append( Long.toString( ip & 0xff ) );
	      return sb.toString();
	}
	
	/**
	 * @param addr
	 * @return
	 */
	public static long ip2Int(String addr) {
		String[] addrArray = addr.split("\\.");
		long num = 0;
		for (int i=0;i<addrArray.length;i++) {
			num += ((Integer.parseInt(addrArray[i])%256 * Math.pow(256,3-i)));
		}
		return num;
	}
	/**
	 * @param addr
	 * @return
	 */
	public static long cidr2Int(String addr) {
		String[] addrArray = addr.split("\\.");
		if(addrArray.length==4) {
			String[] t = addrArray[3].split("/");
			if(t.length==2) {
				addrArray[3]=t[1];
				long num = 0;
				for (int i=0;i<addrArray.length;i++) {
					num += ((Integer.parseInt(addrArray[i])%256 * Math.pow(256,3-i)));
				}
				int c = Integer.parseInt(t[1]);
				if(c >=0 && c<=32) {
					return num & cidrs[32-c];
				}
			}
		}
		throw new RuntimeException("Invalid cidr address "+addr);
	}
	
	/**
	 * @param addr
	 * @return
	 */
	public static String cidr2Subnet(String addr) {
		String[] addrArray = addr.split("\\.");
		if(addrArray.length==4) {
			String[] t = addrArray[3].split("/");
			if(t.length==2) {
				int c = Integer.parseInt(t[1]);
				if(c >=0 && c<=32) {
					return int2IP(cidrs[32-c]);
				}
			}
		}
		throw new RuntimeException("Invalid cidr address "+addr);
	}
	
	public static boolean contains(long net, int prefix_len, long ip) {
		/*System.out.println("\t\t"+IPAddressUtil.int2IP(net)+"/"+prefix_len+" ==? "+IPAddressUtil.int2IP(ip));
		long ack = IPAddressUtil.ip2Int(IPAddressUtil.int2IP(cidrs[32-prefix_len]));
		System.out.println("\t\t\tprefix_len: "+prefix_len);
		System.out.println("\t\t\t\t"+IPAddressUtil.int2IP(ack));
		System.out.println("\t\t\t\t"+Long.toBinaryString(ack));
		System.out.println("\t\t\tIP:");
		System.out.println("\t\t\t\t"+IPAddressUtil.int2IP(ip));
		System.out.println("\t\t\t\t"+Long.toBinaryString(ip));
		System.out.println("\t\t\t\t"+Long.toBinaryString(ip&cidrs[32-prefix_len]));
		System.out.println("\t\t\tnet:");
		System.out.println("\t\t\t\t"+IPAddressUtil.int2IP(net));
		System.out.println("\t\t\t\t"+Long.toBinaryString(net));
		System.out.println("\t\t\t\t"+Long.toBinaryString(net&cidrs[32-prefix_len]));
		System.out.println("\t\t\tcmp:");
		System.out.println("\t\t\t\t"+Long.toBinaryString(ack));
		System.out.println("\t\t\t\t"+Long.toBinaryString(ip&cidrs[32-prefix_len]));
		System.out.println("\t\t\t\t"+Long.toBinaryString(net&cidrs[32-prefix_len]));*/
		
		return (ip&cidrs[32-prefix_len]) == (net&cidrs[32-prefix_len]);
	}
	
	public static void main(String[] args) {
		long[] nets = {ip2Int("192.1.1.1"),ip2Int("192.1.1.0"),ip2Int("192.1.0.0"),ip2Int("192.0.0.0")};
		int[] ps = {27,24,16,8};
		long[] ips = {ip2Int("192.1.1.3"),ip2Int("192.1.3.4"),ip2Int("192.2.3.4"),ip2Int("10.1.1.1")};
		for(int i = 0;i<nets.length;i++) {
			System.out.println(IPAddressUtil.int2IP(nets[i])+"/"+ps[i]);
			for(long ip: ips){
				System.out.println("\t"+IPAddressUtil.int2IP(ip)+" --> "+contains(nets[i],ps[i],ip));
			}
		}
		System.out.println("192.1.1.1/8 -->"+IPAddressUtil.int2IP(cidr2Int("192.1.1.1/8")));
		System.out.println("192.1.1.1/16 -->"+IPAddressUtil.int2IP(cidr2Int("192.1.1.1/16"))+" "+cidr2Subnet("192.1.1.1/16"));
	}
}

