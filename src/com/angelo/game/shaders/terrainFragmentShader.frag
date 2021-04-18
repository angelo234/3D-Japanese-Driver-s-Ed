#version 330 core

const int lights = 4;

in vec2 passTextureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[lights];
in vec3 toCameraVector;
in float visibility;
in vec4 shadowCoords;

layout (location = 0) out vec4 outColor;
layout (location = 1) out vec4 outBrightColor;

uniform sampler2D groundTexture;
uniform sampler2D shadowMap;

uniform vec3 lightColor[lights];
uniform vec3 skyColor;
uniform vec3 attenuation[lights];
uniform float shineDamper;
uniform float reflectivity;
uniform bool doShadowMapping;

const int pcfCount = 3;
const float totalTexels = (pcfCount * 2.0 + 1.0) * (pcfCount * 2.0 + 1.0);

void main(void){
	float mapSize = 4096;
	float texelSize = 1.0 / mapSize;
	float total = 0.0;	
	float lightFactor = 0.0;
	
	if(doShadowMapping){
		for(int x=-pcfCount;x<=pcfCount;x++){
			for(int y=-pcfCount;y<=pcfCount;y++){
				float objectNearestLight = texture(shadowMap, shadowCoords.xy + vec2(x,y) * texelSize).r;
				if(shadowCoords.z > objectNearestLight + 0.002){
					total += 1.0;
				}
			}
		}	
		total /= totalTexels;
		lightFactor = 1.0 - (total * shadowCoords.w);
	}
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	vec2 tiledCoords = passTextureCoords * 1000;
	vec4 backgroundTextureColor = texture(groundTexture, tiledCoords);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	vec4 totalColor = backgroundTextureColor;
	
	for(int i=0;i<lights;i++){
	float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitNormal = normalize(surfaceNormal);
		vec3 unitLightVector = normalize(toLightVector[i]);
		float nDotl = dot(unitNormal,unitLightVector);
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		specularFactor = max(specularFactor,0.0);
		float dampedFactor = pow(specularFactor,shineDamper);
		float brightness = max(nDotl,0.0);
		totalDiffuse = totalDiffuse + (brightness * lightColor[i])/attFactor;
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColor[i])/attFactor;
	}
	totalDiffuse = max(totalDiffuse * lightFactor, 0.4);
	
	outColor = vec4(totalDiffuse,1.0) * totalColor + vec4(totalSpecular,1.0);
	outColor = mix(vec4(skyColor,1.0),outColor,visibility);
	outBrightColor = vec4(0.0);
}