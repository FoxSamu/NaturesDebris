
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var FieldInsnNode = org.objectweb.asm.tree.FieldInsnNode;
var Opcodes = org.objectweb.asm.Opcodes;

function initializeCoreMod() {
    return {
        "playsound": {
            target: {
                type: "CLASS",
                name: "net/minecraft/client/audio/SoundSource"
            },
            transformer: transform
        }
    };
}


function transform( node ) {
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( method.name == "func_216420_a" ) {
            for( var i = 0; i < method.instructions.size(); i ++ ) {
                var insn = method.instructions.get( i );
                if( insn.getOpcode() == Opcodes.RETURN ) {
                    method.instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
                    method.instructions.insertBefore(insn, new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/audio/SoundSource", "field_216441_b", "I"));
                    method.instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 1));
                    method.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "modernity/client/util/ClientHooks", "onPlaySound", "(ILnet/minecraft/util/math/Vec3d;)V", false));
                    break;
                }
            }
        }
    }
    return node;
}