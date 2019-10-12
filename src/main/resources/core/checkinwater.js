
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var InsnNode = org.objectweb.asm.tree.InsnNode;
var FieldInsnNode = org.objectweb.asm.tree.FieldInsnNode;
var Opcodes = org.objectweb.asm.Opcodes;

function initializeCoreMod() {
    return {
        "checkinwater": {
            target: {
                type: "CLASS",
                name: "net/minecraft/entity/Entity"
            },
            transformer: transform
        }
    };
}


function transform( node ) {
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( (method.name == "updateAquatics" || method.name == "func_205011_p") && method.desc == "()V" ) {
            method.maxStack = 2; // Ensure extra stack space
            for( var i = 0; i < method.instructions.size(); i ++ ) {
                var insn = method.instructions.get( i );
                if( insn instanceof MethodInsnNode ) {
                    if( (insn.name == "updateSwimming" || insn.name == "func_205343_av") && insn.desc == "()V" ) {
                        var name = "field_70171_ac";
                        if( method.name == "updateAquatics" ) {
                            name = "inWater";
                        }
                        var insn1 = method.instructions.get( i + 1 );
                        // Inverse order: they are added before each other
                        method.instructions.insertBefore( insn1, new VarInsnNode( Opcodes.ALOAD, 0 ) );
                        method.instructions.insertBefore( insn1, new VarInsnNode( Opcodes.ALOAD, 0 ) );
                        method.instructions.insertBefore( insn1, new VarInsnNode( Opcodes.ALOAD, 0 ) );
                        method.instructions.insertBefore( insn1, new FieldInsnNode( Opcodes.GETFIELD, "net/minecraft/entity/Entity", name, "Z" ) );
                        method.instructions.insertBefore( insn1, new MethodInsnNode( Opcodes.INVOKESTATIC, "modernity/common/util/Hooks", "checkInWater", "(Lnet/minecraft/entity/Entity;Z)Z", false ) );
                        method.instructions.insertBefore( insn1, new FieldInsnNode( Opcodes.PUTFIELD, "net/minecraft/entity/Entity", name, "Z" ) );
                        break;
                    }
                }
            }
        }
    }
    return node;
}