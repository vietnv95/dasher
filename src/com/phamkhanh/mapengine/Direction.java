package com.phamkhanh.mapengine;

public enum Direction {
	NORTH(0), NORTHEAST(1), EAST(3), SOUTHEAST(2), SOUTH(5), SOUTHWEST(4), WEST(7), NORTHWEST(8);
	private int value;
	Direction(int value){
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}
