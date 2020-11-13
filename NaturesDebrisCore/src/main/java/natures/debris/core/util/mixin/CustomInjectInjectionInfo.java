package natures.debris.core.util.mixin;

import com.google.common.base.Strings;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.AnnotationType;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.HandlerPrefix;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.util.Annotations;

@AnnotationType(CustomInjection.class)
@HandlerPrefix("custom")
public class CustomInjectInjectionInfo extends InjectionInfo {
    public CustomInjectInjectionInfo(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(mixin, method, annotation, "at");
    }

    @Override
    protected Injector parseInjector(AnnotationNode injectAnnotation) {
        return new CustomInjectInjector(this, Annotations.<Type>getValue(injectAnnotation, "injector").getClassName());
    }

    @Override
    protected String getDescription() {
        return "Custom injection method";
    }

    @Override
    public String getSliceId(String id) {
        return Strings.nullToEmpty(id);
    }

}
