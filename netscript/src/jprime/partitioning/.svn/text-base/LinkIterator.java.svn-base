package jprime.partitioning;

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

import java.util.ListIterator;

/**
 * @author Nathanael Van Vorst*/
public class LinkIterator implements ListIterator<ProcessingNodeLink> {
	private final ListIterator<ProcessingNodeLink> it_left;
	private final ListIterator<ProcessingNodeLink> it_right;
	public LinkIterator(Partition p) {
		it_left=p.left_links.listIterator();
		it_right=p.right_links.listIterator();
	}
	public void add(ProcessingNodeLink e) {
		throw new RuntimeException("Do not use this function!");
	}
	public boolean hasNext() {
		return it_left.hasNext() || it_right.hasNext();
	}
	public boolean hasPrevious() {
		return it_left.hasPrevious() || it_right.hasPrevious();
	}
	public ProcessingNodeLink next() {
		if(it_left.hasNext())
			return it_left.next();
		return it_right.next();
	}
	public int nextIndex() {
		if(it_left.hasNext())
			return it_left.nextIndex();
		return it_right.nextIndex();
	}
	public ProcessingNodeLink previous() {
		if(it_right.hasPrevious())
			return it_right.previous();
		return it_left.previous();
	}
	public int previousIndex() {
		if(it_right.hasPrevious())
			return it_right.previousIndex();
		return it_left.previousIndex();
	}
	public void remove() {
		throw new RuntimeException("Do not use this function!");
	}
	public void set(ProcessingNodeLink m) {
		throw new RuntimeException("Do not use this function!");
	}
}