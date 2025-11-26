# Performance Improvements

This document outlines the performance improvements made to the codebase and suggests additional optimizations for future consideration.

## Implemented Improvements

### 1. DoublyLinkedList Position-Based Operations (O(n) → O(n/2))

**Files Modified:** `src/dataStructures/DoublyLinkedList.java`

**Problem:** The `get(int position)`, `add(int position, E element)`, and `remove(int position)` methods always traversed from the head of the list, even for positions near the tail. This resulted in worst-case O(n) performance for accessing elements near the end.

**Solution:** Implemented a helper method `getNode(int position)` that:
- Starts from the **head** if `position < size / 2`
- Starts from the **tail** if `position >= size / 2`

**Impact:** Reduces worst-case traversal time by half (O(n/2) instead of O(n)) for random access operations.

**Code Change:**
```java
private DoublyListNode<E> getNode(int position) {
    DoublyListNode<E> current;
    if (position < currentSize / 2) {
        // Start from head
        current = head;
        for (int i = 0; i < position; i++) {
            current = current.getNext();
        }
    } else {
        // Start from tail
        current = tail;
        for (int i = currentSize - 1; i > position; i--) {
            current = current.getPrevious();
        }
    }
    return current;
}
```

### 2. Fixed Duplicated Initialization in ServicesCollectionImpl

**Files Modified:** `src/ServicesCollectionImpl.java`

**Problem:** In the `readObject` deserialization method, `servicesByTypeAndStars` was initialized twice on consecutive lines, wasting memory and CPU cycles.

**Solution:** Removed the duplicated initialization line.

**Impact:** Minor memory and performance improvement during deserialization.

---

## Suggestions for Future Improvements

### 1. Use HashSet for Duplicate Detection in Visit Registration

**Current Implementation:** Both `BookishImpl.registerVisit()` and `OutgoingImpl.registerVisit()` use `indexOf()` (O(n)) to check for duplicates before adding a service to `visitedServices`.

**Suggested Improvement:** Maintain an auxiliary `HashSet<Service>` for O(1) duplicate checks, while keeping the `DoublyLinkedList` for ordered iteration.

**Trade-off:** Increases memory usage but significantly improves performance for students who visit many services.

### 2. Cache Services by Star Rating

**Current Implementation:** `ServicesCollectionImpl.getServicesByStars()` creates a new `DoublyLinkedList` and populates it on every call.

**Suggested Improvement:** Cache the result and invalidate only when services are added or when star ratings change.

**Trade-off:** Requires careful cache invalidation logic but eliminates redundant list creation.

### 3. Optimize `removeOccupant` in EatingImpl and LodgingImpl

**Current Implementation:** Both use `indexOf()` (O(n)) followed by `remove(index)` (O(n)), resulting in O(2n) complexity.

**Suggested Improvement:** Implement a `removeElement(E element)` method in `DoublyLinkedList` that finds and removes in a single pass (O(n)).

### 4. Consider Using Binary Search for Sorted Lists

**Current Implementation:** `SortedDoublyLinkedList` performs linear search when finding insertion positions.

**Suggested Improvement:** For large sorted lists, consider using a skip list or self-balancing tree structure for O(log n) insertions and lookups.

### 5. Lazy Evaluation for FilterIterator

**Current Implementation:** `FilterIterator` pre-computes the first matching element in its constructor.

**Current Status:** This is actually good for most use cases as it validates the iterator immediately. No change recommended.

---

## Performance Complexity Summary

| Operation | Original | Improved |
|-----------|----------|----------|
| `DoublyLinkedList.get(position)` | O(n) | O(n/2) |
| `DoublyLinkedList.add(position, element)` | O(n) | O(n/2) |
| `DoublyLinkedList.remove(position)` | O(n) | O(n/2) |
| `ServicesCollectionImpl.readObject()` | Duplicated init | Fixed |

---

## Notes

- The existing codebase already uses appropriate data structures (AVL trees, hash tables) for many operations.
- The `tagMap` indexing system provides O(1) lookup for services by tag, which is efficient.
- The `servicesByTypeAndStars` index provides O(1) lookup for filtered queries.
