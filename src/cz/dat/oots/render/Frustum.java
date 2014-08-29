package cz.dat.oots.render;

import org.lwjgl.BufferUtils;

import java.nio.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Unknown, found by user from java-gaming, modified by dax105
 */
public class Frustum
{
    public static final int RIGHT   = 0;
    public static final int LEFT    = 1;
    public static final int BOTTOM  = 2;
    public static final int TOP     = 3;
    public static final int BACK    = 4;
    public static final int FRONT   = 5;

    public static final int A = 0;
    public static final int B = 1;
    public static final int C = 2;
    public static final int D = 3;

    private float[][] frustum = new float[6][4];

    private FloatBuffer modelBuffer;
    private FloatBuffer projectionBuffer;

    public Frustum() {
        this.modelBuffer = BufferUtils.createFloatBuffer(16);
        this.projectionBuffer = BufferUtils.createFloatBuffer(16);
    }

    public void normalizePlane(float[][] frustum, int side) {
        float magnitude = (float) Math.sqrt(frustum[side][A] * frustum[side][A] + frustum[side][B] * frustum[side][B] + frustum[side][C] * frustum[side][C]);

        frustum[side][A] /= magnitude;
        frustum[side][B] /= magnitude;
        frustum[side][C] /= magnitude;
        frustum[side][D] /= magnitude;
    }

    public void calculateFrustum() {
        float[] projectionMatrix = new float[16];
        float[] modelMatrix = new float[16];
        float[] clipMatrix = new float[16];

        this.projectionBuffer.rewind();
        glGetFloat(GL_PROJECTION_MATRIX, this.projectionBuffer);
        this.projectionBuffer.rewind();
        this.projectionBuffer.get(projectionMatrix);

        this.modelBuffer.rewind();
        glGetFloat(GL_MODELVIEW_MATRIX, this.modelBuffer);
        this.modelBuffer.rewind();
        this.modelBuffer.get(modelMatrix);

        clipMatrix[ 0] = modelMatrix[ 0] * projectionMatrix[ 0] + modelMatrix[ 1] * projectionMatrix[ 4] + modelMatrix[ 2] * projectionMatrix[ 8] + modelMatrix[ 3] * projectionMatrix[12];
        clipMatrix[ 1] = modelMatrix[ 0] * projectionMatrix[ 1] + modelMatrix[ 1] * projectionMatrix[ 5] + modelMatrix[ 2] * projectionMatrix[ 9] + modelMatrix[ 3] * projectionMatrix[13];
        clipMatrix[ 2] = modelMatrix[ 0] * projectionMatrix[ 2] + modelMatrix[ 1] * projectionMatrix[ 6] + modelMatrix[ 2] * projectionMatrix[10] + modelMatrix[ 3] * projectionMatrix[14];
        clipMatrix[ 3] = modelMatrix[ 0] * projectionMatrix[ 3] + modelMatrix[ 1] * projectionMatrix[ 7] + modelMatrix[ 2] * projectionMatrix[11] + modelMatrix[ 3] * projectionMatrix[15];

        clipMatrix[ 4] = modelMatrix[ 4] * projectionMatrix[ 0] + modelMatrix[ 5] * projectionMatrix[ 4] + modelMatrix[ 6] * projectionMatrix[ 8] + modelMatrix[ 7] * projectionMatrix[12];
        clipMatrix[ 5] = modelMatrix[ 4] * projectionMatrix[ 1] + modelMatrix[ 5] * projectionMatrix[ 5] + modelMatrix[ 6] * projectionMatrix[ 9] + modelMatrix[ 7] * projectionMatrix[13];
        clipMatrix[ 6] = modelMatrix[ 4] * projectionMatrix[ 2] + modelMatrix[ 5] * projectionMatrix[ 6] + modelMatrix[ 6] * projectionMatrix[10] + modelMatrix[ 7] * projectionMatrix[14];
        clipMatrix[ 7] = modelMatrix[ 4] * projectionMatrix[ 3] + modelMatrix[ 5] * projectionMatrix[ 7] + modelMatrix[ 6] * projectionMatrix[11] + modelMatrix[ 7] * projectionMatrix[15];

        clipMatrix[ 8] = modelMatrix[ 8] * projectionMatrix[ 0] + modelMatrix[ 9] * projectionMatrix[ 4] + modelMatrix[10] * projectionMatrix[ 8] + modelMatrix[11] * projectionMatrix[12];
        clipMatrix[ 9] = modelMatrix[ 8] * projectionMatrix[ 1] + modelMatrix[ 9] * projectionMatrix[ 5] + modelMatrix[10] * projectionMatrix[ 9] + modelMatrix[11] * projectionMatrix[13];
        clipMatrix[10] = modelMatrix[ 8] * projectionMatrix[ 2] + modelMatrix[ 9] * projectionMatrix[ 6] + modelMatrix[10] * projectionMatrix[10] + modelMatrix[11] * projectionMatrix[14];
        clipMatrix[11] = modelMatrix[ 8] * projectionMatrix[ 3] + modelMatrix[ 9] * projectionMatrix[ 7] + modelMatrix[10] * projectionMatrix[11] + modelMatrix[11] * projectionMatrix[15];

        clipMatrix[12] = modelMatrix[12] * projectionMatrix[ 0] + modelMatrix[13] * projectionMatrix[ 4] + modelMatrix[14] * projectionMatrix[ 8] + modelMatrix[15] * projectionMatrix[12];
        clipMatrix[13] = modelMatrix[12] * projectionMatrix[ 1] + modelMatrix[13] * projectionMatrix[ 5] + modelMatrix[14] * projectionMatrix[ 9] + modelMatrix[15] * projectionMatrix[13];
        clipMatrix[14] = modelMatrix[12] * projectionMatrix[ 2] + modelMatrix[13] * projectionMatrix[ 6] + modelMatrix[14] * projectionMatrix[10] + modelMatrix[15] * projectionMatrix[14];
        clipMatrix[15] = modelMatrix[12] * projectionMatrix[ 3] + modelMatrix[13] * projectionMatrix[ 7] + modelMatrix[14] * projectionMatrix[11] + modelMatrix[15] * projectionMatrix[15];

        // This will extract the LEFT side of the frustum
        this.frustum[Frustum.LEFT][Frustum.A] = clipMatrix[ 3] + clipMatrix[ 0];
        this.frustum[Frustum.LEFT][Frustum.B] = clipMatrix[ 7] + clipMatrix[ 4];
        this.frustum[Frustum.LEFT][Frustum.C] = clipMatrix[11] + clipMatrix[ 8];
        this.frustum[Frustum.LEFT][Frustum.D] = clipMatrix[15] + clipMatrix[12];
        this.normalizePlane(this.frustum, Frustum.LEFT);

        // This will extract the RIGHT side of the frustum
        this.frustum[Frustum.RIGHT][Frustum.A] = clipMatrix[ 3] - clipMatrix[ 0];
        this.frustum[Frustum.RIGHT][Frustum.B] = clipMatrix[ 7] - clipMatrix[ 4];
        this.frustum[Frustum.RIGHT][Frustum.C] = clipMatrix[11] - clipMatrix[ 8];
        this.frustum[Frustum.RIGHT][Frustum.D] = clipMatrix[15] - clipMatrix[12];
        this.normalizePlane(this.frustum, Frustum.RIGHT);

        // This will extract the BOTTOM side of the frustum
        this.frustum[Frustum.BOTTOM][Frustum.A] = clipMatrix[ 3] + clipMatrix[ 1];
        this.frustum[Frustum.BOTTOM][Frustum.B] = clipMatrix[ 7] + clipMatrix[ 5];
        this.frustum[Frustum.BOTTOM][Frustum.C] = clipMatrix[11] + clipMatrix[ 9];
        this.frustum[Frustum.BOTTOM][Frustum.D] = clipMatrix[15] + clipMatrix[13];
        this.normalizePlane(this.frustum, Frustum.BOTTOM);

        // This will extract the TOP side of the frustum
        this.frustum[Frustum.TOP][Frustum.A] = clipMatrix[ 3] - clipMatrix[ 1];
        this.frustum[Frustum.TOP][Frustum.B] = clipMatrix[ 7] - clipMatrix[ 5];
        this.frustum[Frustum.TOP][Frustum.C] = clipMatrix[11] - clipMatrix[ 9];
        this.frustum[Frustum.TOP][Frustum.D] = clipMatrix[15] - clipMatrix[13];
        this.normalizePlane(this.frustum, Frustum.TOP);

        // This will extract the FRONT side of the frustum
        this.frustum[Frustum.FRONT][Frustum.A] = clipMatrix[ 3] + clipMatrix[ 2];
        this.frustum[Frustum.FRONT][Frustum.B] = clipMatrix[ 7] + clipMatrix[ 6];
        this.frustum[Frustum.FRONT][Frustum.C] = clipMatrix[11] + clipMatrix[10];
        this.frustum[Frustum.FRONT][Frustum.D] = clipMatrix[15] + clipMatrix[14];
        this.normalizePlane(this.frustum, Frustum.FRONT);

        // This will extract the BACK side of the frustum
        this.frustum[Frustum.BACK][Frustum.A] = clipMatrix[ 3] - clipMatrix[ 2];
        this.frustum[Frustum.BACK][Frustum.B] = clipMatrix[ 7] - clipMatrix[ 6];
        this.frustum[Frustum.BACK][Frustum.C] = clipMatrix[11] - clipMatrix[10];
        this.frustum[Frustum.BACK][Frustum.D] = clipMatrix[15] - clipMatrix[14];
        this.normalizePlane(this.frustum, Frustum.BACK);
    }
    
    public boolean cuboidInFrustum(float x0, float y0, float z0, float x1, float y1, float z1) {
        float xsize = x1 - x0;
    	float xmin = x0;
        float xmax = x0 + xsize;
        
        float ysize = y1 - y0;
    	float ymin = y0;
        float ymax = y0 + ysize;
        
        float zsize = z1 - z0;
    	float zmin = z0;
        float zmax = z0 + zsize;
    	
    	for(int i = 0; i < 6; i++ ) {
            if(this.frustum[i][Frustum.A] * (xmin) + this.frustum[i][Frustum.B] * (ymin) + this.frustum[i][Frustum.C] * (zmin) + this.frustum[i][Frustum.D] > 0)
                continue;
            if(this.frustum[i][Frustum.A] * (xmax) + this.frustum[i][Frustum.B] * (ymin) + this.frustum[i][Frustum.C] * (zmin) + this.frustum[i][Frustum.D] > 0)
                continue;
            if(this.frustum[i][Frustum.A] * (xmin) + this.frustum[i][Frustum.B] * (ymax) + this.frustum[i][Frustum.C] * (zmin) + this.frustum[i][Frustum.D] > 0)
                continue;
            if(this.frustum[i][Frustum.A] * (xmax) + this.frustum[i][Frustum.B] * (ymax) + this.frustum[i][Frustum.C] * (zmin) + this.frustum[i][Frustum.D] > 0)
                continue;
            if(this.frustum[i][Frustum.A] * (xmin) + this.frustum[i][Frustum.B] * (ymin) + this.frustum[i][Frustum.C] * (zmax) + this.frustum[i][Frustum.D] > 0)
                continue;
            if(this.frustum[i][Frustum.A] * (xmax) + this.frustum[i][Frustum.B] * (ymin) + this.frustum[i][Frustum.C] * (zmax) + this.frustum[i][Frustum.D] > 0)
                continue;
            if(this.frustum[i][Frustum.A] * (xmin) + this.frustum[i][Frustum.B] * (ymax) + this.frustum[i][Frustum.C] * (zmax) + this.frustum[i][Frustum.D] > 0)
                continue;
            if(this.frustum[i][Frustum.A] * (xmax) + this.frustum[i][Frustum.B] * (ymax) + this.frustum[i][Frustum.C] * (zmax) + this.frustum[i][Frustum.D] > 0)
                continue;

            return false;
        }

        return true;
    }
}
