#version 330 core

const int lights = 16;

in vec2 pass_textureCoordinates;
in vec3 toLightVector[lights];
in vec3 toCameraVector;
in float visibility;

layout (location = 0) out vec4 outColor;
layout (location = 1) out vec4 outBrightColor;

uniform sampler2D modelTexture;
uniform sampler2D normalMap;
uniform vec3 lightColour[lights];
uniform vec3 attenuation[lights];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

void main(void){

	vec4 normalMapValue = 2.0 * texture(normalMap, pass_textureCoordinates) - 1.0;

	vec3 unitNormal = normalize(normalMapValue.rgb);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for(int i=0;i<lights;i++){
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);	
		float nDotl = dot(unitNormal,unitLightVector);
		float brightness = max(nDotl,0.0);
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
		float specularFactor = dot(reflectedLightDirection , unitVectorToCamera);
		specularFactor = max(specularFactor,0.0);
		float dampedFactor = pow(specularFactor,shineDamper);
		totalDiffuse = totalDiffuse + (brightness * lightColour[i])/attFactor;
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColour[i])/attFactor;
	}
	totalDiffuse = max(totalDiffuse, 0.2);
	
	vec4 textureColour = texture(modelTexture,pass_textureCoordinates);
	if(textureColour.a<0.5){
		discard;
	}

	outColor =  vec4(totalDiffuse,1.0) * textureColour + vec4(totalSpecular,1.0);
	outColor = mix(vec4(skyColour,1.0), outColor, visibility);
	outBrightColor = vec4(0.0);
}