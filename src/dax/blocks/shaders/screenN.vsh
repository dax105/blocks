varying vec3 normal;

void main()
{
gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
gl_TexCoord[0] = gl_MultiTexCoord0;
gl_FrontColor = gl_Color;  
normal = (gl_ModelViewMatrix*vec4(gl_Normal, 0.0)).xyz;
}