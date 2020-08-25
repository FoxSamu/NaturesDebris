
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var Opcodes = org.objectweb.asm.Opcodes;

function initializeCoreMod() {
    return {
        "prerendershaders": {
            target: {
                type: "CLASS",
                name: "net/minecraft/client/renderer/GameRenderer"
            },
            transformer: transform
        }
    };
}


function transform( node ) {
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( (method.name == "updateCameraAndRender" || method.name == "func_195458_a") && method.desc == "(FJZ)V" ) {
            for( var i = 0; i < method.instructions.size(); i ++ ) {
                var insn = method.instructions.get( i );
                if( insn instanceof MethodInsnNode ) {
                    if( (insn.name == "renderEntityOutlineFramebuffer" || insn.name == "func_174975_c") && insn.desc == "()V" && insn.owner == "net/minecraft/client/renderer/WorldRenderer" ) {
                        method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.FLOAD, 1 ) );
                        method.instructions.insertBefore( insn, new MethodInsnNode( Opcodes.INVOKESTATIC, "modernity/client/util/ClientHooks", "preRenderShaders", "(F)V", false ) );
                        break;
                    }
                }
            }
        }
    }
    return node;
}