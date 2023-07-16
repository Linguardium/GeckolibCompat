package mod.linguardium.geckolibcompat.mixins;

import com.google.common.collect.Lists;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinConfigPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // The decision was made to sunset Geckolib3 entirely
        // it's a legacy version of the lib on legacy versions of mc
        // I can understand it being annoying that we won't fix it from our side
        //
        // Ok, geckolib, i guess I will remove the conflict myself...
        System.out.println("[GeckolibCompat] Removing geckolib3 mixin from ArmorFeatureRenderer");
        List<String> methods = Lists.newArrayList(
                "geckolib3$selectArmorModel",
                "geckolib3$getArmorTexture",
                "geckolib3$removeStored",
                "geckolib3$storeSlot",
                "geckolib3$storeEntity"
        );
        targetClass.methods.stream().filter(node -> node.name.contains("$geckolib3$")).forEach(methodNode -> {
            if (methods.stream().anyMatch(s -> methodNode.name.contains(s))) {
                System.out.println("[GeckolibCompat] Removing "+methodNode.name);
                // let's just replace their mixin methods with void returns
                // the methods literally do nothing anyway
                methodNode.instructions.clear();
                methodNode.instructions.add(new InsnNode(Opcodes.RETURN));

            }
        });
    }
}

