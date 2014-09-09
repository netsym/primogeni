
package jprime.TrafficType;

import jprime.variable.ResourceIdentifierVariable;

public interface ITrafficType extends jprime.gen.ITrafficType {
	
	public ResourceIdentifierVariable getSrcs();
	public ResourceIdentifierVariable getDsts();
}
