package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import skybox.SkyboxRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class MainGameLoop {

	public static void main(String[] args) {
		
		// ==================TOOLS==================
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		TextMaster.init(loader);
		MasterRenderer renderer = new MasterRenderer(loader);
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		
		// =================TEXTS==================
		
		FontType candara = new FontType(loader.loadFontTexture("fonts/candara"), new File("res/fonts/candara.fnt"));
		GUIText text = new GUIText("I am a man of fortune, and I must seek my fortune", 3f, 0.5f, 0.1f, 0.7f, 0.1f, candara, new Vector2f(0.0f, 0.0f), 1f, true);
		text.setColour(1f, 1f, 1f);
		text.setOutlineColor(0f, 0f, 0f);
		
		// =================LIGHTS==================
		
		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(1200, 800, 800), new Vector3f(1f, 1f, 1f));
		lights.add(sun);
		Light lampLight1 = new Light(new Vector3f(185, 10, -293), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f));
		Light lampLight2 = new Light(new Vector3f(370, 17, -300), new Vector3f(1, 1, 1), new Vector3f(1, 0.01f, 0.002f));
		lights.add(lampLight1);
		lights.add(lampLight2);
//		float h = 0;
//		Vector3f funkyColor = Maths.HSVtoRGB(new Vector3f(h, 1f, 1f));
//		Light funkyLight = new Light(new Vector3f(-200, 10, -200), funkyColor);
//		lights.add(funkyLight);
//		lights.add(funkyLight1);
//		lights.add(funkyLight2);
		
		// ==================TERRAIN==================
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("terrain/grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("terrain/mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("terrain/grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("terrain/path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,
				rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("water/dudv_map"));
		
		Terrain terrain1 = new Terrain(0, -1, loader, texturePack, blendMap, "heightMaps/heightmap");
		Terrain terrain2 = new Terrain(1, -1, loader, texturePack, blendMap, "heightMaps/pondHeightMap");
		Terrain[][] terrainGrid = new Terrain[][] {{terrain1}, {terrain2}};
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(terrain1);
		terrains.add(terrain2);
		
		// ==================BASIC MODELS==================
		
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> normalMapEntities = new ArrayList<Entity>();
		
		TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel/barrel", loader),
				new ModelTexture(loader.loadTexture("barrel/barrel")));
		barrelModel.getTexture().setNormalMap(loader.loadTexture("barrel/barrelNormal"));
		barrelModel.getTexture().setShineDamper(10);
		barrelModel.getTexture().setReflectivity(0.5f);
		Entity barrel = new Entity(barrelModel, new Vector3f(75, 10, -75), 0, 0, 0, 1f);
		normalMapEntities.add(barrel);
		
		//lamps
		ModelData lampData = OBJFileLoader.loadOBJ("lamp/lamp");
		RawModel lampModel = loader.loadToVAO(lampData.getVertices(), lampData.getTextureCoords(),
				lampData.getNormals(), lampData.getIndices());
		ModelTexture lampTexture = new ModelTexture(loader.loadTexture("lamp/lampV2"));
		lampTexture.setUseFakeLighting(true);
//		lampTexture.setShineDamper(1);
//		lampTexture.setReflectivity(2);
		TexturedModel lamp = new TexturedModel(lampModel, lampTexture);
		Entity lamp1 = new Entity(lamp, new Vector3f(185, -4.7f, -293), 0, 0, 0, 1);
		Entity lamp2 = new Entity(lamp, new Vector3f(370, 4.2f, -300), 0, 0, 0, 1);
		entities.add(lamp1);
		entities.add(lamp2);
		
		//stall
		ModelData stallData = OBJFileLoader.loadOBJ("stall/stall");
		RawModel stallModel = loader.loadToVAO(stallData.getVertices(), stallData.getTextureCoords(),
				stallData.getNormals(), stallData.getIndices());
		ModelTexture stallTexture = new ModelTexture(loader.loadTexture("stall/stallTexture"));
		TexturedModel stallTM = new TexturedModel(stallModel, stallTexture);
		ModelTexture stallMT = stallTM.getTexture();
		stallMT.setShineDamper(1);
		stallMT.setReflectivity(0.2f);
		Entity stallEntity = new Entity(stallTM, new Vector3f(20, 10, -15), 0, 0, 0, 1);
		entities.add(stallEntity);
		
		//dragon
		ModelData dragonData = OBJFileLoader.loadOBJ("dragon/dragon");
		RawModel dragonModel = loader.loadToVAO(dragonData.getVertices(), dragonData.getTextureCoords(),
				dragonData.getNormals(), dragonData.getIndices());
		ModelTexture dragonTexture = new ModelTexture(loader.loadTexture("colors/white"));
		TexturedModel dragonTM = new TexturedModel(dragonModel, dragonTexture);
		dragonTexture.setShineDamper(20);
		dragonTexture.setReflectivity(2);
		Entity dragonEntity = new Entity(dragonTM, new Vector3f(0, 10, -30), 0, 0, 0, 1);
		entities.add(dragonEntity);
		
		//tree
		ModelData treeData = OBJFileLoader.loadOBJ("tree/tree");
		RawModel treeModel = loader.loadToVAO(treeData.getVertices(), treeData.getTextureCoords(),
				treeData.getNormals(), treeData.getIndices());
		ModelTexture treeTexture = new ModelTexture(loader.loadTexture("tree/tree"));
		TexturedModel treeTM = new TexturedModel(treeModel, treeTexture);
		
		//fire flower
		ModelData ffData = OBJFileLoader.loadOBJ("fireflower/mario_flower");
		RawModel ffModel = loader.loadToVAO(ffData.getVertices(), ffData.getTextureCoords(),
				ffData.getNormals(), ffData.getIndices());
		ModelTexture ffTexture = new ModelTexture(loader.loadTexture("fireflower/fireflower"));
		TexturedModel ffTM = new TexturedModel(ffModel, ffTexture);
		
		//grass
		ModelData grassData = OBJFileLoader.loadOBJ("tallGrass/grassModel");
		RawModel grassModel = loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(),
				grassData.getNormals(), grassData.getIndices());
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("tallGrass/grassTexture"));
		TexturedModel grass = new TexturedModel(grassModel, grassTexture);
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		
		//fern
		ModelData fernData = OBJFileLoader.loadOBJ("fern/fern");
		RawModel fernModel = loader.loadToVAO(fernData.getVertices(), fernData.getTextureCoords(),
				fernData.getNormals(), fernData.getIndices());
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern/fern"));
		fernTextureAtlas.setNumberOfRows(2);
		TexturedModel fern = new TexturedModel(fernModel, fernTextureAtlas);
		fern.getTexture().setHasTransparency(true);
		
		//rabbit
		ModelData rabbitData = OBJFileLoader.loadOBJ("rabbit/bunny");
		RawModel rabbitModel = loader.loadToVAO(rabbitData.getVertices(), rabbitData.getTextureCoords(),
				rabbitData.getNormals(), rabbitData.getIndices());
		ModelTexture rabbitTexture = new ModelTexture(loader.loadTexture("colors/white"));
		rabbitTexture.setShineDamper(20);
		rabbitTexture.setReflectivity(2);
		TexturedModel rabbit = new TexturedModel(rabbitModel, rabbitTexture);
		Entity rabbitEntity = new Entity(rabbit, new Vector3f(-30, 10, -30), 0, 0, 0, 1);
		entities.add(rabbitEntity);
		
		//box
		ModelData boxData = OBJFileLoader.loadOBJ("box/box");
		RawModel boxModel = loader.loadToVAO(boxData.getVertices(), boxData.getTextureCoords(),
				boxData.getNormals(), boxData.getIndices());
		ModelTexture boxTexture = new ModelTexture(loader.loadTexture("box/box"));
		boxTexture.setShineDamper(1);
		boxTexture.setReflectivity(0);
		TexturedModel box = new TexturedModel(boxModel, boxTexture);
		Entity boxEntity1 = new Entity(box, new Vector3f(50, 5, -50), 0, 0, 0, 3);
		Entity boxEntity2 = new Entity(box, new Vector3f(80, 5, -100), 0, 0, 0, 3);
		entities.add(boxEntity1);
		entities.add(boxEntity2);
		
		//generating random assortment of plants
		List<Entity> allPlants = new ArrayList<Entity>();
		Random random = new Random();
		
		for(int i = 0; i < 500; i++) {
			float x = random.nextFloat() * 800 - 40;
			float z = random.nextFloat() * -600;
			float y = terrain1.getHeightOfTerrain(x, z);
			allPlants.add(new Entity(treeTM, new Vector3f(x, y, z), 0, 0, 0, 3));
			
			x = random.nextFloat() * 800 - 40;
			z = random.nextFloat() * -600;
			y = terrain1.getHeightOfTerrain(x, z);
			allPlants.add(new Entity(grass, new Vector3f(x, y, z), 0, 0, 0, 1));
			
			x = random.nextFloat() * 800 - 40;
			z = random.nextFloat() * -600;
			y = terrain1.getHeightOfTerrain(x, z);
			allPlants.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 0.6f));
			
			x = random.nextFloat() * 800 - 40;
			z = random.nextFloat() * -600;
			y = 3 + terrain1.getHeightOfTerrain(x, z);
			allPlants.add(new Entity(ffTM, random.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 1f));
		}
		entities.addAll(allPlants);
		
		//player & camera
		Player player = new Player(rabbit, new Vector3f(100, 0, -50), 0, -180, 0, 0.3f);
		entities.add(player);
		Camera camera = new Camera(player);
		
		// ==================GUIS==================
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture thinmatrixGui = new GuiTexture(loader.loadTexture("guis/thinmatrix"), new Vector2f(-0.8f, 0.9f), new Vector2f(0.2f, 0.2f));
		GuiTexture minecraftGui = new GuiTexture(loader.loadTexture("guis/minecraft"), new Vector2f(0f, -0.85f), new Vector2f(0.5f, 0.75f));
		guis.add(thinmatrixGui);
		guis.add(minecraftGui);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrainGrid);
		
		// ==================WATER==================
		
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), fbos);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		waters.add(new WaterTile(150, -60, 3));
		
		// ==================PARTICLES==================
		
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particles/fire"), 8, true);
		ParticleSystem particleSystem = new ParticleSystem(particleTexture, 40, 10, 5, 180, 5, 2, 0.1f, 1);
		
		// ==================MAIN GAME LOOP==================
		
		while(!Display.isCloseRequested()) {
			player.move(terrainGrid);
			camera.move();
			picker.update();		
			particleSystem.generateParticles(player.getPosition());
			ParticleMaster.update(camera);
			
			barrel.increaseRotation(0, 0.2f, 0);
			
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
			//render reflection texture
			fbos.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - waters.get(0).getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -waters.get(0).getHeight() + 0.5f));
			camera.getPosition().y += distance;
			camera.invertPitch();
			GL11.glFinish();
			
			//render refraction texture
			fbos.bindRefractionFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, waters.get(0).getHeight() + 0.5f));
			GL11.glFinish();
			
//			// Giving the sun rainbow colors
//			h = (h + 0.5f) % 360;
//			funkyColor = Maths.HSVtoRGB(new Vector3f(h, 1f, 1f));
//			funkyColor.scale(3f / 255f);
//			funkyLight.setColor(funkyColor);
			
			// Making sunlight levels vary across the day
			float sunlight = 1f * (1 - SkyboxRenderer.getDayBlendFactor());
			sun.setColor(new Vector3f(sunlight, sunlight, sunlight));
			
			float offsetX = (Mouse.getX() / 2560f * 2.0f - 1.0f) * 0.006f;
			float offsetY = ((1.0f - Mouse.getY() / 1440f) * 2.0f - 1.0f) * 0.006f;
			text.setOffset(offsetX, offsetY);
			
			//render main scene
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			fbos.unbindCurrentFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, Integer.MAX_VALUE));
			waterRenderer.render(waters, camera, sun);
			
			ParticleMaster.renderParticles(camera);
			
			guiRenderer.render(guis);
			TextMaster.render();
			
			DisplayManager.updateDisplay();
		}
		
		ParticleMaster.cleanUp();
		TextMaster.cleanUp();
		fbos.cleanUp();
		waterShader.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
