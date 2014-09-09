package monitor.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import jprime.ModelNode;
import jprime.ModelNodeReplica;
import jprime.variable.ModelNodeVariable;
import jprime.visitors.TLVVisitor.TLVType;

public class CreateDynamicModelNode extends AbstractCmd{
	public final String tlv;
	public final int part_id;
	public CreateDynamicModelNode(ModelNode n) {
		super(CommandType.CREATE_DYNAMIC_MODEL_NODE);
		this.part_id = -1;
		this.tlv = encode(n).toString();
	}
	public CreateDynamicModelNode(int part_id, String tlv) {
		super(CommandType.CREATE_DYNAMIC_MODEL_NODE);
		this.tlv=tlv;
		this.part_id=part_id;
	}

	private static StringBuffer makeTLV(TLVType type, int length, StringBuffer value) {
		StringBuffer temp = new StringBuffer();
		temp.append(type.str);
		temp.append(Integer.toString(length));
		temp.append('\n');
		temp.append(value);
		value.delete(0, value.length());
		return temp;
	}

	private static StringBuffer makeTLV(TLVType type, int length, String value) {
		StringBuffer temp = new StringBuffer();
		temp.append(type.str);
		temp.append(Integer.toString(length));
		temp.append('\n');
		temp.append(value);
		return temp;
	}

	private static StringBuffer encodeAttr(TLVType t, int id, String value) {
		switch(t) {
		case LIST:
		case BOOL:
		case FLOAT:
		case LONG:
		case OPAQUE:
		case STRING:
		case RESOURCE_ID:
		case SYMBOL:
			StringBuffer attr=makeTLV(TLVType.LONG,Integer.toString(id).length(),Integer.toString(id));
			attr.append(makeTLV(t,value.length(),value));
			return makeTLV(TLVType.PROPERTY,attr.length(),attr);
		default:
			throw new RuntimeException("Unknown expected type "+t);
		}
	}
	
	private static StringBuffer encode(ModelNode n) {
		StringBuffer temp = new StringBuffer();

		//uid
		temp.append(Long.toString(n.getUID()));
		StringBuffer model_node=makeTLV(TLVType.LONG,temp.length(),temp);

		//parent uid
		if(n.getParent()==null) {
			temp.append("0");
		}
		else {
			temp.append(Long.toString(n.getParent().getUID()));
		}
		model_node.append(makeTLV(TLVType.LONG,temp.length(),temp));

		//replica id
		temp.append("0");
		model_node.append(makeTLV(TLVType.LONG,temp.length(),temp));

		//type
		temp.append(Integer.toString(n.getTypeId()));
		model_node.append(makeTLV(TLVType.LONG,temp.length(),temp));

		//attrs
		model_node.append(encodeAttr(TLVType.STRING,ModelNodeVariable.name(),n.getName()));
		model_node.append(encodeAttr(TLVType.LONG,ModelNodeVariable.offset(),Long.toString(n.getOffset())));
		model_node.append(encodeAttr(TLVType.LONG,ModelNodeVariable.size(),Long.toString(n.getSize())));

		TreeSet<Integer> encoded_attrs=new TreeSet<Integer>();
		List<ModelNode> to_encode = new ArrayList<ModelNode>();
		{
			ModelNode base_node = n;
			to_encode.add(base_node);
			while(base_node instanceof ModelNodeReplica) {
				base_node=((ModelNodeReplica)base_node).getReplicatedNode();
				to_encode.add(base_node);
			}
		}
		for(ModelNode base_node : to_encode) {
			for(ModelNodeVariable v : base_node.getAttributeValues()) {
				if(ModelNodeVariable.shouldEncodeAttr(v.getDBName())) {
					if(!encoded_attrs.contains(v.getDBName())) {
						encoded_attrs.add(v.getDBName());
						model_node.append(v.encodeTLV());
					}
				}
			}
		}
		return makeTLV(TLVType.GENERIC_NODE,model_node.length(),model_node);
	}
	
	/* (non-Javadoc)
	 * @see monitor.commands.AbstractCmd#getBodyLength()
	 */
	@Override
	public int getBodyLength() {
		return tlv.length()+(2*Integer.SIZE)/8;
	}
}