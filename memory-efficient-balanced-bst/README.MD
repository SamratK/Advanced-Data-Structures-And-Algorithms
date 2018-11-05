## Memory Efficient Balanced BST
This is a space optimized implementation of an AVL Tree. AVL Tree operations such as search, insert and delete
take O(h) auxiliary space where h is the height of the tree when done using recursive approach. My implementation uses
iterative approach and uses constant space for the same operations. This is achieved using an additional parent node reference
in each AVL tree node. That would make the space complexity O(2n) for n nodes which is constant times n. As the iterative
approach doesn't need stack memory allocations for recursive method calls, it might also increase the performance in terms
of speed along with optimized space usage.