
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var FieldInsnNode = org.objectweb.asm.tree.FieldInsnNode;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var InsnNode = org.objectweb.asm.tree.InsnNode;
var JumpInsnNode = org.objectweb.asm.tree.JumpInsnNode;
var LabelNode = org.objectweb.asm.tree.LabelNode;
var Opcodes = org.objectweb.asm.Opcodes;

function initializeCoreMod() {
    return {
        "recipecategory": {
            target: {
                type: "CLASS",
                name: "net/minecraft/client/util/ClientRecipeBook"
            },
            transformer: transform
        }
    };
}

function transform( node ) {
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( method.name == "newRecipeList" && method.desc == "(Lnet/minecraft/client/util/RecipeBookCategories;)Lnet/minecraft/client/gui/recipebook/RecipeList;" ) {
            var allRecipesFieldName = method.name == "newRecipeList" ? "allRecipes" : "field_197932_f";
            var ctgryRecipesFieldName = method.name == "newRecipeList" ? "recipesByCategory" : "field_197931_e";

            var insn = method.instructions.get( 0 );
            var label = new LabelNode();
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 1 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new FieldInsnNode( Opcodes.GETFIELD, "net/minecraft/client/util/ClientRecipeBook", "allRecipes", "Ljava/util/List;" ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new FieldInsnNode( Opcodes.GETFIELD, "net/minecraft/client/util/ClientRecipeBook", "recipesByCategory", "Ljava/util/Map;" ) );
            method.instructions.insertBefore( insn, new MethodInsnNode( Opcodes.INVOKESTATIC, "modernity/client/util/RecipeHooks", "newRecipeList", "(Lnet/minecraft/client/util/RecipeBookCategories;Ljava/util/List;Ljava/util/Map;)Lnet/minecraft/client/gui/recipebook/RecipeList;", false ) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.DUP ) );
            method.instructions.insertBefore( insn, new JumpInsnNode( Opcodes.IFNULL, label ) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.ARETURN ) );
            method.instructions.insertBefore( insn, label )
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.POP ) );
        }

        if( method.name == "func_202889_b" && method.desc == "(Lnet/minecraft/client/util/RecipeBookCategories;)Lnet/minecraft/client/gui/recipebook/RecipeList;" ) {
            var insn = method.instructions.get( 0 );
            var label = new LabelNode();
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 1 ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new FieldInsnNode( Opcodes.GETFIELD, "net/minecraft/client/util/ClientRecipeBook", "field_197932_f", "Ljava/util/List;" ) );
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new FieldInsnNode( Opcodes.GETFIELD, "net/minecraft/client/util/ClientRecipeBook", "field_197931_e", "Ljava/util/Map;" ) );
            method.instructions.insertBefore( insn, new MethodInsnNode( Opcodes.INVOKESTATIC, "modernity/client/util/RecipeHooks", "newRecipeList", "(Lnet/minecraft/client/util/RecipeBookCategories;Ljava/util/List;Ljava/util/Map;)Lnet/minecraft/client/gui/recipebook/RecipeList;", false ) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.DUP ) );
            method.instructions.insertBefore( insn, new JumpInsnNode( Opcodes.IFNULL, label ) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.ARETURN ) );
            method.instructions.insertBefore( insn, label )
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.POP ) );
        }

        if( (method.name == "getCategory" || method.name == "func_202887_g") && method.desc == "(Lnet/minecraft/item/crafting/IRecipe;)Lnet/minecraft/client/util/RecipeBookCategories;" ) {
            var insn = method.instructions.get( 0 );
            var label = new LabelNode();
            method.instructions.insertBefore( insn, new VarInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new MethodInsnNode( Opcodes.INVOKESTATIC, "modernity/client/util/RecipeHooks", "getRecipeCategory", "(Lnet/minecraft/item/crafting/IRecipe;)Lnet/minecraft/client/util/RecipeBookCategories;", false ) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.DUP ) );
            method.instructions.insertBefore( insn, new JumpInsnNode( Opcodes.IFNULL, label ) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.ARETURN ) );
            method.instructions.insertBefore( insn, label )
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.POP ) );
        }
    }
    return node;
}