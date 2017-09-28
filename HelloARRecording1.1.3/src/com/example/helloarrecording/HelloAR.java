//================================================================================================================================
//
//  Copyright (c) 2015-2017 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
//  EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
//  and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
//
//================================================================================================================================

package com.example.helloarrecording;

import java.util.ArrayList;

import com.helloar.user.MyConst;
import com.helloar.user.TUserBean;
import com.helloar.user.UserBean;

import android.opengl.GLES20;
import android.util.Log;

import cn.easyar.*;

public class HelloAR
{
    private CameraDevice camera;
    private CameraFrameStreamer streamer;
    private Renderer videobg_renderer;
    private RecorderRenderer recorder_renderer;
    private Recorder recorder;
    private boolean viewport_changed = false;
    private Vec2I view_size = new Vec2I(0, 0);
    private Vec4I viewport = new Vec4I(0, 0, 1280, 720);
    private boolean recording_started = false;
    private ArrayList<ImageTracker> trackers;
    private ArrayList<ImageTracker> trackers2;
    private ArrayList<VideoRenderer> video_renderers;
    private VideoRenderer current_video_renderer;
    private int tracked_target = 0;
    private int active_target = 0;
    private ARVideo video = null;
    private int rotation = 0;
	private UserBean bean;
	private TUserBean tbean;

    
    public HelloAR()
    {
        trackers = new ArrayList<ImageTracker>();
        trackers2 = new ArrayList<ImageTracker>();
        bean = new UserBean("");
        bean.setImageNames();
        bean.setVideoNamges();
        tbean = new TUserBean("");
        tbean.setImageNames();
        tbean.setVideoNamges(); 
    }

    private void loadFromImage(ImageTracker tracker, String path)
    {
        ImageTarget target = new ImageTarget();
        String jstr = "{\n"
            + "  \"images\" :\n"
            + "  [\n"
            + "    {\n"
            + "      \"image\" : \"" + path + "\",\n"
            + "      \"name\" : \"" + path.substring(path.lastIndexOf("/")+1, path.lastIndexOf(".")) + "\"\n"
            + "    }\n"
            + "  ]\n"
            + "}";
        target.setup(jstr, StorageType.Absolute | StorageType.Json, "");
        tracker.loadTarget(target, new FunctorOfVoidFromPointerOfTargetAndBool() {
            @Override
            public void invoke(Target target, boolean status) {
                Log.i("HelloAR", String.format("load target (%b): %s (%d)", status, target.name(), target.runtimeID()));
            }
        });
    }

    private void loadAllFromJsonFile(ImageTracker tracker, String path)
    {
        for (ImageTarget target : ImageTarget.setupAll(path, StorageType.Assets)) {
            tracker.loadTarget(target, new FunctorOfVoidFromPointerOfTargetAndBool() {
                @Override
                public void invoke(Target target, boolean status) {
                    try {
                        Log.i("HelloAR", String.format("load target (%b): %s (%d)", status, target.name(), target.runtimeID()));
                    } catch (Throwable ex) {
                    }
                }
            });
        }
    }

    public boolean initialize()
    {
        camera = new CameraDevice();
        streamer = new CameraFrameStreamer();
        streamer.attachCamera(camera);

        boolean status = true;
        status &= camera.open(CameraDeviceType.Default);
        camera.setSize(new Vec2I(1280, 720));
        if (!status) { return status; }
        ImageTracker tracker = new ImageTracker();
        tracker.attachStreamer(streamer);
        //loadAllFromJsonFile(tracker, "targets.json");
        for(int i=0;i<bean.getVideoCount();i++){
        	loadFromImage(tracker, MyConst.FILE_PATH+i+".jpg");
        	//loadFromImage(tracker, "data/data/com.example.helloarvideo/files/6.jpg");
        	  Log.i("ceshi1", "lodeImage:"+MyConst.FILE_PATH+i+".jpg");
        }
      
        for(int i=0;i<tbean.getVideoCount();i++){
        	loadFromImage(tracker, MyConst.FILE_PATH+"s"+i+".jpg");
        	//loadFromImage(tracker, "data/data/com.example.helloarvideo/files/6.jpg");
        	Log.i("ceshi1", "TlodeImage:"+ MyConst.FILE_PATH+"s"+i+".jpg");
        }
        
        trackers.add(tracker);
        return status;
    }

    public void dispose()
    {
    	if (video != null) {
            video.dispose();
            video = null;
        }
        tracked_target = 0;
        active_target = 0;

        for (ImageTracker tracker : trackers) {
            tracker.dispose();
        }
        trackers.clear();
        video_renderers.clear();
        current_video_renderer = null;
        if (recorder != null) {
            recorder.dispose();
            recorder = null;
        }
        recorder_renderer = null;
        if (videobg_renderer != null) {
            videobg_renderer.dispose();
            videobg_renderer = null;
        }
        if (streamer != null) {
            streamer.dispose();
            streamer = null;
        }
        if (camera != null) {
            camera.dispose();
            camera = null;
        }
    }

    public boolean start()
    {
        boolean status = true;
        status &= (camera != null) && camera.start();
        status &= (streamer != null) && streamer.start();
        camera.setFocusMode(CameraDeviceFocusMode.Continousauto);

        for (ImageTracker tracker : trackers) {
            status &= tracker.start();
        }

        return status;
    }

    public boolean stop()
    {
        boolean status = true;
        for (ImageTracker tracker : trackers) {
            status &= tracker.stop();
        }
        status &= (streamer != null) && streamer.stop();
        status &= (camera != null) && camera.stop();
        return status;
    }

    public void initGL()
    {
    	if (active_target != 0) {
            video.onLost();
            video.dispose();
            video  = null;
            tracked_target = 0;
            active_target = 0;
        }
        if (videobg_renderer != null) {
            videobg_renderer.dispose();
        }
        videobg_renderer = new Renderer();
        video_renderers = new ArrayList<VideoRenderer>();
        for (int k = 0; k < bean.getVideoCount()+tbean.getVideoCount(); k += 1) {
            VideoRenderer video_renderer = new VideoRenderer();
            video_renderer.init();
            video_renderers.add(video_renderer);
        }
        current_video_renderer = null;

        recorder_renderer = new RecorderRenderer();
        recorder = new Recorder();
    }

    public void resizeGL(int width, int height)
    {
        view_size = new Vec2I(width, height);
        viewport_changed = true;
    }

    private void updateViewport()
    {
        CameraCalibration calib = camera != null ? camera.cameraCalibration() : null;
        int rotation = calib != null ? calib.rotation() : 0;
        if (rotation != this.rotation) {
            this.rotation = rotation;
            viewport_changed = true;
        }
        if (viewport_changed) {
            Vec2I size = new Vec2I(1, 1);
            if ((camera != null) && camera.isOpened()) {
                size = camera.size();
            }
            if (rotation == 90 || rotation == 270) {
                size = new Vec2I(size.data[1], size.data[0]);
            }
            float scaleRatio = Math.max((float) view_size.data[0] / (float) size.data[0], (float) view_size.data[1] / (float) size.data[1]);
            Vec2I viewport_size = new Vec2I(Math.round(size.data[0] * scaleRatio), Math.round(size.data[1] * scaleRatio));
            viewport = new Vec4I((view_size.data[0] - viewport_size.data[0]) / 2, (view_size.data[1] - viewport_size.data[1]) / 2, viewport_size.data[0], viewport_size.data[1]);

            if ((camera != null) && camera.isOpened())
                viewport_changed = false;

            if (recording_started) {
                recorder_renderer.resize(view_size.data[0], view_size.data[1]);
            }
        }
    }

    public void preRender()
    {
        if (!recording_started) {
            return;
        }
        recorder_renderer.preRender();
    }

    public void render()
    {
        GLES20.glClearColor(1.f, 1.f, 1.f, 1.f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if (videobg_renderer != null) {
            Vec4I default_viewport = new Vec4I(0, 0, view_size.data[0], view_size.data[1]);
            GLES20.glViewport(default_viewport.data[0], default_viewport.data[1], default_viewport.data[2], default_viewport.data[3]);
            if (videobg_renderer.renderErrorMessage(default_viewport)) {
                return;
            }
        }

        if (streamer == null) { return; }
        Frame frame = streamer.peek();
        try {
            updateViewport();
            GLES20.glViewport(viewport.data[0], viewport.data[1], viewport.data[2], viewport.data[3]);

            if (videobg_renderer != null) {
                videobg_renderer.render(frame, viewport);
            }
            ArrayList<TargetInstance> targetInstances = frame.targetInstances();
            if (targetInstances.size() > 0) {
                TargetInstance targetInstance = targetInstances.get(0);
                Target target = targetInstance.target();
                int status = targetInstance.status();
                if (status == TargetStatus.Tracked) {
                    int id = target.runtimeID();
                    if (active_target != 0 && active_target != id) {
                        video.onLost();
                        video.dispose();
                        video  = null;
                        tracked_target = 0;
                        active_target = 0;
                    }
                    if (tracked_target == 0) {
                        if (video == null && video_renderers.size() > 0) {
                            String target_name = target.name();
                            ArrayList<String> videoNames = bean.getVideoNames();
                            ArrayList<String> videoNames2 = tbean.getVideoNames();
                          
                            for(int i=0;i<bean.getVideoCount();i++){
                            	if(target_name.equals(i+"") && video_renderers.get(i).texId() != 0){
                            		video = new ARVideo();
                                    video.openStreamingVideo(MyConst.BASE_URL1+videoNames.get(i), video_renderers.get(i).texId());
                                    current_video_renderer = video_renderers.get(i);
                                    Log.i("ceshi1", "openStreamingVideo:"+MyConst.BASE_URL1+videoNames.get(i));
                                    Log.i("ceshi1", " current_video_renderer:"+ video_renderers.get(i));
                            	}
                            }  
                            for(int i=0;i<tbean.getVideoCount();i++){
                            	if(target_name.equals("s"+i+"") && video_renderers.get(i).texId() != 0){
                            		video = new ARVideo();
                                    video.openStreamingVideo(MyConst.BASE_URL1+videoNames2.get(i), video_renderers.get(i).texId());
                                    current_video_renderer = video_renderers.get(i);
                                    Log.i("ceshi1", "openStreamingVideo:"+MyConst.BASE_URL1+videoNames2.get(i));
                                    Log.i("ceshi1", " current_video_renderer:"+ video_renderers.get(i));
                            	}
                            }
                        }
                        if (video != null) {
                            video.onFound();
                            tracked_target = id;
                            active_target = id;
                        }
                    }
                    ImageTarget imagetarget = target instanceof ImageTarget ? (ImageTarget)(target) : null;
                    if (imagetarget != null) {
                        if (current_video_renderer != null) {
                            video.update();
                            if (video.isRenderTextureAvailable()) {
                                current_video_renderer.render(camera.projectionGL(0.2f, 500.f), targetInstance.poseGL(), imagetarget.size());
                            }
                        }
                    }
                }
            } else {
                if (tracked_target != 0) {
                    video.onLost();
                    tracked_target = 0;
                }
            }
        }

        finally {
            frame.dispose();
        }
    }

    public void postRender()
    {
        if (!recording_started) {
            return;
        }
        recorder_renderer.postRender(viewport);
        recorder.updateFrame();
    }

    public void requestPermissions(FunctorOfVoidFromPermissionStatusAndString callback)
    {
        recorder.requestPermissions(callback);
    }

    public void startRecording(String path, final FunctorOfVoidFromRecordStatusAndString callback)
    {
        if (recording_started) { return; }
        recorder.setOutputFile(path);
        recorder.setZoomMode(RecordZoomMode.ZoomInWithAllContent);
        recorder.setProfile(RecordProfile.Quality_720P_Middle);
        recorder_renderer.resize(view_size.data[0], view_size.data[1]);
        recorder.setInputTexture(recorder_renderer.getTextureId(), view_size.data[0], view_size.data[1]);
        int mode = view_size.data[0] < view_size.data[1] ? RecordVideoOrientation.Portrait:RecordVideoOrientation.Landscape;
        recorder.setVideoOrientation(mode);
        recorder.open(new FunctorOfVoidFromRecordStatusAndString() {
            @Override
            public void invoke(int status, String value) {
                if (status == RecordStatus.OnStopped) {
                    recording_started = false;
                }
                callback.invoke(status, value);
            }
        });
        recorder.start();
        recording_started = true;
    }

    public void stopRecording()
    {
        if (!recording_started) { return; }
        recorder.stop();
        recording_started = false;
    }
    
}
