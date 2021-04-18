#version 330 core

const int lights = 4;

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 passTextureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector[lights];
out vec3 toCameraVector;
out float visibility;
out vec4 shadowCoords;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 toShadowMapSpace;
uniform vec3 lightPosition[lights];
uniform vec4 plane;
uniform bool doShadowMapping;

const float density = 0.0025;
const float gradient = 5;
const float shadowDistance = 50;
const float transitionDistance = 10.0;

void main(void){

	vec4 worldPosition = transformationMatrix * vec4(position,1.0);

	gl_ClipDistance[0] = dot(worldPosition, plane);
	
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;
	passTextureCoords = textureCoords;

	surfaceNormal = (transformationMatrix * vec4(normal,0.0)).xyz;
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