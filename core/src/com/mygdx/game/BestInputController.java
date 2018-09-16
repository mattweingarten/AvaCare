package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;

import sun.rmi.runtime.Log;

public class BestInputController extends GestureDetector {
    /** The button for rotating the camera. */
    public int rotateButton = Buttons.LEFT;
    /** The angle to rotate when moved the full width or height of the screen. */
    public float rotateAngle = 360f;
    /** The button for translating the camera along the up/right plane */
    public int translateButton = Buttons.RIGHT;
    /** The units to translate the camera when moved the full width or height of the screen. */
    public float translateUnits = 10f; // FIXME auto calculate this based on the target
    /** The button for translating the camera along the direction axis */
    public int forwardButton = Buttons.MIDDLE;
    /** The key which must be pressed to activate rotate, translate and forward or 0 to always activate. */
    public int activateKey = 0;
    /** Indicates if the activateKey is currently being pressed. */
    protected boolean activatePressed;
    /** Whether scrolling requires the activeKey to be pressed (false) or always allow scrolling (true). */
    public boolean alwaysScroll = true;
    /** The weight for each scrolled amount. */
    public float scrollFactor = -0.1f;
    /** World units per screen size */
    public float pinchZoomFactor = 10f;
    /** Whether to update the camera after it has been changed. */
    public boolean autoUpdate = true;
    /** The target to rotate around. */
    public Vector3 target = new Vector3();
    /** Whether to update the target on translation */
    public boolean translateTarget = true;
    /** Whether to update the target on forward */
    public boolean forwardTarget = true;
    /** Whether to update the target on scroll */
    public boolean scrollTarget = false;
    public int forwardKey = Keys.W;
    protected boolean forwardPressed;
    public int backwardKey = Keys.S;
    protected boolean backwardPressed;
    public int rotateRightKey = Keys.A;
    protected boolean rotateRightPressed;
    public int rotateLeftKey = Keys.D;
    protected boolean rotateLeftPressed;
    /** The camera. */
    public Camera camera;
    /** The current (first) button being pressed. */
    protected int button = -1;

    public CallbackListener callbackListener;
    private float startX, startY;
    private final Vector3 tmpV1 = new Vector3();
    private final Vector3 tmpV2 = new Vector3();

    protected static class CameraGestureListener extends GestureAdapter {
        public BestInputController controller;
        private float previousZoom;
        public CallbackListener listener;
        public Camera camera;
        public ModelInstance instance;

        public CameraGestureListener(CallbackListener listener, Camera camera, ModelInstance instance) {
            this.listener = listener;
            this.instance = instance;
            this.camera = camera;
        }

        @Override
        public boolean touchDown (float x, float y, int pointer, int button) {
            previousZoom = 0;
            return false;
        }

        @Override
        public boolean tap (float x, float y, int count, int button) {
            return false;
        }

        public static boolean fixedIntersectRayTriangles (final Ray ray, final float[] vertices, final short[] indices, final int vertexSize,
                                                     Vector3 intersection) {

            Vector3 best = new Vector3();
            Vector3 tmp = new Vector3();
            Vector3 tmp1 = new Vector3();
            Vector3 tmp2 = new Vector3();
            Vector3 tmp3 = new Vector3();
            float min_dist = Float.MAX_VALUE;
            boolean hit = false;

            if (indices.length % 3 != 0) throw new RuntimeException("triangle list size is not a multiple of 3");

            for (int i = 0; 3 * i < indices.length; i += 3) {
                int i1 = indices[i] * vertexSize % vertices.length;
                int i2 = indices[i + 1] * vertexSize  % vertices.length;
                int i3 = indices[i + 2] * vertexSize  % vertices.length;

                boolean result = Intersector.intersectRayTriangle(ray, tmp1.set(vertices[i1], vertices[i1 + 1], vertices[i1 + 2]),
                        tmp2.set(vertices[i2], vertices[i2 + 1], vertices[i2 + 2]),
                        tmp3.set(vertices[i3], vertices[i3 + 1], vertices[i3 + 2]), tmp);

                if (result == true) {
                    float dist = ray.origin.dst2(tmp);
                    if (dist < min_dist) {
                        min_dist = dist;
                        best.set(tmp);
                        hit = true;
                    }
                }
            }

            if (hit == false)
                return false;
            else {
                if (intersection != null) intersection.set(best);
                return true;
            }
        }

        @Override
        public boolean longPress (float x, float y) {
            Ray ray = camera.getPickRay(x, y);

            float distance = Float.MAX_VALUE;

            Vector3 touchPoint = new Vector3();

            Matrix4 t = new Matrix4();
            t.rotate(1, 0, 0, -30);
            Array<Mesh> meshes = instance.model.meshes;
            for (int i = 0; i < meshes.size; i++) {
                Mesh thisMesh = meshes.get(i).copy(false);
                thisMesh.transform(t);
                short[] indices = new short[thisMesh.getNumIndices()];
                thisMesh.getIndices(indices);
                float[] vertices = new float[thisMesh.getNumVertices()];
                thisMesh.getVertices(vertices);
                Vector3 intersectionPoint = new Vector3();
                fixedIntersectRayTriangles(ray, vertices, indices, 3, intersectionPoint);

                Vector3 tmp = camera.position.cpy();
                tmp.sub(intersectionPoint);
                float newDistance = tmp.x * tmp.x + tmp.y * tmp.z + tmp.z * tmp.z;

                if (newDistance < distance) {
                    touchPoint = intersectionPoint;
                }
            }

            listener.sendData(touchPoint.x, touchPoint.y, touchPoint.z);
            return false;
        }

        @Override
        public boolean fling (float velocityX, float velocityY, int button) {
            return false;
        }

        @Override
        public boolean pan (float x, float y, float deltaX, float deltaY) {
            return false;
        }

        @Override
        public boolean zoom (float initialDistance, float distance) {
            float newZoom = distance - initialDistance;
            float amount = newZoom - previousZoom;
            previousZoom = newZoom;
            float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
            return controller.pinchZoom(amount / ((w > h) ? h : w));
        }

        @Override
        public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }
    };

    protected final CameraGestureListener gestureListener;

    protected BestInputController (final CameraGestureListener gestureListener, final Camera camera, final CallbackListener listener, ModelInstance instance) {
        super(gestureListener);
        this.callbackListener = listener;
        this.gestureListener = gestureListener;
        this.gestureListener.controller = this;
        this.camera = camera;
    }

    public BestInputController (final Camera camera, final CallbackListener listener, ModelInstance instance) {
        this(new CameraGestureListener(listener, camera, instance), camera, listener, instance);
    }

    public void update () {
        if (rotateRightPressed || rotateLeftPressed || forwardPressed || backwardPressed) {
            final float delta = Gdx.graphics.getDeltaTime();
            if (rotateRightPressed) camera.rotate(camera.up, -delta * rotateAngle);
            if (rotateLeftPressed) camera.rotate(camera.up, delta * rotateAngle);
            if (forwardPressed) {
                camera.translate(tmpV1.set(camera.direction).scl(delta * translateUnits));
                if (forwardTarget) target.add(tmpV1);
            }
            if (backwardPressed) {
                camera.translate(tmpV1.set(camera.direction).scl(-delta * translateUnits));
                if (forwardTarget) target.add(tmpV1);
            }
            if (autoUpdate) camera.update();
        }
    }

    private int touched;
    private boolean multiTouch;

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {

        touched |= (1 << pointer);
        multiTouch = !MathUtils.isPowerOfTwo(touched);
        if (multiTouch)
            this.button = -1;
        else if (this.button < 0 && (activateKey == 0 || activatePressed)) {
            startX = screenX;
            startY = screenY;
            this.button = button;
        }
        return super.touchDown(screenX, screenY, pointer, button) || (activateKey == 0 || activatePressed);
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        touched &= -1 ^ (1 << pointer);
        multiTouch = !MathUtils.isPowerOfTwo(touched);
        if (button == this.button) this.button = -1;
        return super.touchUp(screenX, screenY, pointer, button) || activatePressed;
    }

    protected boolean process (float deltaX, float deltaY, int button) {
        if (button == rotateButton) {
            tmpV1.set(camera.direction).crs(camera.up).y = 0f;
            camera.rotateAround(target, tmpV1.nor(), deltaY * rotateAngle);
            camera.rotateAround(target, Vector3.Y, deltaX * -rotateAngle);
        } else if (button == translateButton) {
            camera.translate(tmpV1.set(camera.direction).crs(camera.up).nor().scl(-deltaX * translateUnits));
            camera.translate(tmpV2.set(camera.up).scl(-deltaY * translateUnits));
            if (translateTarget) target.add(tmpV1).add(tmpV2);
        } else if (button == forwardButton) {
            camera.translate(tmpV1.set(camera.direction).scl(deltaY * translateUnits));
            if (forwardTarget) target.add(tmpV1);
        }
        if (autoUpdate) camera.update();
        return true;
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        boolean result = super.touchDragged(screenX, screenY, pointer);
        if (result || this.button < 0) return result;
        final float deltaX = (screenX - startX) / Gdx.graphics.getWidth();
        final float deltaY = (startY - screenY) / Gdx.graphics.getHeight();
        startX = screenX;
        startY = screenY;
        return process(deltaX, deltaY, button);
    }

    @Override
    public boolean scrolled (int amount) {
        return zoom(amount * scrollFactor * translateUnits);
    }

    public boolean zoom (float amount) {
        if (!alwaysScroll && activateKey != 0 && !activatePressed) return false;
        camera.translate(tmpV1.set(camera.direction).scl(amount));
        if (scrollTarget) target.add(tmpV1);
        if (autoUpdate) camera.update();
        return true;
    }

    protected boolean pinchZoom (float amount) {
        return zoom(pinchZoomFactor * amount);
    }

    @Override
    public boolean keyDown (int keycode) {
        if (keycode == activateKey) activatePressed = true;
        if (keycode == forwardKey)
            forwardPressed = true;
        else if (keycode == backwardKey)
            backwardPressed = true;
        else if (keycode == rotateRightKey)
            rotateRightPressed = true;
        else if (keycode == rotateLeftKey) rotateLeftPressed = true;
        return false;
    }

    @Override
    public boolean keyUp (int keycode) {
        if (keycode == activateKey) {
            activatePressed = false;
            button = -1;
        }
        if (keycode == forwardKey)
            forwardPressed = false;
        else if (keycode == backwardKey)
            backwardPressed = false;
        else if (keycode == rotateRightKey)
            rotateRightPressed = false;
        else if (keycode == rotateLeftKey) rotateLeftPressed = false;
        return false;
    }
}
