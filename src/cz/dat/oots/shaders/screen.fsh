#version 120
#define MAX_COLOR_RANGE 48.0

uniform sampler2D sampler_base;
uniform sampler2D sampler_rain;
uniform float time;

vec4 hejl(vec4 color) {
	vec4 x = max(vec4(0.0), color - vec4(0.004));
	return (x * (6.2 * x + 0.5)) / (x * (6.2 * x + 1.7) + 0.06);
}

float A = 0.15;
float B = 0.2;
float C = 0.1;
float D = 0.2;
float E = 0.02;
float F = 0.3;
float W = MAX_COLOR_RANGE;

vec3 Uncharted2Tonemap(vec3 x) {
	return ((x*(A*x+C*B)+D*E)/(x*(A*x+B)+D*F))-E/F;
}

void main() {

	const float pi = 3.14159265359;
	
	vec2 texcoord = gl_TexCoord[0].st;	

	vec2 newTC = texcoord.st;
	vec3 color = pow(texture2D(sampler_base, newTC).rgb*vec3(0.48),vec3(2.2))*MAX_COLOR_RANGE;
	
	vec3 curr = Uncharted2Tonemap(color);
	vec3 whiteScale = 1.0f/Uncharted2Tonemap(vec3(W));
	
	color = curr*whiteScale; 
	color = (((color - color.rgb )*1.04)+color.rgb) ;
	color /= 1.04;

	color = pow(color,vec3(1.0/1.85));
	
	gl_FragColor = vec4(color, 1.0);
}