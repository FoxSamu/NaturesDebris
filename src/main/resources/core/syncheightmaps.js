
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var InsnNode = org.objectweb.asm.tree.InsnNode;
var LabelNode = org.objectweb.asm.tree.LabelNode;
var JumpInsnNode = org.objectweb.asm.tree.JumpInsnNode;
var Opcodes = org.objectweb.asm.Opcodes;

function initializeCoreMod() {
    return {
        "syncheightmaps": {
            target: {
                type: "CLASS",
                name: "net/minecraft/world/gen/Heightmap$Type"
            },
            transformer: transform
        }
    };
}

function transform( node ) {
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( method.name == "func_222681_b" && method.desc == "()Z" ) {
            var insn = method.instructions.get( 0 );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new MethodInsnNode( Opcodes.INVOKESTATIC, "modernity/common/util/Hooks", "mustSyncHeightmap", "(Lnet/minecraft/world/gen/Heightmap$Type;)Z", false ) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.IRETURN ) );
        }
        if( method.name == "lambda$static$1" && method.desc == "(Lnet/minecraft/block/BlockState;)Z" ) {
            var insn = method.instructions.get( 0 );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new MethodInsnNode( Opcodes.INVOKESTATIC, "modernity/common/util/Hooks", "isMotionBlockingNoLeaves", "(Lnet/minecraft/block/BlockState;)Z", false ) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.IRETURN ) );
        }
    }
    return node;
}