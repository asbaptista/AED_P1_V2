package dataStructures;

import java.io.Serializable;

/**
 * Hash Table
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
 abstract class HashTable<K,V> implements Map<K,V>, Serializable {

    private static final long serialVersionUID = 1L;
     // Default size of the hash table.
     static final int DEFAULT_CAPACITY = 50;

     // Number of entries in the hash table.
     protected int currentSize;

     // predicted number of elements
     private int size;

     // Number of entries to reach MAX_LOAD_FACTOR.
     protected int maxSize;

     public HashTable(int capacity) {
         currentSize=0;
         size=capacity;
     }

     // Public Static Methods

     // Returns the hash code of the specified key,
     // which is an integer in the range 0, ..., b-1.
     public static int hash( String key ){

         int a = 127; // a is a prime number.
         int b = 2147483647; // b is a prime number.
         int hashCode = 0;

         for ( int i = 0; i < key.length(); i++ )
             hashCode = ( hashCode * a + key.charAt(i) ) % b;
         return hashCode;
     }
     // Protected Static Methods

     // Returns a prime number that is not less than the
     // specified number; or zero if all such primes are greater
     // than Integer.MAX VALUE.
     protected static int nextPrime( int number ){
         if (number <= 2)
             return 2;
         if (number % 2 == 0)
             number++;

         while (true) {
             boolean isPrime = true;
             if (number <= 1) {
                 isPrime = false;
             } else if (number == 2) {
                 isPrime = true;
             } else if (number % 2 == 0) {
                 isPrime = false;
             } else {
                 for (int i = 3; i * i <= number; i += 2) {
                     if (number % i == 0) {
                         isPrime = false;
                         break;
                     }
                 }
             }
             if (isPrime)
                 return number;
             number += 2;
             if (number < 0)
                 return 0;
         }
         //TODO: Left as exercise
        
     }

     // Returns true iff the hash table cannot contain more entries.
     protected boolean isFull( ){
         return currentSize == maxSize;
     }
     /**
      * Returns true iff the dictionary contains no entries.
      *
      * @return true if dictionary is empty
      */
     public boolean isEmpty() {
         return currentSize==0;
     }

     /**
      * Returns the number of entries in the dictionary.
      *
      * @return number of elements in the dictionary
      */
     public int size() {
         return currentSize;
     }

     /**
      * If there is an entry in the dictionary whose key is the specified key,
      * returns its value; otherwise, returns null.
      *
      * @param key whose associated value is to be returned
      * @return value of entry in the dictionary whose key is the specified key,
      * or null if the dictionary does not have an entry with that key
      */
     public abstract V get(K key) ;

     /**
      * If there is an entry in the dictionary whose key is the specified key,
      * replaces its value by the specified value and returns the old value;
      * otherwise, inserts the entry (key, value) and returns null.
      *
      * @param key   with which the specified value is to be associated
      * @param value to be associated with the specified key
      * @return previous value associated with key,
      * or null if the dictionary does not have an entry with that key
      */
     public abstract V put(K key, V value);

     /**
      * If there is an entry in the dictionary whose key is the specified key,
      * removes it from the dictionary and returns its value;
      * otherwise, returns null.
      *
      * @param key whose entry is to be removed from the map
      * @return previous value associated with key,
      * or null if the dictionary does not an entry with that key
      */
     public abstract V remove(K key);

     /**
      * Returns an iterator of the entries in the dictionary.
      *
      * @return iterator of the entries in the dictionary
      */
     public abstract Iterator<Entry<K, V>> iterator();

     /**
      * Returns an iterator of the values in the dictionary.
      *
      * @return iterator of the values in the dictionary
      */
	@SuppressWarnings({"unchecked","rawtypes"})
     public Iterator<V> values() {
         return new ValuesIterator(iterator());
     }

     /**
      * Returns an iterator of the keys in the dictionary.
      *
      * @return iterator of the keys in the dictionary
      */
	@SuppressWarnings({"unchecked","rawtypes"})
     public Iterator<K> keys() {
         return new KeysIterator(iterator());
     }
     
 }
