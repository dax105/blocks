package cz.dat.oots.collisions;

public class AABB {

    public float x0;
    public float y0;
    public float z0;
    public float x1;
    public float y1;
    public float z1;
    public int lastX0;
    public int lastY0;
    public int lastZ0;
    public int lastX1;
    public int lastY1;
    public int lastZ1;
    public float offsetX = 0;
    public float offsetY = 0;
    public float offsetZ = 0;
    private float epsilon = 0.0F;

    public AABB(float x0, float y0, float z0, float x1, float y1, float z1) {
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;

        this.lastX0 = (int) (x0 + 99999);
        this.lastY0 = (int) (y0 + 99999);
        this.lastZ0 = (int) (z0 + 99999);

        this.lastX1 = (int) (x0 + 99999);
        this.lastY1 = (int) (y0 + 99999);
        this.lastZ1 = (int) (z0 + 99999);
    }

    public void setOffset(float x, float y, float z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;

        this.x0 += offsetX;
        this.x1 += offsetX;

        this.y0 += offsetY;
        this.y1 += offsetY;

        this.z0 += offsetZ;
        this.z1 += offsetZ;
    }

    public void resetOffset() {
        this.x0 -= offsetX;
        this.x1 -= offsetX;

        this.y0 -= offsetY;
        this.y1 -= offsetY;

        this.z0 -= offsetZ;
        this.z1 -= offsetZ;

        this.offsetX = 0;
        this.offsetY = 0;
        this.offsetZ = 0;
    }

    public AABB expand(float xa, float ya, float za) {
        float _x0 = this.x0;
        float _y0 = this.y0;
        float _z0 = this.z0;
        float _x1 = this.x1;
        float _y1 = this.y1;
        float _z1 = this.z1;
        if (xa < 0.0F) {
            _x0 += xa;
        }

        if (xa > 0.0F) {
            _x1 += xa;
        }

        if (ya < 0.0F) {
            _y0 += ya;
        }

        if (ya > 0.0F) {
            _y1 += ya;
        }

        if (za < 0.0F) {
            _z0 += za;
        }

        if (za > 0.0F) {
            _z1 += za;
        }

        return new AABB(_x0, _y0, _z0, _x1, _y1, _z1);
    }

    public AABB grow(float xa, float ya, float za) {
        float _x0 = this.x0 - xa;
        float _y0 = this.y0 - ya;
        float _z0 = this.z0 - za;
        float _x1 = this.x1 + xa;
        float _y1 = this.y1 + ya;
        float _z1 = this.z1 + za;
        return new AABB(_x0, _y0, _z0, _x1, _y1, _z1);
    }

    public float clipXCollide(AABB c, float xa) {
        if (c.y1 > this.y0 && c.y0 < this.y1) {
            if (c.z1 > this.z0 && c.z0 < this.z1) {
                float max;
                if (xa > 0.0F && c.x1 <= this.x0) {
                    max = this.x0 - c.x1 - this.epsilon;
                    if (max < xa) {
                        xa = max;
                    }
                }

                if (xa < 0.0F && c.x0 >= this.x1) {
                    max = this.x1 - c.x0 + this.epsilon;
                    if (max > xa) {
                        xa = max;
                    }
                }

                return xa;
            } else {
                return xa;
            }
        } else {
            return xa;
        }
    }

    public float clipYCollide(AABB c, float ya) {
        if (c.x1 > this.x0 && c.x0 < this.x1) {
            if (c.z1 > this.z0 && c.z0 < this.z1) {
                float max;
                if (ya > 0.0F && c.y1 <= this.y0) {
                    max = this.y0 - c.y1 - this.epsilon;
                    if (max < ya) {
                        ya = max;
                    }
                }

                if (ya < 0.0F && c.y0 >= this.y1) {
                    max = this.y1 - c.y0 + this.epsilon;
                    if (max > ya) {
                        ya = max;
                    }
                }

                return ya;
            } else {
                return ya;
            }
        } else {
            return ya;
        }
    }

    public float clipZCollide(AABB c, float za) {
        if (c.x1 > this.x0 && c.x0 < this.x1) {
            if (c.y1 > this.y0 && c.y0 < this.y1) {
                float max;
                if (za > 0.0F && c.z1 <= this.z0) {
                    max = this.z0 - c.z1 - this.epsilon;
                    if (max < za) {
                        za = max;
                    }
                }

                if (za < 0.0F && c.z0 >= this.z1) {
                    max = this.z1 - c.z0 + this.epsilon;
                    if (max > za) {
                        za = max;
                    }
                }

                return za;
            } else {
                return za;
            }
        } else {
            return za;
        }
    }

    public boolean intersects(AABB c) {
        return c.x1 > this.x0 && c.x0 < this.x1 ? (c.y1 > this.y0
                && c.y0 < this.y1 ? c.z1 > this.z0 && c.z0 < this.z1 : false)
                : false;
    }

    public void move(float xa, float ya, float za) {
        this.x0 += xa;
        this.y0 += ya;
        this.z0 += za;
        this.x1 += xa;
        this.y1 += ya;
        this.z1 += za;
    }
}
