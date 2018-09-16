package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.TextureProvider;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.SphereShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class MyGdxGame extends ApplicationAdapter {

	public PerspectiveCamera cam;
	public Model model;
	public ModelBatch modelBatch;
	public Environment environment;
	public BestInputController bestInputController;
	public ModelInstance instance;
	public CallbackListener controllerCallback;
	public Array<ModelInstance> spheres;
	//public AssetManager assets;
	//public Array<ModelInstance> instances = new Array<ModelInstance>();
	//public boolean loading;

	CallbackListener listener;

	public MyGdxGame(final CallbackListener listener) {
		this.listener = listener;
		spheres = new Array<ModelInstance>();
		this.controllerCallback = new CallbackListener() {
			@Override
			public void sendData(float x, float y, float z) {
				if (!(x == 0 && y == 0 && z == 0)) {
					ModelBuilder modelBuilder = new ModelBuilder();
					Model newSphere = modelBuilder.createSphere(5, 5, 5, 5, 5, new Material(ColorAttribute.createDiffuse(Color.GREEN)),
							VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
					ModelInstance newSphereInstance = new ModelInstance(newSphere);
					Matrix4 translation = new Matrix4();
					translation.setTranslation(x, y, z);
					newSphereInstance.transform = translation;
					spheres.add(newSphereInstance);

					ModelBuilder newModelBuilder = new ModelBuilder();
					Vector3 intersectionPoint = new Vector3(x, y, z);
					Model debugVector = newModelBuilder.createArrow(cam.position, intersectionPoint, new Material(ColorAttribute.createDiffuse(Color.RED)),
							VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
					ModelInstance debug = new ModelInstance(debugVector);

					Matrix4 cameraPos = new Matrix4();
					translation.setTranslation(cam.position);

					debug.transform = translation;
					spheres.add(debug);
					listener.sendData(x, y, z);
				}
			}
		};
	}
	
	@Override
	public void create () {
	    modelBatch = new ModelBatch();
		cam = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(50f, 50f, 50f);
		cam.lookAt(0, 0, 50);
		cam.near = 0.1f;
		cam.far	= 300f;
		cam.update();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		ModelLoader loader = new ObjLoader();
		model = loader.loadModel(Gdx.files.internal("man.obj"));
		instance = new ModelInstance(model);

		instance.transform.rotate(1,0,0,90);
		instance.transform.rotate(0,1,0,135);
		//instance.transform.rotate(0,1,0,270);

		//assets = new AssetManager();
		//assets.load("assets/data/man.obj", Model.class);
		//loading = true;

		bestInputController = new BestInputController(cam, controllerCallback, instance);
		bestInputController.pinchZoomFactor = 100f;
		Gdx.input.setInputProcessor(bestInputController);
	}

	/*private void doneLoading() {
	    Model man = assets.get("assets/data/man.obj", Model.class);
	    ModelInstance manInstance = new ModelInstance(man);
	    instances.add(manInstance);
	    loading = false;
    }*/

	@Override
	public void render () {
		/*
	    if (loading && assets.update()) {
	        doneLoading();
        }*/
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		//rotatedInstance.transform.setFromEulerAngles(9*Gdx.input.getAccelerometerX(), 9*Gdx.input.getAccelerometerY(), 9*Gdx.input.getAccelerometerZ());

		modelBatch.begin(cam);
		Array<ModelInstance> instances = new Array<ModelInstance>();
		for (int i = 0; i < spheres.size; i++) {
			instances.add(spheres.get(i));
		}
		instances.add(instance);

		modelBatch.render(instances, environment);
		modelBatch.end();

		bestInputController.update();
	}
	
	@Override
	public void dispose () {
		modelBatch.dispose();
		model.dispose();
	}

	@Override
	public void resume() {

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}
}
