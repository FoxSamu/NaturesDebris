
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var InsnNode = org.objectweb.asm.tree.InsnNode;
var JumpInsnNode = org.objectweb.asm.tree.JumpInsnNode;
var LabelNode = org.objectweb.asm.tree.LabelNode;
var Opcodes = org.objectweb.asm.Opcodes;

function initializeCoreMod() {
    return {
        "renderblock": {
            target: {
                type: "CLASS",
                name: "net/minecraft/client/renderer/BlockRendererDispatcher"
            },
            transformer: transform
        }
    };
}

function transform( node ) {
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( (method.name == "renderBlock") && method.desc == "(Lnet/minecraft/block/state/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IEnviromentBlockReader;Lnet/minecraft/client/renderer/BufferBuilder;Ljava/util/Random;Lnet/minecraftforge/client/model/data/IModelData;)Z" ) {
            var insn = method.instructions.get( 0 );
            var label = new LabelNode();
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 1 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 2 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 3 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 4 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 5 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 6 ) );
            method.instructions.insertBefore( insn, new MethodInsnNode( Opcodes.INVOKESTATIC, "modernity/client/util/ClientHooks", "onRenderBlock", "(Lnet/minecraft/client/renderer/BlockRendererDispatcher;Lnet/minecraft/block/state/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IWorldReader;Lnet/minecraft/client/renderer/BufferBuilder;Ljava/util/Random;Lnet/minecraftforge/client/model/data/IModelData;)Ljava/lang/Boolean;", false ) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.DUP ) );
            method.instructions.insertBefore( insn, new JumpInsnNode( Opcodes.IFNULL, label ) );
            method.instructions.insertBefore( insn, new MethodInsnNode( Opcodes.INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false ) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.IRETURN ) );
            method.instructions.insertBefore( insn, label )
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.POP ) );
        }
        if( (method.name == "renderFluid" || method.name == "func_215331_a" ) && method.desc == "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IEnviromentBlockReader;Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/fluid/IFluidState;)Z" ) {
            var insn = method.instructions.get( 0 );
            var label = new LabelNode();
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 1 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 2 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 3 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 4 ) );
            method.instructions.insertBefore( insn, new MethodInsnNode( Opcodes.INVOKESTATIC, "modernity/client/util/ClientHooks", "onRenderFluid", "(Lnet/minecraft/client/renderer/BlockRendererDispatcher;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IEnviromentBlockReader;Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/fluid/IFluidState;)Ljava/lang/Boolean;", false ) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.DUP ) );
            method.instructions.insertBefore( insn, new JumpInsnNode( Opcodes.IFNULL, label ) );
            method.instructions.insertBefore( insn, new MethodInsnNode( Opcodes.INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false ) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.IRETURN ) );
            method.instructions.insertBefore( insn, label )
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.POP ) );
        }
    }
    return node;
}