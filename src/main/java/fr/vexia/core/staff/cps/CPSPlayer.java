package fr.vexia.core.staff.cps;

public class CPSPlayer {

    private int maxRight = 0;
    private int maxLeft = 0;
    private int rightClicks = 0;
    private int leftClicks = 0;
    private int alertLeft = 0;
    private int alertRight = 0;

    public void addLeftClick() {
        this.leftClicks++;
    }

    public void addRightClick() {
        this.rightClicks++;
    }

    public int getRightClicks() {
        return this.rightClicks;
    }

    public int getLeftClicks() {
        return this.leftClicks;
    }

    public void resetLeftClicks() {
        this.leftClicks = 0;
    }

    public void resetRightClicks() {
        this.rightClicks = 0;
    }

    public int getAlertLeft() {
        return this.alertLeft;
    }

    public int getAlertRight() {
        return this.alertRight;
    }

    public void addAlertLeft() {
        this.alertLeft++;
    }

    public void addAlertRight() {
        this.alertRight++;
    }

    public int getMaxLeft() {
        return maxLeft;
    }

    public int getMaxRight() {
        return maxRight;
    }

    public void setMaxLeft(int maxLeft) {
        this.maxLeft = maxLeft;
    }

    public void setMaxRight(int maxRight) {
        this.maxRight = maxRight;
    }
}
