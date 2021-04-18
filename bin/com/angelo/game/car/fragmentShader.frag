#version 330 core

const int lights = 4;

in vec2 passTextureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[lights];
in vec3 toCameraVector;
in vec4 shadowCoords;
in float visibility;

layout (location = 0) out vec4 outColor;
layout (location = 1) out vec4 outBrightColor;

uniform sampler2D textureSampler;
uniform sampler2D specularMap;
uniform sampler2D shadowMap;

uniform float usesSpecularMap;
uniform float shineDamper;
uniform float reflectivity;
uniform bool doShadowMapping;

uniform vec3 lightColor[lights];
uniform vec3 skyColor;
uniform vec3 attenuation[lights];

const float levels = 100;
const int pcfCount = 6;
const float totalTexels = (pcfCount * 2.0 + 1.0) * (pcfCount * 2.0 + 1.0);

void main(void){
	float mapSize = 8192;
	float texelSize = 1.0 / mapSize;
	float total = 0.0;	
	float lightFactor = 0.0;
	
	if(doShadowMapping){
		for(int x=-pcfCount;x<=pcfCount;x++){
			for(int y=-pcfCount;y<=pcfCount;y++){
				float objectNearestLight = texture(shadowMap, shadowCoords.xy + vec2(x,y) * texelSize).r;
				if(shadowCoords.z > objectNearestLight + 0.0015){
					total += 1.0;
				}
			}
		}	
		total /= totalTexels;
		lightFactor = 1.0 - (total * shadowCoords.w);
	}
	
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for(int i=0;i<lights;i++){
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);
		float nDotl = dot(unitNormal,unitLightVector);
		float brightness = max(nDotl,0.0);
		float level = floor(brightness * levels);
		brightness = level / levels;
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		specularFactor = max(specularFactor,0.0);
		float dampedFactor = pow(specularFactor,shineDamper);
		level = floor(dampedFactor * levels);
		dampedFactor = level / levels;
		totalDiffuse = totalDiffuse + (brightness * lightColor[i])/attFactor;
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColor[i])/attFactor;
	}
	
	totalDiffuse = max(totalDiffuse * lightFactor, 1);
	
	vec4 textureColor = texture(textureSampler, passTextureCoords);
	if(textureColor.a<0.5){
		discard;
	}
	
	outBrightColor = vec4(0.0);
	if(usesSpecularMap > 0.5){
		vec4 mapInfo = texture(specularMap, passTextureCoords);
		totalSpecular *= mapInfo.r;
		if(mapInfo.g > 0.5){
			outBrightColor = textureColor + vec4(totalSpecular,1.0);
			totalDiffuse = vec3(1.0);
		}
	}
	
	outColor = vec4(totalDiffuse,1.0) * texture(textureSampler, passTextureCoords) + vec4(totalSpecular,1.0);
	outColor = mix(vec4(skyColor,1.0),outColor,visibility);
}