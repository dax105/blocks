package cz.dat.oots.util;

public class Rectangle {
    private Coord2D position;
    private int width;
    private int height;

    public Rectangle(int x, int y, int width, int height) {
        this.position = new Coord2D(x, y);
        this.width = width;
        this.height = height;
    }

    public void set(int x, int y, int width, int height) {
        this.position.x = x;
        this.position.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return this.position.x;
    }

    public int getY() {
        return this.position.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Coord2D getPosition() {
        return this.position;
    }

    public int getTopRight() {
        return this.getX() + this.getWidth();
    }

    public int getBottomLeft() {
        return this.getY() + this.getHeight();
    }

    public boolean intersects(Rectangle other) {
        return !(this.getX() > other.getTopRight()
                || other.getX() > this.getTopRight()
                || this.getY() > other.getBottomLeft() || other.getY() > this
                .getBottomLeft());
    }

    public boolean isInRectangle(Coord2D point) {
        return (point.x > this.getX() && point.x < this.getTopRight()
                && point.y > this.getY() && point.y < this.getBottomLeft());
    }

    @Override
    public int hashCode() {
        return this.position.hashCode() + this.width * 31 + this.height;
    }

    @Override
    public boolean equals(Object ob) {
        if (ob instanceof Rectangle) {
            Rectangle r = (Rectangle) ob;

            return (r.getPosition().equals(this.getPosition())
                    && r.getWidth() == this.getWidth() && r.getHeight() == this
                    .getHeight());
        }

        return false;
    }
}
