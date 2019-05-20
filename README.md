Build with:<p>
`mvn clean compile assembly:single`

Run with:<p>
`java -jar target/pipertranscribe-1.0-SNAPSHOT-jar-with-dependencies.jar <file.wav>`<p>
file.wav is meant to be a fifo and a pcm 16 as needed by AWS Transcribe 
and created from piperpcm.c by reading a fifo
and writing to file.wav, also a fifo.

Can test with just a WAV file for file.wav?
