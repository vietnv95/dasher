package com.phamkhanh.object;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.phamkhanh.exception.MapErrorException;
import com.phamkhanh.image.ImageLoader;
import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;

public class Conveyer extends Cell {
	public static final int CONVEYER = 0;
	public static final int TURN_LEFT = 1;
	public static final int TURN_RIGHT = 2;

	private Direction direction;
	private int type;

	private static int speed = 2;
	private static ConveyerPlayer player = new ConveyerPlayer();

	public Conveyer() {
	}

	public Conveyer(Point ptMap, Direction direction) {
		super(ptMap);
		this.direction = direction;
		this.type = CONVEYER;
	}

	public Conveyer(Point ptMap, Direction direction, int type) {
		this(ptMap, direction);
		this.type = type;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static void setSpeed(int speed) {
		Conveyer.speed = speed;
		player.seqDuration = (int) (1000 / speed);
		player.showPeriod = (int) (player.seqDuration / player.totalStep);
	}

	public static void update() {
		player.updateStick();
	}

	@Override
	public void draw(Graphics2D g) {
		Point ptTile = MapEngine.tilePlotter(getPtMap());
		g.drawImage(ImageLoader.getImage("conveyer.png"), ptTile.x - 4,
				ptTile.y - 2, null);
	}

	public void drawLine(Graphics2D g) {
		Point ptTile = MapEngine.tilePlotter(getPtMap());
		if (type == CONVEYER)
			drawLineConveyer(g, ptTile);
		else
			drawLineBranch(g, ptTile);
	}

	private void drawLineConveyer(Graphics2D g, Point ptTile) {
		// direction == null trong trường hợp tileSelected
		if (direction == Direction.NORTHWEST) {
			g.drawImage(ImageLoader.getImage("linenw.png"), ptTile.x
					- player.offsetX, ptTile.y - player.offsetY, null);
		} else if (direction == Direction.NORTHEAST) {
			g.drawImage(ImageLoader.getImage("linene.png"), ptTile.x
					+ player.offsetX, ptTile.y - player.offsetY, null);
		} else if (direction == Direction.SOUTHEAST) {
			g.drawImage(ImageLoader.getImage("linese.png"), ptTile.x
					+ player.offsetX, ptTile.y + player.offsetY, null);
		} else if (direction == Direction.SOUTHWEST) {
			g.drawImage(ImageLoader.getImage("linesw.png"), ptTile.x
					- player.offsetX, ptTile.y + player.offsetY, null);
		}
	}

	private void drawLineBranch(Graphics2D g, Point ptTile) {
		AffineTransform origin = g.getTransform();
		// Ve anh cho nga re phai
		if (type == TURN_RIGHT) {
			if (direction == Direction.SOUTHEAST) {
				g.drawImage(
						ImageLoader.getImage("crossse" + player.step + ".png"),
						ptTile.x, ptTile.y, null);
			} else if (direction == Direction.NORTHWEST) {
				g.translate(ptTile.x + MapEngine.TILEWIDTH / 2, ptTile.y
						+ MapEngine.TILEHEIGHT / 2);
				g.rotate(Math.PI);
				g.drawImage(
						ImageLoader.getImage("crossse" + player.step + ".png"),
						-MapEngine.TILEWIDTH / 2, -MapEngine.TILEHEIGHT / 2,
						null);
				g.setTransform(origin);
			} else if (direction == Direction.SOUTHWEST) {
				g.drawImage(
						ImageLoader.getImage("crosssw" + player.step + ".png"),
						ptTile.x, ptTile.y, null);
			} else if (direction == Direction.NORTHEAST) {
				g.translate(ptTile.x + MapEngine.TILEWIDTH / 2, ptTile.y
						+ MapEngine.TILEHEIGHT / 2);
				g.rotate(Math.PI);
				g.drawImage(
						ImageLoader.getImage("crosssw" + player.step + ".png"),
						-MapEngine.TILEWIDTH / 2, -MapEngine.TILEHEIGHT / 2,
						null);
				g.setTransform(origin);
			}

		}// Ve anh cho nga re phai
		else if (type == TURN_LEFT) {
			if (direction == Direction.NORTHEAST) {
				drawHorizFlip(g,
						ImageLoader.getImage("crossse" + player.step + ".png"),
						ptTile.x, ptTile.y);
			} else if (direction == Direction.SOUTHWEST) {
				g.translate(ptTile.x + MapEngine.TILEWIDTH / 2, ptTile.y
						+ MapEngine.TILEHEIGHT / 2);
				g.rotate(Math.PI);
				drawHorizFlip(g,
						ImageLoader.getImage("crossse" + player.step + ".png"),
						-MapEngine.TILEWIDTH / 2, -MapEngine.TILEHEIGHT / 2);
				g.setTransform(origin);
			} else if (direction == Direction.NORTHWEST) {
				drawHorizFlip(g,
						ImageLoader.getImage("crosssw" + player.step + ".png"),
						ptTile.x, ptTile.y);
			} else if (direction == Direction.SOUTHEAST) {
				g.translate(ptTile.x + MapEngine.TILEWIDTH / 2, ptTile.y
						+ MapEngine.TILEHEIGHT / 2);
				g.rotate(Math.PI);
				drawHorizFlip(g,
						ImageLoader.getImage("crosssw" + player.step + ".png"),
						-MapEngine.TILEWIDTH / 2, -MapEngine.TILEHEIGHT / 2);
				g.setTransform(origin);
			}

		}

	}

	public void drawHorizFlip(Graphics2D g, BufferedImage im, int x, int y) {
		if (im == null) {
			System.out.println("drawHorizFlip: input image is null");
			return;
		}
		int imWidth = im.getWidth();
		int imHeight = im.getHeight();
		g.drawImage(im, x, y + imHeight, x + imWidth, y, 0, 0, imWidth,
				imHeight, null);
	}

	@Override
	public String toString() {
		return "Conveyer [direction=" + direction + "," + super.toString()
				+ "]";
	}

	@Override
	public String getProperty() {
		return "2." + direction.getValue() + "-" + type;
	}

	public static Cell getInstance(String property, Point ptMap)
			throws MapErrorException {
		try {
			StringTokenizer tokens = new StringTokenizer(property, "-");
			Direction direction = Direction.getDirection(Integer
					.parseInt(tokens.nextToken()));
			int type = Integer.parseInt(tokens.nextToken());
			return new Conveyer(ptMap, direction, type);
		} catch (NoSuchElementException | NumberFormatException e) {
			throw new MapErrorException(
					"Thuộc tính của Conveyer không đúng định dạng");
		}
	}

	/**
	 * Đối tượng ObjectPlayer thiết kế dưới mẫu singleton (duy nhất một Instance
	 * cho tất cả Conveyer). Thực hiện tạo animation cho Conveyer dựa trên hướng
	 * của Conveyer Cách làm : Conveyer bao gồm ảnh chính nó và ảnh một thanh
	 * ngang chuyển động trên bề mặt nó Vị trí ảnh Conveyer cố định, nhưng vị
	 * trí thanh ngang nằm trên Conveyer thay đổi theo thời gian và theo
	 * hướng,thể hiện bởi offset
	 * 
	 * @author Khanh
	 * 
	 */
	private static class ConveyerPlayer {
		private Point ptDelta; // Độ lệch giữa 2 bước liên tiếp (pixel)
		private int totalStep; // Tổng số bước cần để chuyển động hết một ô tile
		private int animPeriod; // Chu kì một vòng lặp game

		private int seqDuration; // ms Thời gian chuyển động hết một ô tile
		private int showPeriod; // ms Thời gian chuyển động một bước nhỏ

		private int step; // Bước hiện tại (0 -> totalStep-1)
		private long animTotalTime; // ms Thời gian từ lúc bắt đầu chuyển động
									// xét trong một chu kì seqDuration

		private int offsetX;
		private int offsetY;

		public ConveyerPlayer() {
			this.ptDelta = new Point(4, 2);
			this.totalStep = (int) (MapEngine.TILEWIDTH / (2 * ptDelta.x));
			this.animPeriod = (int) (1000 / MapEngine.FPS);

			this.seqDuration = (int) (1000 / speed);
			this.showPeriod = (int) (seqDuration / this.totalStep);

			this.animTotalTime = 0L;
			this.step = 0;
		}

		/**
		 * Thực hiện update tọa độ offset,gọi mỗi vòng lặp game Sau nhiều lần
		 * gọi updateStick, offset mới thay đổi,vì thời gian vòng lặp game rất
		 * ngắn
		 */
		public void updateStick() {
			// Xác định thời gian từ lúc bắt đầu chuyển động trong một tile xét
			// một chu kì seqDuration
			animTotalTime = (animTotalTime + animPeriod) % seqDuration;

			// Sau nhiều lần updateStick, step mới tăng
			step = (int) animTotalTime / showPeriod;

			offsetX = step * ptDelta.x;
			offsetY = step * ptDelta.y;
		}

	}

}
