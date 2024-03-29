package mc.mod.lilysrc.entity;

import mc.mod.lilysrc.MainRegistry;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Class for the entity Lily, core of the mod. It's registered in the entity registry of the game.
 */
public class EntityLily extends EntityCreature {

	public EntityLily(World worldIn) {
		super(worldIn);
		this.setNoAI(true);
	}

	public static void mainRegistry() {
		registerEntity();
	}

	public static void registerEntity() {
		createEntity(EntityLilyMob.class, "Lily", 0xffff00, 0xffb3b3);
	}

	public static void createEntity(Class entityClass, String entityName,
			int solidColor, int spotColor) {

		EntityRegistry.registerModEntity(entityClass, entityName, 1,
				MainRegistry.modInstance, 80, 1, false);
		EntityRegistry.registerEgg(entityClass, solidColor, spotColor);

		createEgg(1, solidColor, spotColor);
	}

	private static void createEgg(int id, int solidColor, int spotColor) {
		EntityList.entityEggs.put(String.valueOf(id),
				new EntityList.EntityEggInfo(String.valueOf(id), solidColor,
						spotColor));
	}
}
