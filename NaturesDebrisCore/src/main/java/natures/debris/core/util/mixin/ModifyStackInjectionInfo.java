package natures.debris.core.util.mixin;

import com.google.common.base.Strings;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.AnnotationType;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.HandlerPrefix;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

/**
 * Information about a constant modifier injector
 */
@AnnotationType(ModifyStack.class)
@HandlerPrefix("stack")
public class ModifyStackInjectionInfo extends InjectionInfo {
    public ModifyStackInjectionInfo(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(mixin, method, annotation, "at");
    }

    @Override
    protected Injector parseInjector(AnnotationNode injectAnnotation) {
        return new ModifyStackInjector(this);
    }

    @Override
    protected String getDescription() {
        return "Stack modifier method";
    }

    @Override
    public String getSliceId(String id) {
        return Strings.nullToEmpty(id);
    }

}
