package mc.mod.prove.discardedAI;

import javax.swing.plaf.synth.SynthSeparatorUI;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

/*
 * Based on the Minecraft AI class EntityAIWander
 */
public class EntityAILookAroundVecchia extends EntityAIBase
{
	private final int maxTimer = 20;
	
    private EntityCreature entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
    private boolean mustUpdate;
    private int moveTimer = maxTimer; //1 secondo
    

    public EntityAILookAroundVecchia(EntityCreature creatureIn, double speedIn)
    {
        this.entity = creatureIn;
        this.speed = speedIn;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
       	if (moveTimer > 0) {
    		moveTimer--;
    		return false;
    		
    	} else {
    		
    		Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.entity, 2, 0);
    		
	        if (vec3d == null)
	        {
	            return false;
	        }
	        else
	        {
	            this.xPosition = vec3d.xCoord;
	            this.yPosition = vec3d.yCoord;
	            this.zPosition = vec3d.zCoord;
	            
	            moveTimer = maxTimer;
	     
	            this.mustUpdate = false;
	            return true;
	        }
    	}
        
    }

    
    
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
    	Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.entity, 2, 0);
    	
    	 this.xPosition = vec3d.xCoord;
         this.yPosition = vec3d.yCoord;
         this.zPosition = vec3d.zCoord;
    	
        return !this.entity.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    /**
     * Makes task to bypass chance
     */
    public void makeUpdate()
    {
        this.mustUpdate = true;
    }
}