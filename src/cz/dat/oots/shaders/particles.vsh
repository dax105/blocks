#version 120

uniform vec2 screenSize;
uniform float spriteSize;

varying vec4 color;

vec3 unpackColor(float f) {
    vec3 color;
    color.b = floor(f / 256.0 / 256.0);
    color.g = floor((f - color.b * 256.0 * 256.0) / 256.0);
    color.r = floor(f - color.b * 256.0 * 256.0 - color.g * 256.0);
    // now we have a vec3 with the 3 components in range [0..256]. Let's normalize it!
    return color / 256.0;
}

void main() {
	vec4 eyePos = gl_ModelViewMatrix * gl_Vertex;
	vec4 projVoxel = gl_ProjectionMatrix * vec4(spriteSize,spriteSize,eyePos.z,eyePos.w);
    vec2 projSize = screenSize * projVoxel.xy / projVoxel.w;
	gl_PointSize = 0.25 * (projSize.x+projSize.y);
    gl_Position = gl_ProjectionMatrix * eyePos;
    
    color = gl_Color;
}