package natures.debris.core.util.mixin;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.spongepowered.asm.mixin.injection.struct.Target;

import java.util.function.Consumer;

@FunctionalInterface
public interface IInject {
    void modify(Target target, InsnList methodInsnList, AbstractInsnNode node, Consumer<InsnList> handlerInvoker);
}
