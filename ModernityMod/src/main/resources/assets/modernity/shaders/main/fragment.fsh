#define FOG_EXP 2048
#define FOG_EXP2 2049
#define FOG_LINEAR 9729

#define TYPE_LIGHT 0
#define TYPE_DISTORT 1
#define TYPE_SHADOW 2

struct LightSource {
    vec3 position;
    vec4 color;
    float radius;
    int type;
};

varying vec2 texCoord;

uniform sampler2D diffuse;
uniform sampler2D depth;
uniform sampler2D handDepth;
uniform float depthRange;
uniform mat4 inverseMVP;
uniform int fogMode;
uniform int lightCount;
uniform LightSource lights[LIGHT_COUNT];

float depth2D(sampler2D sampler, vec2 coord) {
    return texture2D(sampler, coord).x;
}

float computeDepth() {
    float mDepth = depth2D(depth, texCoord);
    float hDepth = depth2D(handDepth, texCoord);
    if (hDepth < depthRange) {
        return hDepth;
    } else {
        return mDepth;
    }
}

vec3 getFragPos() {
    //    float depthValue = min(depth2D(depth, texCoord), depth2D(handDepth, texCoord));
    float depthValue = computeDepth();
    float depthCoord = depthValue * 2.0 - 1.0;

    vec4 fragPos = vec4(texCoord.xy * 2.0 - 1.0, depthCoord, 1.0) * inverseMVP;
    fragPos.xyz /= fragPos.w;

    return fragPos.xyz;
}


float getFogMultiplier(vec3 fragPos, int fogMode) {
    if (fogMode == FOG_LINEAR) {
        return clamp((length(fragPos) - gl_Fog.start) * gl_Fog.scale, 0.0, 1.0);
    } else if (fogMode == FOG_EXP) {
        return 1.0 - clamp(exp(- gl_Fog.density * length(fragPos)), 0.0, 1.0);
    } else if (fogMode == FOG_EXP2) {
        return 1.0 - clamp(exp(- pow(gl_Fog.density * length(fragPos), 2.0)), 0.0, 1.0);
    }
    return 0.0;
}

void main() {
    vec3 fragPos = getFragPos();

    vec4 sourceColor = texture2D(diffuse, texCoord);
    vec4 color = vec4(sourceColor);

    float fogMult = 1.0 - getFogMultiplier(fragPos, fogMode);
    for (int i = 0; i < lightCount; i++) {
        LightSource light = lights[i];
        vec3 lightPos = light.position;
        float dist = distance(lightPos, fragPos);
        float radius = light.radius;
        vec3 lightColor = light.color.xyz * light.color.w * fogMult;

        if (dist < radius) {
            if (light.type == TYPE_LIGHT) {
                color += sourceColor * (vec4(lightColor * pow(1.0 - dist / radius, 2), 0.0));
            }
            if (light.type == TYPE_SHADOW) {
                color -= sourceColor * (vec4(lightColor * pow(1.0 - dist / radius, 2), 0.0));
                if (color.r < 0) color.r = 0;
                if (color.g < 0) color.g = 0;
                if (color.b < 0) color.b = 0;
            }
        }
    }

    gl_FragColor = color;
}