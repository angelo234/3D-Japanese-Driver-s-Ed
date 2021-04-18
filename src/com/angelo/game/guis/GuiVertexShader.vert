#version 330 core

in vec2 position;
in vec2 textureCoords;
out vec2 passTextureCoords;

uniform mat4 transformationMatrix;

void main(void){

	gl_Position = vec4(-1.0 + 2.0 * position.x, 1.0 - 2.0 * position.y, 0.0, 1.0);
	passTextureCoords = textureCoords;
	//textureCoords = vec2(position.x,position.y);
	//gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
	//textureCoords = vec2((position.x+1.0)/2.0,1 - (position.y+1.0)/2.0);
}