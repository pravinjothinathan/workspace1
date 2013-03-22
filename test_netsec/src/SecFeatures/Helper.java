package SecFeatures;

public class Helper {
	
	 public byte[] asBytes (String s) {
	        String s2;
	        byte[] b = new byte[s.length() / 2];
	        int i;
	        for (i = 0; i < s.length() / 2; i++) {
	            s2 = s.substring(i * 2, i * 2 + 2);
	            b[i] = (byte)(Integer.parseInt(s2, 16) & 0xff);
	        }
	        return b;
	    }
	 
	 public byte[] ReduceKeySizetoHalf(byte[] in)
	 {
		 int mid = in.length/2;
		 byte[] out = new byte[mid];
		 for (int i = 0; i < mid; i++) {
			out[i] = (byte) (in[i] ^ in[mid+i]);
		}
		 return out;
	 }

}
