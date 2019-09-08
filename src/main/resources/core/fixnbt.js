
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var InsnNode = org.objectweb.asm.tree.InsnNode;
var Opcodes = org.objectweb.asm.Opcodes;

function initializeCoreMod() {
    return {
        "fixnbt": {
            target: {
                type: "CLASS",
                name: "net/minecraft/nbt/NBTUtil"
            },
            transformer: transform
        }
    };
}


function transform( node ) {
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( (method.name == "update" || method.name == "func_210821_a") && method.desc == "(Lcom/mojang/datafixers/DataFixer;Lcom/mojang/datafixers/DSL$TypeReference;Lnet/minecraft/nbt/NBTTagCompound;II)Lnet/minecraft/nbt/NBTTagCompound;" ) {
            var insn = method.instructions.get( 0 );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 1 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 2 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ILOAD, 3 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ILOAD, 4 ) );
            method.instructions.insertBefore( insn, new MethodInsnNode( Opcodes.INVOKESTATIC, "modernity/common/util/Hooks", "onFixNBT", "(Lcom/mojang/datafixers/DataFixer;Lcom/mojang/datafixers/DSL$TypeReference;Lnet/minecraft/nbt/NBTTagCompound;II)Lnet/minecraft/nbt/NBTTagCompound;", false ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ASTORE, 2 ) );
        }
    }
    return node;
}