/**
 * \file trie.h
 * \brief Header file for the Trie class.
 * \author Jason Liu
 *
 *
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

#ifndef __TRIE_H__
#define __TRIE_H__

#include "os/ssfnet_types.h"

namespace prime {
namespace ssfnet {

#define TRIE_KEY_SPAN       2    // digital trie - 0, 1
#define TRIE_KEY_SIZE(key) 32

/**
 * \internal
 * \brief The key type for the trie.
 *
 * This typedef can be changed (along with a few macros in trie.cc) to
 * support tries of strings.
 */
typedef unsigned int TRIEBITSTRING;

class TrieNode;


/**
 * \brief A data structure for forwarding tables.
 *
 * We use this class to implement the routing table so that we can do
 * fast longest-matching prefix lookups. Our trie is a container for
 * bit-strings. When a new bit-string is added, the number of
 * significant bits must be specified. Just as in IP addressing, the
 * significant bits are counted starting from the most significant
 * bit. The maximum size of the bit-string is 32 bits. With each key,
 * we also store a corresponding pointer to hold user data.
 */
class Trie {
public:
	/** The constructor. */
	Trie();

	/** The destructor. */
	virtual ~Trie();

	/**
	 * Insert a new (key, data) pair. If the key already exists, and if
	 * the replace flag is set, we will replace the old data with the
	 * new one and return the old data. If the replace flag is not set,
	 * the old data remains in the trie and the new data is returned. If
	 * the key does not exist in trie, the data will be inserted and the
	 * return value is 0.
	 */
	TrieData* insert(TRIEBITSTRING key, int nBits,
			TrieData* data, bool replace);

	/**
	 * Remove the key from the trie, if it exists, and returns the data
	 * corresponding to the removed key. If the key is not in the trie,
	 * return NULL.
	 */
	TrieData* remove(TRIEBITSTRING key, int nBits);

	/**
	 * Perform a longest-matching prefix search for the key and if
	 * found, places the data in pData. Otherwise, pData is NULL.
	 */
	void lookup(TRIEBITSTRING key, TrieData** pData);

	/** Return the number of items in the trie. */
	int size() { return nElements; }

protected:
	/**
	 * Helper function for the insert and lookup methods. This function
	 * will search the trie for the key and if found, return a pointer
	 * to the TrieNode. If not found, it will return NULL, and set
	 * failedNode to the TrieNode where the search fails and
	 * failedbitpos to the bit position that failed.
	 */
	TrieNode* search(TRIEBITSTRING key, int len,
			TrieNode** failedNode, int* failedbitpos);

	/**
	 * Helper function for the insert method. Creates nBits nodes for
	 * the key starting at TrieNode theroot.
	 */
	static void create(TrieNode* theroot, TRIEBITSTRING key,
			int nBits, TrieData* data);

	/** Deallocate the trie recursively. */
	static void deallocate(TrieNode* theroot);

	/** The root of the trie. */
	TrieNode* root;

	/** Number of data elements stored in the trie. */
	int nElements;

public:
	/**
	 * \internal
	 * Depth-first iterator.
	 */
	class iterator {
	public:
		/**
		 * \internal
		 * Undefined iterator state exception.
		 */
		class State {};

		enum NextMove {
			LEFT  = 0,
			RIGHT = 1,
			UP    = 2,
			UNDEF = 3
		};

		class NodeNext { // stored in stack
		public:
			TrieNode* node;
			NextMove  next;
			NodeNext(TrieNode* _node, NextMove _next) :
				node(_node), next(_next) {}
			int operator==(NodeNext& rhs) {
				return (node == rhs.node && next == rhs.next);
			}
		};

		// the constructors and the destructor
		iterator();
		iterator(Trie* t);
		iterator(const Trie::iterator& it); // copy costr
		~iterator();

		/** Assignment operator. Does deep copy of state stack. */
		iterator& operator=(iterator& rhs);

		/** Comparison operator. */
		int operator==(iterator& rhs);

		/** Comparison operator. */
		int operator!=(iterator& rhs);

		/** Prefix incr. operator. */
		iterator& operator++();

		/** Arrow operator. */
		TrieNode* operator->();

		Trie* myTrie;
		TrieNode* currentNode;
		SSFNET_STACK(NodeNext*) stateStack;

		// debug
		friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const Trie::iterator& x);

	private:
		/** Copy state stack. Assignment helper. */
		void copyStack(SSFNET_STACK(NodeNext*)& from, bool deepCopy=true);

		/** Move to next node. Iterator helper. */
		void nextNode();
	};

	//  iterator first;
	iterator last;

	iterator begin();
	iterator& end() { return last; }
};

/**
 * \internal
 * The class representing each node in the trie.
 */
class TrieNode {
public:
	/** The constructor. */
	TrieNode();

	/** The destructor. */
	~TrieNode();

	/** Data field. */
	TrieData* data;

	/** Pointers to children nodes. */
	TrieNode* children[TRIE_KEY_SPAN];

	friend PRIME_OSTREAM& operator<<(PRIME_OSTREAM &os, const TrieNode& x);
};

}; // namespace ssfnet
}; // namespace prime

#endif /*__TRIE_H__*/
