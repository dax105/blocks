#version 120

varying vec2 pos;

void main() {
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	pos = gl_Position.st;
}