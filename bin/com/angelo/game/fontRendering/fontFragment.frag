#version 330 core

in vec2 passTextureCoords;

out vec4 outColor;

uniform vec3 color;
uniform sampler2D fontAtlas;

const float width = 0.5;
const float edge = 0.1;

const float borderWidth = 0.0;
const float borderEdge = 0.4;

const vec2 offset = vec2(0.0, 0.0);

const vec3 outlineColor = vec3(0.2,0.2,0.2);

void main(void){

	float distance = 1.0 - texture(fontAtlas, passTextureCoords).a;
	float alpha = 1.0 - smoothstep(width, width + edge, distance);
	
	float distance2 = 1.0 - texture(fontAtlas, passTextureCoords + offset).a;
	float outlineAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distance2);
	
	float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
	vec3 overAllColor = mix(outlineColor, color, alpha / overallAlpha);
	
	outColor = vec4(overAllColor, overallAlpha);

}