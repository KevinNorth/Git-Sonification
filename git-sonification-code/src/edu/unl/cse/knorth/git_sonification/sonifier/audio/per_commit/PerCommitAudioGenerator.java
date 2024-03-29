package edu.unl.cse.knorth.git_sonification.sonifier.audio.per_commit;

import edu.unl.cse.knorth.git_sonification.sonifier.Measure;
import edu.unl.cse.knorth.git_sonification.sonifier.audio.AudioGenerator;
import edu.unl.cse.knorth.git_sonification.sonifier.audio.ClockSpeedController;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.audiofile.AudioFileType;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.RecordToSample;

/**
 * Sonifies commits so that every measure represents a single commit, and the
 * amount of time the sonification plays for a single day changes with how many
 * commits there are.
 * @author knorth
 */
public class PerCommitAudioGenerator extends AudioGenerator {
    // This speed matches the audio samples' tempo.
    public float CLOCK_SPEED = 38500F;
    
    @Override
    public void produceAudio(ConcurrentLinkedQueue<Measure> measures, String filepath) throws IOException {
        final AudioContext ac = new AudioContext();
        final Sample outputSample = new Sample(20000D);
        outputSample.clear();
        final RecordToSample recorder = new RecordToSample(ac, outputSample,
                RecordToSample.Mode.INFINITE);
        final ClockSpeedController clockSpeedController
                = new ClockSpeedController(ac, CLOCK_SPEED);

        final Clock clock = new Clock(ac, clockSpeedController);
        clock.addMessageListener(new Bead() {
            @Override
            public void messageReceived(Bead beadMessage) {
                // When the messagesQueue is empty, output the audio file
                // and stop creating the sonification.
                if (measures.isEmpty()) {
                    // Before clipping, the buffer the recorder uses internally
                    // is longer than the actual recording
                    recorder.clip();
                    Sample sample = recorder.getSample();
                    try {
                        // Writes sound to file
                        sample.write(filepath, AudioFileType.WAV);
                    } catch (IOException e) {
                        System.out.println("Couldn't save sonification:");
                        e.printStackTrace();
                    }
                    // Ends sonfication
                    ac.stop();
                } else {
                    Measure measure = measures.poll();
                    
                    PerCommitMeasureSonifier measureSonifier =
                            new PerCommitMeasureSonifier();
                    measureSonifier.sonifyMeasure(ac, measure,
                            clockSpeedController);
                }
            }
        });
        
        /*
         * Set up the recorder to listen to the sounds generated by the clock
         * and start the sonification!
         */
        // Hook up recorder to audio context
        recorder.addInput(ac.out);
        ac.out.addDependent(recorder);
        // Necessary to have clock produce sound
        ac.out.addDependent(clock);
        // Starts the audio context
        ac.runNonRealTime();
    }
}