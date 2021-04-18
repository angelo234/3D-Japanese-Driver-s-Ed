#version 330 core

const int lights = 4;

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoords;
layout (location = 2) in vec3 normal;

out vec2 passTextureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector[lights];
out vec3 toCameraVector;
out vec4 shadowCoords;
out float visibility;


uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 toShadowMapSpace;
uniform vec3 lightPosition[lights];

uniform float useFakeLighting;
uniform float numberOfRows;

uniform vec2 offset;

uniform bool doShadowMapping;

const float density = 0.0035;
const float gradient = 5.0;
const float shadowDistance = 50;
const float transitionDistance = 10.0;

uniform vec4 plane;

void main(void){

	vec4 worldPosition = transformationMatrix * vec4(position,1.0);
	
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	
	gl_Position = projectionMatrix * positionRelativeToCam;
	
	gl_ClipDistance[0] = dot(worldPosition, plane);
	
	passTextureCoords = (textureCoords/numberOfRows) + offset;

	vec3 actualNormal = normal;
	if(useFakeLighting > 0.5){
		actualNormal = vec3(0.0,1.0,0.0);
	}

	surfaceNormal = (transformationMatrix * vec4(actualNormal,0.0)).xyz;
	for(int i=0;i<lights;i++){
		toLightVector[i] = lightPosition[i] - worldPosition.xyz;
	}
	
	toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz;
	
	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow((distance*density),gradient));
	visibility = clamp(visibility,0.0,1.0);
	
	if(doShadowMapping){
		distance = distance - (shadowDistance - transitionDistance);
		distance = distance / transitionDistance;
		
		shadowCoords = toShadowMapSpace * worldPosition;
		shadowCoords.w = clamp(1.0-distance, 0.0, 1.0);
	}
	else{
		distance = distance - transitionDistance;
		distance = distance / transitionDistance;
	}
	
}