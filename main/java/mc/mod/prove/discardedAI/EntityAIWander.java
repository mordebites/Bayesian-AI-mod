package mc.mod.prove.discardedAI;

import javax.swing.plaf.synth.SynthSeparatorUI;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

/*
 * Based on the Minecraft AI class EntityAIWander
 */
public class EntityAIWander extends EntityAIBase
{
    private EntityCreature entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
    private boolean mustUpdate;
    private int moveTimer = 60; //3 secondi

    public EntityAIWander(EntityCreature creatureIn, double speedIn)
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
	            
	            moveTimer = 60;
	     
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
    	this.beforeExecuting();
        return !this.entity.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {	
    	this.beforeExecuting();
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    
    private void beforeExecuting(){
    	if (moveTimer > 0) {
    		moveTimer--;
    	} else {
    		
    		Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.entity, 2, 0);
    		
	        if (vec3d != null)
	        {
	            this.xPosition = vec3d.xCoord;
	            this.yPosition = vec3d.yCoord;
	            this.zPosition = vec3d.zCoord;
	            
	            moveTimer = 60;
	     
	            this.mustUpdate = false;
	        }
    	}
    }
    /**
     * Makes task to bypass chance
     */
    public void makeUpdate()
    {
        this.mustUpdate = true;
    }
}
