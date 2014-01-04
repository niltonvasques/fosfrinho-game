package com.niltonvasques.starassault.model;

import com.badlogic.gdx.utils.Array;

public class Bag {
	
	private Array<Item> items;
	private int capacity;
	
	public Bag(int capacity){
		this.capacity = capacity;
		items = new Array<Item>(capacity);
	}
	
	public Array<Item> getItems() {
		return items;
	}

	public int getCapacity() {
		return capacity;
	}
	
	public boolean addItem(Item item){
		if(items.size < capacity){
			items.add(item);
			return true;
		}
		return false;
	}

	public void clear() {
		items.clear();
	}

}
