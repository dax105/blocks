#version 120

uniform sampler2D sampler;

vec4 hejl(vec4 color) {
	vec4 x = max(vec4(0.0), color - vec4(0.004));
	return (x * (6.2 * x + 0.5)) / (x * (6.2 * x + 1.7) + 0.06);
}

void main() {
	float vignette = vec2(1.0) - length(vec2(0.5) - gl_TexCoord[0].st) / length(vec2(0.5));
	vignette = pow(vignette, 0.1);
	
	const float exposure = 1.25;
	vec4 color = texture2D(sampler, gl_TexCoord[0].st)*exposure;
	
	gl_FragColor = vec4(color.rgb*hejl(color).rgb*vignette, 1.0);
}