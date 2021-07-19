package com.github.smallru8.util;

public class Pair<T1,T2> {
	public T1 first;
	public T2 second;
	
	public void makePair(T1 first,T2 second) {
		this.first = first;
		this.second = second;
	}
}