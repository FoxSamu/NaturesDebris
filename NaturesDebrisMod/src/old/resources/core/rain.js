
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var InsnNode = org.objectweb.asm.tree.InsnNode;
var LabelNode = org.objectweb.asm.tree.LabelNode;
var JumpInsnNode = org.objectweb.asm.tree.JumpInsnNode;
var Opcodes = org.objectweb.asm.Opcodes;

function initializeCoreMod() {
    return {
        "rain": {
            target: {
                type: "CLASS",
                name: "net/minecraft/world/World"
            },
            transformer: transform
        }
    };
}

function transform( node ) {
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( (method.name == "func_72896_J" || method.name == "isRaining") && method.desc == "()Z" ) {
            var insn = method.instructions.get( 0 );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new MethodInsnNode( Opcodes.INVOKESTATIC, "modernity/common/util/Hooks", "checkRaining", "(Lnet/minecraft/world/World;)Z", false ) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.IRETURN ) );
        }
        if( (method.name == "func_175727_C" || method.name == "isRainingAt")  && method.desc == "(Lnet/minecraft/util/math/BlockPos;)Z" ) {
            var insn = method.instructions.get( 0 );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 1 ) );
            method.instructions.insertBefore( insn, new MethodInsnNode( Opcodes.INVOKESTATIC, "modernity/common/util/Hooks", "checkRainingAt", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", false ) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.IRETURN ) );
        }
    }
    return node;
}