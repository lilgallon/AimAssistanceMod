package dev.nero.aimassistance.events;

import com.mrcrayfish.controllable.Controllable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ControllerEvents {

    /*
    @SubscribeEvent
    public void onButtonInput(ControllerEvent.ButtonInput event) {
        if (event.getState()) {
            PlayerEntity player = Minecraft.getInstance().player;
            if(player == null)
                return;

            switch(event.getButton()) {
                //case Buttons.A: break;
                //case Buttons.X: break;
                //case Buttons.SELECT: break;
                //case Buttons.RIGHT_BUMPER: break;
                //case Buttons.LEFT_BUMPER: break;
                //case Buttons.RIGHT_TRIGGER: break;
                //case Buttons.LEFT_TRIGGER: break;
                default:
                    break;
            }
        }
    }*/

    public static boolean keyAttackDown() {
        return Controllable.getController() != null && Controllable.getController().getRTriggerValue() != 0.0F;
    }
}
