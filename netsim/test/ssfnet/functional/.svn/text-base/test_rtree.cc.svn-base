//
// Test.cpp
//
// This is a direct port of the C version of the RTree test program.
//

#include "os/rtree.h"
using namespace prime::ssfnet;


class Foo {
public:
	Foo(char* v) {
		val.append(v);
	}
	std::string val;
};

class MyRect : public RTree<Foo*, int, 2, float>::Rect {
public:
	MyRect(int a_minX, int a_minY, int a_maxX, int a_maxY)
  {
		m_min[0] = a_minX;
		m_min[1] = a_minY;

    m_max[0] = a_maxX;
    m_max[1] = a_maxY;
  }
};

class SR : public RTree<Foo*, int, 2, float>::SearchResult {
public:
	SR(){}
	~SR(){}
	bool addResult(Foo*& result) {
		results.push_back(result);
		return true;
	}
	SSFNET_LIST(Foo*) results;
};

struct MyRect rects[] =
{
	MyRect(0, 1, 2, 1),
	MyRect(1, 1, 1, 1),
	MyRect(0, 0, 2, 2),
	MyRect(5, 5, 7, 7),
	MyRect(8, 5, 9, 6),
	MyRect(7, 1, 9, 2),
};

int nrects = sizeof(rects) / sizeof(rects[0]);

int main(int argc, char** argv) {
	int i, itIndex = 0;
	char ack[128];
	RTree<Foo*, int, 2, float> tree;

	for(i=0; i<nrects; i++)
	{
		sprintf(ack,"<rectangle %d,%d,%d,%d>",rects[i].m_min[0],rects[i].m_min[1],rects[i].m_max[0],rects[i].m_max[1]);
		tree.Insert(rects[i], new Foo(ack)); // Note, all values including zero are fine in this version
	}
	RTree<Foo*, int, 2, float>::Iterator it;
	for (tree.GetFirst(it); !tree.IsNull(it); tree.GetNext(it)) {
		Foo* x = tree.GetAt(it);
	    int boundsMin[2] = {0,0};
	    int boundsMax[2] = {0,0};
	    it.GetBounds(boundsMin, boundsMax);
	    printf("Rectangle [%d] = (%d,%d,%d,%d) -- %s \n", itIndex++, boundsMin[0], boundsMin[1], boundsMax[0], boundsMax[1], x->val.c_str());
	}
	printf("There are %i rectangles here...\n",tree.Count());
	MyRect search_rect(1, 1, 1, 1); // search will find above rects that this one overlaps
	//MyRect search_rect(6, 4, 10, 6); // search will find above rects that this one overlaps
	SR rv;
	printf("searching for (%d,%d,%d,%d)\n", search_rect.m_min[0],search_rect.m_min[1],search_rect.m_max[0],search_rect.m_max[1]);
	tree.Search(search_rect, rv);
	for(SSFNET_LIST(Foo*)::iterator i=rv.results.begin(); i != rv.results.end(); i++) {
		Foo* x=*i;
		printf("matched %s\n", x->val.c_str());
	}

	return -1;
}
