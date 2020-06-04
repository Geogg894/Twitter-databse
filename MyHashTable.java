package FinalProject_Template;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;



public class MyHashTable<K,V> implements Iterable<HashPair<K,V>>{
	// num of entries to the table
	private int numEntries;
	// num of buckets 
	private int numBuckets;
	// load factor needed to check for rehashing 
	private static final double MAX_LOAD_FACTOR = 0.75;
	// ArrayList of buckets. Each bucket is a LinkedList of HashPair
	private ArrayList<LinkedList<HashPair<K,V>>> buckets; 


	// constructor
	public MyHashTable(int initialCapacity) {

		if(initialCapacity<0) {

			throw new IllegalArgumentException();
		}

		this.numEntries=0;
		this.numBuckets=initialCapacity;
		this.buckets= new ArrayList<LinkedList<HashPair<K,V>>>(this.numBuckets);

		for(int i=0;i<this.numBuckets;i++) {

			buckets.add(new LinkedList<HashPair<K,V>>()); 			
		}
	}



	public int size() {
		return this.numEntries;
	}

	public boolean isEmpty() {
		return this.numEntries == 0;
	}

	public int numBuckets() {
		return this.numBuckets;
	}

	/**
	 * Returns the buckets variable. Useful for testing  purposes.
	 */
	public ArrayList<LinkedList< HashPair<K,V> > > getBuckets(){
		return this.buckets;
	}

	/**
	 * Given a key, return the bucket position for the key. 
	 */
	public int hashFunction(K key) {
		int hashValue = Math.abs(key.hashCode())%this.numBuckets;
		return hashValue;
	}


	private void throwIfKeyNull(K key) {

		if(key==null) {

			throw new NullPointerException();
		}

	}

	private double loadFactor() {

		return (double) size()/numBuckets();

	}



	/**
	 * Takes a key and a value as input and adds the corresponding HashPair
	 * to this HashTable. Expected average run time  O(1)
	 * @throws Exception 
	 */

	public V put(K key, V value)  {

		throwIfKeyNull(key);

		int index=hashFunction(key);
		LinkedList<HashPair<K, V>> list=buckets.get(index);
		HashPair<K, V> pair=new HashPair<K, V>(key, value);

		if(list.isEmpty()){

			list.add(pair);
			numEntries++;

			if(loadFactor()>=MAX_LOAD_FACTOR) {

				rehash();
			}
			return null;
		}

		else {

			for(HashPair<K, V> f:list) {

				if (f.getKey().equals(key)) {

					V x=f.getValue();
					f.setValue(value);
					return x;

				}
			}

			list.add(pair);
			numEntries++;

			if(loadFactor()>=MAX_LOAD_FACTOR) {

				rehash();
			}

			return null;

		}	
	}		



	/**
	 * Get the value corresponding to key. Expected average runtime O(1)
	 */

	public V get(K key) {


		throwIfKeyNull(key);


		int index=hashFunction(key);


		LinkedList<HashPair<K, V>> list = buckets.get(index);

		if(list==null) {

			return null;
		}

		else {


			for(HashPair<K, V> pair:list) {

				if (pair.getKey().equals(key)) {

					return pair.getValue();
				}

			}
		}

		return null;
	}


	/**
	 * Remove the HashPair corresponding to key . Expected average runtime O(1) 
	 */
	public V remove(K key) {


		throwIfKeyNull(key);

		int index=hashFunction(key);
		LinkedList<HashPair<K, V>> element = buckets.get(index);

		V value=null;

		if(element.isEmpty()) {

			return null;
		}

		for(HashPair<K, V> pair:element) {


			if(pair.getKey().equals(key)) {

				value=pair.getValue();
				element.remove(pair);
				numEntries--;
				return value;

			}

		}       

		return value;
	}



	/** 
	 * Method to double the size of the hashtable if load factor increases
	 * beyond MAX_LOAD_FACTOR.
	 * Made public for ease of testing.
	 * Expected average runtime is O(m), where m is the number of buckets
	 */
	public void rehash() {

		ArrayList<LinkedList<HashPair<K, V>>> current=buckets;

		buckets=new ArrayList<LinkedList<HashPair<K, V>>>(2*numBuckets);

		for (int i =0;i<2*numBuckets;i++) { 

			buckets.add(null); 
		} 

		for (int i=0;i<buckets.size(); i++) { 

			buckets.set(i, new LinkedList<HashPair<K, V>>()); 
		} 

		numEntries=0;
		numBuckets*=2;

		for(int k=0;k<current.size();k++) {		

			for( HashPair<K, V> y : current.get(k) ) {

				put(y.getKey(), y.getValue());
			}
		}

	}


	/**
	 * Return a list of all the keys present in this hashtable.
	 * Expected average runtime is O(m), where m is the number of buckets
	 */

	public ArrayList<K> keys() {

		ArrayList<K> arrayList=new ArrayList<K>(this.numEntries);

		for(int i=0;i<buckets.size();i++) {

			for(HashPair<K, V> z:buckets.get(i)) {

				K key=z.getKey();
				arrayList.add(key);
			}
		}

		return arrayList;
	}

	/**
	 * Returns an ArrayList of unique values present in this hashtable.
	 * Expected average runtime is O(m) where m is the number of buckets
	 */

	public ArrayList<V> values() {

		ArrayList<V> arrayList=new ArrayList<V>(this.numEntries);
		MyHashTable<V, K> table=new MyHashTable<V, K>(this.numBuckets);
		ArrayList<K> getAllKeys=this.keys();

		for(K posKey:getAllKeys) {

			table.put(this.get(posKey), posKey);
		}

		arrayList=table.keys();
		return arrayList;
	}


	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2), where n is the number 
	 * of pairs in the map. 
	 */
	public static <K, V extends Comparable<V>> ArrayList<K> slowSort (MyHashTable<K, V> results) {
		ArrayList<K> sortedResults = new ArrayList<>();
		for (HashPair<K, V> entry : results) {
			V element = entry.getValue();
			K toAdd = entry.getKey();
			int i = sortedResults.size() - 1;
			V toCompare = null;
			while (i >= 0) {
				toCompare = results.get(sortedResults.get(i));
				if (element.compareTo(toCompare) <= 0 )
					break;
				i--;
			}
			sortedResults.add(i+1, toAdd);
		}
		return sortedResults;
	}


	private static <K,V extends Comparable<V>> ArrayList <HashPair<K, V>> mergeSort(ArrayList <HashPair<K, V>> list) {


		if(list.size()==1) {

			return list;
		}

		else {

		
			ArrayList <HashPair<K, V>> left= new ArrayList <HashPair<K, V>>();
			ArrayList <HashPair<K, V>> right=new ArrayList <HashPair<K, V>>();;


			for(int k=0;k<list.size()/2;k++) {

				left.add(list.get(k));
			}

			for(int i=list.size()/2;i<list.size();i++) {

				right.add(list.get(i));
			}

			ArrayList <HashPair<K, V>> result= new ArrayList <HashPair<K, V>>();

			left=mergeSort(left);
			right=mergeSort(right);


			result=merge(left,right);

			return result;


		}
	}



	public static <K,V extends Comparable<V>> ArrayList <HashPair<K, V>> merge(ArrayList <HashPair<K, V>> left,ArrayList <HashPair<K, V>> right) {

		ArrayList <HashPair<K, V>> result=new ArrayList <HashPair<K, V>>();

		while(!left.isEmpty()&&!right.isEmpty()) {

			if(left.get(0).getValue().compareTo(right.get(0).getValue())>0) {

				result.add(left.remove(0));
			}

			else {

				result.add(right.remove(0));
			}
		}

		while(!left.isEmpty()&&right.isEmpty()) {

			result.add(left.remove(0));

		}

		while(!right.isEmpty()&&left.isEmpty()) {

			result.add(right.remove(0));
		}

		return result;
	}


	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to.
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 * @return 
	 */

	public static <K, V extends Comparable<V>> ArrayList<K> fastSort(MyHashTable<K, V> results) {

		ArrayList <HashPair<K, V>> list=new ArrayList <HashPair<K, V>>();
		ArrayList<K> list2=new ArrayList<K>();

		for(HashPair<K, V> pair:results) {

			list.add(pair);
		}

		list=mergeSort(list);

		for(HashPair<K, V> p:list) {

			list2.add(p.getKey());
		}

		return list2;

	}


	@Override
	public MyHashIterator iterator() {

		return new MyHashIterator();
	}   

	private class MyHashIterator implements Iterator<HashPair<K,V>> {


		LinkedList<HashPair<K,V>> list;
		Iterator<HashPair<K,V>> iterator;

		private void HashTableIterator(ArrayList<LinkedList<HashPair<K,V>>> arrayList){
		
			list = new LinkedList<HashPair<K,V>>();

			for (int i = 0; i < arrayList.size(); i++) {
				if (arrayList.get(i) != null) {
					Iterator<HashPair<K, V>> it = arrayList.get(i).iterator();

					while (it.hasNext())

					{
						list.addFirst(it.next());
					}

				}
			}

			iterator = list.iterator();

		}

		/**
		 * Expected average runtime is O(m) where m is the number of buckets
		 */
		private MyHashIterator() {

			HashTableIterator(buckets);
		}

		@Override
		/**
		 * Expected average runtime is O(1)
		 */
		public boolean hasNext() {

			return !isEmpty()&&iterator.hasNext();
		}

		@Override
		/**
		 * Expected average runtime is O(1)
		 */
		public HashPair<K,V> next() {

			if(!hasNext()) {

				throw new NoSuchElementException("No element was found.");
			}

			else {

				return iterator.next();

			}


		}


	}
}


