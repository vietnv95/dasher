package com.phamkhanh.mapengine;

import java.awt.Point;

/**
 * Staggered isometric tile map engine
 * 
 * @author Khanh
 * 
 */
public class MapEngine {
	public static final int FPS = 40;
	public static final int TILEWIDTH = 32;
	public static final int TILEHEIGHT = 16;
	private static int[][] mouseMapTable = getMouseMapTable(TILEWIDTH,
			TILEHEIGHT);

	/**
	 * Exchange world map ordinate to pixel ordinate
	 * 
	 * @param ptMap
	 *            World map ordinate by tile
	 * @return pixel Ordinate of world map by pixel
	 */
	public static Point tilePlotter(Point ptMap) {
		Point returnPoint = new Point();
		returnPoint.x = ptMap.x * TILEWIDTH + (ptMap.y & 1) * (TILEWIDTH / 2);
		returnPoint.y = ptMap.y * (TILEHEIGHT / 2);
		return returnPoint;
	}

	/**
	 * Change map ordinate by direction
	 * 
	 * @param ptMap
	 *            Orginal map ordinate
	 * @param direction
	 *            Direction to move near tile
	 * @return After map ordinate
	 */
	public static Point tileWalker(Point ptMap, Direction direction) {
		if (direction == Direction.EAST)
			ptMap.x++;
		else if (direction == Direction.WEST)
			ptMap.x--;
		else if (direction == Direction.NORTH)
			ptMap.y -= 2;
		else if (direction == Direction.SOUTH)
			ptMap.y += 2;
		else if (direction == Direction.NORTHWEST) {
			ptMap.x = (ptMap.x - 1 + (ptMap.y & 1));
			ptMap.y--;
		} else if (direction == Direction.NORTHEAST) {
			ptMap.x = (ptMap.x + (ptMap.y & 1));
			ptMap.y--;
		} else if (direction == Direction.SOUTHEAST) {
			ptMap.x = (ptMap.x + (ptMap.y & 1));
			ptMap.y++;
		} else if (direction == Direction.SOUTHWEST) {
			ptMap.x = (ptMap.x - 1 + (ptMap.y & 1));
			ptMap.y++;
		}
		return ptMap;
	}

	/**
	 * Exchange pixel ordinate to map ordinate
	 * 
	 * @param ptMouse
	 *            Mouse ordinate by pixel
	 * @return Map ordinate by tile
	 */
	public static Point mouseMap(Point ptMouse) {
		// Chia ban do thanh cac o hinh chu nhat, tim xem chuot nam trong o hinh
		// chu nhat nao.
		Point ptMouseMapCoarse = new Point();
		ptMouseMapCoarse.x = ptMouse.x / TILEWIDTH;
		ptMouseMapCoarse.y = ptMouse.y / TILEHEIGHT;

		// Trong o hinh chu nhat, toa do cua chuot so voi top-left conner.
		Point ptMouseMapFine = new Point();
		ptMouseMapFine.x = ptMouse.x % TILEWIDTH;
		ptMouseMapFine.y = ptMouse.y % TILEHEIGHT;
		// Adjust for negative fine coordinates
		if (ptMouseMapFine.x < 0) {
			ptMouseMapFine.x += TILEWIDTH;
			ptMouseMapCoarse.x--;
		}
		if (ptMouseMapFine.y < 0) {
			ptMouseMapFine.y += TILEHEIGHT;
			ptMouseMapCoarse.y--;
		}
		Point ptMap = new Point(0, 0);

		// Tim toa do tile nam giua hinh chu nhat
		for (int i = 0; i < ptMouseMapCoarse.x; i++) {
			ptMap = tileWalker(ptMap, Direction.EAST);
		}
		for (int i = 0; i < ptMouseMapCoarse.y; i++) {
			ptMap = tileWalker(ptMap, Direction.SOUTH);
		}

		// Xac dinh vi tri cua chuot nam trong tile nao so voi tile trung tam
		// trong hinh chu nhat
		switch (mouseMapTable[ptMouseMapFine.x][ptMouseMapFine.y]) {
		case MouseMapDirection.CENTER:
			break;
		case MouseMapDirection.NORTHEAST:
			ptMap = tileWalker(ptMap, Direction.NORTHEAST);
			break;
		case MouseMapDirection.NORTHWEST:
			ptMap = tileWalker(ptMap, Direction.NORTHWEST);
			break;
		case MouseMapDirection.SOUTHEAST:
			ptMap = tileWalker(ptMap, Direction.SOUTHEAST);
			break;
		case MouseMapDirection.SOUTHWEST:
			ptMap = tileWalker(ptMap, Direction.SOUTHWEST);
			break;
		}

		return ptMap;
	}

	/**
	 * Tra ve huong tu ptHead den ptTail
	 * 
	 * @param ptHead
	 *            Toa do o bat dau
	 * @param ptTail
	 *            Toa do o ket thuc
	 * @return Huong can tim
	 */
	public static Direction tileDirecter(final Point ptHead, final Point ptTail) {
		Point ptHeadPixel = MapEngine.tilePlotter(ptHead);
		Point ptTailPixel = MapEngine.tilePlotter(ptTail);
		int deltaX = ptTailPixel.x - ptHeadPixel.x;
		int deltaY = ptTailPixel.y - ptHeadPixel.y;

		if (deltaX == 0 && deltaY == 0)
			return Direction.SOUTHEAST;
		if (deltaX == 0) {
			if (deltaY > 0)
				return Direction.SOUTH;
			else if (deltaY < 0)
				return Direction.NORTH;
		}
		if (deltaY == 0) {
			if (deltaX > 0)
				return Direction.EAST;
			else if (deltaX < 0)
				return Direction.WEST;
		}
		if (deltaX != 0 && deltaY != 0) {
			if (Math.abs(deltaX) * TILEHEIGHT == Math.abs(deltaY) * TILEWIDTH) {
				if (deltaX > 0 && deltaY > 0)
					return Direction.SOUTHEAST;
				else if (deltaX > 0 && deltaY < 0)
					return Direction.NORTHEAST;
				else if (deltaX < 0 && deltaY > 0)
					return Direction.SOUTHWEST;
				else if (deltaX < 0 && deltaY < 0)
					return Direction.NORTHWEST;
			}
		}

		return null;
	}
	
	// Tra ve huong nguoc lai cua tham so
	public static Direction reverseDirection(Direction direct) {
		if (direct == Direction.NORTHEAST)
			return Direction.SOUTHWEST;
		if (direct == Direction.SOUTHWEST)
			return Direction.NORTHEAST;
		if (direct == Direction.SOUTHEAST)
			return Direction.NORTHWEST;
		if (direct == Direction.NORTHWEST)
			return Direction.SOUTHEAST;
		if(direct == Direction.NORTH)
			return Direction.SOUTH;
		if(direct == Direction.SOUTH)
			return Direction.NORTH;
		if(direct == Direction.WEST)
			return Direction.EAST;
		if(direct == Direction.EAST)
			return Direction.WEST;
		return null;
	}

	private static class MouseMapDirection {
		private static final int CENTER = 0;
		private static final int NORTHWEST = 1;
		private static final int NORTHEAST = 2;
		private static final int SOUTHEAST = 3;
		private static final int SOUTHWEST = 4;
	}

	private static int[][] getMouseMapTable(int tileWidth, int tileHeight) {
		int[][] mouseMapTable = new int[tileWidth][tileHeight];
		int a = tileWidth / 2;
		int b = tileHeight / 2;
		for (int i = 0; i < tileWidth; i++) {
			for (int j = 0; j < tileHeight; j++) {
				if ((b * i + a * j - a * b) >= 0
						&& (-b * i + a * j + a * b >= 0)
						&& (b * i + a * j - 3 * a * b <= 0)
						&& (b * i - a * j + a * b >= 0)) {
					mouseMapTable[i][j] = MouseMapDirection.CENTER;
				} else {
					if (i < a && j < b)
						mouseMapTable[i][j] = MouseMapDirection.NORTHWEST;
					if (i < a && j > b)
						mouseMapTable[i][j] = MouseMapDirection.SOUTHWEST;
					if (i > a && j < b)
						mouseMapTable[i][j] = MouseMapDirection.NORTHEAST;
					if (i > a && j > b)
						mouseMapTable[i][j] = MouseMapDirection.SOUTHEAST;
				}
			}
		}
		return mouseMapTable;
	}

	// Test
	public static void main(String[] args) {
		System.out.println(MapEngine.tileWalker(new Point(1, 2),
				Direction.SOUTHWEST));
		System.out.println(MapEngine.mouseMap(new Point(100, 200)));
	}
}
