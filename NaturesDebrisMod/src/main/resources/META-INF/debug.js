
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var FieldInsnNode = org.objectweb.asm.tree.FieldInsnNode;
var Opcodes = org.objectweb.asm.Opcodes;

function initializeCoreMod() {
    return {
        "debug1": {
            target: {
                type: "CLASS",
                name: "net/minecraft/client/renderer/debug/DebugRenderer"
            },
            transformer: transform
        },
        "debug2": {
            target: {
                type: "CLASS",
                name: "net/minecraft/network/DebugPacketSender"
            },
            transformer: transform
        }
    };
}
// func_229019_a_(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer$Impl;DDD)V
// func_218803_a(Lnet/minecraft/world/World;Lnet/minecraft/entity/MobEntity;Lnet/minecraft/pathfinding/Path;F)V

function transform( node ) {
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( (method.name == "func_229019_a_" || method.name == "render") && method.desc == "(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer$Impl;DDD)V" ) {
            for( var i = 0; i < method.instructions.size(); i ++ ) {
                var insn = method.instructions.get( i );
                if( insn.getOpcode() == Opcodes.RETURN ) {
                    method.instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
                    method.instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 1));
                    method.instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 2));
                    method.instructions.insertBefore(insn, new VarInsnNode(Opcodes.DLOAD, 3));
                    method.instructions.insertBefore(insn, new VarInsnNode(Opcodes.DLOAD, 5));
                    method.instructions.insertBefore(insn, new VarInsnNode(Opcodes.DLOAD, 7));
                    method.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "natures/debris/client/util/ClientHooks", "onRenderDebug", "(Lnet/minecraft/client/renderer/debug/DebugRenderer;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer$Impl;DDD)V", false));
                    break;
                }
            }
        }
        if( (method.name == "func_218803_a" || method.name == "sendPath") && method.desc == "(Lnet/minecraft/world/World;Lnet/minecraft/entity/MobEntity;Lnet/minecraft/pathfinding/Path;F)V" ) {
            for( var i = 0; i < method.instructions.size(); i ++ ) {
                var insn = method.instructions.get( i );
                if( insn.getOpcode() == Opcodes.RETURN ) {
                    method.instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
                    method.instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 1));
                    method.instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 2));
                    method.instructions.insertBefore(insn, new VarInsnNode(Opcodes.FLOAD, 3));
                    method.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "natures/debris/common/util/Hooks", "onSendPath", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/MobEntity;Lnet/minecraft/pathfinding/Path;F)V", false));
                    break;
                }
            }
        }
    }
    return node;
}
