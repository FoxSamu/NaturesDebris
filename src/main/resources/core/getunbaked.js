
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var InsnNode = org.objectweb.asm.tree.InsnNode;
var JumpInsnNode = org.objectweb.asm.tree.JumpInsnNode;
var LabelNode = org.objectweb.asm.tree.LabelNode;
var Opcodes = org.objectweb.asm.Opcodes;

function initializeCoreMod() {
    return {
        "getunbaked": {
            target: {
                type: "CLASS",
                name: "net/minecraft/client/renderer/model/ModelBakery"
            },
            transformer: transform
        }
    };
}

function transform( node ) {
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( (method.name == "getUnbakedModel" || method.name == "func_209597_a") && method.desc == "(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/renderer/model/IUnbakedModel;" ) {
            var insn = method.instructions.get( 0 );
            var label = new LabelNode();
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 1 ) );
            method.instructions.insertBefore( insn, new MethodInsnNode( Opcodes.INVOKESTATIC, "modernity/client/util/ClientHooks", "onGetUnbakedModel", "(Lnet/minecraft/client/renderer/model/ModelBakery;Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/renderer/model/IUnbakedModel;", false ) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.DUP ) );
            method.instructions.insertBefore( insn, new JumpInsnNode( Opcodes.IFNULL, label ) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.ARETURN ) );
            method.instructions.insertBefore( insn, label )
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.POP ) );
        }
    }
    return node;
}