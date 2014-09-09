package slingshot.visualization;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jprime.IModelNode;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.PersistableObject.PersistableState;
import jprime.Net.INet;
import jprime.ResourceIdentifier.EvalutedResourceID;
import jprime.ResourceIdentifier.ResourceID;
import jprime.partitioning.Partitioning;
import jprime.partitioning.PartitioningVisitor;
import jprime.routing.IRouteVisitor;
import jprime.routing.RouteCalculationVisitor;
import jprime.util.PersistentChildList;
import jprime.util.UniqueName;
import jprime.variable.Dataset;
import jprime.variable.Dataset.SimpleDatum;
import jprime.variable.ModelNodeVariable;
import jprime.variable.ModelNodeVariable.VizAttr;
import jprime.visitors.IGenericVisitor;
import jprime.visitors.IPAddressAssignment;
import jprime.visitors.TLVVisitor;
import jprime.visitors.UIDAssignmentVisitor;
import jprime.visitors.VerifyVisitor;
import jprime.visitors.XMLVisitor;

public class PortalNetwork implements IModelNode {
	private final List<String> networks;
	private final INet owner;
	private PrefuseLocation loc;
	private int row;
	public PortalNetwork(INet owner, List<String> networks) {
		super();
		this.networks = networks;
		this.owner = owner;
		this.row=-1;
		this.loc=null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(IModelNode o) {
		if(o == this)
			return 0;
		if(o instanceof PortalNetwork)
			return this.hashCode() - o.hashCode();
		if(o == owner)
			return 1;
		return (int)(this.getUID() - o.getUID());
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getDBID()
	 */
	@Override
	public long getDBID() {
		//throw new UnsupportedOperationException();
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getParentId()
	 */
	@Override
	public long getParentId() {
		return owner.getDBID();
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getUID()
	 */
	@Override
	public long getUID() {
		return owner.getUID();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#setUID(long)
	 */
	@Override
	public void setUID(long id) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getRank(jprime.IModelNode)
	 */
	@Override
	public long getRank(IModelNode anchor) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getMinRank(jprime.IModelNode)
	 */
	@Override
	public long getMinRank(IModelNode anchor) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getMinUID()
	 */
	@Override
	public long getMinUID() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getMaxRank(jprime.IModelNode)
	 */
	@Override
	public long getMaxRank(IModelNode anchor) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#hasBeenReplicated()
	 */
	@Override
	public boolean hasBeenReplicated() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#containsRank(jprime.IModelNode, long)
	 */
	@Override
	public boolean containsRank(IModelNode anchor, long rank) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#copy(java.lang.String, jprime.IModelNode)
	 */
	@Override
	public IModelNode copy(String name, IModelNode parent) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#containsUID(long)
	 */
	@Override
	public boolean containsUID(long uid) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getName()
	 */
	@Override
	public String getName() {
		return "TrafficPortal";
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getSize()
	 */
	@Override
	public long getSize() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#setSize(long)
	 */
	@Override
	public void setSize(long s) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getOffset()
	 */
	@Override
	public long getOffset() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#setOffset(long)
	 */
	@Override
	public void setOffset(long o) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getOrder()
	 */
	@Override
	public int getOrder() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getTypeId()
	 */
	@Override
	public int getTypeId() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#deference()
	 */
	@Override
	public IModelNode deference() {
		return null;
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getAttributes(boolean)
	 */
	
	public Collection<jprime.variable.ModelNodeVariable> getAllAttributeValues(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Map<String, String> getAttributes(boolean includeDefaultValues) {
		return getAttributes();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getVizAttributes(jprime.variable.Dataset)
	 */
	@Override
	public Map<String, VizAttr> getVizAttributes(Dataset d) {
		return new HashMap<String, ModelNodeVariable.VizAttr>();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getAttributes()
	 */
	@Override
	public Map<String, String> getAttributes() {
		HashMap<String, String> rv = new HashMap<String, String>();
		int i=0;
		for(String s : networks) {
			rv.put("route "+i,s);
			i++;
		}
		return rv;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getAttributeValues(boolean)
	 */
	
	@Override
	public Collection<jprime.variable.ModelNodeVariable> getAttributeValues(){
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getAttributeByName(java.lang.String)
	 */
	@Override
	public ModelNodeVariable getAttributeByName(String name) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getAttributeByName(int)
	 */
	@Override
	public ModelNodeVariable getAttributeByName(int name) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getAttributeValueByName(int)
	 */
	@Override
	public String getAttributeValueByName(int name) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#setAttribute(int, java.lang.String)
	 */
	@Override
	public ModelNodeVariable setAttribute(int varId, String value)
			throws InstantiationException, IllegalAccessException {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getRuntimeValueByName(int, jprime.variable.Dataset)
	 */
	@Override
	public SimpleDatum getRuntimeValueByName(int name, Dataset ds) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getAllChildren()
	 */
	@Override
	public List<ModelNode> getAllChildren() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getPersistentChildList()
	 */
	@Override
	public PersistentChildList getPersistentChildList() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getMetadata()
	 */
	@Override
	public Metadata getMetadata() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getAlignments(jprime.partitioning.Partitioning)
	 */
	@Override
	public Set<Integer> getAlignments(Partitioning p) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getParent_nofetch()
	 */
	@Override
	public IModelNode getParent_nofetch() {
		return owner;
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getParent()
	 */
	@Override
	public IModelNode getParent() {
		return owner;
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getUniqueName()
	 */
	@Override
	public UniqueName getUniqueName() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getNumberOfChildren()
	 */
	@Override
	public int getNumberOfChildren() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getChildByName(java.lang.String)
	 */
	@Override
	public IModelNode getChildByName(String name) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#isReplica()
	 */
	@Override
	public boolean isReplica() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#isAlias()
	 */
	@Override
	public boolean isAlias() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#enforceMinimumChildConstraints()
	 */
	@Override
	public void enforceMinimumChildConstraints() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getChildByRank(long, jprime.IModelNode)
	 */
	@Override
	public IModelNode getChildByRank(long rank, IModelNode anchor) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#evaluateResourceID(jprime.ResourceIdentifier.ResourceID, int, int, jprime.ResourceIdentifier.ResourceID.EvalutedResourceID)
	 */
	@Override
	public void evaluateResourceID(ResourceID resourceid, int list_idx,
			int term_idx, EvalutedResourceID result) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.visitors.IGenericVisitor)
	 */
	@Override
	public void accept(IGenericVisitor visitor) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.visitors.IPAddressAssignment)
	 */
	@Override
	public void accept(IPAddressAssignment visitor) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.visitors.UIDAssignmentVisitor)
	 */
	@Override
	public void accept(UIDAssignmentVisitor visitor) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.visitors.TLVVisitor)
	 */
	@Override
	public void accept(TLVVisitor visitor) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.routing.IRouteVisitor)
	 */
	@Override
	public void accept(IRouteVisitor visitor) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.visitors.VerifyVisitor)
	 */
	@Override
	public void accept(VerifyVisitor visitor) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.routing.RouteCalculationVisitor)
	 */
	@Override
	public void accept(RouteCalculationVisitor visitor) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.partitioning.PartitioningVisitor)
	 */
	@Override
	public void accept(PartitioningVisitor visitor) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.visitors.XMLVisitor)
	 */
	@Override
	public void accept(XMLVisitor visitor) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#addAttr(jprime.variable.ModelNodeVariable)
	 */
	@Override
	public void addAttr(ModelNodeVariable attr) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getPersistableState()
	 */
	@Override
	public PersistableState getPersistableState() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#findNodeByUID(long)
	 */
	@Override
	public IModelNode findNodeByUID(long uid) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#delete()
	 */
	@Override
	public void delete() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#__copy(java.lang.String, jprime.IModelNode, jprime.IModelNode)
	 */
	@Override
	public IModelNode __copy(String name, IModelNode toCopy, IModelNode parent) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getNodeType()
	 */
	@Override
	public Class<?> getNodeType() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getChildByQuery(java.lang.String)
	 */
	@Override
	public IModelNode getChildByQuery(String query) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#get(java.lang.String)
	 */
	@Override
	public IModelNode get(String query) {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#help()
	 */
	@Override
	public List<String> help() {
		return new LinkedList<String>();
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getRow()
	 */
	@Override
	public int getRow() {
		return row;
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#setRow(int)
	 */
	@Override
	public void setRow(int row) {
		this.row=row;
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getHub()
	 */
	@Override
	public boolean getHub() {
		return false;
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#setHub(boolean)
	 */
	@Override
	public void setHub(boolean h) {
	}
	/* (non-Javadoc)
	 * @see jprime.IModelNode#updateLocation(jprime.IModelNode.PrefuseLocation)
	 */
	@Override
	public PrefuseLocation updateLocation(PrefuseLocation loc) {
		PrefuseLocation rv = this.loc;
		this.loc=loc;
		return rv;
	}
}
