#version 120

uniform sampler2D sampler_r;
uniform sampler2D sampler_g;
uniform sampler2D sampler_b;

uniform float time;

float rand(float co){
    return fract(sin(dot(vec2(co), vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
	float offsetR = 0.005 + 0.005 * rand(gl_TexCoord[0].t*time);
	float offsetB = 0.005 + 0.005 * rand(gl_TexCoord[0].t*time*2);

	float colorR = texture2D(sampler_r, vec2(gl_TexCoord[0].s-offsetR, gl_TexCoord[0].t)).r;
	float colorG = texture2D(sampler_g, vec2(gl_TexCoord[0].s, gl_TexCoord[0].t)).g;
	float colorB = texture2D(sampler_b, vec2(gl_TexCoord[0].s+offsetB, gl_TexCoord[0].t)).b;
	
	gl_FragColor = vec4(colorR, colorG, colorB, 1.0);
}