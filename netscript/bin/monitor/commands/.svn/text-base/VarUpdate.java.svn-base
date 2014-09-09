package monitor.commands;

import jprime.visitors.TLVVisitor.TLVType;

import org.apache.mina.core.buffer.IoBuffer;

public abstract class VarUpdate {
	public final int var_id;
	public final TLVType var_type;
	VarUpdate(int var_id, TLVType var_type) {
		this.var_id=var_id;
		this.var_type=var_type;
	}
	public void encode(IoBuffer buf, boolean forPrime) {
		buf.putInt(var_id);
		buf.putInt(var_type.type);
	}
	public abstract int size();
	public abstract String asString();
	public static VarUpdate parseVarUpdate(IoBuffer in, boolean forPrime) {
		final int id = in.getInt();
		final int t = in.getInt();
		TLVType type = TLVType.fromInt(t);
		switch(type) {
		case STRING: 
			return new StringUpdate(id,type,in);
		case FLOAT:
			return new DoubleUpdate(id,type,in,forPrime);
		case LONG:
			return new LongUpdate(id,type,in);
		case BOOL:
			return new BoolUpdate(id,type,in);
		}
		throw new RuntimeException("unknown type!");
	}

	public static class DoubleUpdate extends VarUpdate {
		public final double value;
		public DoubleUpdate(int id, TLVType type, double value) {
			super(id,type);
			this.value=value;			
		}
		public DoubleUpdate(int id, TLVType type, IoBuffer in, boolean forPrime) {
			super(id,type);
			if(forPrime) {
				byte b[] = new byte[in.getInt()];
				in.get(b);
				String s=new String(b);
				this.value=Double.parseDouble(s);
			}
			else {
				this.value=in.getDouble();
			}
		}
		public void encode(IoBuffer buf, boolean forPrime) {
			super.encode(buf,forPrime);
			if(forPrime)  {
				String v = Double.toString(value);
				buf.putInt(v.length());
				buf.put(v.getBytes());
			}
			else {
			buf.putDouble(value);
			}
			
		}
		public int size() {
			return (Integer.SIZE*2 + Double.SIZE)/8;
		}
		public String toString() {
			return "["+var_id+":"+value+"]";
		}
		public String asString() {
			return Double.toString(value);
		}
	}
	public static class BoolUpdate extends VarUpdate {
		public final boolean value;
		public BoolUpdate(int id, TLVType type, boolean value) {
			super(id,type);
			this.value=value;			
		}
		public BoolUpdate(int id, TLVType type, IoBuffer in) {
			super(id,type);
			this.value=(0xFF == in.get())?false:true;
		}
		public void encode(IoBuffer buf, boolean forPrime) {
			super.encode(buf,forPrime);
			if(value)buf.put((byte)0xFF);
			else buf.put((byte)0x00);
		}
		public int size() {
			return (Integer.SIZE*2 + Byte.SIZE)/8;
		}
		public String toString() {
			return "["+var_id+":"+value+"]";
		}
		public String asString() {
			if(value) return "true";
			return "false";
		}
	}
	public static class LongUpdate extends VarUpdate {
		public final long value;
		public LongUpdate(int id, TLVType type, long value) {
			super(id,type);
			this.value=value;			
		}
		public LongUpdate(int id, TLVType type, IoBuffer in) {
			super(id,type);
			this.value=in.getLong();
		}
		public void encode(IoBuffer buf, boolean forPrime) {
			super.encode(buf,forPrime);
			buf.putLong(value);
		}
		public int size() {
			return (Integer.SIZE*2 + Long.SIZE)/8;
		}
		public String toString() {
			return "["+var_id+":"+value+"]";
		}
		public String asString() {
			return Long.toString(value);
		}
	}
	public static class StringUpdate extends VarUpdate {
		public final String value;
		public StringUpdate(int id, TLVType type, String value) {
			super(id,type);
			this.value=value;
		}
		public StringUpdate(int id, TLVType type, byte value[]) {
			super(id,type);
			this.value=new String(value);
		}
		public StringUpdate(int id, TLVType type, IoBuffer in) {
			super(id,type);
			byte b[] = new byte[in.getInt()];
			in.get(b);
			this.value=new String(b);
		}
		public void encode(IoBuffer buf, boolean forPrime) {
			super.encode(buf,forPrime);
			buf.putInt(value.length());
			buf.put(value.getBytes());
		}
		public int size() {
			return (Integer.SIZE*3 + Byte.SIZE*value.length())/8;
		}
		public String toString() {
			return "["+var_id+":"+value+"]";
		}
		public String asString() {
			return value;
		}
	}

}