package fr.wonder.commons.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

public class MapOperator {
	
	public static <T, K, V> Map<K, V> map(Collection<T> collection, Function<T, K> keyGen, Function<T, V> valGen) {
		Map<K, V> map = new HashMap<>(collection.size());
		for(T v : collection)
			map.put(keyGen.apply(v), valGen.apply(v));
		return map;
	}

	public static <K, V> Map<K, V> mapKeys(Collection<K> collection, Function<K, V> valGen) {
		return map(collection, Function.identity(), valGen);
	}
	
	public static <K, T> Map<K, T> mapValues(Collection<T> collection, Function<T, K> keyGen) {
		return map(collection, keyGen, Function.identity());
	}
	
	public static <K, V> Map<K, V> map(K[] keys, V[] values) {
		if(keys.length != values.length)
			throw new IllegalArgumentException("Keys and values have different lengths");
		Map<K, V> map = new HashMap<>();
		for(int i = 0; i < keys.length; i++)
			map.put(keys[i], values[i]);
		return map;
	}
	
	public static <K, V> Map<K, V> map(Collection<? extends K> keys, Collection<? extends V> values) {
		if(keys.size() != values.size())
			throw new IllegalArgumentException("Keys and values have different lengths");
		Map<K, V> map = new HashMap<>();
		Iterator<? extends K> ki = keys.iterator();
		Iterator<? extends V> vi = values.iterator();
		while(ki.hasNext())
			map.put(ki.next(), vi.next());
		return map;
	}
	
	public static <K, V> Map<K, V> reverse(Map<V, K> map) {
		Map<K, V> newMap = new HashMap<>();
		for(Entry<V, K> e : map.entrySet())
			newMap.put(e.getValue(), e.getKey());
		return newMap;
	}
	
}
