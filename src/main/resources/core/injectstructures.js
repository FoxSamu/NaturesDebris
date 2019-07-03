
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var Opcodes = org.objectweb.asm.Opcodes;

function initializeCoreMod() {
    return {
        "injectstructures": {
            target: {
                type: "CLASS",
                name: "net/minecraft/world/biome/Biome"
            },
            transformer: transform
        }
    };
}


function transform( node ) {
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( (method.name == "addStructureFeatures" || method.name == "func_203605_a") && method.desc == "()V" ) {
            var insn = method.instructions.get( 0 );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new MethodInsnNode( Opcodes.INVOKESTATIC, "modernity/common/util/Hooks", "registerCustomBiomeStructures", "(Lnet/minecraft/world/biome/Biome;)V", false ) );
        }
    }
    return node;
}