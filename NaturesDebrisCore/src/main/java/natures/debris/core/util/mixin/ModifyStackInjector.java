package natures.debris.core.util.mixin;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.injection.invoke.RedirectInjector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;

public class ModifyStackInjector extends RedirectInjector {

    public ModifyStackInjector(InjectionInfo info) {
        super(info, "@ModifyStack");
    }

    @Override
    protected void inject(Target target, InjectionNodes.InjectionNode node) {
        if (!preInject(node)) {
            return;
        }

        if (node.isReplaced()) {
            throw new UnsupportedOperationException("Target failure for " + info);
        }

        AbstractInsnNode targetNode = node.getCurrentTarget();
        checkTargetModifiers(target, false);
        injectStackModifier(target, targetNode);
    }

    private void injectStackModifier(Target target, AbstractInsnNode node) {
        Target.Extension extraStack = target.extendStack();
        InsnList before = new InsnList();
        invokeStackHandler(target, extraStack, before);
        extraStack.apply();
        target.insertBefore(node, before);
    }

    private AbstractInsnNode invokeStackHandler(Target target, Target.Extension extraStack, InsnList before) {
        if (!isStatic) {
            before.add(new VarInsnNode(Opcodes.ALOAD, 0));
            before.add(new InsnNode(Opcodes.SWAP));
            extraStack.add();
        }

        pushArgs(target.arguments, before, target.getArgIndices(), 0, target.arguments.length - 1, extraStack);

        return invokeHandler(before);
    }

}
