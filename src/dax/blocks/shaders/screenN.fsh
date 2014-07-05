varying vec3 normal;
uniform sampler2D texture1;

void main ()
{
const vec4 AmbientColor = vec4(0.1, 0.0, 0.0, 1.0);
const vec4 DiffuseColor = vec4(1.0, 0.0, 0.0, 1.0);

vec3 normalized_normal = normalize(normal);

vec4 color = vec4(1.0, 1.0, 1.0, 1.0);
gl_FragColor = gl_Color * color * texture2D(texture1, gl_TexCoord[0].st);
}