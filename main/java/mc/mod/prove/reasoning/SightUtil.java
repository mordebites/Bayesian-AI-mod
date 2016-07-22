package mc.mod.prove.reasoning;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

public class SightUtil {

	public static boolean isInFieldOfVision(EntityLivingBase observed, EntityLiving observer){
	    //save observer's original rotation variables
	    float rotationYawPrime = observer.rotationYaw;
	    float rotationPitchPrime = observer.rotationPitch;
	    
	    //make observer directly face Entity 1
	    observer.faceEntity(observed, 360F, 360F);
	    
	    //switch values of prime rotation variables with current rotation variables
	    float f = observer.rotationYaw;
	    float f2 = observer.rotationPitch;
	    observer.rotationYaw = rotationYawPrime;
	    observer.rotationPitch = rotationPitchPrime;
	    rotationYawPrime = f;
	    rotationPitchPrime = f2;
	    
	    //assuming field of vision consists of everything within X degrees from rotationYaw and Y degrees from rotationPitch, check if observer's current rotationYaw and rotationPitch within this X and Y range
	    float X = 60F;
	    float Y = 60F;
	    float yawFOVMin = observer.rotationYaw - X;
	    float yawFOVMax = observer.rotationYaw + X;
	    float pitchFOVMin = observer.rotationPitch - Y;
	    float pitchFOVMax = observer.rotationPitch + Y;
	    
	    boolean flag1 = (yawFOVMin < 0F && (rotationYawPrime >= yawFOVMin + 360F || rotationYawPrime <= yawFOVMax)) || (yawFOVMax >= 360F && (rotationYawPrime <= yawFOVMax - 360F || rotationYawPrime >= yawFOVMin)) || (yawFOVMax < 360F && yawFOVMin >= 0F && rotationYawPrime <= yawFOVMax && rotationYawPrime >= yawFOVMin);
	    boolean flag2 = (pitchFOVMin <= -180F && (rotationPitchPrime >= pitchFOVMin + 360F || rotationPitchPrime <= pitchFOVMax)) || (pitchFOVMax > 180F && (rotationPitchPrime <= pitchFOVMax - 360F || rotationPitchPrime >= pitchFOVMin)) || (pitchFOVMax < 180F && pitchFOVMin >= -180F && rotationPitchPrime <= pitchFOVMax && rotationPitchPrime >= pitchFOVMin)
	    		;
	    if(flag1 && flag2 && observer.canEntityBeSeen(observed))
	        return true;
	    else return false;
	}
}
