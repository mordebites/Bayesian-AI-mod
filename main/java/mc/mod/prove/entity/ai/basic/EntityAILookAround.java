package mc.mod.prove.entity.ai.basic;

import java.util.Random;

import mc.mod.prove.entity.movement.JumpHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.math.Vec3d;

/*
 * Based on the Minecraft AI class EntityAIWander
 */
public class EntityAILookAround extends EntityAIBase
{
	private final int  maxMoveTimer = 100; //5 secondi
	private final int maxRotationTimer = 10;//mezzo secondo
	
    private EntityCreature entity;
    private double xPosition; 
    private double yPosition;
    private double zPosition;
    private double speed;
    private int moveTimer = maxMoveTimer;
    private int rotationTimer = maxRotationTimer;
    private Random rdm = new Random();
    
    private EntityLiving fakeRotationEntity;
    private int[][] positions = {{-2,2}, {-1,2}, {0,2}, {1,2}, {2,2},
    								{-2,1}, {2,1}, {-2, 0}, {2,0}};

    public EntityAILookAround(EntityCreature creatureIn, double speedIn)
    {
        this.entity = creatureIn;
        this.speed = speedIn;
        
        fakeRotationEntity = new EntityCow(Minecraft.getMinecraft().theWorld);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
    	return true;
    }

    private void executing() {
    	//parte 1: stabilisce quando deve ruotare su se stesso e quando muoversi
    	if (moveTimer > 0) {
    		moveTimer--;
    		if (rotationTimer > 0) {
    			rotationTimer--;
    		} else {
    			rotationTimer = maxRotationTimer;
    		}
    		
    	} else {
    		Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.entity, 1, 0);
    		
	        if (vec3d != null) {
	            this.xPosition = vec3d.xCoord;
	            this.yPosition = vec3d.yCoord;
	            this.zPosition = vec3d.zCoord;
	            
	            moveTimer = maxMoveTimer;
	        }
    	}
    	
    	//parte 2: si occupa di far ruotare o muovere l'npc
    	if(rotationTimer == maxRotationTimer) {
    		setFakeEntityPosition();
    		entity.faceEntity(fakeRotationEntity, 180F, 180F);
    	} else if (moveTimer == maxMoveTimer){
    		this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    	}
    	//JumpHelper.pathHelper(entity);
    }
    
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
    	this.executing();
    	
    	return true;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
    	this.executing();
    }

    
    private void setFakeEntityPosition(){
    	int rdmPos = rdm.nextInt(9);
    	fakeRotationEntity.setPosition(positions[rdmPos][0] + entity.posX, fakeRotationEntity.posY, positions[rdmPos][1] + entity.posZ);
    }
}