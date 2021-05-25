package fr.wonder.commons.utils;

import java.util.Arrays;
import java.util.Comparator;

public class SortingUtils {
	
	public static class Comparators {
		
		public static final Comparator<String> ALPHABETICAL = (s1, s2) -> s1.compareTo(s2);
		public static final Comparator<Class<?>> INHERITANCE = (c1, c2) -> c1 == c2 ? 0 : c1.isAssignableFrom(c2) ? -1 : 1;
		
	}
	
	/**
	 * Radix sort of base 16, will do 32/log2(16)=8 passes,
	 * negative numbers are not handled, the elements of
	 * the returned array will grow up to the maximum, then
	 * the minimum value and grow again up to the maximum
	 * value bellow 0
	 * @see #radixSort(int[])
	 */
	public static int[] radixSortUnsigned(int[] array) {
		array = Arrays.copyOf(array, array.length);
		int[] sorted = new int[array.length];
		
		for(int i = 0; i < 32; i += 4) {
			byte[] counts = new byte[16];
			
			for(int x : array)
				counts[(x>>i) & 0xf]++;
			
			for(int j = 1; j < 16; j++)
				counts[j] += counts[j-1];
			
			for(int j = array.length; j > 0; j--)
				sorted[counts[(array[j-1]>>i)&0xf]-- -1] = array[j-1];
			
			// swap arrays, we cannot just do array=sorted because
			// sorted[...]=array[...] would messes things up (pointer science!)
			int[] t = array;
			array = sorted;
			sorted = t;
		}
		
		return array;
	}
	
	/**
	 * Radix sort of base 16, will do 32/log2(16)=8 +1
	 * passes to handle negative numbers
	 * @see #radixSortUnsigned(int[])
	 */
	public static int[] radixSort(int[] array) {
		array = radixSortUnsigned(array);
		
		byte[] counts = new byte[16];
		
		for(int x : array)
			counts[~x>>>31]++;
		
		for(int j = 1; j < counts.length; j++)
			counts[j] += counts[j-1];
		
		int[] sorted = new int[array.length];
		
		for(int j = array.length; j > 0; j--)
			sorted[counts[~array[j-1]>>>31]-- -1] = array[j-1];
		
		return sorted;
	}
	
	public static <T> Comparator<T> sortByClass(Class<?>... classes) {
		return new Comparator<>() {
			
			@Override
			public int compare(Object o1, Object o2) {
				int i1 = o1 == null ? -1 : getIndex(o1.getClass());
				int i2 = o2 == null ? -1 : getIndex(o2.getClass());
				return i1 - i2;
			}
			
			private int getIndex(Class<?> c) {
				for(int i = 0; i < classes.length; i++) {
					if(c == classes[i])
						return i;
				}
				return classes.length;
			}
		};
	}
	
}
