#version 120

//======================
//= EDITABLE VARIABLES =
//======================

//#define ENABLE_FOG
#define FOG_RAMP 30.0

//=============================
//= END OF EDITABLE VARIABLES =
//=============================

varying vec4 color;
varying vec4 vposition;
varying float vdist;
varying float blockidinterp;

uniform sampler2D sampler;

uniform float time;
uniform float fog;
uniform float texture;
uniform float fogDist;

void main() {

    if (texture > 0) {
    	if (blockidinterp > 11.5 && blockidinterp < 12.5) {
    		gl_FragColor = color * texture2D(sampler, gl_TexCoord[0].st);
    	} else {
    		gl_FragColor = color * texture2D(sampler, gl_TexCoord[0].st);
    	}	
    } else {
    	gl_FragColor = color;
    }	
    
    #ifdef ENABLE_FOG
    
    if (fog > 0) {    
    	float depth = vdist;
    	float fogFactor = smoothstep(fogDist-FOG_RAMP, fogDist, depth);
    	gl_FragColor = mix(gl_FragColor, vec4(gl_Fog.color.rgb, mix(gl_FragColor.a, gl_Fog.color.a, fogFactor)), fogFactor);
    }	
    
    #endif
    
}