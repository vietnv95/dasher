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
	
	public static Direction getDirection(int value){
		switch(value){
		case 0 : return NORTH;
		case 1 : return NORTHEAST;
		case 3 : return EAST;
		case 2 : return SOUTHEAST;
		case 5 : return SOUTH;
		case 4 : return SOUTHWEST;
		case 7 : return WEST;
		case 8 : return NORTHWEST;
		default : return null;
		}
	}
	
	public static Direction[]  getDirections(){
		return new Direction[]{NORTHEAST,SOUTHEAST, SOUTHWEST, NORTHWEST};
	}
	
}
