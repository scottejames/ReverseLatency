package scott.latency.misc;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Really need to write some java doc on this class as its a bit fruity but delivery 
 * first
 * 
 * @author sjames1
 *
 * @param <E>
 */
public class ThreadSafeList<E> implements Collection<E>,Serializable{

	private LinkedList<E> readList = new LinkedList<E>();
	private LinkedList<E> writeList = new LinkedList<E>();
	
	public void rollList()
	{
		LinkedList<E> newList = new LinkedList<E>();
		synchronized (this) {
			readList = writeList;
			writeList = newList;			
		}		
	}
	public E get(int i)
	{
		return readList.get(i);
	}
	
	public LinkedList<E> getAll()
	{
		return readList;
	}
	public synchronized boolean add(E o) {
		return writeList.add(o);
	}

	public synchronized boolean addAll(Collection<? extends E> c) {
		return writeList.addAll(c);		
	}

	public synchronized void clear() {
		writeList.clear();
	}

	public synchronized boolean remove(Object o) {
		return writeList.remove(o);
	}

	public synchronized boolean removeAll(Collection<?> c) {
		return writeList.removeAll(c);
	}

	public synchronized boolean retainAll(Collection<?> c) {
		return writeList.retainAll(c);		
	}

	
	public boolean contains(Object o) {
		return readList.contains(o);		
	}

	public boolean containsAll(Collection<?> c) {
		return readList.contains(c);
	}

	public boolean isEmpty() {
		return readList.isEmpty();
	}

	public Iterator<E> iterator() {
		return readList.iterator();
	}

	
	public int size() {
		return readList.size();
	}

	public Object[] toArray() {

		return readList.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return readList.toArray(a);
	}
	
	
}
