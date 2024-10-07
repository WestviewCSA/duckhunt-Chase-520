import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Music implements Runnable {
    private Thread t;
    protected File audioFile;
    protected AudioInputStream audioStream;
    protected Clip audioClip;
    protected String fn;
    private boolean loop;

    public Music(String fileName, boolean loops) {
        fn = fileName;
        loop = loops;
        initializeAudio();
    }

    // Method to initialize the audio clip
    protected void initializeAudio() {
        audioFile = new File(fn);
        try {
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Method to play the audio (either looped or not)
    public void play() {
        if (loop) {
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            audioClip.start();
        }
        startBackgroundThread();
    }

    // Method to run audio in background thread
    private void startBackgroundThread() {
        if (t == null || !t.isAlive()) {
            t = new Thread(this, fn);
            t.start();
        }
    }

    // Method to stop the audio
    public void stop() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
        }
    }

    // Overridable run method for background play
    @Override
    public void run() {
        if (audioClip != null) {
            audioClip.start();
        }
    }

    // Clean up resources
    public void close() {
        stop();
        if (audioClip != null) {
            audioClip.close();
        }
        try {
            if (audioStream != null) {
                audioStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
