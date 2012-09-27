package com.phamkhanh.object;

import java.awt.Point;
import com.phamkhanh.mapengine.Direction;
import com.phamkhanh.mapengine.MapEngine;

/**
 * Đối tượng ObjectPlayer thiết kế dưới mẫu singleton (duy nhất một Instance cho tất cả Conveyer).
 * Thực hiện tạo animation cho Conveyer dựa trên hướng của Conveyer
 * Cách làm : Conveyer bao gồm ảnh chính nó và ảnh một thanh ngang chuyển động trên bề mặt nó
 *            Vị trí ảnh Conveyer cố định, nhưng vị trí thanh ngang nằm trên Conveyer thay đổi
 *            theo thời gian và theo hướng,thể hiện bởi offset
 * @author Khanh
 *
 */
public class ObjectPlayer {
	private int speed;     // Tốc độ chuyển động (tile/s)

	private int seqDuration; // ms Thời gian chuyển động hết một ô tile
	private int showPeriod;  // ms Thời gian chuyển động một bước nhỏ
	private int totalStep;   // Tổng số bước cần để chuyển động hết một ô tile
	private int step;        // Bước hiện tại (0 -> totalStep-1)

	private int animPeriod;   // Chu kì một vòng lặp game
	private long animTotalTime;   // ms Thời gian từ lúc bắt đầu chuyển động xét trong một chu kì seqDuration
	private Point ptDelta;   // Độ lệch giữa 2 bước (pixel)
							
	private Point offset;
	private static ObjectPlayer instance;

	public static ObjectPlayer getInstance() {
		if (instance == null) {
			instance = new ObjectPlayer(2);
		}
		return instance;
	}

	private ObjectPlayer(int speed) {
		this.speed = speed;
		this.seqDuration = (int) (1000 / speed);
		
		this.ptDelta = new Point(4, 2);
		this.totalStep = (int) (MapEngine.TILEWIDTH / (2 * ptDelta.x));
		
		this.showPeriod = (int) (seqDuration / this.totalStep);
		this.animPeriod = (int) (1000 / MapEngine.FPS);
		this.animTotalTime = 0L;

		this.step = 0;

		this.offset = new Point();
	}

	/**
	 *  Thực hiện update tọa độ offset,gọi mỗi vòng lặp game
	 *  Sau nhiều lần gọi updateStick, offset mới thay đổi,vì thời gian vòng lặp game rất ngắn
	 */
	public void updateStick() {
		// Xác định thời gian từ lúc bắt đầu chuyển động trong một tile xét một chu kì seqDuration
		animTotalTime = (animTotalTime + animPeriod) % seqDuration; 
		
		// Sau nhiều lần updateStick, step mới tăng
		step = (int) animTotalTime / showPeriod; 

	}

	/**
	 * Thực hiện việc tính toán offset theo hướng chuyển động của Conveyer
	 *  Do đó,offset có thể âm
	 * @param direction Hướng chuyển động hiện tại của Conveyer
	 * @return
	 */
	public Point getOffset(Direction direction) {
		if (direction == Direction.SOUTHEAST) {
			offset.setLocation(step * ptDelta.x, step * ptDelta.y);
		} else if (direction == Direction.SOUTHWEST) {
			offset.setLocation(-step * ptDelta.x, step * ptDelta.y);
		} else if (direction == Direction.NORTHEAST) {
			offset.setLocation(step * ptDelta.x, -step * ptDelta.y);
		} else if (direction == Direction.NORTHWEST) {
			offset.setLocation(-step * ptDelta.x, -step * ptDelta.y);
		}
		return offset;
	}

	public Point getPtDelta() {
		return ptDelta;
	}

	public void setPtDelta(Point ptDelta) {
		this.ptDelta = ptDelta;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
		seqDuration = (int) (1000 / speed);
	}
}