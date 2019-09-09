
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var InsnNode = org.objectweb.asm.tree.InsnNode;
var LabelNode = org.objectweb.asm.tree.LabelNode;
var JumpInsnNode = org.objectweb.asm.tree.JumpInsnNode;
var Opcodes = org.objectweb.asm.Opcodes;

function initializeCoreMod() {
    return {
        "renderparticles": {
            target: {
                type: "CLASS",
                name: "net/minecraft/client/particle/ParticleManager"
            },
            transformer: transform
        }
    };
}

// func_78873_a = addEffect(Lnet/minecraft/client/particle/Particle;)V
function transform( node ) {
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( (method.name == "renderParticles" || method.name == "func_78874_a") && method.desc == "(Lnet/minecraft/entity/Entity;F)V" ) {
            var insn = method.instructions.get( 0 );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 1 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.FLOAD, 2 ) );
            method.instructions.insertBefore( insn, new MethodInsnNode( Opcodes.INVOKESTATIC, "modernity/client/util/ClientHooks", "preRenderParticles", "(Lnet/minecraft/entity/Entity;F)V", false ) );
        }
        if( (method.name == "addEffect" || method.name == "func_78873_a") && method.desc == "(Lnet/minecraft/client/particle/Particle;)V" ) {
            var insn = method.instructions.get( 0 );
//            var label = new LabelNode();
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 1 ) );
            method.instructions.insertBefore( insn, new MethodInsnNode( Opcodes.INVOKESTATIC, "modernity/client/util/ClientHooks", "addParticle", "(Lnet/minecraft/client/particle/Particle;)Lnet/minecraft/client/particle/Particle;", false ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ASTORE, 1 ) );
//            method.instructions.insertBefore( insn, new JumpInsnNode( Opcodes.IFEQ, label ) );
//            method.instructions.insertBefore( insn, new InsnNode( Opcodes.RETURN ) );
//            method.instructions.insertBefore( insn, label );
        }
    }
    return node;
}