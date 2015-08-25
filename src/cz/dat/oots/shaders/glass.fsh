#version 120

uniform sampler2D sampler_bg;

uniform float time;

varying vec2 pos;

void main() {
	
	vec4 bg = texture2D(sampler_bg, vec2(gl_FragCoord.x/640.0+sin(time)*0.01, gl_FragCoord.y/480.0));
	
	vec4 bgi = vec4(1.0)-bg;
	
	gl_FragColor = bgi.bgra;
}