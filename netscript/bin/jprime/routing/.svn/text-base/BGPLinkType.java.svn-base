package jprime.routing;

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

import java.lang.ref.SoftReference;

import jprime.EntityFactory;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.PersistableObject;
import jprime.Link.ILink;
import jprime.database.PKey;

/**
 * @author Nathanael Van Vorst
 * @author Ting Li
 */
public class BGPLinkType extends PersistableObject{
	//Transient
	private ILink theLink;
	private SoftReference<BGP> __parent;
	private Metadata meta;
	private BGPRelationShipType linkType;	

	//persisted
	private long dbid;
	private long metadata_id;
	private long parent_id;
	private int link_type_id;	
	private boolean first_child_is_src;	
	private String aliasPath; //this path is based from this node's grand parent
	
	/**
	 * Used when loading from DB
	 * @param dbid
	 * @param parent
	 * @param linkType
	 * @param first_child_is_src
	 * @param aliasPath
	 */
	public BGPLinkType(Metadata meta, long dbid, long parent_id, int link_type_id, boolean first_child_is_src, String aliasPath) {
		super();
		this.meta=meta;
		this.metadata_id=meta.getDBID();
		this.dbid=dbid;
		this.__parent=null;
		this.parent_id=parent_id;
		this.theLink=null;
		this.persistable_state=PersistableState.UNMODIFIED;
		this.mods=Modified.NOTHING.id;
		this.link_type_id=link_type_id;
		this.linkType=BGPRelationShipType.fromInt(link_type_id);
		meta.loaded(this);
	}
	
	/**
	 * for jpa
	 */
	public BGPLinkType(BGP parent, ILink theLink, BGPRelationShipType linkType, boolean first_child_is_src) {
		super();
		if(theLink.getAllChildren().size()!=2) {
			throw new RuntimeException("BGP links can only have _two_ endpoints. Found "+theLink.getAllChildren().size()+" on the link "+theLink.getUniqueName());
		}
		this.meta=parent.getMetadata();
		this.metadata_id=meta.getDBID();
		this.dbid=meta.getNextModelNodeDBID();
		this.__parent=new SoftReference<BGP>(parent);
		this.parent_id=parent.getDBID();
		this.theLink=theLink;
		this.link_type_id=linkType.getTypeId();
		this.linkType=linkType;
		this.first_child_is_src=first_child_is_src;
		this.aliasPath=ModelNode.relativePath(getParent().getParent(), theLink);
		this.persistable_state=PersistableState.NEW;
		this.mods=Modified.ALL.id;
		meta.loaded(this);
	}
	
	@Override
	protected void finalize() throws Throwable {
		meta.collect(this);
		super.finalize();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BGPLinkType) {
			return this.getMetadataId() == ((BGPLinkType)obj).getMetadataId() && getDBID() ==  ((BGPLinkType)obj).getDBID();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see jprime.IPeristable#save()
	 */
	public synchronized void save() {
		meta.save(this);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IPeristable#getMetadata()
	 */
	public Metadata getMetadata() {
		return meta;
	}
	
	/* (non-Javadoc)
	 * @see jprime.PeristableObject#getPKey()
	 */
	public PKey getPKey() {
		return new PKey(metadata_id, dbid);
	}
	
	/**
	 * @return
	 */
	public ILink getLink() {
		if(theLink==null) {
			theLink=(ILink)ModelNode.evaluatePath(getParent().getParent(), aliasPath);
		}
		return theLink;
	}
	
	/**
	 * @return
	 */
	public BGPRelationShipType getLinkType() {
		return linkType;
	}
	
	/**
	 * @return
	 */
	public BGP getParent() {
		if(__parent==null || __parent.get()==null) {
			this.__parent=new SoftReference<BGP>((BGP)meta.loadStaticRoutingProtocol(parent_id));
		}
		return __parent.get();
	}
	
	/**
	 * @return
	 */
	public boolean firstChildIsSrc() {
		return first_child_is_src;
	}
	
	/**
	 * @return
	 */
	public String getAliasPath() {
		return aliasPath;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IPeristable#getTypeId()
	 */
	public int getTypeId() {
		return EntityFactory.BGPLinkType;
	}
	
	public long getDBID() {
		return dbid;
	}

	public long getMetadataId() {
		return metadata_id;
	}

	public long getParentId() {
		return parent_id;
	}

	public int getLinkTypeId() {
		return link_type_id;
	}
}
